import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchAgent;
import aima.search.informed.HillClimbingSearch;
import aima.search.informed.SimulatedAnnealingSearch;

import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Problema {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        Random random = new Random();
        //f heursitica, sol inicial, algorisme, seed
        System.out.println("Que algoritmo deseas usar para realizar la búsqueda local?");
        System.out.println("Hill Climbing: 1");
        System.out.println("Simulated Annealing: 2");
        System.out.println("Terminar programa: 0");

        int op = in.nextInt();
        while (op != 0 && op != 1 && op != 2) {
            System.out.println("Introduce un valor correcto: 0, 1 o 2");
            op = in.nextInt();
        }

        while (op != 0) {
            System.out.println("Configura los atributos del estado");
            System.out.println("Introduce el valor para el atributo seed o -1 si deseas usar un valor aleatorio");
            int seed = in.nextInt();
            if (seed == -1) seed = random.nextInt(10000); //TODO
            System.out.println("Introduce el número de paquetes");
            int nPaq = in.nextInt();
            System.out.println("Introduce la proporción de paquetes en cada oferta");
            double proporcion = in.nextDouble();

            Estado e = new Estado(nPaq, seed, proporcion);

            System.out.println("A continuación, elije la solución inicial");
            System.out.println("1: Prioriza la felicidad");
            System.out.println("2: Prioriza el precio");
            int solIni = in.nextInt();
            while (solIni != 1 && solIni != 2) {
                System.out.println("Introduce un valor correcto: 1 o 2");
                solIni = in.nextInt();
            }
            if (solIni == 1) e.generarSolucionInicial1();
            else e.generarSolucionInicial2();

            System.out.println("A continuación, elije la función heurística");
            System.out.println("1: Tiene en cuenta el precio");
            System.out.println("2: Tiene en cuenta el precio y la felicidad");
            int funcHeur = in.nextInt();
            while (funcHeur != 1 && funcHeur != 2) {
                System.out.println("Introduce un valor correcto: 1 o 2");
                funcHeur = in.nextInt();
            }


            if (op == 1) {
                try {
                    BusquedaHillClimbing(e, funcHeur);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            else {
                try {
                    BusquedaSimulatedAnnealing(e, funcHeur);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }

    }

    public static void BusquedaHillClimbing(Estado e, int funcionHeuristica) throws Exception {
        Problem problema;
        if (funcionHeuristica == 1) problema = new Problem(e, new EstadosSucesores(), new EstadoFinal(), new FuncionHeuristica1());
        else problema = new Problem(e, new EstadosSucesores(), new EstadoFinal(), new FuncionHeuristica2());
        Search search = new HillClimbingSearch();
        SearchAgent agent = new SearchAgent(problema, search);

        printActions(agent.getActions());

        System.out.print(((Estado) search.getGoalState()).toString());
        //System.out.println("\n" + ((Estado) search.getGoalState()).correspondenciasToString()); todo
    }

    private static void BusquedaSimulatedAnnealing(Estado e, int funcionHeuristica) throws Exception {
        Problem problema;
        if (funcionHeuristica == 1) problema = new Problem(e, new EstadosSucesores(), new EstadoFinal(), new FuncionHeuristica1());
        else problema = new Problem(e, new EstadosSucesores(), new EstadoFinal(), new FuncionHeuristica2());
        Search search = new SimulatedAnnealingSearch(); //TODO
        SearchAgent agent = new SearchAgent(problema, search);

        printActions(agent.getActions());

        System.out.print(((Estado) search.getGoalState()).toString());
        //System.out.println("\n" + ((Estado) search.getGoalState()).correspondenciasToString()); todo
    }

    private static void printActions(List actions) {
        for (int i = 0; i < actions.size(); i++) {
            String action = (String) actions.get(i);
            System.out.println(action);
        }
    }
}
