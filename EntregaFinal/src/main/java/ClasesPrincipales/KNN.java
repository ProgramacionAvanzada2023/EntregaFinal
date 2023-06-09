package ClasesPrincipales;

import ClasesDistancias.Distance;
import ClasesDistancias.DistanceClient;
import ClasesDistancias.EuclideanDistance;

import java.util.ArrayList;
import java.util.List;

public class KNN implements Algorithm<TableWithLabels, Integer, DistanceClient> {
    private TableWithLabels tabla;
    private Distance distance;

    public KNN(TableWithLabels tabla, Distance distance) {
        this.tabla = tabla;
        this.distance = distance;
    }

    public KNN(Distance distance){
        this.distance = distance;
    }

    public KNN() {}
    @Override
    public void train(TableWithLabels data){
        tabla = data;
    }

    @Override
    public Integer estimate(List<Double> data){
        Integer tipo = Integer.MAX_VALUE;
        List<Double> distancias = new ArrayList<>();
        for (Row fila : tabla.getListaFilas()) {
            RowWithLabel filaLabel = (RowWithLabel) fila;
            List<Double> flor = filaLabel.getFila();
            distancias.add(distance.calculateDistance(flor,data));
        }
        Integer posMin = getPosicionMasCerca(distancias);
        if(!posMin.equals(Integer.MAX_VALUE)){
            RowWithLabel fila = tabla.getRowAt(posMin);
            tipo = fila.getNumberClass();
        }
        return tipo;
    }

    private Integer getPosicionMasCerca(List<Double> distancias){
        Integer posMin = Integer.MAX_VALUE;
        Double min = Double.MAX_VALUE;

        if (distancias == null || distancias.size() == 0) {
            return posMin;
        }
        for (int i = 0; i < distancias.size(); i++) {
            Double distancia = distancias.get(i);
            if(min > distancia){
                min = distancia;
                posMin = i;
            }
        }
        return posMin;
    }

    public Distance getDistance() {
        return distance;
    }

    public void setDistance(Distance distance) {
        this.distance = distance;
    }
}
