use serde::{Deserialize, Serialize};

use crate::{WinstonError, CLICKUP_LIST_ID, CLIENT};

use super::{
    post_comment, ClickupIdentifiable, TaskPriority, TaskSize, TaskStatus, TaskTag,
    CLICKUP_CUSTOM_DISCORD_FIELD_ID, CLICKUP_CUSTOM_SIZE_FIELD_ID,
};

#[derive(Debug, Serialize)]
pub struct CreateTaskRequest {
    name: String,
    description: String,
    tags: Vec<String>,
    status: String,
    priority: u8,
    custom_fields: Vec<CustomField>,
}

#[derive(Debug, Serialize)]
pub struct CustomField {
    id: String,
    value: String,
}

#[derive(Debug, Deserialize)]
pub struct CreateTaskResponse {
    id: String,
}

pub async fn create_task_in_clickup(
    title: &str,
    description: &str,
    priority: &TaskPriority,
    size: &TaskSize,
    tags: &[TaskTag],
    discord_channel_id: String,
    discord_channel_url: String,
) -> Result<String, WinstonError> {
    let request = CreateTaskRequest {
        name: title.to_string(),
        description: description.to_string(),
        tags: tags.into_iter().map(|tag| tag.raw_string()).collect(),
        status: TaskStatus::Backlog.raw_string(),
        priority: priority.into(),
        custom_fields: vec![
            CustomField {
                id: CLICKUP_CUSTOM_SIZE_FIELD_ID.to_string(),
                value: size.clickup_id(),
            },
            CustomField {
                id: CLICKUP_CUSTOM_DISCORD_FIELD_ID.to_string(),
                value: discord_channel_id,
            },
        ],
    };
    let result = CLIENT
        .post(format!(
            "https://api.clickup.com/api/v2/list/{}/task",
            CLICKUP_LIST_ID
        ))
        .json(&request)
        .send()
        .await?;

    if !result.status().is_success() {
        return Err(WinstonError::ClickupApiError(
            result.status().as_u16(),
            result.text().await?,
        ));
    }

    let response: CreateTaskResponse = result.json().await?;
    let url = format!(
        "https://sharing.clickup.com/9015308602/v/6-901502296591-2/t/h/{}/a4c4c9d59ec667d",
        response.id
    );

    post_comment(&response.id, &discord_channel_url).await?;

    Ok(url)
}
