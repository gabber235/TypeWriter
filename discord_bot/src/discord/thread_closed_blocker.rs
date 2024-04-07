use async_trait::async_trait;
use indoc::formatdoc;
use poise::serenity_prelude::{
    Context, CreateMessage, EditMessage, EventHandler, Mentionable, Message,
};

use crate::{CloseReason, TICKET_FORUM_ID};

pub struct ThreadClosedBlockerHandler;

#[async_trait]
impl EventHandler for ThreadClosedBlockerHandler {
    async fn message(&self, ctx: Context, new_message: Message) {
        // Let's not get into an infinite loop
        if new_message.author.bot {
            return;
        }

        let Ok(channel) = new_message.channel(&ctx).await else {
            return;
        };

        let Some(thread) = channel.guild() else {
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

        if parent.id != TICKET_FORUM_ID {
            return;
        }

        let available_tags = parent.available_tags;
        let close_tags = available_tags
            .iter()
            .filter(|tag| CloseReason::is_close_tag(tag))
            .collect::<Vec<_>>();

        let has_close_tag = thread
            .applied_tags
            .iter()
            .any(|tag| close_tags.iter().any(|close_tag| close_tag.id == *tag));

        if !has_close_tag {
            return;
        }

        let message_content = new_message.content.trim();

        let error_message = new_message
            .channel_id
            .send_message(
                &ctx,
                CreateMessage::default().content(formatdoc!("
                                                            {} You cannot send messages in a closed thread. 
                                                            If you need further assistance, please create a new thread.

                                                            Your message:
                                                            ```
                                                            {}
                                                            ```
                                                            ",
                    new_message.author.mention(), &message_content
                )),
            )
            .await;

        if let Err(e) = new_message.delete(&ctx).await {
            eprintln!("Error deleting message: {}", e);
            return;
        }

        let mut error_message = match error_message {
            Ok(error_message) => error_message,
            Err(e) => {
                eprintln!("Error sending error message: {}", e);
                return;
            }
        };

        for seconds_left in (1..=15).rev() {
            if let Err(e) = error_message
                .edit(
                    &ctx,
                    EditMessage::default().content(formatdoc!(
                        "
                            {} You cannot send messages in a closed thread.
                            If you need further assistance, please create a new thread.

                            Your message:
                            ```
                            {}
                            ```
                            This message will be deleted in {} seconds.
                                            ",
                        new_message.author.mention(),
                        &message_content,
                        seconds_left
                    )),
                )
                .await
            {
                eprintln!("Error editing error message: {}", e);
                return;
            }

            tokio::time::sleep(std::time::Duration::from_secs(1)).await;
        }

        if let Err(e) = error_message.delete(&ctx).await {
            eprintln!("Error deleting error message: {}", e);
            return;
        }
    }
}
