# target install: Make program and install.
install: dist ; ./gradlew -q installDist

# target dist: Make program.
dist: clean ; ./gradlew -q assembleDist

# target clean: Refresh dependencies and clean.
clean: ; ./gradlew -q clean --refresh-dependencies

# target targets: Display callable targets.
targets: ; grep "^# target" [Mm]akefile | cut -c10-

# target help: Display masoes help.
help: ; ./gradlew -q run -Pargs="-h"

# target run: Exec masoes without env.
run: ; ./gradlew -q run

# target dummy: Exec masoes with dummy env.
dummy: ; ./gradlew -q run -Pargs="-Edummy"

# target wikipedia: Exec masoes with wikipedia env.
wikipedia: ; ./gradlew -q run -Pargs="-Ewikipedia"

# target uncopyrighted: Search files without copyright.
uncopyrighted: ; grep --include *.java -Lr "Copyright (c)" .

# target unit-tests: Exec unit test.
unit-tests: ; ./gradlew test

# target functional-tests: Exec functional test.
functional-tests: ; ./gradlew -q functionalTest

# target tests: Exec all test.
tests: unit-tests functional-tests

# target docker-build: Make docker image.
docker-build: install ; docker-compose build

# target docker-up: Starts docker image.
docker-up: ; docker-compose up
