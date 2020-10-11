import aima.search.framework.GoalTest;

public class EstadoFinal implements GoalTest {

    public boolean isGoalState(Object estado) {
        Estado e = (Estado) estado;
        return (e.esEstadoFinal());
    }
}