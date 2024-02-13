use url::Url;

use crate::{WinstonError, CLICKUP_LIST_ID, CLIENT};

use super::{Task, TaskStatus};

pub struct TasksQuery {
    pub statuses: Vec<TaskStatus>,
}

#[derive(Debug, serde::Deserialize)]
pub struct TaskListings {
    pub tasks: Vec<TaskListing>,
}

#[derive(Debug, serde::Deserialize)]
pub struct TaskListing {
    pub id: String,
}

pub async fn get_tasks(query: TasksQuery) -> Result<TaskListings, WinstonError> {
    let mut params = vec![];

    for status in query.statuses {
        params.push(("statuses[]", status.raw_string()));
    }

    let url = Url::parse_with_params(
        &format!(
            "https://api.clickup.com/api/v2/list/{}/task",
            CLICKUP_LIST_ID
        ),
        params,
    )?;

    let result = CLIENT.get(url).send().await?;

    if !result.status().is_success() {
        return Err(WinstonError::ClickupApiError(
            result.status().as_u16(),
            result.text().await?,
        ));
    }

    let text = result.text().await?;

    match serde_json::from_str::<TaskListings>(&text) {
        Ok(tasks) => Ok(tasks),
        Err(e) => {
            eprintln!("failed to deserialize task: {}", e);
            eprintln!("response: {}", text);
            Err(WinstonError::ParseJson(e))
        }
    }
}
