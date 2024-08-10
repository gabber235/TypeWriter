use async_trait::async_trait;
use poise::serenity_prelude::{Context, EditThread, EventHandler, GuildChannel};

use crate::{webhooks::GetTagId, TICKET_FORUM_ID};

pub struct ThreadPostedHandler;

#[async_trait]
impl EventHandler for ThreadPostedHandler {
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

        if parent.id != TICKET_FORUM_ID {
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
}
