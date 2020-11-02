import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EstadosSucesoresSA implements SuccessorFunction {

    @Override
    public List getSuccessors(Object o) {
        ArrayList<Successor> sucesores = new ArrayList<>();
        Estado e = (Estado) o;
        int nPaq = e.getPaquetes().size();
        int nOfertas = e.getOfertas().size();
        int nMoverPaquete = nOfertas * nPaq;
        int nIntercambiarPaquete = nPaq * (nPaq - 1) / 2;
        Random random = new Random();
        while (true) {
            int r = random.nextInt(nMoverPaquete + nIntercambiarPaquete);
            ++r;
            if (r <= nMoverPaquete) {
                Estado e2 = new Estado(e);
                int i = random.nextInt(nPaq);
                int j = random.nextInt(nOfertas);
                if (e2.moverPaquete(i, j)) {
                    sucesores.add(new Successor("paquete " + i + " movido a oferta " + j, e2));
                    break;
                }
            }
            else {
                Estado e2 = new Estado(e);
                int i = random.nextInt(nPaq);
                int j = random.nextInt(nPaq);
                while (j == i) j = random.nextInt(nPaq);
                if (e2.intercambiarPaquetes(i, j)) {
                    sucesores.add(new Successor("paquete " + i + " intercambiado con paquete " + j, e2));
                    break;
                }
            }
        }
        return sucesores;
    }
}