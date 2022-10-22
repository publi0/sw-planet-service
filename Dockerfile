FROM gradle:jdk16

COPY . /home/src
WORKDIR /home/src

EXPOSE 9081

ENTRYPOINT ["gradle","--info","bootRun"]
