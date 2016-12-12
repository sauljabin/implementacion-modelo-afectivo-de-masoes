FROM openjdk:8
COPY build/distributions/*.tar /masoes/app.tar
RUN tar -xf /masoes/app.tar -C /masoes --strip 1
WORKDIR /masoes/bin
CMD ./masoes -Jgui=false -Ewikipedia -Sdocker