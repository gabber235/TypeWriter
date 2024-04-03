use async_trait::async_trait;
use poise::serenity_prelude::{Context, EditThread, EventHandler, GuildChannel};

use crate::{CloseReason, TICKET_FORUM_ID};

pub struct ThreadArchivingHandler;

#[async_trait]
impl EventHandler for ThreadArchivingHandler {
    /// Only if the thread has
    async fn thread_update(&self, ctx: Context, old: Option<GuildChannel>, new: GuildChannel) {
        // If the thread was updated and the archived state changed
        let old_archive = old
            .and_then(|old| old.thread_metadata)
            .map(|meta| meta.archived)
            .unwrap_or(false);
        let new_archive = new
            .thread_metadata
            .map(|meta| meta.archived)
            .unwrap_or(false);

        if old_archive == new_archive {
            return;
        }

        // Check that the parent is the ticket forum
        let Some(parent) = new.parent_id else {
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
        let close_tags = available_tags
            .iter()
            .filter(|tag| CloseReason::is_close_tag(tag))
            .collect::<Vec<_>>();

        let has_close_tag = new
            .applied_tags
            .iter()
            .any(|tag| close_tags.iter().any(|close_tag| close_tag.id == *tag));

        if new_archive == has_close_tag {
            return;
        }

        let mut new = new;

        if let Err(e) = new
            .edit_thread(&ctx, EditThread::default().archived(has_close_tag))
            .await
        {
            eprintln!("Error editing thread: {}", e);
            return;
        }
    }
}
