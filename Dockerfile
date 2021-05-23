FROM gradle:jdk16 as builder
COPY --chown=gradle:gradle . /home/src
WORKDIR /home/src
RUN gradle build

FROM adoptopenjdk/openjdk16:alpine-slim as runner
COPY --from=builder /home/src/build/libs/*.jar /app.jar

EXPOSE 9081

ENTRYPOINT ["java","-jar","/app.jar"]