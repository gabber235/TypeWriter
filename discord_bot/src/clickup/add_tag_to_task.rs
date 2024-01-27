use crate::{WinstonError, CLIENT};

pub async fn add_tag_to_task(task_id: &str, tag_name: &str) -> Result<(), WinstonError> {
    let result = CLIENT
        .post(format!(
            "https://api.clickup.com/api/v2/task/{}/tag/{}",
            task_id, tag_name
        ))
        .send()
        .await?;

    if !result.status().is_success() {
        return Err(WinstonError::ClickupApiError(
            result.status().as_u16(),
            result.text().await?,
        ));
    }

    Ok(())
}
