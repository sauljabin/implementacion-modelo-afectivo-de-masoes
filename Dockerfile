FROM openjdk:8-jre-alpine
COPY build/install/masoes/lib /masoes
WORKDIR /masoes
ENTRYPOINT ["java", "-cp", "*", "application.ApplicationMain"]
CMD ["-Jgui=false", "-Ewikipedia", "-Sdocker"]