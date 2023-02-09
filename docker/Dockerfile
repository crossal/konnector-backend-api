FROM debian:stable-slim

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ARG ENTRYPOINT_FILE=docker/entrypoint.sh
COPY ${ENTRYPOINT_FILE} /

RUN apt-get -y update
RUN apt-get -y upgrade

RUN apt-get -y install gnupg ca-certificates curl
RUN curl -s https://repos.azul.com/azul-repo.key | gpg --dearmor -o /usr/share/keyrings/azul.gpg
RUN echo "deb [signed-by=/usr/share/keyrings/azul.gpg] https://repos.azul.com/zulu/deb stable main" | tee /etc/apt/sources.list.d/zulu.list

RUN apt-get -y update

RUN apt-get -y install zulu17-jdk
RUN apt-get -y install git
RUN apt-get -y install python3 python3-venv libaugeas0
RUN apt-get -y remove certbot
RUN python3 -m venv /opt/certbot/
RUN /opt/certbot/bin/pip install --upgrade pip
RUN /opt/certbot/bin/pip install certbot certbot
RUN ln -s /opt/certbot/bin/certbot /usr/bin/certbot

ENTRYPOINT ["/entrypoint.sh"]