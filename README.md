# Azamon-IA

PASOS PARA EJECUTAR LA PRÁCTICA
1. Configurar el proyecto para que el IDE incluya las siguientes librerías externas:
	- AIMA
	- Azamon
	- jfreechart-1.0.13
	- jfreechart-1.0.13-experimental
	- jfreechart-1.0.13-swt

2. Ejecutar el main de la clase Problema.java


EJECUCIÓN DE LA PRÁCTICA
Una vez ejecute la práctica, podrá elegir que acción realizar:
	- ejecutar el algoritmo Hill Climbing: introduce el número 1
	- ejecutar el algoritmo Simulated Annealing: introduce el número 2
	- ejecutar un experimento: introduce el número 3
	- finalizar ejecución: introduce el número 0

1. HILL CLIMBING y 2. SIMULATED ANNEALING
Primero deberá configurar los atributos del estado. Para ello, el programa le pedirá que introduzca:
	- un valor para el atributo seed (entero)
	- un número de paquetes (entero)
	- una proporción de paquetes en cada oferta (entero o decimal, importante usar una coma y no un punto)
	- un valor para elegir la solución inical:
		- solución inicial que prioriza la felicidad: introduce el número 1
		- solución inicial que prioriza el precio: introduce el número 2

Finalmente, también le pedirá que introduzca un valor para elegir la función heurística:
	- función heurística que tiene en cuenta el precio: introduce el número 1
	- función heurística que tiene en cuenta el precio y la felicidad: introduce el número 2

Cómo resultado verá el coste de transporte y almacenamiento (Precio) y la felicidad de la solución final. 

Si desea ver los cambios realizados desde el estado inicial, puede descomentar la línea 142 del fichero Problema.java, donde se llama al método imprimirAcciones.

3. EXPERIMENTOS
Para ejecutar un experimento, deberá introducir el valor del número del experimento según el apartado 3.5 del enunciado de la práctica. Sin embargo, en algunos experimentos no se generarán gráficos en la ejecución, dado que la recolección de datos de dichos experimentos ha sido realizada mediante otros métodos.
