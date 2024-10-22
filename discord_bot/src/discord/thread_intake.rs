use async_openai::{
    error::OpenAIError,
    types::{
        ChatCompletionRequestAssistantMessageArgs, ChatCompletionRequestAssistantMessageContent,
        ChatCompletionRequestMessage, ChatCompletionRequestSystemMessageArgs,
        ChatCompletionRequestUserMessageArgs, ChatCompletionRequestUserMessageContent,
        ChatCompletionToolArgs, ChatCompletionToolType, CreateChatCompletionRequest,
        CreateChatCompletionRequestArgs, FunctionObjectArgs,
    },
    Client,
};
use async_trait::async_trait;
use indoc::formatdoc;
use itertools::Itertools;
use once_cell::sync::Lazy;
use poise::{
    serenity_prelude::{
        CacheHttp, Context, CreateAttachment, CreateEmbed, CreateMessage, EditThread, EventHandler,
        GetMessages, GuildChannel, Mentionable, Message,
    },
    CreateReply,
};
use serde_json::json;
use std::{fs, path::Path};

use crate::{
    check_is_support, webhooks::GetTagId, WinstonError, QUESTIONS_FORUM_ID, SUPPORT_ROLE_ID,
};

pub struct ThreadIntakeHandler;

#[async_trait]
impl EventHandler for ThreadIntakeHandler {
    async fn thread_create(&self, ctx: Context, mut thread: GuildChannel) {
        // When tags are already applied, the bot is added later not on creation. Like at closing
        // the channel.
        if !thread.applied_tags.is_empty() {
            return;
        }
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

        println!("Started intake process for {}", thread.name());
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

        if !thread.applied_tags.iter().any(|tag| *tag == intake_tag)
            && thread.applied_tags.len() > 1
        {
            return;
        }

        // We want to have a few safety checks to make sure we don't get a super high open AI bill.

        if thread.message_count.unwrap_or(0) > 5 {
            println!(
                "Force close intake process for {} because of to many messages",
                thread.name()
            );
            complete_intake_with(&ctx, &mut thread, IntakeCompletion::TooManyMessages).await;
            return;
        }

        send_intake_reply(&ctx, &mut thread, &new_message).await;
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

async fn complete_intake_with(
    ctx: &impl CacheHttp,
    thread: &mut GuildChannel,
    completion: IntakeCompletion,
) {
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

fn get_system_message() -> String {
    let mut system_message = fs::read_to_string("/usr/local/bin/ai/intake_system_message.md")
        .expect("Failed to read intake system message");

    let mut examples = String::new();
    let examples_folder = Path::new("/usr/local/bin/ai/intake_examples");

    if examples_folder.exists() {
        for entry in fs::read_dir(examples_folder).expect("Failed to read examples folder") {
            let entry = entry.expect("Failed to read directory entry");
            let path = entry.path();

            if path.is_file() {
                let content =
                    fs::read_to_string(&path).expect(&format!("Failed to read file: {:?}", path));
                examples.push_str(&formatdoc! {"
                    <example>
                    {}
                    </example>
                    ", content.trim_end_matches('\n')
                });
            }
        }
    } else {
        panic!("Examples folder does not exist.");
    }

    system_message =
        system_message.replace("<#include_examples>", &examples.trim_end_matches('\n'));

    system_message
}

static INTAKE_SYSTEM_MESSAGE: Lazy<String> = Lazy::new(get_system_message);

async fn send_intake_reply(ctx: &Context, thread: &mut GuildChannel, message: &Message) {
    let _ = thread.broadcast_typing(&ctx).await;

    let Some(request) = create_ai_request(&ctx, &thread).await else {
        return;
    };

    let client = Client::new();

    let response = client.chat().create(request).await;
    let ai_message = match response {
        Ok(response) => response
            .choices
            .first()
            .map(|choice| choice.message.clone()),
        Err(e) => {
            eprintln!("Error getting chat completion response: {}", e);
            return;
        }
    };

    let ai_message = match ai_message {
        Some(message) => message,
        None => {
            eprintln!("No message in chat completion response");
            return;
        }
    };

    println!("Got message back from AI: {:?}", ai_message);

    if let Some(content) = ai_message.content {
        if let Err(err) = message.reply(&ctx, content).await {
            eprintln!("Error sending intake reply: {}", err);
        }
    }

    let Some(tool_calls) = ai_message.tool_calls else {
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

    complete_intake_with(
        &ctx,
        thread,
        if args.provided_inquiry {
            IntakeCompletion::Success
        } else {
            IntakeCompletion::InquiryNotProvided
        },
    )
    .await;
}

async fn create_ai_request(
    ctx: &impl CacheHttp,
    thread: &GuildChannel,
) -> Option<CreateChatCompletionRequest> {
    let discord_messages = match thread
        .messages(&ctx, GetMessages::default().limit(11))
        .await
    {
        Ok(messages) => messages,
        Err(e) => {
            eprintln!("Error getting messages: {}", e);
            return None;
        }
    };

    let mut messages = discord_messages
        .iter()
        .sorted_by(|a, b| a.timestamp.cmp(&b.timestamp))
        .filter_map(|message| ai_message(message).ok())
        .collect::<Vec<_>>();

    messages.insert(
        0,
        ChatCompletionRequestSystemMessageArgs::default()
            .content(INTAKE_SYSTEM_MESSAGE.to_string())
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
        .temperature(0.5)
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
                          "provided_inquiry"
                        ],
                        "properties": {
                          "provided_inquiry": {
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
                    return None;
                }
            };

    Some(request)
}

#[derive(serde::Deserialize)]
struct IntakeCompletionArguments {
    provided_inquiry: bool,
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

#[poise::command(slash_command, ephemeral, check = "check_is_support")]
pub async fn complete_intake(ctx: crate::Context<'_>) -> Result<(), WinstonError> {
    let handle = ctx
        .send(CreateReply::default().content("Manually Completing Intake"))
        .await?;

    let mut channel = ctx
        .channel_id()
        .to_channel(ctx)
        .await?
        .guild()
        .ok_or(WinstonError::NotAGuildChannel)?;

    let parent_channel = channel
        .parent_id
        .ok_or(WinstonError::NotAGuildChannel)?
        .to_channel(&ctx)
        .await?
        .guild()
        .ok_or(WinstonError::NotAGuildChannel)?;

    let available_tags = parent_channel.available_tags;

    let Some(intake_tag) = available_tags.get_tag_id("intake") else {
        eprintln!("Intake tag not found in available tags");
        return Err(WinstonError::TagNotFound("intake".to_string()));
    };

    if !channel.applied_tags.iter().any(|tag| *tag == intake_tag) {
        handle
            .edit(
                ctx,
                CreateReply::default().content(format!(
                    "Cannot complete intake for thread {}, as it is not an intake thread.",
                    channel.name()
                )),
            )
            .await?;
        return Ok(());
    }

    complete_intake_with(&ctx, &mut channel, IntakeCompletion::Manual).await;

    // Because we forcefully closed the ticket. We likely want to edit and add it to the examples.
    // So lets create a <tread_name>.md file with the template to download

    let Some(attachment) = create_example_attachment(&ctx, &channel).await else {
        return Err(WinstonError::AttachmentError(channel.name().to_string()));
    };

    handle
        .edit(
            ctx,
            CreateReply::default()
                .attachment(attachment)
                .content("Created the attachment for you."),
        )
        .await?;

    Ok(())
}

#[poise::command(slash_command, ephemeral, check = "check_is_support")]
pub async fn example_attachment(ctx: crate::Context<'_>) -> Result<(), WinstonError> {
    let channel = ctx
        .channel_id()
        .to_channel(ctx)
        .await?
        .guild()
        .ok_or(WinstonError::NotAGuildChannel)?;

    let Some(attachment) = create_example_attachment(&ctx, &channel).await else {
        return Err(WinstonError::AttachmentError(channel.name().to_string()));
    };

    ctx.send(
        CreateReply::default()
            .attachment(attachment)
            .content("Created the attachment for you."),
    )
    .await?;

    Ok(())
}

async fn create_example_attachment(
    ctx: &impl CacheHttp,
    channel: &GuildChannel,
) -> Option<CreateAttachment> {
    let Some(request) = create_ai_request(&ctx, &channel).await else {
        return None;
    };

    let content: String = request
        .messages
        .iter()
        .map(|message| match message {
            ChatCompletionRequestMessage::User(info) => match &info.content {
                ChatCompletionRequestUserMessageContent::Text(string) => {
                    format!("<user>\n{}\n</user>", string)
                }
                _ => String::new(),
            },
            ChatCompletionRequestMessage::Assistant(info) => match &info.content {
                Some(ChatCompletionRequestAssistantMessageContent::Text(string)) => {
                    format!("<assistant>\n{}\n</assistant>", string)
                }
                _ => String::new(),
            },
            _ => String::new(),
        })
        .filter(|s| !s.is_empty())
        .join("\n");

    Some(CreateAttachment::bytes(
        content.as_bytes(),
        format!("{}.md", channel.name().to_lowercase().replace(" ", "_")),
    ))
}
