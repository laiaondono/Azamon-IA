import IA.Azamon.*;
import java.util.*;

public class Estado {
    private static Transporte ofertas;
    private static Paquetes paquetes;
    private ArrayList<Integer> asignacion;
    private ArrayList<Double> capacidad;
    private double precio;
    private int felicidad;

    //ATRIBUTOS USADOS PARA LOS EXPERIMENTOS
    private int operadores;
    private int ponderacion;

    public Estado(int nPaq, int seed, double proporcion, int operadores, int ponderacion) {
        paquetes = new Paquetes(nPaq, seed);
        ofertas = new Transporte(paquetes, proporcion, seed);
        asignacion = new ArrayList<>(nPaq);
        for (int i = 0; i < nPaq; ++i)
            asignacion.add(-1);
        capacidad = new ArrayList<>(ofertas.size());
        precio = 0.0;
        felicidad = 0;
        this.operadores = operadores;
        this.ponderacion = ponderacion;

    }

    public Estado(Estado e) {
        paquetes = e.getPaquetes();
        ofertas = e.getOfertas();
        asignacion = e.getAsignacion();
        capacidad = e.getCapacidad();
        precio = e.getPrecio();
        felicidad = e.getFelicidad();
        operadores = e.getOperadores();
        ponderacion = e.getPonderacion();
    }

    public Paquetes getPaquetes() {
        return paquetes;
    }

    public Transporte getOfertas() {
        return ofertas;
    }

    public ArrayList<Integer> getAsignacion() {
        return asignacion;
    }

    public ArrayList<Double> getCapacidad() {
        return capacidad;
    }

    public double getPrecio() {
        return precio;
    }

    public int getFelicidad() {
        return felicidad;
    }

    public int getOperadores() {
        return operadores;
    }

    public int getPonderacion() {
        return ponderacion;
    }

    public boolean esEstadoFinal() {
        return false;
    }

    public void calcularPrecio() {
        precio = 0.0;
        for (int i = 0; i < ofertas.size(); ++i) {
            precio += (ofertas.get(i).getPesomax() - capacidad.get(i)) * ofertas.get(i).getPrecio();
            if (ofertas.get(i).getDias() == 5) precio += (ofertas.get(i).getPesomax() - capacidad.get(i)) * 0.5;
            else if (ofertas.get(i).getDias() >= 3) precio += (ofertas.get(i).getPesomax() - capacidad.get(i)) * 0.25;
        }
    }

    public void calcularFelicidad() {
        felicidad = 0;
        for (int i = 0; i < asignacion.size(); ++i) {
            int prioridad = paquetes.get(i).getPrioridad();
            int dias = ofertas.get(asignacion.get(i)).getDias();
            if (prioridad == 1 && dias == 1) ++felicidad;
            else if (prioridad == 2) {
                if (dias == 3) ++felicidad;
                else if (dias == 2) felicidad += 2;
                else if (dias == 1) felicidad += 3;
            }
        }
    }

    public void generarSolucionInicial1() {
        Collections.sort(paquetes, new Comparator<Paquete>() {
            @Override
            public int compare(Paquete p1, Paquete p2) { //orden ascendente
                return Integer.compare(p1.getPrioridad(), p2.getPrioridad());
            }
        });

        Collections.sort(ofertas, new Comparator<Oferta>() {
            @Override
            public int compare(Oferta o1, Oferta o2) { //orden ascendente
                return Integer.compare(o1.getDias(), o2.getDias());
            }
        });

        for (int i = 0; i < ofertas.size(); ++i)
            capacidad.add(ofertas.get(i).getPesomax());

        for (int i = 0; i < paquetes.size(); ++i) {
            double peso = paquetes.get(i).getPeso();
            for (int j = 0; j < capacidad.size(); ++j) {
                if (capacidad.get(j) >= peso) {
                    asignacion.set(i, j);
                    capacidad.set(j, capacidad.get(j) - peso);
                    break;
                }
            }
        }
        calcularPrecio();
        calcularFelicidad();
    }

    public void generarSolucionInicial2() {
        ArrayList<Integer> Prioridad0 = new ArrayList<>();
        ArrayList<Integer> Prioridad1 = new ArrayList<>();
        ArrayList<Integer> Prioridad2 = new ArrayList<>();
        Random random = new Random();

        for(int i = 0; i < ofertas.size(); ++i){
            if (ofertas.get(i).getDias() == 1)
                Prioridad0.add(i);
            else if (ofertas.get(i).getDias() <= 3)
                Prioridad1.add(i);
            else
                Prioridad2.add(i);
        }

        for (int i = 0; i < ofertas.size(); ++i)
            capacidad.add(ofertas.get(i).getPesomax());

        for(int i = 0; i < paquetes.size(); ++i){
            int prioritat = paquetes.get(i).getPrioridad();
            int oferta;
            boolean assignat = false;
            do {
                if (prioritat == 0)
                    oferta = Prioridad0.get(random.nextInt(Prioridad0.size()));
                else if (prioritat == 1)
                    oferta = Prioridad1.get(random.nextInt(Prioridad1.size()));
                else
                    oferta = Prioridad2.get(random.nextInt(Prioridad2.size()));

                if (capacidad.get(oferta) >= paquetes.get(i).getPeso()) {
                    asignacion.set(i, oferta);
                    capacidad.set(oferta, capacidad.get(oferta) - paquetes.get(i).getPeso());
                    assignat = true;
                }
            } while(!assignat);
        }

        calcularPrecio();
        calcularFelicidad();
    }

    public boolean moverPaquete(int p, int o) {
        if (capacidad.get(o) >= paquetes.get(p).getPeso()) {
            if ((paquetes.get(p).getPrioridad() == 0 && ofertas.get(o).getDias() == 1) ||
                    (paquetes.get(p).getPrioridad() == 1 && ofertas.get(o).getDias() <= 3) || paquetes.get(p).getPrioridad() == 2) {
                capacidad.set(asignacion.get(p), capacidad.get(asignacion.get(p)) + paquetes.get(p).getPeso());
                asignacion.set(p, o);
                capacidad.set(o, capacidad.get(o) - paquetes.get(p).getPeso());

                calcularPrecio();
                calcularFelicidad();
                return true;
            }
        }
        return false;
    }

    public boolean intercambiarPaquetes(int p1, int p2) {
        int o1 = asignacion.get(p1);
        int o2 = asignacion.get(p2);

        if (capacidad.get(o2) + paquetes.get(p2).getPeso() >= paquetes.get(p1).getPeso() &&
                capacidad.get(o1) + paquetes.get(p1).getPeso() >= paquetes.get(p2).getPeso()) {
            if (((paquetes.get(p1).getPrioridad() == 0 && ofertas.get(o2).getDias() == 1) ||
                    (paquetes.get(p1).getPrioridad() == 1 && ofertas.get(o2).getDias() <= 3) || paquetes.get(p1).getPrioridad() == 2) &&
                    ((paquetes.get(p2).getPrioridad() == 0 && ofertas.get(o1).getDias() == 1) ||
                            (paquetes.get(p2).getPrioridad() == 1 && ofertas.get(o1).getDias() <= 3) || paquetes.get(p2).getPrioridad() == 2)) {
                capacidad.set(asignacion.get(p1), capacidad.get(asignacion.get(p1)) + paquetes.get(p1).getPeso());
                asignacion.set(p1, o2);
                capacidad.set(o2, capacidad.get(o2) - paquetes.get(p1).getPeso());

                capacidad.set(asignacion.get(p2), capacidad.get(asignacion.get(p2)) + paquetes.get(p2).getPeso());
                asignacion.set(p2, o1);
                capacidad.set(o1, capacidad.get(o1) - paquetes.get(p2).getPeso());

                calcularPrecio();
                calcularFelicidad();
                return true;
            }
        }
        return false;
    }

}




