package ClasesDistancias;

import java.util.List;

public class ManhattanDistance implements Distance{

    @Override
    public Double calculateDistance(List<Double> p, List<Double> q) {
        return calculardistanciaManhattan(p,q);
    }

    private static Double calculardistanciaManhattan(List<Double> distancia1, List<Double> distancia2){
        int pos = 0;
        Double suma = 0D;
        int minTamanyo = Math.min(distancia1.size(), distancia2.size());
        while ( pos < minTamanyo){
            suma += Math.abs(distancia2.get(pos) - distancia1.get(pos));
            pos++;
        }
        return suma;
    }
}
