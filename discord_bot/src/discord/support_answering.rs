use poise::{serenity_prelude::EditThread, CreateReply};

use crate::{check_is_contributor, webhooks::GetTagId, Context, WinstonError};

#[poise::command(slash_command, ephemeral, check = "check_is_contributor")]
pub async fn support_answering(
    ctx: Context<'_>,
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
