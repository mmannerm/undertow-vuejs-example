#FROM gcr.io/distroless/base:latest
FROM debian:buster-slim

ENV LANG='en_US.UTF-8' LANGUAGE='en_US:en' LC_ALL='en_US.UTF-8'

RUN apt-get update \
    && apt-get install -y --no-install-recommends curl ca-certificates fontconfig locales \
    && echo "en_US.UTF-8 UTF-8" >> /etc/locale.gen \
    && locale-gen en_US.UTF-8 \
    && rm -rf /var/lib/apt/lists/*

COPY undertow-vuejs-example-linux-x64/ /opt/app/

EXPOSE 8443

ENTRYPOINT ["/opt/app/bin/undertow-vuejs-example"]
