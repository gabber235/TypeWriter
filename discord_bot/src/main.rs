use poise::serenity_prelude as serenity;

mod commands;
mod github;

use commands::*;

pub struct Data {} // User data, which is stored and accessible in all command invocations
pub type Context<'a> = poise::Context<'a, Data, WinstonError>;
pub type ApplicationContext<'a> = poise::ApplicationContext<'a, Data, WinstonError>;

const GUILD_ID: serenity::GuildId = serenity::GuildId::new(1054708062520360960);
const CONTRIBUTOR_ROLE_ID: serenity::RoleId = serenity::RoleId::new(1054708457535713350);
const GITHUB_PROJECT_ID: &str = "PVT_kwHOAPaj_s4AYpTR";

#[tokio::main]
async fn main() {
    dotenv::dotenv().ok();

    let github_token = std::env::var("GITHUB_TOKEN").expect("missing GITHUB_TOKEN");
    octocrab::initialise(
        octocrab::Octocrab::builder()
            .personal_token(github_token)
            .build()
            .expect("failed to initialise octocrab"),
    );

    let discord_token = std::env::var("DISCORD_TOKEN").expect("missing DISCORD_TOKEN");
    let intents = serenity::GatewayIntents::non_privileged();

    let framework = poise::Framework::builder()
        .options(poise::FrameworkOptions {
            commands: vec![create_card()],
            on_error: |error| Box::pin(on_error(error)),
            ..Default::default()
        })
        .setup(|ctx, _ready, framework| {
            Box::pin(async move {
                poise::builtins::register_globally(ctx, &framework.options().commands).await?;
                Ok(Data {})
            })
        })
        .build();

    let client = serenity::ClientBuilder::new(discord_token, intents)
        .framework(framework)
        .await;
    client.unwrap().start().await.unwrap();
}
async fn on_error(error: poise::FrameworkError<'_, Data, WinstonError>) {
    match error {
        poise::FrameworkError::Setup { error, .. } => panic!("Failed to start bot: {:?}", error),
        poise::FrameworkError::Command { error, ctx, .. } => {
            println!("Error in command `{}`: {:?}", ctx.command().name, error,);
        }
        error => {
            if let Err(e) = poise::builtins::on_error(error).await {
                println!("Error while handling error: {}", e)
            }
        }
    }
}
pub async fn check_is_contributor(ctx: Context<'_>) -> Result<bool, WinstonError> {
    Ok(ctx
        .author()
        .has_role(ctx, GUILD_ID, CONTRIBUTOR_ROLE_ID)
        .await?)
}

#[derive(thiserror::Error, Debug)]
pub enum WinstonError {
    #[error("Discord error: {0}")]
    Discord(#[from] serenity::Error),

    #[error("Octocrab error: {0}")]
    Octocrab(#[from] octocrab::Error),

    #[error("Github error: {0:?}")]
    GithubError(Vec<graphql_client::Error>),

    #[error("Query error: {0}")]
    QueryError(String),
}
