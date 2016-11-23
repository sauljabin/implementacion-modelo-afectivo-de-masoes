# target dist: Make program.
dist: clean ; ./gradlew build && cp build/distributions/masoes-*.zip .

# target clean: Refresh dependencies and clean.
clean: ; ./gradlew clean --refresh-dependencies

# target install: Make program and install.
install: dist ; unzip -o masoes-*.zip && cd masoes-*/bin && ./masoes

# target help: Display callable targets.
help: ; grep "^# target" [Mm]akefile | cut -c10-

# target help-masoes: Display masoes help.
help-masoes: ; ./gradlew run -Pargs="-h"

# target run: Exec masoes with dummy env.
run: ; ./gradlew run -Pargs="-b"

# target run-wikipedia: Exec masoes with wikipedia env.
run-wikipedia: ; ./gradlew run -Pargs="-b -e wikipedia"

# target no-copyright: Search files without copyright.
no-copyright: ; grep --include *.java -Lr "Copyright (c)" .

# target unit-test: Exec unit test.
unit-test: clean ; ./gradlew test

# target functional-test: Exec functional test.
functional-test: clean ; ./gradlew functionalTest

# target all-test: Exec all test.
all-test: unit-test functional-test
