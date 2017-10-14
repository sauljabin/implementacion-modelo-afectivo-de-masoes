# Trabajo de Grado

**Implementación de un Modelo Afectivo para la Arquitectura Multiagente para Sistemas Emergentes y Auto-Organizados (MASOES)**

## Descripción

Trabajo presentado en la Universidad Centroccidental "Lisandro Alvarado" para
optar al grado de Magister Scientiarum en Ciencias de la Computación Mención
Inteligencia Artificial. Barquisimeto, Venezuela.

* Autor: MSc. Saúl Jabín Piña Alvarado (<sauljabin@gmail.com>)
* Tutora: Dra. Niriaska Perozo Guédez (<nperozo@ucla.edu.ve>)
* Trabajo de Grado: https://github.com/sauljabin/implementacion-modelo-afectivo-de-masoes-documento
* Implemención en JAVA: https://github.com/sauljabin/implementacion-modelo-afectivo-de-masoes
* JADE: http://jade.tilab.com

## Licencia

* Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
* [Licencia GPL3](http://www.gnu.org/licenses/)

## Resumen

La computación emocional es un área reciente de la inteligencia artificial que
tiene como objetivo mejorar los procesos interactivos entre agentes emocionales
y el ser humano, tanto en aplicaciones de software como de hardware. Debido a
las posibles aplicaciones en el área, actualmente la comunidad científica
realiza esfuerzos para aplicar las teorías existentes en sistemas multiagente.
Diferentes autores estudian modelos emocionales con el objetivo de mejorar la
interacción entre agentes inteligentes, un ejemplo es el modelo afectivo de
MASOES, aunque este modelo afectivo ha sido verificado formalmente a nivel de
diseño, no ha sido verificado a nivel de implementación. Frente a lo expuesto,
el presente trabajo propone una implementación del modelo afectivo de MASOES
sobre un sistema multiagente, con la finalidad de brindar un entorno para la
interacción entre los procesos emocionales y las diferentes funciones de un
agente. Adicionalmente, se propone el cálculo de la Emoción Social, permitiendo
describir el estado emocional colectivo de un grupo de agentes emocionales. Para
esto, se diseñó y desarrolló un sistema multiagente basado en el marco de
trabajo JADE, el cual utiliza el estándar FIPA que permite el desarrollo de
agentes universales. Posteriormente, se aplicó lo implementado sobre un caso de
estudio utilizando simulaciones para generar emociones a nivel individual y
colectivo, y se comparó los resultados a nivel de implementación con los
obtenidos por Perozo (2011) a nivel de diseño.

## Instrucciones Gradle

Crear estructura de proyecto para eclipse:

```
./gradlew eclipse
```

Crear estructura de proyecto para IntelliJ IDEA:

```
./gradlew idea
```

Iniciar aplicación:

```
./gradlew run
```

Iniciar aplicación limpiando dependencias:

```
./gradlew clean run --refresh-dependencies
```

Iniciar aplicación con argumentos:

```
./gradlew run -Pargs='arg1 arg2'
```

Iniciar aplicación con argumentos y espacios en blanco:

```
./gradlew run -Pargs='arg1 arg2="token token"'
```

Crear empaquetado:

```
./gradlew build
```

Ejecución de pruebas:

```
./gradlew test
```

Ejecución de pruebas con filtro:

```
./gradlew test --tests *Prueba
```

Ejecución de pruebas unitarias:

```
./gradlew unitTest
```

Ejecución de pruebas funcionales:

```
./gradlew functionalTest
```

Limpiar compilados:

```
./gradlew clean
```

Migración de DB:

```
./gradlew flywayMigrate -i
```

Limpiar DB:

```
./gradlew flywayClean
```

Ayuda:

```
./gradlew run -Pargs="-h"

Usage: masoes
 -b,--boot      Starts the application (Default option)
 -E <arg>       Sets the environment, example:
                -Edummy
 -h,--help      Shows the options
 -J <arg>       Sets JADE settings, example:
                -Jkey=value -Jkey=value
 -M <arg>       Sets MASOES settings, example:
                -Mkey=value -Mkey=value
 -S <arg>       Sets application settings, example:
                -Skey=value -Skey=value
 -v,--version   Shows the application version
```

## Ejecución con make

Limpiar compilados:

```
make clean
```

Opciones del make:

```
make targets
```

Ayuda MASOES:

```
make help
```

Ejecutar sin env:

```
make run
```

Ejecutar dummy:

```
make dummy
```

Ejecutar ambiente de simulación:

```
make simulation
```

Buscar archivos sin copyright:

```
make uncopyrighted
```

Ejecución de pruebas unitarias:

```
make unit-test
```

Ejecución de pruebas funcionales:

```
make functional-test
```

Ejecución de todas las pruebas:

```
make test
```

Ejecución de pruebas con filtro:

```
make test tests=*Prueba
```

Crear DB:

```
make migrate
```

Limpiar DB:

```
make clean-db
```

Mostar versión:

```
make version
```
