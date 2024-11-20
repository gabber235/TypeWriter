use crate::{check_is_support, Context, WinstonError};
use poise::CreateReply;
use std::fmt::{self, Display, Formatter};

#[derive(Debug, poise::ChoiceParameter)]
pub enum Message {
    #[doc = "mclogs"]
    McLogs,
    #[doc = "somethingelse"]
    Somethingelse,
    #[doc = "idk"]
    Idk,
}

impl Display for Message {
    fn fmt(&self, f: &mut Formatter<'_>) -> fmt::Result {
        match self {
            Message::McLogs => write!(f, "mclogs"),
            Message::Somethingelse => write!(f, "somethingelse"),
            Message::Idk => write!(f, "idk"),
            _ => write!(f, "unknown message type"),
        }
    }
}

#[poise::command(
    slash_command,
    ephemeral,
    check = "check_is_support"
)]
pub async fn follow_up(
    ctx: Context<'_>,
    #[description = "The reason for closing the ticket"] reason: Message,
) -> Result<(), WinstonError> {
    let _handle = ctx
        .send(CreateReply::default().content(format! ("Reason: {reason}")))
        .await?;

    Ok(())
}