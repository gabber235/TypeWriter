use std::fmt::Display;

use crate::{check_is_contributor, github::add_card, Context, WinstonError};
use poise::{
    serenity_prelude::{CreateEmbed, CreateMessage},
    CreateReply,
};

pub trait GithubIdentifiable {
    fn github_id(&self) -> String;
}

#[derive(Debug, poise::ChoiceParameter)]
pub enum CardType {
    #[name = "🐛 Bug"]
    Bug,
    #[name = "🚀 Feature"]
    Feature,
    #[name = "📖 Documentation"]
    Documentation,
}

impl Display for CardType {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        match self {
            CardType::Bug => write!(f, "🐛 Bug"),
            CardType::Feature => write!(f, "🚀 Feature"),
            CardType::Documentation => write!(f, "📖 Documentation"),
        }
    }
}

impl GithubIdentifiable for CardType {
    fn github_id(&self) -> String {
        match self {
            CardType::Bug => "ebef9534".to_string(),
            CardType::Feature => "3a3588b7".to_string(),
            CardType::Documentation => "91285c05".to_string(),
        }
    }
}

#[derive(Debug, poise::ChoiceParameter)]
pub enum CardPriority {
    #[name = "🌋 Urgent"]
    Urgent,
    #[name = "🏔 High"]
    High,
    #[name = "🏕 Medium"]
    Medium,
    #[name = "🏝 Low"]
    Low,
}

impl Display for CardPriority {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        match self {
            CardPriority::Urgent => write!(f, "🌋 Urgent"),
            CardPriority::High => write!(f, "🏔 High"),
            CardPriority::Medium => write!(f, "🏕 Medium"),
            CardPriority::Low => write!(f, "🏝 Low"),
        }
    }
}

impl GithubIdentifiable for CardPriority {
    fn github_id(&self) -> String {
        match self {
            CardPriority::Urgent => "06446d2a".to_string(),
            CardPriority::High => "d2448d33".to_string(),
            CardPriority::Medium => "a57b9bb8".to_string(),
            CardPriority::Low => "09f15830".to_string(),
        }
    }
}

#[derive(Debug, poise::ChoiceParameter)]
pub enum TimeEstimate {
    #[name = "🐋 Months"]
    Months,
    #[name = "🦑 Weeks"]
    Weeks,
    #[name = "🐂 Days"]
    Days,
    #[name = "🐇 Hours"]
    Hours,
    #[name = "🦔 Minutes"]
    Minutes,
}

impl Display for TimeEstimate {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        match self {
            TimeEstimate::Months => write!(f, "🐋 Months"),
            TimeEstimate::Weeks => write!(f, "🦑 Weeks"),
            TimeEstimate::Days => write!(f, "🐂 Days"),
            TimeEstimate::Hours => write!(f, "🐇 Hours"),
            TimeEstimate::Minutes => write!(f, "🦔 Minutes"),
        }
    }
}

impl GithubIdentifiable for TimeEstimate {
    fn github_id(&self) -> String {
        match self {
            TimeEstimate::Months => "cd5c7b63".to_string(),
            TimeEstimate::Weeks => "e286002b".to_string(),
            TimeEstimate::Days => "45c07fd0".to_string(),
            TimeEstimate::Hours => "474616a4".to_string(),
            TimeEstimate::Minutes => "a63ad48d".to_string(),
        }
    }
}

#[poise::command(slash_command, ephemeral, check = "check_is_contributor")]
pub async fn create_card(
    ctx: Context<'_>,
    #[description = "The title of the card"] title: String,
    #[description = "The description of the card"] description: String,
    #[description = "The type of card"] card_type: CardType,
    #[description = "The priority of this card"] priority: CardPriority,
    #[description = "The time estimate for this card"] time_estimate: TimeEstimate,
) -> Result<(), WinstonError> {
    let channel = ctx.channel_id();

    let handle = ctx
        .send(
            CreateReply::default()
                .embed(
                    embed_card_data(&title, &description, &card_type, &priority, &time_estimate)
                        .color(0x1F85DE),
                )
                .content("Creating card..."),
        )
        .await?;

    let result = add_card(
        &title,
        &description,
        &card_type,
        &time_estimate,
        &priority,
        channel.get().to_string(),
    )
    .await;

    if let Err(e) = result {
        handle
            .edit(
                ctx,
                CreateReply::default().content(format!("Failed to create card: {}", e)),
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
                .content("This ticket has been linked to the following card:")
                .embed(
                    embed_card_data(&title, &description, &card_type, &priority, &time_estimate)
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
    card_type: &CardType,
    priority: &CardPriority,
    time_estimate: &TimeEstimate,
) -> CreateEmbed {
    CreateEmbed::new()
        .title(title)
        .description(description)
        .field("Type", card_type.to_string(), true)
        .field("Priority", priority.to_string(), true)
        .field("Time Estimate", time_estimate.to_string(), true)
}
