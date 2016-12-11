# target dist: Make program.
dist: clean ; ./gradlew -q build

# target clean: Refresh dependencies and clean.
clean: ; ./gradlew -q clean --refresh-dependencies

# target install: Make program and install.
install: dist ; unzip -o masoes-*.zip && cd masoes-*/bin && ./masoes

# target targets: Display callable targets.
targets: ; grep "^# target" [Mm]akefile | cut -c10-

# target help: Display masoes help.
help: ; ./gradlew -q run -Pargs="-h"

# target dummy: Exec masoes with dummy env.
dummy: ; ./gradlew -q run -Pargs="-b"

# target wikipedia: Exec masoes with wikipedia env.
wikipedia: ; ./gradlew -q run -Pargs="-e wikipedia -b"

# target uncopyrighted: Search files without copyright.
uncopyrighted: ; grep --include *.java -Lr "Copyright (c)" .

# target unit-test: Exec unit test.
unit-test: clean ; ./gradlew test

# target functional-test: Exec functional test.
functional-test: clean ; ./gradlew -q functionalTest

# target all-test: Exec all test.
all-test: unit-test functional-test

# target docker-build: Make docker image.
docker-build: dist ; docker-compose build

# target docker-up: Starts docker image.
docker-up: docker-build ; docker-compose up
