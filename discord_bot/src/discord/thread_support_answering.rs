use async_trait::async_trait;
use poise::serenity_prelude::{Context, EditThread, EventHandler, Message};

use crate::{webhooks::GetTagId, CONTRIBUTOR_ROLE_ID, GUILD_ID, QUESTIONS_FORUM_ID};

pub struct SupportAnsweringHandler;

#[async_trait]
impl EventHandler for SupportAnsweringHandler {
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

        let is_contributor = match new_message
            .author
            .has_role(&ctx, GUILD_ID, CONTRIBUTOR_ROLE_ID)
            .await
        {
            Ok(true) => true,
            Ok(false) => false,
            Err(e) => {
                eprintln!("Error while handling error: {}", e);
                return;
            }
        };

        let new_tags = if is_contributor {
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
