use async_trait::async_trait;
use indoc::formatdoc;
use poise::serenity_prelude::{
    ComponentInteraction, Context, CreateEmbed, CreateInteractionResponseFollowup, CreateMessage,
    CreateQuickModal, EditMessage, EditThread, EventHandler, Interaction, Timestamp,
};

use crate::{check_permissions, CONTRIBUTOR_ROLE_ID};

pub struct TicketReopenHandler;

#[async_trait]
impl EventHandler for TicketReopenHandler {
    async fn interaction_create(&self, ctx: Context, interaction: Interaction) {
        let Some(mut component) = interaction.message_component() else {
            return;
        };
        let custom_id = &component.data.custom_id;
        if custom_id != "reopen_ticket" {
            return;
        }

        let Some((mut guild_channel, _owner_id)) = check_permissions(&ctx, &component).await else {
            return;
        };

        let responds = component
            .quick_modal(
                &ctx,
                CreateQuickModal::new("Why do you want to reopen this ticket?")
                    .paragraph_field("Reason"),
            )
            .await;

        let responds = match responds {
            Ok(Some(responds)) if responds.inputs.len() > 0 => responds,
            Err(err) => {
                send_followup(&ctx, &component, "Failed to open modal").await;
                eprintln!("Failed to open modal: {}", err);
                return;
            }
            _ => {
                send_followup(
                    &ctx,
                    &component,
                    "You need to supply a reason to reopen the ticket",
                )
                .await;
                return;
            }
        };

        if let Err(e) = responds
            .interaction
            .create_response(
                &ctx,
                poise::serenity_prelude::CreateInteractionResponse::Acknowledge,
            )
            .await
        {
            eprintln!("Failed to acknowledge interaction: {}", e);
            return;
        }

        let reason = responds.inputs[0].as_str();

        if let Err(e) = guild_channel
            .edit_thread(
                &ctx,
                EditThread::default()
                    .applied_tags([])
                    .archived(false)
                    .locked(false),
            )
            .await
        {
            send_followup(&ctx, &component, "Failed to reopen ticket").await;
            eprintln!("Failed to edit thread: {}", e);
            return;
        }

        let embed = CreateEmbed::default()
            .title("Ticket Reopened")
            .color(0x8c44ff)
            .description(formatdoc! {"
                <@&{}> This ticket has been reopened.

                **Reason**: 
                {}
                ", CONTRIBUTOR_ROLE_ID, reason})
            .timestamp(Timestamp::now());

        if let Err(e) = component
            .message
            .channel_id
            .send_message(
                &ctx,
                CreateMessage::default()
                    .embed(embed)
                    .content(format!("<@&{}>", CONTRIBUTOR_ROLE_ID)),
            )
            .await
        {
            send_followup(&ctx, &component, "Failed to reopen ticket").await;
            eprintln!("Failed to send new message: {}", e);
            return;
        }

        if let Err(e) = component.message.delete(&ctx).await {
            send_followup(&ctx, &component, "Failed to reopen ticket").await;
            eprintln!("Failed to delete old message: {}", e);
            return;
        }
    }
}

async fn send_followup(ctx: &Context, interaction: &ComponentInteraction, content: &str) {
    if let Err(e) = interaction
        .create_followup(
            &ctx,
            CreateInteractionResponseFollowup::default()
                .content(content)
                .ephemeral(true),
        )
        .await
    {
        eprintln!("Failed to send followup: {}", e);
    }
}
