FROM openjdk:11
COPY ./target/aLIVE-0.0.1-SNAPSHOT.jar aLIVE-0.0.1-SNAPSHOT.jar
ADD wait-for-it.sh /
RUN chmod u+x ./wait-for-it.sh

CMD ["./wait-for-it.sh","rabbit:5672","--","java","-jar","aLIVE-0.0.1-SNAPSHOT.jar"]
