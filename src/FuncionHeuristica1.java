import aima.search.framework.HeuristicFunction;

public class FuncionHeuristica1 implements HeuristicFunction {

    @Override
    public double getHeuristicValue(Object o) {
        Estado e = (Estado) o;
        return e.getPrecio();
    }
}
