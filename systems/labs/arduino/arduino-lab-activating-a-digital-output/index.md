# Arduino Laboratory: Activating a Digital Output

## Objetivos

### Objetivo General

Desarrollar y simular un programa de Arduino que encienda y apague
iterativamente un LED.

### Objetivos Específicos

- General el código objeto (hex) del programa de Arduino.
- Arreglar una tarjeta Arduino Uno en Proteus.
- Diseñar un circuito electrónico trivial para conectar el LED a la salida del
  Arduino en Proteus.
- Cargar y ejecutar el programa en Proteus.

## Marco Teórico

Para cubrir este laboratorio se utilizará Arduino y Proteus para poder simular
el programa que se desarrollará.

### Arduino

Oficialmente temenos que:

> Arduino is an open-source electronics platform based on easy-to-use hardware
> and software. It's intended for anyone making interactive projects.
> Source: Arduino.cc [@arduino-2022]

Destaca que es una plataforma de hardware de código abierto lo cual es una buena
característica de plataformas actuales modernas y que son de utilidad, ya que se
cuenta con sus comunidades de colaboradores de código abierto.

Además tenemos que Arduino es:

> Arduino designs, manufactures, and supports electronic devices and software,
> allowing people around the world to easily access advanced technologies that
> interact with the physical world. Our products are straightforward, simple,
> and powerful, ready to satisfy users’ needs from students to makers and all
> the way to professional developers.
> Source: Arduino.cc [@arduino-2022]

Para escribir los programas y el código objeto (compilado en hex) se utiliza
Arduino IDE el cual es el IDE oficial de Arduino para programar estas tarjetas y
está disponible desde
su [descarga oficial](https://docs.arduino.cc/software/ide-v2/tutorials/getting-started/ide-v2-downloading-and-installing)
.

### Simulador Proteus

Según *Labcenter Electronics*[@labcenter-electronics-2022] -Empresa proveedora
de Proteus-:

> The Proteus Design Suite combines ease of use with a powerful feature set to
> enable the rapid design, test and layout of professional printed circuit
> boards.
> Source: *Labcenter Electronics*[@labcenter-electronics-2022]

#### Reseña

Proteus es un software de uso en áreas como la ingeniería en electrónica donde
se puede hacer simulaciones de todo tipo de circuito electrónico. Este software
ha sido de gran utilidad tanto para profesionales como estudiantes, aunque ya ha
quedado muy obsoleto y con altos modelos de monetización debido a que ha estado
muchas décadas ya en el mercado. En lo personal, yo he usado Proteus de forma
básica desde que estudié electrónica en la secundaria.

#### Diseñador para Arduino

La plataforma Proteus permite entre tantos, diseñar y correr simulaciones en la
tarjeta Arduino.

Según la información oficial, con las capacidades de Proteus podemos:

> Often the trickiest part of embedded development is the hardware design.
> The Arduino™ ecosystem goes a long way to solving this problem with lots of
> ready made shields. Visual Designer takes this into the software domain, using
> our professional schematic capture and Proteus VSM simulation engine to make 
> simulation of complete Arduino systems possible. The Peripheral Gallery in
> Visual Designer then simplifies the whole process as it will autoplace and 
> autoconnect the electronics on the schematic for you. Finally, Visual Designer
> provides high level methods to enable the control of the embedded system from
> a flowchart editor.
> 
> In addition to full Arduino Shields we have included many 
> individual sensors and modules from the Grove system and also added a bunch of 
> useful parts as breakout boards. More advanced users can even place and wire
> their own custom hardware directly on the schematic using the thousands of 
> simulation models in Proteus VSM.
> 
> Source: *Arduino Simulation Software - Processor, Shields and Peripherals*
> [@labcenter-electronics-2022]

![Tarjeta Arduino en Proteus](images/proteus.png)

Fuente: *Arduino Simulation Software - Processor, Shields and
Peripherals* [@labcenter-electronics-2022], bajo uso justo.

#### Instalar la Biblioteca de Arduino

En caso de ser necesario, se deberá instalar la biblioteca de Arduino para
Proteus. Al finalizar con la simple instalación ya se podrá agregar la tarjeta
Arduino desde la lista de dispositivos. Para mayor detalles, ir a *How to Add
Arduino Library in to Proteus 7 $\&$ 8*
[@instructables-2018].

### Cálculo de la Resistencia del LED

El diodo LED se alimenta usualmente de una fuente directa de $3-5V$. La
corriente y potencia del LED está especificada de acuerdo a cada diodo pero se
sabe de antemano que algunos valores aproximados funcionan bien para un simple
LED que se utilizará. Según *Omni Calculator*
[@szyk-2022] tenemos que conocer las siguientes variables:

- **Tipo de Circuito:** Serie o paralelo.

- **n:** Número de LEDs conectados.

- **V:** Fuente de voltaje.

- **$V_0$:** Caída de voltaje por cada LED.

- **$I_0$:** Corriente por LED.

Los valores estándar más comunes son configuración en serie; pilas, fuentes o
baterías desde $1.5-12V$; voltaje de LED de $1.7-3.6V$ que depende del color del
LED; y corrientes de $20-30mA$ [@szyk-2022].

Como bien sabemos por la ley de Ohm $R = \frac{V}{I}$ por lo que se deberá
aplicar en el cálculo de la resistencia del LED.

Para otros cálculos con configuración en serie tenemos que [@szyk-2022]:

- $R = \frac{V - n*V_0}{I_0}$

- $P_0 = V_0 * I_0$

- $P = n * V_0 * I_0$

- $P_r = I_0^2 * R$

## Procedimiento Experimental

El procedimiento consiste en la implementación en Arduino IDE y en Proteus.

### Crear Programa Arduino

Primero hay que abrir un nuevo proyecto en Arduino IDE.

![Programa inicial en Arduino IDE](images/arduino-1.png)

Para actualizar el nombre del programa ir a File -\> Save As y seleccionar el
directorio de destino y nombre del programa. En este caso, el nombre del
programa es "activating-a-digital-output".

Se utilizará el siguiente programa sensillo para este laboratorio:

```c
const int PIN = 12;

void setup() 
{
  pinMode(PIN, OUTPUT);
}

void loop() 
{
  digitalWrite(PIN, HIGH);
  delay(500);
  digitalWrite(PIN, LOW);
  delay(100);
}
```

![Programa final a correr](images/arduino-2.png)

Para obtener el binario hexadecimal compilado del código fuente, ir a Sketch -\>
Export compiled binary. Ahora, los archivos .hex compilados se encontrarán en el
directorio donde del programa fue guardado.

### Correr simulación en Proteus

Para correr la simulación se usará Proteus.

Al abrir Proteus, ir a File - New Project dar un nombre al proyecto.

Siguiente - Seleccionar "Crear un esquema del template seleccionado" con la
opción "DEFAULT".

Siguiente - Seleccionar "No crear un layout PCB".

Siguiente - Seleccionar con las opciones de Familia: ARDUINO, Controlador:
Arduino Uno y Compilador: Arduino AVR (Proteus). Dejar seleccionado "Crear
Archivos de Inicio Rápido".

Siguiente - Terminar.

![Configuración inicial del Proteus con Arduino Uno](images/sim-1.png)

En el simulador se debe cargar el programa que se escribió. Dar clic derecho a
la tarjeta de Arduino e ir a Propiedades. En seleccionar el archivo binario que
se compiló, que contiene el bootloader. En este caso, el archivo es
"activating-a-digital-output.ino.withbootloader.standard.hex".

Calculando el valor de la resistencia tenemos que con valores estándar

$$R_{LED} = \frac{5v}{20mA} = 100\Omega$$

En caso de no ser exacto la resistencia en valor comercial se deberá redondear o
aplicar otra configuración para obtener la resistencia equivalente.

![Configuración del circuito final en Proteus](images/sim-2.png)

## Análisis de Resultados

Al correr satisfactoriamente el programa en Proteus, el LED $D2$ parpadea de
acuerdo a las iteraciones establecidas en el programa. Tener en cuenta que la
simulación puede correr más o menos rápido de acuerdo a la velocidad del
simulador.

La tarjeta Arduino emitió las señales digitales mediante el pin $12$ que se
definió en el loop del programa.

## Conclusiones

Se desarrolló un programa en Arduino IDE que activa la señal del pin $12$ del
Arduino dado un intervalo establecido. Luego, se creó una simulación del
circuito agregando un LED como carga a la salida $12$ del Arduino. Se calculó de
antemano la resistencia del LED de forma que fuera de protección para este.

## Referencias

- [Arduino - Home (@arduino-2022)](https://www.arduino.cc)
- [Omni Calculator | LED Resistor Calculator (@szyk-2022)](https://www.omnicalculator.com/physics/led)
- [Labcenter Electronics | Arduino Simulation Software - Processor, Shields and Peripherals (@labcenter-electronics-2022)](https://www.labcenter.com/visualdesigner/arduino)
- [Instructables | How to Add Arduino Library in to Proteus 7 \& 8 (@instructables-2018)](https://www.instructables.com/How-to-add-Arduino-Library-in-to-Proteus-7-8)
