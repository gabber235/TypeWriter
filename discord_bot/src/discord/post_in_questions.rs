use crate::{check_is_support, Context, WinstonError, QUESTIONS_CHANNEL};
use indoc::formatdoc;
use poise::serenity_prelude::Mentionable;

#[poise::command(
    context_menu_command = "Post In Questions",
    ephemeral,
    check = "check_is_support"
)]
pub async fn post_in_questions(
    ctx: Context<'_>,
    #[description = "The message to reply to"] message: poise::serenity_prelude::Message,
) -> Result<(), WinstonError> {
    message
        .reply_ping(
            ctx,
            formatdoc! { "
                Hey {author}, thanks for asking your question!

                To keep track of all the different questions we get, we request that you post your questions in the {questions_channel} channel.

                We will be happy to help you out over there!
            ", author = message.author.mention(), questions_channel = QUESTIONS_CHANNEL.mention() }
        )
        .await?;

    ctx.reply("Replied to the question with").await?;
    Ok(())
}

#[poise::command(
    context_menu_command = "Post Bug In Questions",
    ephemeral,
    check = "check_is_support"
)]
pub async fn post_bug_in_questions(
    ctx: Context<'_>,
    #[description = "The message to reply to"] message: poise::serenity_prelude::Message,
) -> Result<(), WinstonError> {
    message
        .reply_ping(
            ctx,
            formatdoc! { "
                Hey {author}, thanks for reporting a bug!

                To keep track of all the different bugs we get, we request that you post your bug in the {questions_channel} channel.

                We will be happy to help you out over there!
            ", author = message.author.mention(), questions_channel = QUESTIONS_CHANNEL.mention() }
        )
        .await?;

    ctx.reply("Replied to the bug with").await?;
    Ok(())
}

#[poise::command(
    context_menu_command = "Post Suggestion In Questions",
    ephemeral,
    check = "check_is_support"
)]
pub async fn post_suggestion_in_questions(
    ctx: Context<'_>,
    #[description = "The message to reply to"] message: poise::serenity_prelude::Message,
) -> Result<(), WinstonError> {
    message
        .reply_ping(
            ctx,
            formatdoc! { "
                Hey {author}, thanks for suggesting a feature!

                To keep track of all the different suggestions we get, we request that you post your suggestion in the {questions_channel} channel.

                We will be happy to help you out over there!
            ", author = message.author.mention(), questions_channel = QUESTIONS_CHANNEL.mention() }
        )
        .await?;

    ctx.reply("Replied to the suggestion with").await?;
    Ok(())
}
