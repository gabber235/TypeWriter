use crate::{
    check_is_contributor,
    clickup::{create_task_in_clickup, TaskPriority, TaskSize, TaskType},
    Context, WinstonError,
};
use poise::{
    serenity_prelude::{CreateEmbed, CreateMessage, EditThread},
    CreateReply,
};

#[poise::command(slash_command, ephemeral, check = "check_is_contributor")]
pub async fn create_task(
    ctx: Context<'_>,
    #[description = "The title of the task"] title: String,
    #[description = "The description of the task"] description: String,
    #[description = "The priority of this task"] priority: TaskPriority,
    #[description = "The time estimate for this task"] size: TaskSize,
    #[description = "The type of the task"] task_type: TaskType,
) -> Result<(), WinstonError> {
    let channel = ctx.channel_id();

    let handle = ctx
        .send(
            CreateReply::default()
                .embed(
                    embed_card_data(&title, &description, &priority, &size, &task_type)
                        .color(0x1F85DE),
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
        &task_type,
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
                    embed_card_data(&title, &description, &priority, &size, &task_type)
                        .color(0x78ee5c)
                        .url(url),
                ),
        )
        .await?;

    channel
        .edit_thread(ctx, EditThread::default().name(title))
        .await?;

    Ok(())
}

fn embed_card_data(
    title: &str,
    description: &str,
    priority: &TaskPriority,
    time_estimate: &TaskSize,
    task_type: &TaskType,
) -> CreateEmbed {
    CreateEmbed::new()
        .title(title)
        .description(description)
        .field("Priority", priority.to_string(), true)
        .field("Time Estimate", time_estimate.to_string(), true)
        .field("Type", task_type.to_string(), true)
}
