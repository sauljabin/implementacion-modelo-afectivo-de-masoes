FROM openjdk:8
COPY build/distributions/*.tar /masoes/app.tar
RUN tar -xf /masoes/app.tar -C /masoes --strip 1
WORKDIR /masoes/bin
CMD ./masoes --settings="{jade.gui=false}" --boot