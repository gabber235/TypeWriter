FROM rust:1.75 as builder

WORKDIR /usr/src/app

COPY . .

RUN cargo install --path .

FROM debian:bookworm-slim
RUN apt-get update && \
    apt-get install -y openssl libssl-dev ca-certificates && \
    rm -rf /var/lib/apt/lists/*
RUN /usr/sbin/update-ca-certificates
COPY --from=builder /usr/local/cargo/bin/discord_bot /usr/local/bin/discord_bot
CMD ["discord_bot"]
