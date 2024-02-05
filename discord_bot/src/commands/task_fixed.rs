use async_trait::async_trait;
use indoc::formatdoc;
use itertools::Itertools;
use poise::serenity_prelude::{
    ComponentInteraction, Context, EditInteractionResponse, EditMessage, EventHandler,
    GuildChannel, Interaction, UserId,
};

use crate::{
    clickup::{add_tag_to_task, update_task, TaskStatus, UpdateTask},
    WinstonError, CONTRIBUTOR_ROLE_ID,
};

pub struct TaskFixedHandler;

#[async_trait]
impl EventHandler for TaskFixedHandler {
    async fn interaction_create(&self, ctx: Context, interaction: Interaction) {
        let Some(mut component) = interaction.message_component() else {
            return;
        };

        let custom_id = &component.data.custom_id;
        if !custom_id.starts_with("task-fixed-")
            && !custom_id.starts_with("task-broken-")
            && !custom_id.starts_with("task-implemented-")
        {
            return;
        }

        if let Err(err) = component.defer_ephemeral(&ctx).await {
            eprintln!("Failed to defer ephemeral: {}", err);
            return;
        }

        let split = custom_id.split('-').collect_vec();
        if split.len() != 3 {
            update_response(
                &ctx,
                &component,
                "Something whent wrong with the button. It seems to be outdated.",
            )
            .await;
            eprintln!("Invalid custom_id: {}", custom_id);
            return;
        }
        let task_status = split[1].to_string();
        let task_id = split[2].to_string();

        let Some(_) = check_permissions(&ctx, &component).await else {
            return;
        };

        let result = match task_status.as_str() {
            "fixed" => {
                update_response(&ctx, &component, "Marking task as fixed...").await;
                mark_task_as_fixed(&ctx, &mut component, &task_id).await
            }
            "implemented" => {
                update_response(&ctx, &component, "Marking task as implemented...").await;
                mark_task_as_implemented(&ctx, &mut component, &task_id).await
            }
            "broken" => {
                update_response(&ctx, &component, "Marking task as broken...").await;
                mark_task_as_broken(&ctx, &mut component, &task_id).await
            }
            _ => {
                update_response(
                    &ctx,
                    &component,
                    "Something whent wrong with the button. It seems to be outdated.",
                )
                .await;
                eprintln!("Invalid task status: {}", task_status);
                return;
            }
        };

        if let Err(e) = result {
            update_response(
                &ctx,
                &component,
                format!("Failed to mark task as {}: {}", task_status, e),
            )
            .await;
            eprintln!("Failed to mark task as {}: {}", task_status, e);
            return;
        }
    }
}

async fn mark_task_as_fixed(
    ctx: &Context,
    interaction: &mut ComponentInteraction,
    task_id: &str,
) -> Result<(), WinstonError> {
    add_tag_to_task(task_id, "fixed").await?;

    update_response(
        ctx,
        interaction,
        "Thanks for marking the task as fixed! This helps us keep track of the status of the task.",
    )
    .await;

    interaction
        .message
        .edit(
            ctx,
            EditMessage::default()
                .components(Vec::new())
                .content(formatdoc! {"
                        # In Development (Fixed)
                        This task has been marked as **In Development**.
                        This has been verified to be **fixed** and can be downloaded [here]({}). 
                  ", "https://modrinth.com/plugin/typewriter/versions?c=beta"}),
        )
        .await?;
    Ok(())
}

async fn mark_task_as_implemented(
    ctx: &Context,
    interaction: &mut ComponentInteraction,
    task_id: &str,
) -> Result<(), WinstonError> {
    add_tag_to_task(task_id, "implemented").await?;

    update_response(
        ctx,
        interaction,
        "Thanks for marking the task as implemented! This helps us keep track of the status of the task.",
    )
    .await;

    interaction
        .message
        .edit(
            ctx,
            EditMessage::default()
                .components(Vec::new())
                .content(formatdoc! {"
                        # In Development (Implemented)
                        This task has been marked as **In Development**.
                        This has been verified to be **implemented** and can be downloaded [here]({}). 
                  ", "https://modrinth.com/plugin/typewriter/versions?c=beta"}),
        )
        .await?;
    Ok(())
}

async fn mark_task_as_broken(
    ctx: &Context,
    interaction: &mut ComponentInteraction,
    task_id: &str,
) -> Result<(), WinstonError> {
    update_task(
        task_id,
        UpdateTask {
            status: Some(TaskStatus::InProgress),
            ..Default::default()
        },
    )
    .await?;

    update_response(
        ctx,
        interaction,
        "Thanks for marking the task as broken! This helps us keep know that the task is still broken and needs to be fixed.",
    ).await;

    interaction
        .message
        .edit(
            ctx,
            EditMessage::default()
                .components(Vec::new())
                .content(formatdoc! {"
                    # Broken <@&{}>
                    This task has been marked as **Broken**. And has been moved back to the **In Progress**.
                    __Please indicate what is still broken, and what needs to be fixed.__
                  ", CONTRIBUTOR_ROLE_ID
                }),
        )
        .await?;

    Ok(())
}

pub async fn update_response(
    ctx: &Context,
    interaction: &ComponentInteraction,
    content: impl Into<String>,
) {
    let result = interaction
        .edit_response(ctx, EditInteractionResponse::default().content(content))
        .await;

    if let Err(e) = result {
        eprintln!("Failed to update response: {}", e);
    }
}

pub async fn check_permissions(
    ctx: &Context,
    component: &ComponentInteraction,
) -> Option<(GuildChannel, UserId)> {
    let Ok(channel) = component.channel_id.to_channel(&ctx).await else {
        update_response(
            &ctx,
            &component,
            "Something whent wrong with the button. It seems to be outdated.",
        )
        .await;
        eprintln!("No channel found");
        return None;
    };

    let Some(guild_channel) = channel.guild() else {
        update_response(
            &ctx,
            &component,
            "Something whent wrong with the button. It seems to be outdated.",
        )
        .await;
        eprintln!("No guild channel found");
        return None;
    };

    let Some(owner_id) = guild_channel.owner_id else {
        update_response(
            &ctx,
            &component,
            "Something whent wrong with the button. It seems to be outdated.",
        )
        .await;
        eprintln!("No owner found for channel: {}", guild_channel.name());
        return None;
    };

    let is_contributor = component
        .user
        .has_role(&ctx, guild_channel.guild_id, CONTRIBUTOR_ROLE_ID)
        .await
        .unwrap_or(false);

    // Only allow the owner to change the status, or a contributor
    if component.user.id != owner_id && !is_contributor {
        update_response(
            &ctx,
            &component,
            "Only the original ticket creator can do this.",
        )
        .await;
        return None;
    }

    Some((guild_channel, owner_id))
}
