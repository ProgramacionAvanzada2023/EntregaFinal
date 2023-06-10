package ClasesDistancias;

import java.util.List;

public class EuclideanDistance implements Distance{

    @Override
    public Double calculateDistance(List<Double> p, List<Double> q) {
        return calcularMetricaEuclidea(p,q);
    }

    private static double calcularMetricaEuclidea(List<Double> distancia1, List<Double> distancia2){
        int pos = 0;
        double suma = 0D;
        int minTamanyo = Math.min(distancia1.size(), distancia2.size());
        while ( pos < minTamanyo){
            suma += Math.pow(distancia2.get(pos) - distancia1.get(pos),2);
            pos++;
        }
        return Math.sqrt(suma);
    }
}
