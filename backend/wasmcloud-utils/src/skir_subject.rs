use crate::wasmcloud::messaging;
use otel_wasi::ResultWithSlug;
use serde::Deserialize;

const MEMBERSHIP_STREAM: &str = "TYPEWRITER_MEMBERSHIP";

#[derive(Deserialize)]
struct JetStreamPublishAck {
    stream: String,
}

pub struct SkirSubject<M> {
    subject: String,
    serialize: fn(&M) -> Vec<u8>,
}

impl<M> SkirSubject<M> {
    pub fn new(subject: impl Into<String>, serialize: fn(&M) -> Vec<u8>) -> Self {
        Self {
            subject: subject.into(),
            serialize,
        }
    }

    pub fn subject(&self) -> &str {
        &self.subject
    }

    pub async fn publish(&self, message: M) -> Result<(), otel_wasi::Error> {
        messaging::publish(self.subject.clone(), (self.serialize)(&message)).await
    }

    pub async fn persist(&self, message: M) -> Result<(), otel_wasi::Error> {
        let response = messaging::request(self.subject.clone(), (self.serialize)(&message)).await?;
        let acknowledgement: JetStreamPublishAck = serde_json::from_slice(&response.body)
            .error_with_slug("membership-event-ack-decode-failed")?;
        if acknowledgement.stream != MEMBERSHIP_STREAM {
            return Err(otel_wasi::Error::new(
                "membership-event-wrong-stream",
                format!(
                    "expected {MEMBERSHIP_STREAM}, got {}",
                    acknowledgement.stream
                ),
            ));
        }
        Ok(())
    }
}

#[macro_export]
macro_rules! define_skir_subjects {
    (
        $(
            $name:ident ( $( $param:ident ),* $(,)? )
                -> $response:ty = $template:literal;
        )*
    ) => {
        $(
            pub fn $name(
                $( $param: impl ::std::fmt::Display ),*
            ) -> $crate::SkirSubject<$response> {
                $crate::SkirSubject::new(
                    format!(
                        $template,
                        $( $param = $param ),*
                    ),
                    |message: &$response| <$response>::serializer().to_bytes(message),
                )
            }
        )*
    };
}
