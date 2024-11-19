use async_trait::async_trait;
use poise::{
    serenity_prelude::{Context, EditThread, EventHandler, GuildChannel, Message},
    CreateReply,
};

use crate::{
    check_is_support, webhooks::GetTagId, WinstonError, GUILD_ID, QUESTIONS_FORUM_ID,
    SUPPORT_ROLE_ID,
};

pub struct SupportAnsweringHandler;

#[async_trait]
impl EventHandler for SupportAnsweringHandler {
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
        let Some(support_tag) = available_tags.get_tag_id("support") else {
            eprintln!("Support tag not found in available tags");
            return;
        };

        let Some(pending_tag) = available_tags.get_tag_id("pending") else {
            eprintln!("Pending tag not found in available tags");
            return;
        };
        thread
            .edit_thread(
                &ctx,
                EditThread::default().applied_tags([support_tag, pending_tag]),
            )
            .await
            .ok();
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
        let Some(support_tag) = available_tags.get_tag_id("support") else {
            eprintln!("Support tag not found in available tags");
            return;
        };

        if !thread.applied_tags.iter().any(|tag| *tag == support_tag) {
            return;
        }

        let Some(answered_tag) = available_tags.get_tag_id("answered") else {
            eprintln!("Answered tag not found in available tags");
            return;
        };

        let Some(pending_tag) = available_tags.get_tag_id("pending") else {
            eprintln!("Pending tag not found in available tags");
            return;
        };

        let is_support = match new_message
            .author
            .has_role(&ctx, GUILD_ID, SUPPORT_ROLE_ID)
            .await
        {
            Ok(true) => true,
            Ok(false) => false,
            Err(e) => {
                eprintln!("Error while handling error: {}", e);
                return;
            }
        };

        println!(
            "Marking thread {} ({}) as {}",
            thread.id,
            thread.name(),
            if is_support { "Answered" } else { "Pending" }
        );

        let new_tags = if is_support {
            vec![support_tag, answered_tag]
        } else {
            vec![support_tag, pending_tag]
        };

        thread
            .edit_thread(&ctx, EditThread::default().applied_tags(new_tags))
            .await
            .ok();
    }
}

#[poise::command(slash_command, ephemeral, check = "check_is_support")]
pub async fn support_answering(
    ctx: crate::Context<'_>,
    #[description = "If the post has been answered"] answered: bool,
) -> Result<(), WinstonError> {
    let state = if answered {
        "**Answered**"
    } else {
        "_**Pending**_"
    };
    let handle = ctx
        .send(CreateReply::default().content(format!("Marking post as {}.", state)))
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

    // Check if the ticket has the support tag
    let Some(support_tag) = available_tags.get_tag_id("support") else {
        eprintln!("Support tag not found in available tags");
        return Err(WinstonError::TagNotFound("support".to_string()));
    };

    if !channel.applied_tags.iter().any(|tag| *tag == support_tag) {
        handle
            .edit(
                ctx,
                CreateReply::default().content(format!(
                    "Cannot mark post as {}, as it is not a suppor ticket",
                    state
                )),
            )
            .await?;
        return Ok(());
    }

    let target_tag_name = if answered { "answered" } else { "pending" };
    let Some(target_tag) = available_tags.get_tag_id(target_tag_name) else {
        eprintln!("Target tag not found in available tags");
        return Err(WinstonError::TagNotFound(target_tag_name.to_string()));
    };

    channel
        .edit_thread(
            &ctx,
            EditThread::default().applied_tags([support_tag, target_tag]),
        )
        .await?;

    handle
        .edit(
            ctx,
            CreateReply::default().content(format!("Marked post as {}.", state)),
        )
        .await?;

    return Ok(());
}
