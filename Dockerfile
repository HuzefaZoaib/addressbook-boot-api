FROM maven:3.9.8-amazoncorretto-21 as BUILD

#ADD repository.tar.gz /usr/share/maven/ref/
RUN mvn -Dmaven.repo.local=./repository clean package
RUN tar cf /usr/share/maven/ref/repository.tar.gz ./repository

COPY . /usr/src/app
WORKDIR /usr/src/app
RUN mvn -s /usr/share/maven/ref/settings-docker.xml package


FROM amazoncorretto:21
EXPOSE 8080 5005
COPY --from=BUILD /usr/src/app/target /opt/target
WORKDIR /opt/target
ENV _JAVA_OPTIONS '-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005'

CMD ["java", "-jar", "addressbook-boot-api-0.0.1-SNAPSHOT.jar", "com.example.addressbook.AddressBookApplication"]

