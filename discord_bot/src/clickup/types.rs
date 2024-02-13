use std::fmt::Display;

use strum::EnumIter;

pub trait ClickupIdentifiable {
    fn clickup_id(&self) -> String;
}

#[derive(Debug, poise::ChoiceParameter, EnumIter, Hash, PartialEq, Eq, Clone)]
pub enum TaskStatus {
    #[name = "📋 Backlog"]
    Backlog,
    #[name = "🏗 In progress"]
    InProgress,
    #[name = "✅ Done"]
    Done,
    #[name = "👀 In beta"]
    InBeta,
    #[name = "🚀 In production"]
    InProduction,
}

impl TaskStatus {
    pub fn raw_string(&self) -> String {
        match self {
            TaskStatus::Backlog => "backlog".to_string(),
            TaskStatus::InProgress => "in progress".to_string(),
            TaskStatus::Done => "done".to_string(),
            TaskStatus::InBeta => "in beta".to_string(),
            TaskStatus::InProduction => "in production".to_string(),
        }
    }
}

impl Display for TaskStatus {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        match self {
            TaskStatus::Backlog => write!(f, "📋 Backlog"),
            TaskStatus::InProgress => write!(f, "🏗 In progress"),
            TaskStatus::Done => write!(f, "✅ Done"),
            TaskStatus::InBeta => write!(f, "👀 In beta"),
            TaskStatus::InProduction => write!(f, "🚀 In production"),
        }
    }
}

impl ClickupIdentifiable for TaskStatus {
    fn clickup_id(&self) -> String {
        match self {
            TaskStatus::Backlog => "p90150987376_OOQuHfTS".to_string(),
            TaskStatus::InProgress => "p90150987376_2VuuCTca".to_string(),
            TaskStatus::Done => "p90150987376_i5JDI7b8".to_string(),
            TaskStatus::InBeta => "p90150987376_rgJ7Kpgr".to_string(),
            TaskStatus::InProduction => "p90150987376_gaWBoToF".to_string(),
        }
    }
}

#[derive(Debug, poise::ChoiceParameter)]
pub enum TaskTag {
    #[name = "🐛 Bug"]
    Bug,
    #[name = "🎁 Feature"]
    Feature,
    #[name = "📖 Documentation"]
    Documentation,
}

impl Display for TaskTag {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        match self {
            TaskTag::Bug => write!(f, "🐛 Bug"),
            TaskTag::Feature => write!(f, "🎁 Feature"),
            TaskTag::Documentation => write!(f, "📖 Documentation"),
        }
    }
}

impl TaskTag {
    pub fn raw_string(&self) -> String {
        match self {
            TaskTag::Bug => "bug".to_string(),
            TaskTag::Feature => "feature".to_string(),
            TaskTag::Documentation => "documentation".to_string(),
        }
    }
}

#[derive(Debug, poise::ChoiceParameter)]
pub enum TaskPriority {
    #[name = "🌋 Urgent"]
    Urgent,
    #[name = "🏔 High"]
    High,
    #[name = "🏕 Medium"]
    Medium,
    #[name = "🏝 Low"]
    Low,
}

impl Display for TaskPriority {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        match self {
            TaskPriority::Urgent => write!(f, "🌋 Urgent"),
            TaskPriority::High => write!(f, "🏔 High"),
            TaskPriority::Medium => write!(f, "🏕 Medium"),
            TaskPriority::Low => write!(f, "🏝 Low"),
        }
    }
}

impl From<&TaskPriority> for u8 {
    fn from(priority: &TaskPriority) -> Self {
        match priority {
            TaskPriority::Urgent => 1,
            TaskPriority::High => 2,
            TaskPriority::Medium => 3,
            TaskPriority::Low => 4,
        }
    }
}

pub const CLICKUP_CUSTOM_SIZE_FIELD_ID: &str = "36000367-aed7-461a-9e84-7ac155605fb9";

#[derive(Debug, poise::ChoiceParameter)]
pub enum TaskSize {
    #[name = "🐋 Months"]
    Months,
    #[name = "🦑 Weeks"]
    Weeks,
    #[name = "🐂 Days"]
    Days,
    #[name = "🐇 Hours"]
    Hours,
    #[name = "🦔 Minutes"]
    Minutes,
}

impl Display for TaskSize {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        match self {
            TaskSize::Months => write!(f, "🐋 Months"),
            TaskSize::Weeks => write!(f, "🦑 Weeks"),
            TaskSize::Days => write!(f, "🐂 Days"),
            TaskSize::Hours => write!(f, "🐇 Hours"),
            TaskSize::Minutes => write!(f, "🦔 Minutes"),
        }
    }
}

impl ClickupIdentifiable for TaskSize {
    fn clickup_id(&self) -> String {
        match self {
            TaskSize::Months => "d861267c-0528-457a-9ca6-a437eb0ee1b9".to_string(),
            TaskSize::Weeks => "04c1c537-aee6-4a32-9d0c-f68fa1ac00b1".to_string(),
            TaskSize::Days => "df457cfa-b018-42ea-acf4-18eee65142ed".to_string(),
            TaskSize::Hours => "b776bfcf-bdae-489c-9617-77e338e26f7a".to_string(),
            TaskSize::Minutes => "83efdfff-0efa-4084-b814-2ae20cd61277".to_string(),
        }
    }
}

pub const CLICKUP_CUSTOM_DISCORD_FIELD_ID: &str = "e6c36282-9f38-438b-a9da-9796affff15a";
