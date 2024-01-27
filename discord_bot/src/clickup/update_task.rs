use serde::Serialize;

use crate::WinstonError;

use super::{Task, TaskStatus};

#[derive(Debug, Default)]
pub struct UpdateTask {
    pub status: Option<TaskStatus>,
}

#[derive(Debug, Serialize)]
struct UpdateTaskRequest {
    #[serde(skip_serializing_if = "Option::is_none")]
    status: Option<String>,
}

pub async fn update_task(task_id: &str, update: UpdateTask) -> Result<Task, WinstonError> {
    let request = UpdateTaskRequest {
        status: update.status.map(|status| status.raw_string()),
    };

    let result = crate::CLIENT
        .put(format!("https://api.clickup.com/api/v2/task/{}", task_id))
        .json(&request)
        .send()
        .await?;

    if !result.status().is_success() {
        return Err(WinstonError::ClickupApiError(
            result.status().as_u16(),
            result.text().await?,
        ));
    }

    let task: Task = result.json().await?;

    Ok(task)
}
