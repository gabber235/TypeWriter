use std::collections::HashMap;

use indoc::formatdoc;
use itertools::Itertools;
use poise::serenity_prelude::{
    ButtonStyle, ChannelId, CreateButton, CreateMessage, EditThread, ForumTag, ForumTagId,
    Mentionable, ReactionType, UserId,
};

use crate::{
    clickup::{get_task_from_clickup, TaskStatus},
    get_discord,
    webhook::{TaskCreated, TaskStatusUpdated, TaskTag, TaskUpdated},
    WinstonError,
};

pub async fn handle_task_created(event: TaskCreated) -> Result<(), WinstonError> {
    update_discord_channel(&event.task_id, false).await
}

pub async fn handle_task_updated(event: TaskUpdated) -> Result<(), WinstonError> {
    update_discord_channel(&event.task_id, false).await
}

pub async fn handle_task_status_updated(event: TaskStatusUpdated) -> Result<(), WinstonError> {
    update_discord_channel(&event.task_id, true).await
}

async fn update_discord_channel(task_id: &str, moved: bool) -> Result<(), WinstonError> {
    let discord = get_discord()?;

    let task = get_task_from_clickup(task_id).await?;

    let Some(channel_id) = task
        .custom_fields
        .iter()
        .find(|field| field.name == "Discord Channel")
        .and_then(|field| field.value.as_ref())
        .and_then(|value| value.as_str())
    else {
        return Ok(());
    };

    let status: TaskStatus = (&task.status).into();

    let channel: ChannelId = channel_id.parse::<u64>()?.into();

    let guild_channel = channel
        .to_channel(&discord)
        .await?
        .guild()
        .ok_or(WinstonError::NotAGuildChannel)?;

    let parent_channel = guild_channel
        .parent_id
        .ok_or(WinstonError::NotAGuildChannel)?
        .to_channel(&discord)
        .await?
        .guild()
        .ok_or(WinstonError::NotAGuildChannel)?;

    let available_tags = parent_channel.available_tags;

    let status_tags = get_status_tags(&available_tags);

    let mut new_tags = Vec::new();

    new_tags.push(status_tags[&status].clone());

    if TaskStatus::InProduction == status {
        if let Some(resolved_tag) = available_tags.get_tag_id("Resolved") {
            new_tags.push(resolved_tag);
        }
    }

    new_tags.extend(
        task.tags
            .iter()
            .map(|tag| tag.name.to_string())
            .filter_map(|name| available_tags.get_tag_id(&name)),
    );

    new_tags = new_tags.into_iter().unique().collect::<Vec<_>>();

    let lock = status == TaskStatus::InProduction;

    channel
        .edit_thread(
            &discord,
            EditThread::default()
                .applied_tags(new_tags)
                .locked(lock)
                .archived(lock),
        )
        .await?;

    if !moved {
        return Ok(());
    }

    if TaskStatus::InProduction == status {
        let _ = channel
            .send_message(
                &discord,
                CreateMessage::default().content(formatdoc! {"
                        # In Production
                        This task has been marked as **In Production** and is now locked.
                        __If you have any additional questions or concerns, please create a new thread.__
                        You can download the latest build [here]({}).
                        ", "https://modrinth.com/plugin/typewriter/versions?c=release"}),
            )
            .await?;
    }

    if TaskStatus::InBeta == status {
        let Some(owner) = channel
            .to_channel(&discord)
            .await?
            .guild()
            .ok_or(WinstonError::NotAGuildChannel)?
            .owner_id
        else {
            return Ok(());
        };

        let _ = channel
            .send_message(
                &discord,
                create_development_message(task_id, &owner, &task.tags),
            )
            .await?;
    }

    // Need to run it again as the message send may unarchive the thread
    channel
        .edit_thread(&discord, EditThread::default().locked(lock).archived(lock))
        .await?;

    Ok(())
}

fn create_development_message(task_id: &str, owner: &UserId, tags: &[TaskTag]) -> CreateMessage {
    if tags.iter().any(|tag| tag.name == "bug") {
        return                 CreateMessage::default()
                    .content(formatdoc! {"
                        # In Development
                        This task has been marked as **In Development**.
                        You can download latest build [here]({}). 
                        
                        __{}: Please verify that this task is fixed or still broken, and indicate by clicking the button below.__
                        ", "https://modrinth.com/plugin/typewriter/versions?c=beta", owner.mention()})
                    .button(
                        CreateButton::new(format!("task-fixed-{}", task_id))
                            .label("Fixed")
                            .style(ButtonStyle::Success)
                            .emoji(ReactionType::Unicode("👍".to_string())),
                    )
                    .button(
                        CreateButton::new(format!("task-broken-{}", task_id))
                            .label("Broken")
                            .style(ButtonStyle::Danger)
                            .emoji(ReactionType::Unicode("🚧".to_string())),
                    );
    } else if tags.iter().any(|tag| tag.name == "feature") {
        return CreateMessage::default()
                    .content(formatdoc! {"
                        # In Development
                        This task has been marked as **In Development**.
                        You can download latest build [here]({}). 
                        
                        __{}: Please verify that this task is correctly implemented or if it is broken, and indicate by clicking the button below.__
                        ", "https://modrinth.com/plugin/typewriter/versions?c=beta", owner.mention()})
                    .button(
                        CreateButton::new(format!("task-implemented-{}", task_id))
                            .label("Implemented")
                            .style(ButtonStyle::Success)
                            .emoji(ReactionType::Unicode("👍".to_string())),
                    )
                    .button(
                        CreateButton::new(format!("task-broken-{}", task_id))
                            .label("Broken")
                            .style(ButtonStyle::Danger)
                            .emoji(ReactionType::Unicode("🚧".to_string())),
                    );
    }

    return CreateMessage::default().content(formatdoc! {"
                        # In Development
                        This task has been marked as **In Development**.
                        You can download latest build [here]({}). 
                        ", "https://modrinth.com/plugin/typewriter/versions?c=beta"});
}

fn get_status_tags(available_tags: &[ForumTag]) -> HashMap<TaskStatus, ForumTagId> {
    available_tags
        .into_iter()
        .filter_map(|tag| match tag.name.as_str() {
            "Backlog" => Some((TaskStatus::Backlog, tag.id.clone())),
            "In Progress" => Some((TaskStatus::InProgress, tag.id.clone())),
            "Done" => Some((TaskStatus::Done, tag.id.clone())),
            "In Development" => Some((TaskStatus::InBeta, tag.id.clone())),
            "In Production" => Some((TaskStatus::InProduction, tag.id.clone())),
            _ => None,
        })
        .collect::<HashMap<_, _>>()
}

pub trait GetTagId {
    fn get_tag_id(&self, name: &str) -> Option<ForumTagId>;
}

impl GetTagId for Vec<ForumTag> {
    fn get_tag_id(&self, name: &str) -> Option<ForumTagId> {
        self.iter()
            .find(|tag| tag.name.to_lowercase() == name.to_lowercase())
            .map(|tag| tag.id.clone())
    }
}
