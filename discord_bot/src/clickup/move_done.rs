use crate::WinstonError;

use super::{get_tasks, update_task, TaskStatus, TasksQuery, UpdateTask};

pub async fn move_done_to_beta() -> Result<(), WinstonError> {
    let tasks = get_tasks(TasksQuery {
        statuses: vec![TaskStatus::Done],
    })
    .await?;

    for task in tasks {
        update_task(
            &task.id,
            UpdateTask {
                status: Some(TaskStatus::InBeta),
                ..Default::default()
            },
        )
        .await?;
    }

    Ok(())
}
