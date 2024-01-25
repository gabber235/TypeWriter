use graphql_client::{GraphQLQuery, Response};

use crate::commands::*;
use crate::{WinstonError, GITHUB_PROJECT_ID};

#[derive(GraphQLQuery)]
#[graphql(
    schema_path = "src/github/github_schema.graphql",
    query_path = "src/github/create_card/create_card.graphql",
    variables_derives = "Clone, Debug",
    response_derives = "Clone, Debug"
)]
pub struct CreateCard {}

#[derive(GraphQLQuery)]
#[graphql(
    schema_path = "src/github/github_schema.graphql",
    query_path = "src/github/create_card/create_card.graphql",
    variables_derives = "Clone, Debug",
    response_derives = "Clone, Debug"
)]
pub struct SetupCardFieldValues {}

pub async fn add_card(
    title: &str,
    body: &str,
    card_type: &CardType,
    time_estimate: &TimeEstimate,
    priority: &CardPriority,
    discord_channel_id: String,
) -> Result<String, WinstonError> {
    let vars = create_card::Variables {
        project_id: GITHUB_PROJECT_ID.to_string(),
        title: title.to_string(),
        body: Some(body.to_string()),
    };

    let responds: Response<create_card::ResponseData> = octocrab::instance()
        .graphql(&CreateCard::build_query(vars))
        .await?;

    if let Some(errors) = responds.errors {
        return Err(WinstonError::GithubError(errors));
    }

    let Some(card) = responds.data else {
        return Err(WinstonError::QueryError("No card returned".to_string()));
    };

    let item = card
        .add_project_v2_draft_issue
        .and_then(|i| i.project_item)
        .ok_or(WinstonError::QueryError("No card returned".to_string()))?;

    let vars = setup_card_field_values::Variables {
        project_id: GITHUB_PROJECT_ID.to_string(),
        item_id: item.id.to_string(),
        type_id: card_type.github_id(),
        size_id: time_estimate.github_id(),
        priority_id: priority.github_id(),
        discord_channel_id,
    };

    let responds: Response<setup_card_field_values::ResponseData> = octocrab::instance()
        .graphql(&SetupCardFieldValues::build_query(vars))
        .await?;

    if let Some(errors) = responds.errors {
        return Err(WinstonError::GithubError(errors));
    }

    let url = format!(
        "https://github.com/users/gabber235/projects/2?pane=issue&itemId={}",
        item.database_id.unwrap_or(0)
    );

    Ok(url)
}
