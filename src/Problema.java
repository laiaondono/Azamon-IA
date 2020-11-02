import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchAgent;
import aima.search.informed.HillClimbingSearch;
import aima.search.informed.SimulatedAnnealingSearch;

import javax.swing.*;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.*;

public class Problema {
    //ATRIBUTOS USADOS PARA LOS EXPERIMENTOS
    static private int niteracio = 1;
    static private ArrayList<Integer> felicidad = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        Scanner in = new Scanner(System.in);
        Random random = new Random();

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
                while (op < 1 || op > 9 || op == 8) {
                    System.out.println("Introduce un valor del 1 al 9 (exceptuando el 8)");
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

                Estado e;
                if (op == 1) e = new Estado(nPaq, seed, proporcion, 1, 11);
                else e = new Estado(nPaq, seed, proporcion, 1, 10);

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

                if (op == 1)
                    BusquedaHillClimbing(e, funcHeur);
                else
                    BusquedaSimulatedAnnealing(e, funcHeur, 10000, 100, 25, 0.0001);

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

    public static double BusquedaHillClimbing(Estado e, int funcionHeuristica) throws Exception {
        Problem problema;
        if (funcionHeuristica == 1) problema = new Problem(e, new EstadosSucesoresHC(), new EstadoFinal(), new FuncionHeuristica1());
        else problema = new Problem(e, new EstadosSucesoresHC(), new EstadoFinal(), new FuncionHeuristica2());
        Search search = new HillClimbingSearch();
        long ini = System.currentTimeMillis();
        SearchAgent agent = new SearchAgent(problema, search);
        long fin = System.currentTimeMillis();
        System.out.println("Tiempo de ejecución = " + (fin - ini) + " ms");

        imprimirAcciones(agent.getActions());

        double precio = (double)Math.round(((Estado) search.getGoalState()).getPrecio() * 100d) / 100d;

        System.out.println("Precio = " + precio);
        System.out.println("Felicidad = " + ((Estado) search.getGoalState()).getFelicidad());

        FileWriter fichero = null;
        fichero = new FileWriter("exp5.txt", true);
        PrintWriter pw = new PrintWriter(fichero);
        if (niteracio % 20 == 0)
            pw.println(String.format("%.2f", ((Estado) search.getGoalState()).getPrecio()) + " ");
        else
            pw.print(String.format("%.2f", ((Estado) search.getGoalState()).getPrecio()) + " ");
        fichero.close();
        ++niteracio;

        felicidad.add(((Estado) search.getGoalState()).getFelicidad());

        return ((Estado) search.getGoalState()).getPrecio();
    }

    private static double BusquedaSimulatedAnnealing(Estado e, int funcionHeuristica, int steps, int stiter, int k, double lamb) throws Exception {
        Problem problema;
        if (funcionHeuristica == 1) problema = new Problem(e, new EstadosSucesoresSA(), new EstadoFinal(), new FuncionHeuristica1());
        else problema = new Problem(e, new EstadosSucesoresSA(), new EstadoFinal(), new FuncionHeuristica2());
        Search search = new SimulatedAnnealingSearch(steps, stiter, k, lamb);
        SearchAgent agent = new SearchAgent(problema, search);

        double precio = (double)Math.round(((Estado) search.getGoalState()).getPrecio() * 100d) / 100d;

        System.out.println("Precio = " + precio);
        System.out.println("Felicidad = " + ((Estado) search.getGoalState()).getFelicidad());

        felicidad.add(((Estado) search.getGoalState()).getFelicidad());

        return ((Estado) search.getGoalState()).getPrecio();
    }

    private static void imprimirAcciones(List actions) {
        for (int i = 0; i < actions.size(); i++) {
            String action = (String) actions.get(i);
            System.out.println(action);
        }
    }

    //MÉTODOS USADOS PARA LOS EXPERIMENTOS
    private static void experimento1() throws Exception {
        Random random = new Random();
        ArrayList<Double> c1 = new ArrayList<>(), c2  = new ArrayList<>(), c3  = new ArrayList<>();
        ArrayList<Long> t1 = new ArrayList<>(), t2  = new ArrayList<>(), t3  = new ArrayList<>();
        for (int i = 0; i < 10; ++i) {
            int seed = random.nextInt(10000);
            for (int j = 1; j < 4; ++j) {
                Estado e = new Estado(100, seed, 1.2, j, 5);
                e.generarSolucionInicial2();

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
            }
        }

        for (int i = 0; i < c1.size(); ++i) {
            c1.set(i, (double)Math.round(c1.get(i) * 100d) / 100d);
            c2.set(i, (double)Math.round(c1.get(i) * 100d) / 100d);
            c3.set(i, (double)Math.round(c1.get(i) * 100d) / 100d);
        }

        System.out.println("c1: " + c1);
        System.out.println("c2: " + c2);
        System.out.println("c3: " + c3);
        System.out.println("t1: " + t1);
        System.out.println("t2: " + t2);
        System.out.println("t3: " + t3);
    }

    private static void experimento2() throws Exception {
        Random random = new Random();
        ArrayList<Double> c1 = new ArrayList<>(), c2  = new ArrayList<>();
        ArrayList<Long> t1 = new ArrayList<>(), t2  = new ArrayList<>();
        for (int i = 0; i < 10; ++i) {
            int seed = random.nextInt(10000);
            for (int j = 1; j < 3; ++j) {
                Estado e = new Estado(100, seed, 1.2, 1, 5);
                if (j == 1)
                    e.generarSolucionInicial1();
                else
                    e.generarSolucionInicial2();
                long ini = System.currentTimeMillis();
                double coste = BusquedaHillClimbing(e, 1);
                long fin = System.currentTimeMillis();
                if (j == 1) {
                    c1.add(coste);
                    t1.add(fin - ini);
                } else {
                    c2.add(coste);
                    t2.add(fin - ini);
                }
            }
        }

        for (int i = 0; i < c1.size(); ++i) {
            c1.set(i, (double)Math.round(c1.get(i) * 100d) / 100d);
            c2.set(i, (double)Math.round(c1.get(i) * 100d) / 100d);
        }

        System.out.println("c1: " + c1);
        System.out.println("c2: " + c2);
        System.out.println("t1: " + t1);
        System.out.println("t2: " + t2);
    }

    private static void experimento3() throws Exception {
        experimento31();
        experimento32();
    }

    private static void experimento31() throws Exception {
        Random random = new Random();
        ArrayList<Double> costes = new ArrayList<>();
        for (int i = 0; i < 12; ++i)
            costes.add(0.0);

        ArrayList<Integer> k = new ArrayList<>(Arrays.asList(1, 5, 25, 125));
        ArrayList<Double> lambda = new ArrayList<>(Arrays.asList(1.0, 0.01, 0.0001));
        int cnt = 0;

        for (int i = 0; i < 10; ++i) {
            int seed = random.nextInt(10000);
            for (int j = 0; j < k.size(); ++j) {
                for (int l = 0; l < lambda.size(); ++l) {
                    Estado e = new Estado(100, seed, 1.2, 1, 5);
                    e.generarSolucionInicial2();
                    double c = BusquedaSimulatedAnnealing(e, 1, 50000, 500, k.get(j), lambda.get(l));
                    costes.set(cnt, costes.get(cnt) + c);
                    ++cnt;
                }
            }
            cnt = 0;
        }
        for (int i = 0; i < costes.size(); ++i)
            costes.set(i, costes.get(i)/10);

        for (int i = 0; i < costes.size(); ++i)
            costes.set(i, (double)Math.round(costes.get(i) * 100d) / 100d);

        System.out.println("costes final: " + costes);
        generarGraficaBarras(costes, k, lambda);
    }

    private static void experimento32() throws Exception {
        Random random = new Random();
        ArrayList<Double> costes = new ArrayList<>();
        for (int i = 0; i < 3; ++i)
            costes.add(0.0);

        ArrayList<Integer> steps = new ArrayList<>(Arrays.asList(5000, 10000, 50000));
        ArrayList<Integer> stiter = new ArrayList<>(Arrays.asList(100, 100, 500));
        int cnt = 0;
        for (int i = 0; i < 10; ++i) {
            int seed = random.nextInt(10000);
            for (int j = 0; j < steps.size(); ++j) {
                Estado e = new Estado(100, seed, 1.2, 1, 5);
                e.generarSolucionInicial2();
                double c = BusquedaSimulatedAnnealing(e, 1, steps.get(j), stiter.get(j), 25, 0.0001);
                costes.set(cnt, costes.get(cnt) + c);
                ++cnt;
            }
            cnt = 0;
        }
        for (int i = 0; i < costes.size(); ++i)
            costes.set(i, costes.get(i)/10);

        for (int i = 0; i < costes.size(); ++i)
            costes.set(i, (double)Math.round(costes.get(i) * 100d) / 100d);

        System.out.println("costes final: " + costes);
        generarGraficaBarras2(costes);
    }

    private static void experimento4() throws Exception {
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

    private static void experimento41() throws Exception {
        Random random = new Random();
        ArrayList<Long> t1 = new ArrayList<>(), t2 = new ArrayList<>(), t3 = new ArrayList<>(), t4 = new ArrayList<>(), t5 = new ArrayList<>();
        ArrayList<Long> t6 = new ArrayList<>(), t7 = new ArrayList<>(), t8 = new ArrayList<>(), t9 = new ArrayList<>(), t10 = new ArrayList<>();

        for (int i = 0; i < 10; ++i) {
            int seed = random.nextInt(10000);
            System.out.println("semilla " + seed);
            double proporcion = 1.2;

            while (proporcion <= 5.1) {
                Estado e = new Estado(100, seed, proporcion, 1, 5);
                e.generarSolucionInicial2();

                long ini = System.currentTimeMillis();
                BusquedaHillClimbing(e, 1);
                long fin = System.currentTimeMillis();
                System.out.println("temps " + (fin-ini));
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

        generarGraficaLineas(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10);
    }

    private static void experimento42() throws Exception {
        Random random = new Random();
        ArrayList<Long> t1 = new ArrayList<>(), t2 = new ArrayList<>(), t3 = new ArrayList<>(), t4 = new ArrayList<>(), t5 = new ArrayList<>();
        ArrayList<Long> t6 = new ArrayList<>(), t7 = new ArrayList<>(), t8 = new ArrayList<>(), t9 = new ArrayList<>(), t10 = new ArrayList<>();

        for (int i = 0; i < 10; ++i) {
            int seed = random.nextInt(10000);
            System.out.println("semilla " + seed);
            int nPaquetes = 100;

            while (nPaquetes <= 1000) {
                System.out.println("npaquetes " + nPaquetes);
                Estado e = new Estado(nPaquetes, seed, 1.2, 1, 5);
                e.generarSolucionInicial2();

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

    private static void experimento5() throws Exception {
        FileWriter fichero = null;
        fichero = new FileWriter("exp5.txt", false);
        PrintWriter pw = new PrintWriter(fichero);
        pw.print("");
        if (null != fichero)
            fichero.close();
        experimento41();
    }

    private static void experimento6() throws Exception {
        Random random = new Random();
        ArrayList<Long> tiemposEjec = new ArrayList<>();
        ArrayList<Double> costes = new ArrayList<>();
        ArrayList<Double> felicidad2 = new ArrayList<>();

        for (int i = 0; i < 20; ++i) {
            tiemposEjec.add((long)0);
            costes.add(0.0);
            felicidad2.add(0.0);
        }

        for (int i = 0; i < 10; ++i) {
            int seed = random.nextInt(10000);
            for (int pond = 1; pond <= 20; ++pond) {
                Estado e = new Estado(100, seed, 1.2, 1, pond);
                e.generarSolucionInicial2();

                long ini = System.currentTimeMillis();
                double c = BusquedaHillClimbing(e, 2);
                long fin = System.currentTimeMillis();

                tiemposEjec.set(pond - 1, tiemposEjec.get(pond - 1) + fin-ini);
                costes.set(pond - 1, costes.get(pond - 1) + c);
            }
        }

        for (int i = 0; i < felicidad.size(); ++i) {
            felicidad2.set(i % 20, felicidad2.get(i % 20) + felicidad.get(i));
        }

        for (int i = 0; i < 20; ++i) {
            tiemposEjec.set(i, tiemposEjec.get(i) /10);
            costes.set(i, costes.get(i)/10);
            felicidad2.set(i, felicidad2.get(i)/10);
        }

        for (int i = 0; i < costes.size(); ++i)
            costes.set(i, (double)Math.round(costes.get(i) * 100d) / 100d);

        System.out.println("costes " + costes);
        System.out.println("teimpo " + tiemposEjec);

        generarGraficaLineas2(costes);
        generarGraficaLineas3(tiemposEjec);
        generarGraficaLineas4(felicidad2);
    }

    private static void experimento7() throws Exception {
        Random random = new Random();
        ArrayList<Long> tiemposEjec = new ArrayList<>();
        ArrayList<Double> costes = new ArrayList<>();
        ArrayList<Double> felicidad2 = new ArrayList<>();

        for (int i = 0; i < 20; ++i) {
            tiemposEjec.add((long)0);
            costes.add(0.0);
            felicidad2.add(0.0);
        }

        for (int i = 0; i < 10; ++i) {
            int seed = random.nextInt(10000);
            for (int pond = 1; pond <= 20; ++pond) {
                Estado e = new Estado(100, seed, 1.2, 1, pond);
                e.generarSolucionInicial2();

                long ini = System.currentTimeMillis();
                double c = BusquedaSimulatedAnnealing(e, 2, 10000, 100, 25, 0.0001);
                long fin = System.currentTimeMillis();

                tiemposEjec.set(pond - 1, tiemposEjec.get(pond - 1) + fin-ini);
                costes.set(pond - 1, costes.get(pond - 1) + c);
            }
        }

        for (int i = 0; i < felicidad.size(); ++i) {
            felicidad2.set(i % 20, felicidad2.get(i % 20) + felicidad.get(i));
        }

        for (int i = 0; i < 20; ++i) {
            tiemposEjec.set(i, tiemposEjec.get(i) /10);
            costes.set(i, costes.get(i)/10);
            felicidad2.set(i, felicidad2.get(i)/10);
        }

        for (int i = 0; i < costes.size(); ++i)
            costes.set(i, (double)Math.round(costes.get(i) * 100d) / 100d);

        System.out.println("costes " + costes);
        System.out.println("teimpo " + tiemposEjec);

        generarGraficaLineas2(costes);
        generarGraficaLineas3(tiemposEjec);
        generarGraficaLineas4(felicidad2);
    }

    private static void experimento9() throws Exception {
        Estado e = new Estado(100, 1234, 1.2, 1, 5);
        e.generarSolucionInicial2();
        BusquedaHillClimbing(e, 1);
    }

    private static void generarGraficaBarras(ArrayList<Double> precios, ArrayList<Integer> k, ArrayList<Double> l) {
        BarChart chart = new BarChart(precios, k, l);
        chart.setAlwaysOnTop(true);
        chart.pack();
        chart.setSize(600, 400);
        chart.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        chart.setVisible(true);
    }

    private static void generarGraficaBarras2(ArrayList<Double> precios) {
        BarChart chart = new BarChart(precios);
        chart.setAlwaysOnTop(true);
        chart.pack();
        chart.setSize(500, 400);
        chart.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        chart.setVisible(true);
    }

    private static void generarGraficaLineas(ArrayList<Long> t1, ArrayList<Long> t2, ArrayList<Long> t3, ArrayList<Long> t4, ArrayList<Long> t5,
                                             ArrayList<Long> t6, ArrayList<Long> t7, ArrayList<Long> t8, ArrayList<Long> t9, ArrayList<Long> t10) {
        LineChart chart = new LineChart(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10);
        chart.setAlwaysOnTop(true);
        chart.pack();
        chart.setSize(600, 400);
        chart.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        chart.setVisible(true);
    }

    private static void generarGraficaLineas3(ArrayList<Long> tiemposEjec) {
        LineChart chart = new LineChart(tiemposEjec);
        chart.setAlwaysOnTop(true);
        chart.pack();
        chart.setSize(600, 400);
        chart.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        chart.setVisible(true);
    }

    private static void generarGraficaLineas2(ArrayList<Double> costes) {
        LineChart chart = new LineChart(costes, 0);
        chart.setAlwaysOnTop(true);
        chart.pack();
        chart.setSize(600, 400);
        chart.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        chart.setVisible(true);
    }

    private static void generarGraficaLineas4(ArrayList<Double> felicidad) {
        LineChart chart = new LineChart(felicidad, 1);
        chart.setAlwaysOnTop(true);
        chart.pack();
        chart.setSize(600, 400);
        chart.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        chart.setVisible(true);
    }

}
