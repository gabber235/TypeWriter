use async_trait::async_trait;
use indoc::formatdoc;
use poise::serenity_prelude::{
    Context, CreateEmbed, CreateInteractionResponseFollowup, CreateQuickModal, EditMessage,
    EditThread, EventHandler, Interaction, Timestamp,
};

use crate::{check_permissions, update_response, CONTRIBUTOR_ROLE_ID};

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
                update_response(&ctx, &component, "Failed to open modal").await;
                eprintln!("Failed to open modal: {}", err);
                return;
            }
            _ => {
                update_response(
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
            .create_followup(
                &ctx,
                CreateInteractionResponseFollowup::default().content("Reopening ticket..."),
            )
            .await
        {
            update_response(&ctx, &component, "Failed to reopen ticket").await;
            eprintln!("Failed to reopen ticket: {}", e);
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
            update_response(&ctx, &component, "Failed to reopen ticket").await;
            eprintln!("Failed to reopen ticket: {}", e);
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
            .edit(&ctx, EditMessage::default().embed(embed).components(vec![]))
            .await
        {
            update_response(&ctx, &component, "Failed to reopen ticket").await;
            eprintln!("Failed to reopen ticket: {}", e);
            return;
        }
    }
}
