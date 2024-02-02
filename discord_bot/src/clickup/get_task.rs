use crate::{webhook::TaskTag, WinstonError, CLIENT};
use serde::{Deserialize, Serialize};
use serde_json::Value;
use strum::IntoEnumIterator;

use super::{ClickupIdentifiable, TaskStatus};

pub async fn get_task_from_clickup(task_id: &str) -> Result<Task, WinstonError> {
    let result = CLIENT
        .get(format!("https://api.clickup.com/api/v2/task/{}", task_id))
        .send()
        .await?;

    if !result.status().is_success() {
        return Err(WinstonError::ClickupApiError(
            result.status().as_u16(),
            result.text().await?,
        ));
    }

    let text = result.text().await?;

    match serde_json::from_str::<Task>(&text) {
        Ok(task) => Ok(task),
        Err(e) => {
            eprintln!("failed to deserialize task: {}", e);
            eprintln!("response: {}", text);
            Err(WinstonError::ParseJson(e))
        }
    }
}

#[derive(Default, Debug, Clone, PartialEq, Serialize, Deserialize)]
pub struct Task {
    pub id: String,
    // pub custom_id: Value,
    // pub custom_item_id: i64,
    pub name: String,
    pub text_content: String,
    pub description: String,
    pub status: Status,
    // pub date_created: String,
    // pub date_updated: String,
    // pub date_closed: Value,
    // pub date_done: Value,
    pub archived: bool,
    // pub creator: User,
    // pub assignees: Vec<Value>,
    // pub watchers: Vec<User>,
    // pub checklists: Vec<Value>,
    pub tags: Vec<TaskTag>,
    // pub parent: Value,
    // pub priority: Priority,
    // pub due_date: Value,
    // pub start_date: Value,
    // pub points: Value,
    // pub time_estimate: Value,
    // pub time_spent: i64,
    pub custom_fields: Vec<CustomField>,
    // pub dependencies: Vec<Value>,
    // pub linked_tasks: Vec<Value>,
    // pub locations: Vec<Value>,
    // pub team_id: String,
    // pub url: String,
    // pub sharing: Sharing,
    // pub permission_level: String,
    // pub list: List,
    // pub project: Project,
    // pub folder: Folder,
    // pub space: Space,
    // pub attachments: Vec<Value>,
}

#[derive(Default, Debug, Clone, PartialEq, Serialize, Deserialize)]
pub struct Status {
    pub id: String,
    pub status: String,
    pub color: String,
    pub orderindex: i64,
    #[serde(rename = "type")]
    pub type_field: String,
}

impl From<&Status> for TaskStatus {
    fn from(status: &Status) -> Self {
        TaskStatus::iter()
            .find(|s| s.clickup_id() == status.id)
            .expect(format!("Unknown status: {}", status.id).as_str())
    }
}

#[derive(Default, Debug, Clone, PartialEq, Serialize, Deserialize)]
pub struct Priority {
    pub color: String,
    pub id: String,
    pub orderindex: String,
    pub priority: String,
}

#[derive(Default, Debug, Clone, PartialEq, Serialize, Deserialize)]
pub struct CustomField {
    pub id: String,
    pub name: String,
    #[serde(rename = "type")]
    pub type_field: String,
    pub type_config: TypeConfig,
    pub date_created: String,
    pub hide_from_guests: bool,
    pub value: Option<Value>,
    pub required: bool,
}

#[derive(Default, Debug, Clone, PartialEq, Serialize, Deserialize)]
pub struct TypeConfig {
    pub new_drop_down: Option<bool>,
    pub options: Option<Vec<TaskOption>>,
}

#[derive(Default, Debug, Clone, PartialEq, Serialize, Deserialize)]
pub struct TaskOption {
    pub id: String,
    pub name: String,
    pub color: String,
    pub orderindex: i64,
}

#[derive(Default, Debug, Clone, PartialEq, Serialize, Deserialize)]
pub struct Sharing {
    pub public: bool,
    pub public_share_expires_on: Value,
    pub public_fields: Vec<String>,
    pub token: Value,
    pub seo_optimized: bool,
}

#[derive(Default, Debug, Clone, PartialEq, Serialize, Deserialize)]
pub struct List {
    pub id: String,
    pub name: String,
    pub access: bool,
}

#[derive(Default, Debug, Clone, PartialEq, Serialize, Deserialize)]
pub struct Project {
    pub id: String,
    pub name: String,
    pub hidden: bool,
    pub access: bool,
}

#[derive(Default, Debug, Clone, PartialEq, Serialize, Deserialize)]
pub struct Folder {
    pub id: String,
    pub name: String,
    pub hidden: bool,
    pub access: bool,
}

#[derive(Default, Debug, Clone, PartialEq, Serialize, Deserialize)]
pub struct Space {
    pub id: String,
}
