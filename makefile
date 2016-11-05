# target dist: Make program.
dist: refresh ; ./gradlew build ; cp build/distributions/masoes*.zip .

# target clean: Refresh dependencies and clean.
clean: ; ./gradlew clean --refresh-dependencies

# target install: Make program and install.
install: compile ; unzip masoes*.zip

# target help: Display callable targets.
help: ; grep "^# target" [Mm]akefile | cut -c10-

# target help-masoes: Display masoes help.
help-masoes: ; ./gradlew run -Pargs="-h"

# target run: Exec masoes with dummy env.
run: ; ./gradlew run -Pargs="-b"

# target no-copyright: Search files without copyright.
no-copyright: ; grep --include *.java -Lr "Copyright (c)" .

# target unit-test: Exec unit test.
unit-test: ; ./gradlew test
