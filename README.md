# Trabajo de Grado

**Implementación de un Modelo Afectivo para la Arquitectura Multiagente para Sistemas Emergentes y Auto-Organizados (MASOES)**

## Descripción

Trabajo presentado en la Universidad Centroccidental "Lisandro Alvarado" para
optar al grado de Magister Scientiarum en Ciencias de la Computación Mención
Inteligencia Artificial. Barquisimeto, Venezuela.

* Autor: Ing. Saúl Jabín Piña Alvarado (<sauljabin@gmail.com>)
* Tutora: Dra. Niriaska Perozo Guédez (<nperozo@ucla.edu.ve>)
* [Licencia GPL3](http://www.gnu.org/licenses/)
* Trabajo de Grado: https://bitbucket.org/sauljabin/trabajo-de-grado-ucla
* Implemención en JAVA: https://bitbucket.org/sauljabin/trabajo-de-grado-ucla-implementacion
* JADE: http://jade.tilab.com

## Resumen

La computación emocional es un área reciente de la inteligencia artificial que
tiene como objetivo mejorar los procesos interactivos entre agentes emocionales
y el ser humano tanto en aplicaciones de software como de hardware. Debido a las
posibles aplicaciones en el área, actualmente la comunidad científica realiza
esfuerzos para aplicar las teorías existentes en sistemas computacionales.
Diferentes autores estudian modelos emocionales en sistemas multiagente, con el
objetivo de mejorar la interacción entre agentes, un ejemplo es el modelo
afectivo de MASOES, aunque dicho modelo afectivo ha sido verificado formalmente
a nivel de diseño, no ha sido verificado a nivel de implementación. Frente a lo
expuesto, el presente trabajo plantea implementar el modelo afectivo de MASOES
sobre un sistema multiagente, con la finalidad de evaluar la efectividad de
dicho modelo a nivel de implementación y así, estudiar las emociones tanto a
nivel individual como colectivo. Todo esto con la finalidad de brindar un
entorno para la interacción entre los procesos emocionales y las diferentes
funciones de un agente, a su vez aportar a la una implementación del reciente
modelo afectivo propuesto en MASOES. Para esto, se plantea aplicar el modelo
afectivo implementado sobre un caso de estudio y se comparará los resultados a
nivel de implementación con los obtenidos por Perozo (2011) a nivel de
diseño.

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

Ejecución de pruebas funcionales:

```
./gradlew functionalTest
```

Limpiar empaquetados:

```
./gradlew clean
```

Ayuda:

```
./gradlew run -Pargs="-h"

Usage: masoes
 -b,--boot      Starts the application (Default option)
 -E <arg>       Sets the environment (dummy, wikipedia), example:
                -Edummy
 -h,--help      Shows the options
 -J <arg>       Sets JADE settings, example:
                -Jkey=value -Jkey=value
 -S <arg>       Sets application settings, example:
                -Skey=value -Skey=value
 -v,--version   Shows the application version
```

## Ejecución desde Empaquetado

Ayuda:

```
./masoes -h
```

Versión:

```
./masoes -v
```

Settings:

```
./masoes -Skey1=value1 -Skey2=value2
```

Settings de JADE:

```
./masoes -Jkey1=value1 -Jkey2=value2
./masoes -Jgui=false -Jagents=a1:masoes.core.agent.TestAgent
./masoes -Jagents="a1:masoes.core.agent.TestAgent(arg1, arg2)"
```

Iniciar dummy:

```
./masoes -Edummy
```

Iniciar caso de estudio:

```
./masoes -Ewikipedia
```

## Ejecución con make

Instalar:

```
make install
```

Crear empaquetado:

```
make dist
```

Limpiar:

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

Ejecutar wikipedia:

```
make wikipedia
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

Crear imagen:

```
make docker-build
```

Iniciar con docker:

```
make docker-up
```