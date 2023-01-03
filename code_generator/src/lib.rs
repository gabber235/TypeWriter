#[macro_export]
macro_rules! err {
    (json, $message:expr) => {
        InvalidJson($message.to_string())
    };
    (file, $message:expr) => {
        InvalidFile($message.to_string())
    };
}

#[macro_export]
macro_rules! collection {
    // map-like
    ($($k:expr => $v:expr),* $(,)?) => {{
        core::convert::From::from([$(($k, $v),)*])
    }};
}

pub async fn fetch_icon_json(url: String) -> Result<String, reqwest::Error> {
    let response = reqwest::get(url).await?;
    let body = response.text().await?;
    Ok(body)
}
