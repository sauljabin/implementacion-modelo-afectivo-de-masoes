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
y el ser humano tanto en aplicaciones de software como de hardware. El presente
trabajo plantea implementar el modelo afectivo de MASOES sobre un sistema
multiagente, con la finalidad de evaluar la efectividad de dicho modelo a nivel
de implementación y así, estudiar las emociones tanto a nivel individual como
colectivo. Para esto, se plantea diseñar una arquitectura multiagente basada en
el modelo afectivo anteriormente mencionado y aplicar la arquitectura sobre un
caso de estudio.

## Instrucciones Gradle

Crear estructura de proyecto para eclipse:

```
./gradlew eclipse
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
./gradlew run -Pargs="arg1 arg2"
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

Limpiar empaquetados:

```
./gradlew clean
```


Ayuda:

```
./gradlew run -Pargs="-h"

Usage: masoes
 -v,--version          Shows the application version
 -h,--help             Shows the options
 -s,--settings=<arg>   Sets application settings, examples:
                       -s "{setting1=value1, setting2=value2}"
                       --settings="{setting1=value1, setting2=value2}"
 -a,--agents=<arg>     Starts JADE with agents, examples:
                       Adds agents:
                       --agents="<name>:<class>;<name>:<class>"
                       Agent arguments:
                       --agents="<name>:<class>(arg1,arg2)"
 -e,--env=<arg>        Sets the environment for case study
 -b,--boot             Starts the application
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

Nuevas configuraciones:

```
./masoes --settings="{setting1=value1,setting2=value2}"
```

Ejecutar JADE manualmente:

```
./masoes --agents="a1:masoes.core.agent.TestAgent(arg1, arg2)"
```

Ingresar environment a ejecutar:

```
./masoes --env=wikipedia
```

Iniciar caso de estudio:

```
./masoes --env=wikipedia --boot
```