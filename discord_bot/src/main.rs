use std::sync::Arc;

use actix_web::{middleware::Logger, App, HttpServer};
use once_cell::sync::Lazy;
use poise::serenity_prelude as serenity;
use tokio::signal;
use tokio_util::sync::CancellationToken;

mod clickup;
mod commands;
mod webhook;
mod webhooks;

use commands::*;
use reqwest::{
    header::{HeaderMap, HeaderValue, AUTHORIZATION},
    Client,
};
use webhook::clickup_webhook;

use crate::webhook::{publish_beta_version, webhook_get};

pub struct Data {} // User data, which is stored and accessible in all command invocations
pub type Context<'a> = poise::Context<'a, Data, WinstonError>;
pub type ApplicationContext<'a> = poise::ApplicationContext<'a, Data, WinstonError>;

const GUILD_ID: serenity::GuildId = serenity::GuildId::new(1054708062520360960);
const CONTRIBUTOR_ROLE_ID: serenity::RoleId = serenity::RoleId::new(1054708457535713350);
const TICKET_FORUM_ID: u64 = 1199700329948782613;

const CLICKUP_LIST_ID: &str = "901502296591";
const CLICKUP_USER_ID: u32 = 62541886;

static CLIENT: Lazy<Client> = Lazy::new(|| {
    let mut headers = HeaderMap::new();

    headers.insert(
        reqwest::header::CONTENT_TYPE,
        HeaderValue::from_static("application/json"),
    );

    let mut auth_value = HeaderValue::from_str(
        std::env::var("CLICKUP_TOKEN")
            .expect("missing CLICKUP_TOKEN")
            .as_str(),
    )
    .expect("failed to create header value");
    auth_value.set_sensitive(true);
    headers.insert(AUTHORIZATION, auth_value);

    Client::builder()
        .default_headers(headers)
        .build()
        .expect("failed to build reqwest client")
});

static DISCORD_CLIENT: Lazy<arc_swap::ArcSwap<Option<serenity::Context>>> =
    Lazy::new(|| arc_swap::ArcSwap::from_pointee(None));

#[tokio::main]
async fn main() {
    dotenv::dotenv().ok();

    let token = CancellationToken::new();
    let webhook_token = token.clone();
    let discord_token = token.clone();

    let webhook_task = tokio::spawn(async move {
        tokio::select! {
            _ = webhook_token.cancelled() => {}
            _ = startup_webhook() => {}
        }
    });

    let discord_task = tokio::spawn(async move {
        tokio::select! {
            _ = discord_token.cancelled() => {}
            _ = startup_discord_bot() => {}
        }
    });

    match signal::ctrl_c().await {
        Ok(()) => {
            println!("\nShutting down...");
            token.cancel();
        }
        Err(err) => {
            eprintln!("Unable to listen for shutdown signal: {}", err);
            token.cancel();
        }
    }

    tokio::join!(webhook_task, discord_task).0.unwrap();
}

async fn startup_webhook() {
    // env_logger::init_from_env(env_logger::Env::new().default_filter_or("info"));
    println!("Starting webhook server...");
    HttpServer::new(|| {
        App::new()
            .wrap(Logger::default())
            .service(webhook_get)
            .service(publish_beta_version)
            .service(clickup_webhook)
    })
    .bind("0.0.0.0:8080")
    .expect("failed to bind server")
    .run()
    .await
    .expect("failed to run server");
}

async fn startup_discord_bot() {
    let discord_token = std::env::var("DISCORD_TOKEN").expect("missing DISCORD_TOKEN");
    let intents = serenity::GatewayIntents::non_privileged();

    let framework = poise::Framework::builder()
        .options(poise::FrameworkOptions {
            commands: vec![create_task(), close_ticket()],
            on_error: |error| Box::pin(on_error(error)),
            ..Default::default()
        })
        .setup(|ctx, _ready, framework| {
            Box::pin(async move {
                poise::builtins::register_globally(ctx, &framework.options().commands).await?;
                DISCORD_CLIENT.store(Arc::from(Some(ctx.clone())));
                Ok(Data {})
            })
        })
        .build();

    let client = serenity::ClientBuilder::new(discord_token, intents)
        .event_handler(TaskFixedHandler)
        .event_handler(TicketReopenHandler)
        .event_handler(ThreadArchivingHandler)
        .framework(framework)
        .await;

    println!("Starting bot...");

    client
        .unwrap()
        .start()
        .await
        .expect("failed to start discord bot");
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
    let has_role = ctx
        .author()
        .has_role(ctx, GUILD_ID, CONTRIBUTOR_ROLE_ID)
        .await?;

    if !has_role {
        eprintln!(
            "User {} is not a contributor and tried to run command",
            ctx.author().name
        );
        return Ok(false);
    }
    return Ok(true);
}

pub fn get_discord() -> Result<serenity::Context, WinstonError> {
    match DISCORD_CLIENT.load().as_ref() {
        Some(discord) => Ok(discord.clone()),
        None => Err(WinstonError::DiscordClientNotInitialized),
    }
}

#[derive(thiserror::Error, Debug)]
pub enum WinstonError {
    #[error("Discord error: {0}")]
    Discord(#[from] serenity::Error),

    #[error("Reqwest error: {0}")]
    Reqwest(#[from] reqwest::Error),

    #[error("Query error: {0}")]
    QueryError(String),

    #[error("Clickup API error: {0}: {1}")]
    ClickupApiError(u16, String),

    #[error("Discord client not initialized")]
    DiscordClientNotInitialized,

    #[error("Not a guild channel")]
    NotAGuildChannel,

    #[error("Failed to parse int: {0}")]
    ParseInt(#[from] std::num::ParseIntError),

    #[error("Failed to parse json: {0}")]
    ParseJson(#[from] serde_json::Error),

    #[error("Failed to parse url: {0}")]
    ParseUrl(#[from] url::ParseError),

    #[error("Tag not found: {0}")]
    TagNotFound(String),
}
