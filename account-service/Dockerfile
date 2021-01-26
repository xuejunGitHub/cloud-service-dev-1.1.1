FROM java:8
VOLUME /tmp
ARG JAR_FILE=./build/libs/**.jar
COPY ${JAR_FILE} app.jar
RUN bash -c "touch /app.jar"
VOLUME /data/yswl/log
ENV LANG C.UTF-8
EXPOSE 8080
ENTRYPOINT ["nohup","java","-jar","app.jar", "--server.port=8080","> /data/yswl/log/account-service.log"]