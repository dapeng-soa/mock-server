FROM docker.today36524.com.cn:5000/base/openjdk:server-jre8

RUN mkdir /mock-server && mkdir /mock-server/logs && mkdir /mock-server/matedata_files
COPY  ./startup.sh  /mock-server/
RUN chmod 755 /mock-server/startup.sh
ENV LANG en_US.UTF-8
ENV LANGUAGE en_US:en
ENV LC_ALL en_US.UTF-8
ENV TZ CST-8

WORKDIR /mock-server
COPY ./apps/dapeng-mock-server-2.1.1.jar /mock-server/
ENTRYPOINT exec  /mock-server/startup.sh

