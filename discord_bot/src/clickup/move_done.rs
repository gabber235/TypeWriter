use crate::WinstonError;

use super::{get_tasks, update_task, TaskStatus, TasksQuery, UpdateTask};

pub async fn move_done_to_beta() -> Result<(), WinstonError> {
    let tasks = get_tasks(TasksQuery {
        statuses: vec![TaskStatus::Done],
    })
    .await?;

    println!(
        "Moving tasks to beta: {}",
        tasks
            .tasks
            .iter()
            .map(|task| task.id.clone())
            .collect::<Vec<_>>()
            .join(", ")
    );

    for task in tasks.tasks {
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
