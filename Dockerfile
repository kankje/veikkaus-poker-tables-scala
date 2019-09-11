FROM openjdk:8

ENV JAVA_HOME /usr/lib/jvm/java-1.8-openjdk
ENV PATH $PATH:/usr/lib/jvm/java-1.8-openjdk/jre/bin:/usr/lib/jvm/java-1.8-openjdk/bin
ENV SBT_VERSION 1.1.6

RUN apt-get update && apt-get -y install apt-transport-https

RUN curl -L -o sbt-$SBT_VERSION.deb https://dl.bintray.com/sbt/debian/sbt-$SBT_VERSION.deb && \
  dpkg -i sbt-$SBT_VERSION.deb && \
  rm sbt-$SBT_VERSION.deb

RUN curl -sL https://deb.nodesource.com/setup_8.x | bash -
RUN curl -sS https://dl.yarnpkg.com/debian/pubkey.gpg | apt-key add -
RUN echo "deb https://dl.yarnpkg.com/debian/ stable main" | tee /etc/apt/sources.list.d/yarn.list

RUN apt-get update && apt-get -y install sbt nodejs yarn

ADD . /app
WORKDIR /app

RUN sbt update

RUN yarn --frozen-lockfile --no-progress

ARG NODE_ENV=production
ENV NODE_ENV $NODE_ENV
RUN yarn build

EXPOSE 9000
HEALTHCHECK CMD curl -fs http://localhost/healthz || exit 1
ENTRYPOINT ["sbt", "run"]
