# target clean:               Refresh dependencies and clean.
clean: ; @ ./gradlew -q --refresh-dependencies clean && rm -rf log/

# target targets:             Display callable targets.
targets: ; @ grep "^# target" [Mm]akefile | cut -c10-

# target help:                Display masoes help.
help: ; @ ./gradlew -q run -Pargs="-h"

# target run:                 Exec masoes without env.
run: ; @ ./gradlew -q run -Pargs="$(args)"

# target dummy:               Exec masoes with dummy env.
dummy: ; @ ./gradlew -q run -Pargs="-Edummy"

# target wikipedia:           Exec masoes with wikipedia env.
wikipedia: ; @ ./gradlew -q run -Pargs="-Ewikipedia"

# target uncopyrighted:       Search files without copyright.
uncopyrighted: ; @ grep --include *.java -Lr "Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>" .

# target unit-test:           Exec unit test.
unit-test: ; @ ./gradlew unitTest

# target functional-test:     Exec functional test.
functional-test: ; @ ./gradlew functionalTest

# target test:                Exec all test, use tests=*Test.
tests=*Test
test: ; @ ./gradlew test --tests $(tests)

# target migrate:             Create database.
migrate: ; @ ./gradlew flywayMigrate -i

# target clean-db:            Clean database.
clean-db: ; @ ./gradlew -q flywayClean

# target version:             Shows app version.
version: ; @ ./gradlew -q run -Pargs="-v"