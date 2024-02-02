use serde::{Deserialize, Serialize};

use crate::{WinstonError, CLICKUP_USER_ID, CLIENT};

#[derive(Debug, Serialize)]
struct CommentRequest {
    comment_text: String,
    assignee: u32,
    notify_all: bool,
}

#[derive(Debug, Deserialize)]
pub struct CommentResponse {
    pub id: u64,
    pub hist_id: String,
    pub date: u64,
}

pub async fn post_comment(task_id: &str, comment: &str) -> Result<CommentResponse, WinstonError> {
    let data = CommentRequest {
        comment_text: comment.to_owned(),
        assignee: CLICKUP_USER_ID,
        notify_all: false,
    };
    let result = CLIENT
        .post(format!(
            "https://api.clickup.com/api/v2/task/{}/comment",
            task_id
        ))
        .json(&data)
        .send()
        .await?;

    if !result.status().is_success() {
        return Err(WinstonError::ClickupApiError(
            result.status().as_u16(),
            result.text().await?,
        ));
    }

    let comment: CommentResponse = result.json().await?;

    Ok(comment)
}
