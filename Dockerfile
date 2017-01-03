FROM openjdk:8
COPY build/install/masoes /masoes
WORKDIR /masoes/bin
ENTRYPOINT ["./masoes"]
CMD ["-Jgui=false", "-Ewikipedia", "-Sdocker"]