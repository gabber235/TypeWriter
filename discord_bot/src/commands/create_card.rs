use crate::{
    check_is_contributor,
    clickup::{create_task_in_clickup, TaskPriority, TaskSize, TaskTag},
    Context, WinstonError,
};
use poise::{
    serenity_prelude::{CreateEmbed, CreateMessage},
    CreateReply,
};

#[poise::command(slash_command, ephemeral, check = "check_is_contributor")]
pub async fn create_task(
    ctx: Context<'_>,
    #[description = "The title of the task"] title: String,
    #[description = "The description of the task"] description: String,
    #[description = "The priority of this task"] priority: TaskPriority,
    #[description = "The time estimate for this task"] size: TaskSize,
    // Sadly discord doesn't support multiple tags yet
    #[description = "All the tags this task has"] tags: TaskTag,
) -> Result<(), WinstonError> {
    let channel = ctx.channel_id();

    let tags = [tags];

    let handle = ctx
        .send(
            CreateReply::default()
                .embed(
                    embed_card_data(&title, &description, &priority, &size, &tags).color(0x1F85DE),
                )
                .content("Creating task..."),
        )
        .await?;

    let channel_id = channel.get().to_string();
    let guild_id = channel
        .to_channel(ctx)
        .await?
        .guild()
        .ok_or(WinstonError::NotAGuildChannel)?
        .guild_id
        .to_string();

    let url = format!("https://discord.com/channels/{}/{}", guild_id, channel_id);

    let result = create_task_in_clickup(
        &title,
        &description,
        &priority,
        &size,
        &tags,
        channel_id,
        url,
    )
    .await;

    if let Err(e) = result {
        handle
            .edit(
                ctx,
                CreateReply::default().content(format!("Failed to create task: {}", e)),
            )
            .await?;
        return Err(e);
    }

    let url = result.unwrap();

    handle.delete(ctx).await?;

    channel
        .send_message(
            ctx,
            CreateMessage::default()
                .content("This ticket has been linked to the following task:")
                .embed(
                    embed_card_data(&title, &description, &priority, &size, &tags)
                        .color(0x78ee5c)
                        .url(url),
                ),
        )
        .await?;

    Ok(())
}

fn embed_card_data(
    title: &str,
    description: &str,
    priority: &TaskPriority,
    time_estimate: &TaskSize,
    tags: &[TaskTag],
) -> CreateEmbed {
    CreateEmbed::new()
        .title(title)
        .description(description)
        .field("Priority", priority.to_string(), true)
        .field("Time Estimate", time_estimate.to_string(), true)
        .field(
            "Tags",
            tags.iter()
                .map(|tag| tag.to_string())
                .collect::<Vec<String>>()
                .join(", "),
            false,
        )
}
