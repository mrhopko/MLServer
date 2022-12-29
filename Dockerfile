# Build an image that can do training and inference in SageMaker
# Uses Scala, ZIO

FROM ubuntu:18.04

ARG SBT_VERSION=1.7.2
ARG SCALA_VERSION=2.13.10
ARG JAVA_VERSION=11.0.17

# install wget
RUN apt-get update &&  \
    apt-get -qq -y install \
    wget \
    curl \
    unzip \
    zip


RUN curl -s "https://get.sdkman.io" | bash
RUN chmod a+x "$HOME/.sdkman/bin/sdkman-init.sh"

# copy the build files
RUN mkdir /app
COPY build.sbt /app
COPY project /app/project
COPY src /app/src

# Install Java.
SHELL ["/bin/bash", "-c"]
WORKDIR /app
RUN \
  source "$HOME/.sdkman/bin/sdkman-init.sh" && \
  sdk install java ${JAVA_VERSION}-zulu && \
    apt-get update && \
    apt-get install -y ant && \
    apt-get install -y maven && \
    rm -rf /var/lib/apt/lists/* && \
    sdk install scala $SCALA_VERSION && \
    sdk install sbt $SBT_VERSION && \
    sbt update && \
    sbt package

# copy the built jar
RUN mkdir -p /opt/ml/code && \
    cp target/scala-2.13/mlserver_2.13-0.01.jar /opt/ml/code

# set the startup command to run your app
ENTRYPOINT ["/root/.sdkman/candidates/sbt/1.7.2/bin/sbt", "run"]
