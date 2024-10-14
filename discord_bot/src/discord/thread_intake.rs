use async_openai::{
    error::OpenAIError,
    types::{
        ChatCompletionRequestAssistantMessageArgs, ChatCompletionRequestMessage,
        ChatCompletionRequestSystemMessageArgs, ChatCompletionRequestUserMessageArgs,
        ChatCompletionToolArgs, ChatCompletionToolType, CreateChatCompletionRequestArgs,
        FunctionObjectArgs,
    },
    Client,
};
use async_trait::async_trait;
use indoc::formatdoc;
use once_cell::sync::Lazy;
use poise::serenity_prelude::{
    Context, CreateEmbed, CreateMessage, EditThread, EventHandler, GetMessages, GuildChannel,
    Mentionable, Message,
};
use serde_json::json;

use crate::{webhooks::GetTagId, QUESTIONS_FORUM_ID, SUPPORT_ROLE_ID};

pub struct ThreadIntakeHandler;

#[async_trait]
impl EventHandler for ThreadIntakeHandler {
    async fn thread_create(&self, ctx: Context, mut thread: GuildChannel) {
        let Some(parent) = thread.parent_id else {
            return;
        };
        let parent = match parent.to_channel(&ctx).await {
            Ok(parent) => parent,
            Err(e) => {
                eprintln!("Error getting parent channel: {}", e);
                return;
            }
        };

        let Some(parent) = parent.guild() else {
            return;
        };

        if parent.id != QUESTIONS_FORUM_ID {
            return;
        }

        let available_tags = parent.available_tags;
        let Some(intake_tag) = available_tags.get_tag_id("intake") else {
            eprintln!("Intake tag not found in available tags");
            return;
        };

        thread
            .edit_thread(&ctx, EditThread::default().applied_tags([intake_tag]))
            .await
            .ok();

        send_intake_reply(&ctx, &mut thread).await;
    }

    async fn message(&self, ctx: Context, new_message: Message) {
        // If we send the close message, this makes sure we won't override the new tags
        if new_message.author.bot {
            return;
        }

        let Ok(channel) = new_message.channel(&ctx).await else {
            return;
        };

        let Some(mut thread) = channel.guild() else {
            return;
        };

        let Some(parent) = thread.parent_id else {
            return;
        };

        let parent = match parent.to_channel(&ctx).await {
            Ok(parent) => parent,
            Err(e) => {
                eprintln!("Error getting parent channel: {}", e);
                return;
            }
        };

        let Some(parent) = parent.guild() else {
            return;
        };

        if parent.id != QUESTIONS_FORUM_ID {
            return;
        }

        let available_tags = parent.available_tags;
        let Some(intake_tag) = available_tags.get_tag_id("intake") else {
            eprintln!("Intake tag not found in available tags");
            return;
        };

        if !thread.applied_tags.iter().any(|tag| *tag == intake_tag) {
            return;
        }

        // We want to have a few safety checks to make sure we don't get a super high open AI bill.

        if thread.message_count.unwrap_or(0) > 10 {
            complete_intake(&ctx, &mut thread, IntakeCompletion::TooManyMessages).await;
            return;
        }

        send_intake_reply(&ctx, &mut thread).await;
    }
}

enum IntakeCompletion {
    Success,
    Manual,
    TooManyMessages,
    InquiryNotProvided,
}

impl IntakeCompletion {
    fn completion_embed(&self) -> CreateEmbed {
        match self {
            Self::Success => CreateEmbed::default()
                .title("Intake Completed")
                .color(0x0ccf92)
                .description(formatdoc! {"
                        Thanks for your clear description of the problem!
                        I will pass the ticket on to the Support team.
                    "}),
            Self::Manual => CreateEmbed::default()
                .title("Intake Done")
                .color(0x0ccf92)
                .description(formatdoc! {"
                        A Support member has manually marked this intake as done.
                        They will likely help you out soon.
                    "}),
            Self::TooManyMessages => CreateEmbed::default()
                .title("Intake Too Long")
                .color(0xFF0000)
                .description(formatdoc! {"
                        This intake is too long. It has been closed.
                        I will pass the ticket on to the Support team.
                    "}),
            Self::InquiryNotProvided => CreateEmbed::default()
                .title("Intake Not Provided")
                .color(0xFF0000)
                .description(formatdoc! {"
                        Could not complete the intake because the inquiry was not provided.
                        A Support member will take over this ticket.
                    "}),
        }
    }
}

async fn complete_intake(ctx: &Context, thread: &mut GuildChannel, completion: IntakeCompletion) {
    let Some(parent) = thread.parent_id else {
        return;
    };

    let parent = match parent.to_channel(&ctx).await {
        Ok(parent) => parent,
        Err(e) => {
            eprintln!("Error getting parent channel: {}", e);
            return;
        }
    };

    let Some(parent) = parent.guild() else {
        return;
    };

    if parent.id != QUESTIONS_FORUM_ID {
        return;
    }

    let available_tags = parent.available_tags;
    let Some(support_tag) = available_tags.get_tag_id("support") else {
        eprintln!("Support tag not found in available tags");
        return;
    };

    let Some(pending_tag) = available_tags.get_tag_id("pending") else {
        eprintln!("Pending tag not found in available tags");
        return;
    };

    match thread
        .send_message(
            &ctx,
            CreateMessage::default()
                .content(format!("{}", SUPPORT_ROLE_ID.mention()))
                .embed(completion.completion_embed()),
        )
        .await
    {
        Ok(_) => {}
        Err(e) => {
            eprintln!("Error sending intake completion message: {}", e);
            return;
        }
    }

    let new_tags = vec![support_tag, pending_tag];

    match thread
        .edit_thread(&ctx, EditThread::default().applied_tags(new_tags))
        .await
    {
        Ok(_) => {}
        Err(e) => {
            eprintln!("Error marking thread as pending: {}", e);
            return;
        }
    }
}

static INTAKE_SYSTEM_MESSAGE: Lazy<String> =
    Lazy::new(|| std::fs::read_to_string("ai/intake_system_message.md").unwrap());

async fn send_intake_reply(ctx: &Context, thread: &mut GuildChannel) {
    thread.broadcast_typing(&ctx).await;

    let discord_messages = match thread
        .messages(&ctx, GetMessages::default().limit(11))
        .await
    {
        Ok(messages) => messages,
        Err(e) => {
            eprintln!("Error getting messages: {}", e);
            return;
        }
    };

    let mut messages = discord_messages
        .iter()
        .filter_map(|message| ai_message(message).ok())
        .collect::<Vec<_>>();

    messages.insert(
        0,
        ChatCompletionRequestSystemMessageArgs::default()
            .content(INTAKE_SYSTEM_MESSAGE.as_str())
            .build()
            .unwrap_or_default()
            .into(),
    );

    messages.insert(
        1,
        ChatCompletionRequestUserMessageArgs::default()
            .content(format!("# {}", thread.name()))
            .build()
            .unwrap_or_default()
            .into(),
    );

    let request = match CreateChatCompletionRequestArgs::default()
        .max_tokens(512u32)
        .model("gpt-4o-mini")
        .messages(messages)
        .temperature(0.6)
        .tools(vec![ChatCompletionToolArgs::default()
            .r#type(ChatCompletionToolType::Function)
            .function(
                FunctionObjectArgs::default()
                    .name("complete_intake")
                    .description("Complete the intake process, indicating success or failure.")
                    .strict(true)
                    .parameters(json!({
                        "type": "object",
                        "required": [
                          "success"
                        ],
                        "properties": {
                          "success": {
                            "type": "boolean",
                            "description": "Indicates whether the users has provided the required information or not."
                          }
                        },
                        "additionalProperties": false
                    }))
                    .build()
                    .unwrap_or_default(),
            )
            .build()
            .unwrap_or_default()])
            .build()
            {
                Ok(request) => request,
                Err(e) => {
                    eprintln!("Error creating chat completion request: {}", e);
                    return;
                }
            };

    let client = Client::new();

    let response = client.chat().create(request).await;
    let message = match response {
        Ok(response) => response
            .choices
            .first()
            .map(|choice| choice.message.clone()),
        Err(e) => {
            eprintln!("Error getting chat completion response: {}", e);
            return;
        }
    };

    let message = match message {
        Some(message) => message,
        None => {
            eprintln!("No message in chat completion response");
            return;
        }
    };

    if let Some(content) = message.content {
        if let Err(err) = thread.say(&ctx, content).await {
            eprintln!("Error sending intake reply: {}", err);
        }
    }

    let Some(tool_calls) = message.tool_calls else {
        return;
    };

    let Some(tool_call) = tool_calls.first() else {
        return;
    };

    if tool_call.function.name != "complete_intake" {
        eprintln!("Unexpected tool call: {}", tool_call.function.name);
        return;
    }

    let args =
        match serde_json::from_str::<IntakeCompletionArguments>(&tool_call.function.arguments) {
            Ok(args) => args,
            Err(e) => {
                eprintln!("Error parsing tool call arguments: {}", e);
                return;
            }
        };

    complete_intake(
        &ctx,
        thread,
        if args.success {
            IntakeCompletion::Success
        } else {
            IntakeCompletion::InquiryNotProvided
        },
    )
    .await;
}

#[derive(serde::Deserialize)]
struct IntakeCompletionArguments {
    success: bool,
}

fn ai_message(message: &Message) -> Result<ChatCompletionRequestMessage, OpenAIError> {
    if message.author.bot {
        Ok(ChatCompletionRequestAssistantMessageArgs::default()
            .content(message.content.as_str())
            .build()?
            .into())
    } else {
        Ok(ChatCompletionRequestUserMessageArgs::default()
            .content(message.content.as_str())
            .build()?
            .into())
    }
}
