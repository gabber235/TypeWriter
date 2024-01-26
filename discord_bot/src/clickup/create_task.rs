use serde::{Deserialize, Serialize};

use crate::{WinstonError, CLICKUP_LIST_ID, CLIENT};

use super::{
    ClickupIdentifiable, TaskPriority, TaskSize, TaskStatus, TaskTag,
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
    url: String,
}

pub async fn create_task_in_clickup(
    title: &str,
    description: &str,
    priority: &TaskPriority,
    size: &TaskSize,
    tags: &[TaskTag],
    discord_channel_id: String,
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

    // let text = result.text().await?;
    // println!("{}", text);
    //
    // Ok("".to_string())
    let response: CreateTaskResponse = result.json().await?;

    Ok(response.url)
}
