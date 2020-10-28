import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchAgent;
import aima.search.informed.HillClimbingSearch;
import aima.search.informed.SimulatedAnnealingSearch;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Problema {
    static private int niteracio = 1;

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        Random random = new Random();
        //f heursitica, sol inicial, algorisme, seed
        System.out.println("Que deseas realizar?");
        System.out.println("Hill Climbing: 1");
        System.out.println("Simulated Annealing: 2");
        System.out.println("Experimentos: 3");
        System.out.println("Terminar programa: 0");

        int op = in.nextInt();
        while (op != 0 && op != 1 && op != 2 && op != 3) {
            System.out.println("Introduce un valor correcto: 0, 1, 2 o 3");
            op = in.nextInt();
        }

        while (op != 0) {
            if (op == 3) {
                System.out.println("Qué experimento deseas realizar? Introduce el número del experimento del apartado 3.5");
                op = in.nextInt();
                while (op < 1 || op > 9) {
                    System.out.println("Introduce un valor del 1 al 9");
                    op = in.nextInt();
                }

                switch (op) {
                    case 1:
                        experimento1();
                        break;
                    case 2:
                        experimento2();
                        break;
                    case 3:
                        experimento3();
                        break;
                    case 4:
                        experimento4();
                        break;
                    case 5:
                        experimento5();
                        break;
                    case 6:
                        experimento6();
                        break;
                    case 7:
                        experimento7();
                        break;
                    case 8:
                        experimento8();
                        break;
                    case 9:
                        experimento9();
                        break;

                }
                System.out.println("Que deseas realizar?");
                System.out.println("Hill Climbing: 1");
                System.out.println("Simulated Annealing: 2");
                System.out.println("Experimentos: 3");
                System.out.println("Terminar programa: 0");

                op = in.nextInt();
                while (op != 0 && op != 1 && op != 2 && op != 3) {
                    System.out.println("Introduce un valor correcto: 0, 1, 2 o 3");
                    op = in.nextInt();
                }

            }
            else {
                System.out.println("Configura los atributos del estado");
                System.out.println("Introduce el valor para el atributo seed o -1 si deseas usar un valor aleatorio");
                int seed = in.nextInt();
                if (seed == -1) seed = random.nextInt(10000);
                System.out.println("Introduce el número de paquetes");
                int nPaq = in.nextInt();
                System.out.println("Introduce la proporción de paquetes en cada oferta");
                double proporcion = in.nextDouble();

                Estado e = new Estado(nPaq, seed, proporcion, 1);

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
                } else {
                    try {
                        BusquedaSimulatedAnnealing(e, funcHeur);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                System.out.println("Que deseas realizar?");
                System.out.println("Hill Climbing: 1");
                System.out.println("Simulated Annealing: 2");
                System.out.println("Experimentos: 3");
                System.out.println("Terminar programa: 0");

                op = in.nextInt();
                while (op != 0 && op != 1 && op != 2 && op != 3) {
                    System.out.println("Introduce un valor correcto: 0, 1, 2 o 3");
                    op = in.nextInt();
                }
            }
        }
    }

    private static void experimento1() {
        Random random = new Random();
        ArrayList<Double> c1 = new ArrayList<>(), c2  = new ArrayList<>(), c3  = new ArrayList<>();
        ArrayList<Long> t1 = new ArrayList<>(), t2  = new ArrayList<>(), t3  = new ArrayList<>();
        for (int i = 0; i < 10; ++i) {
            int seed = random.nextInt(10000);
            for (int j = 1; j < 4; ++j) {
                Estado e = new Estado(100, seed, 1.2, j);
                e.generarSolucionInicial2();
                try {
                    long ini = System.currentTimeMillis();
                    double coste = BusquedaHillClimbing(e, 1);
                    long fin = System.currentTimeMillis();
                    if (j == 1) {
                        c1.add(coste);
                        t1.add(fin-ini);
                    }
                    else if (j == 2) {
                        c2.add(coste);
                        t2.add(fin-ini);
                    }
                    else  {
                        c3.add(coste);
                        t3.add(fin-ini);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        System.out.println("c1: " + c1);
        System.out.println("c2: " + c2);
        System.out.println("c3: " + c3);
        System.out.println("t1: " + t1);
        System.out.println("t2: " + t2);
        System.out.println("t3: " + t3);
    }

    private static void experimento2() {
        Random random = new Random();
        ArrayList<Double> c1 = new ArrayList<>(), c2  = new ArrayList<>();
        ArrayList<Long> t1 = new ArrayList<>(), t2  = new ArrayList<>();
        for (int i = 0; i < 10; ++i) {
            int seed = random.nextInt(10000);
            for (int j = 1; j < 3; ++j) {
                Estado e = new Estado(100, seed, 1.2, 1);
                if (j == 1)
                    e.generarSolucionInicial1();
                else {
                    e.generarSolucionInicial2();
                }
                try {
                    long ini = System.currentTimeMillis();
                    double coste = BusquedaHillClimbing(e, 1);
                    long fin = System.currentTimeMillis();
                    if (j == 1) {
                        c1.add(coste);
                        t1.add(fin-ini);
                    }
                    else {
                        c2.add(coste);
                        t2.add(fin-ini);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        System.out.println("c1: " + c1);
        System.out.println("c2: " + c2);
        System.out.println("t1: " + t1);
        System.out.println("t2: " + t2);
    }

    private static void experimento3() {
    }

    private static void experimento4() {
        System.out.println("Qué desea aumentar?\nProporción del peso transportable: 1\nNúmero de paquetes: 2");
        Scanner in = new Scanner(System.in);
        int op = in.nextInt();
        while (op != 1 && op != 2) {
            System.out.println("Introduce un valor correcto: 1 o 2");
            op = in.nextInt();
        }
        if (op == 1) experimento41();
        else experimento42();
    }

    private static void experimento41() {
        Random random = new Random();
        ArrayList<Long> t1 = new ArrayList<>(), t2 = new ArrayList<>(), t3 = new ArrayList<>(), t4 = new ArrayList<>(), t5 = new ArrayList<>();
        ArrayList<Long> t6 = new ArrayList<>(), t7 = new ArrayList<>(), t8 = new ArrayList<>(), t9 = new ArrayList<>(), t10 = new ArrayList<>();

        for (int i = 0; i < 10; ++i) {
            int seed = random.nextInt(10000);
            System.out.println("semilla " + seed);
            double proporcion = 1.2;

            while (proporcion <= 5) {
                System.out.println("proporcion " + proporcion);
                Estado e = new Estado(100, seed, proporcion, 1);
                e.generarSolucionInicial2();

                try {
                    long ini = System.currentTimeMillis();
                    BusquedaHillClimbing(e, 1);
                    long fin = System.currentTimeMillis();
                    System.out.println("temps " + (fin-ini));
                    System.out.println("fin " + fin);
                    System.out.println("inic " + ini);
                    if (i == 0)
                        t1.add(fin - ini);
                    else if (i == 1)
                        t2.add(fin - ini);
                    else if (i == 2)
                        t3.add(fin- ini);
                    else if (i == 3)
                        t4.add(fin - ini);
                    else if (i == 4)
                        t5.add(fin - ini);
                    else if (i == 5)
                        t6.add(fin - ini);
                    else if (i == 6)
                        t7.add(fin - ini);
                    else if (i == 7)
                        t8.add(fin - ini);
                    else if (i == 8)
                        t9.add(fin - ini);
                    else
                        t10.add(fin - ini);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                proporcion += 0.2;
            }
        }

        System.out.println("t1: " + t1);
        System.out.println("t2: " + t2);
        System.out.println("t3: " + t3);
        System.out.println("t4: " + t4);
        System.out.println("t5: " + t5);
        System.out.println("t6: " + t6);
        System.out.println("t7: " + t7);
        System.out.println("t8: " + t8);
        System.out.println("t9: " + t9);
        System.out.println("t10: " + t10);
    }

    private static void experimento42() {
        Random random = new Random();
        ArrayList<Long> t1 = new ArrayList<>(), t2 = new ArrayList<>(), t3 = new ArrayList<>(), t4 = new ArrayList<>(), t5 = new ArrayList<>();
        ArrayList<Long> t6 = new ArrayList<>(), t7 = new ArrayList<>(), t8 = new ArrayList<>(), t9 = new ArrayList<>(), t10 = new ArrayList<>();

        for (int i = 0; i < 10; ++i) {
            int seed = random.nextInt(10000);
            System.out.println("semilla " + seed);
            int nPaquetes = 100;

            while (nPaquetes <= 1000) {
                System.out.println("npaquetes " + nPaquetes);
                Estado e = new Estado(nPaquetes, seed, 1.2, 1);
                e.generarSolucionInicial2();

                try {
                    long ini = System.currentTimeMillis();
                    BusquedaHillClimbing(e, 1);
                    long fin = System.currentTimeMillis();
                    System.out.println("temps " + (fin-ini));
                    System.out.println("fin " + fin);
                    System.out.println("inic " + ini);
                    if (i == 0)
                        t1.add(fin - ini);
                    else if (i == 1)
                        t2.add(fin - ini);
                    else if (i == 2)
                        t3.add(fin- ini);
                    else if (i == 3)
                        t4.add(fin - ini);
                    else if (i == 4)
                        t5.add(fin - ini);
                    else if (i == 5)
                        t6.add(fin - ini);
                    else if (i == 6)
                        t7.add(fin - ini);
                    else if (i == 7)
                        t8.add(fin - ini);
                    else if (i == 8)
                        t9.add(fin - ini);
                    else
                        t10.add(fin - ini);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                nPaquetes += 50;
            }
        }

        System.out.println("t1: " + t1);
        System.out.println("t2: " + t2);
        System.out.println("t3: " + t3);
        System.out.println("t4: " + t4);
        System.out.println("t5: " + t5);
        System.out.println("t6: " + t6);
        System.out.println("t7: " + t7);
        System.out.println("t8: " + t8);
        System.out.println("t9: " + t9);
        System.out.println("t10: " + t10);
    }

    private static void experimento5() {
        FileWriter fichero = null;
        try {
            fichero = new FileWriter("/Users/laia.ondono/Documents/AAAquart-Q1/ia/laboratori/practica1/IA-Busqueda-Local/exp5.txt", false);
            PrintWriter pw = new PrintWriter(fichero);
            pw.print("");
        } catch (Exception e2) {
            e2.printStackTrace();
        } finally {
            try {
                if (null != fichero)
                    fichero.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        experimento41();
    }

    private static void experimento6() { //todo
    }

    private static void experimento7() { //todo
    }

    private static void experimento8() { //todo
    }

    private static void experimento9() {
        Estado e = new Estado(100, 1234, 1.2, 1);
        e.generarSolucionInicial2();
        try {
            BusquedaHillClimbing(e, 1);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static double BusquedaHillClimbing(Estado e, int funcionHeuristica) throws Exception {
        Problem problema;
        if (funcionHeuristica == 1) problema = new Problem(e, new EstadosSucesoresHC(), new EstadoFinal(), new FuncionHeuristica1());
        else problema = new Problem(e, new EstadosSucesoresHC(), new EstadoFinal(), new FuncionHeuristica2());
        Search search = new HillClimbingSearch();
        long ini = System.currentTimeMillis();
        SearchAgent agent = new SearchAgent(problema, search);
        long fin = System.currentTimeMillis();
        System.out.println("Tiempo de ejecución = " + (fin - ini) + " ms");

        //printActions(agent.getActions());

        System.out.println("Precio = " + ((Estado) search.getGoalState()).getPrecio());
        System.out.println("Felicidad = " + ((Estado) search.getGoalState()).getFelicidad());

        FileWriter fichero = null;
        try {
            fichero = new FileWriter("/Users/laia.ondono/Documents/AAAquart-Q1/ia/laboratori/practica1/IA-Busqueda-Local/exp5.txt", true);
            PrintWriter pw = new PrintWriter(fichero);
            if (niteracio % 19 == 0)
                pw.println(String.format("%.2f", ((Estado) search.getGoalState()).getPrecio()) + " ");
            else
                pw.print(String.format("%.2f", ((Estado) search.getGoalState()).getPrecio()) + " ");
            ++niteracio;
        } catch (Exception e2) {
            e2.printStackTrace();
        } finally {
            try {
                if (null != fichero)
                    fichero.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }

        return ((Estado) search.getGoalState()).getPrecio();
    }

    private static void BusquedaSimulatedAnnealing(Estado e, int funcionHeuristica) throws Exception {
        Problem problema;
        if (funcionHeuristica == 1) problema = new Problem(e, new EstadosSucesoresSA(), new EstadoFinal(), new FuncionHeuristica1());
        else problema = new Problem(e, new EstadosSucesoresSA(), new EstadoFinal(), new FuncionHeuristica2());
        Search search = new SimulatedAnnealingSearch(10000, 100, 5, 0.001); //TODO
        SearchAgent agent = new SearchAgent(problema, search);

        System.out.println("Precio = " + ((Estado) search.getGoalState()).getPrecio());
        System.out.println("Felicidad = " + ((Estado) search.getGoalState()).getFelicidad());
    }

    private static void printActions(List actions) {
        for (int i = 0; i < actions.size(); i++) {
            String action = (String) actions.get(i);
            System.out.println(action);
        }
    }
}
