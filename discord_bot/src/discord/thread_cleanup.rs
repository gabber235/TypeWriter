use chrono::Duration;
use indoc::formatdoc;
use poise::serenity_prelude::{
    model::channel, ButtonStyle, ChannelId, Context, CreateButton, CreateEmbed, CreateMessage,
    EditThread, ForumTag, ForumTagId, Mentionable, ReactionType, Timestamp,
};

use crate::{
    get_discord, webhooks::GetTagId, CloseReason, WinstonError, GUILD_ID, QUESTIONS_CHANNEL, QUESTIONS_FORUM_ID
};

pub async fn cleanup_threads() -> Result<(), WinstonError> {
    let discord = get_discord()?;

    let guild_channel = QUESTIONS_CHANNEL
        .to_channel(&discord)
        .await?
        .guild()
        .ok_or(WinstonError::NotAGuildChannel)?;

    let available_tags = guild_channel.available_tags;

    let closed_tags = available_tags
        .iter()
        .filter(|tag| CloseReason::is_close_tag(tag))
        .collect::<Vec<_>>();

    let answered_tag = get_tag(&available_tags, "answered")?;

    let active_threads = GUILD_ID.get_active_threads(&discord).await?;

    for thread in active_threads.threads {
        if !is_ticket_forum_thread(&thread).await {
            continue;
        }

        let has_close_tag = thread
            .applied_tags
            .iter()
            .any(|tag| closed_tags.iter().any(|close_tag| close_tag.id == *tag));

        if has_close_tag {
            archive_thread(&discord, thread).await?;
            continue;
        }

        if thread.applied_tags.iter().any(|tag| *tag == answered_tag) {
            resolve_answered_thread(&discord, thread, &available_tags).await?;
            continue;
        }

        if unverified_in_beta(&available_tags, &thread.applied_tags)? {
            reask_verification(&discord, thread).await?;
            continue;
        }
    }

    Ok(())
}

async fn is_ticket_forum_thread(channel: &channel::GuildChannel) -> bool {
    let Some(parent) = channel.parent_id else {
        return false;
    };

    parent.get() == QUESTIONS_FORUM_ID
}

async fn archive_thread(
    discord: &Context,
    mut thread: channel::GuildChannel,
) -> Result<(), WinstonError> {
    let Some(last_message_date) = get_last_message_date(&thread) else {
        return Ok(());
    };

    let now = Timestamp::now();
    let duration = now.timestamp() - last_message_date.timestamp();

    if duration < Duration::days(3).num_seconds() {
        return Ok(());
    }

    println!("Archiving thread {} ({})", thread.id, thread.name());

    thread
        .edit_thread(&discord, EditThread::default().archived(true))
        .await?;

    Ok(())
}

async fn resolve_answered_thread(
    discord: &Context,
    mut thread: channel::GuildChannel,
    available_tags: &[ForumTag],
) -> Result<(), WinstonError> {
    let Some(last_message_date) = get_last_message_date(&thread) else {
        return Ok(());
    };

    let now = Timestamp::now();
    let duration = now.timestamp() - last_message_date.timestamp();

    if duration < Duration::days(3).num_seconds() {
        return Ok(());
    }

    println!("Auto Resolving thread {} ({})", thread.id, thread.name());


    // Close the thread
    let Some(resolved_tag) = available_tags.get_tag_id("resolved") else {
        return Err(WinstonError::TagNotFound("resolved".to_string()));
    };

    thread
        .edit_thread(&discord, EditThread::default().applied_tags([resolved_tag]))
        .await?;

    let owner_id = thread.owner_id.ok_or(WinstonError::NotAThreadChannel)?;

    thread
        .send_message(
            discord,
            CreateMessage::default()
                .content(format!("{}", owner_id.mention()))
                .embed(
                    CreateEmbed::default()
                        .title("Ticket Closed")
                        .color(CloseReason::Resolved.get_color())
                        .description(formatdoc! {"
                            This ticket has been closed due to inactivity.

                            *If your question has not been answered yet, reopen the ticket by clicking the button bellow*"
                        })
                )
                .button(CreateButton::new("reopen_ticket")
                .label("Reopen Ticket")
                .style(ButtonStyle::Secondary)
                .emoji(ReactionType::Unicode("🔓".to_string()))
                .disabled(false)),

        )
        .await?;

    Ok(())
}

async fn reask_verification(
    discord: &Context,
    thread: channel::GuildChannel,
) -> Result<(), WinstonError> {
    let Some(last_message_date) = get_last_message_date(&thread) else {
        return Ok(());
    };

    let now = Timestamp::now();
    let duration = now.timestamp() - last_message_date.timestamp();

    if duration < Duration::days(3).num_seconds() {
        return Ok(());
    }

    println!(
        "Reasking verification for thread {} ({})",
        thread.id,
        thread.name()
    );

    let owner_id = thread.owner_id.ok_or(WinstonError::NotAThreadChannel)?;

    thread
        .send_message(
            discord,
            CreateMessage::default()
                .content(format!("Hey {}, it looks like you haven't verified the issue yet. Can you verify the issue, and then click one of the buttons above to indicate your findings?", owner_id.mention()))
        )
        .await?;

    Ok(())
}

fn unverified_in_beta(
    available_tags: &[ForumTag],
    tags: &[ForumTagId],
) -> Result<bool, WinstonError> {
    let beta_tag = get_tag(available_tags, "in beta")?;

    let fixed_tag = get_tag(available_tags, "fixed")?;
    let implemented_tag = get_tag(available_tags, "implemented")?;

    if !tags.contains(&beta_tag) {
        return Ok(false);
    }

    if tags.contains(&fixed_tag) || tags.contains(&implemented_tag) {
        return Ok(false);
    }

    Ok(true)
}

fn get_tag(available_tags: &[ForumTag], tag: &str) -> Result<ForumTagId, WinstonError> {
    available_tags
        .get_tag_id(tag)
        .ok_or_else(|| WinstonError::TagNotFound(tag.to_string()))
}

fn get_last_message_date(thread: &channel::GuildChannel) -> Option<Timestamp> {
    Some(thread.last_message_id?.created_at())
}
