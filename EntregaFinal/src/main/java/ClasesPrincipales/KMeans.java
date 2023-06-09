package ClasesPrincipales;

import ClasesDistancias.Distance;
import ClasesDistancias.DistanceClient;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class KMeans implements Algorithm<TableWithLabels, Integer, DistanceClient> {
    private TableWithLabels tabla;
    private List<Row> listaCentroides;
    private int numClusters;
    private int numIterations;
    private Random random;
    private Distance distance;

    public KMeans(int numClusters, int numIterations, long seed, Distance distance) {
        //numClusters (K) es el número de grupos
        this.numClusters = numClusters;
        //numIterations (N) // es el número de iteraciones
        this.numIterations = numIterations;
        this.random = new Random(seed);
        this.distance = distance;
    }

    public KMeans(int numClusters, int numIterations, long seed) {
        //numClusters (K) es el número de grupos
        this.numClusters = numClusters;
        //numIterations (N) // es el número de iteraciones
        this.numIterations = numIterations;
        this.random = new Random(seed);
    }

    @Override
    public void train(TableWithLabels data) {
        tabla = data;
        if (numClusters > numIterations) {
            throw new InvalidKException("K tiene que ser menor o igual que N");
        }
        //Centros aleatorios
        listaCentroides = generarCentroidesAleatorios(tabla.getHeaders().size());

        for (int iteraciones = 0; iteraciones < numIterations; iteraciones++) {
            Map<Integer, List<Row>> clusters = getClusterAsociadoACadaCentroide();
            recalcularCentroides(clusters);
        }
    }

    private void recalcularCentroides(Map<Integer, List<Row>> clusters) {
        for (Integer cluster : clusters.keySet()) {
            List<Double> puntoCentro = new ArrayList<>();
            for ( int i = 0; i < tabla.getHeaders().size()-1; i++){
                double suma = 0;
                int cantidad = clusters.get(cluster).size();
                for (Row fila : clusters.get(cluster)) {
                    suma += fila.getFila().get(i);
                }
                puntoCentro.add(suma/cantidad);
            }
            Row nuevoCentroide = new Row(puntoCentro);
            listaCentroides.add(nuevoCentroide);
        }
    }

    @Override
    public Integer estimate(List<Double> data) {
        return obtenerClusterMasCercano(data);
    }

    public void setNumIterations(int numIterations){
        this.numIterations = numIterations;
    }
    private List<Row> generarCentroidesAleatorios(int numeroDeCentroides) {
        List<Row> centroides = new ArrayList<>();
        for (int i = 0; i < numClusters; i++){
            List<Double> punto = new ArrayList<>();
            for (int j = 0; j < numeroDeCentroides; j++){
                punto.add(random.nextDouble());
            }
            Row fila = new Row(punto);
            centroides.add(fila);
        }
        return centroides;
    }

    private int obtenerClusterMasCercano(List<Double> fila) {
        double distanciaMinima = Double.MAX_VALUE;
        int clusterMasCercano = -1;
        for (int i = 0; i < listaCentroides.size(); i++) {
            double distancia = distance.calculateDistance(fila, listaCentroides.get(i).getFila());
            if (distancia < distanciaMinima) {
                distanciaMinima = distancia;
                clusterMasCercano = i;
            }
        }

        return clusterMasCercano;
    }

    public List<List<Double>> getCentroides() {
        List<List<Double>> centroides = new ArrayList<>();
        for (Row centroide : listaCentroides) {
            centroides.add(centroide.getFila());
        }
        return centroides;
    }

    public Map<Integer, List<Row>> getClusterAsociadoACadaCentroide() {
        Map<Integer, List<Row>> clusters = new HashMap<>();

        for (int i = 0; i < tabla.getNumeroDeFilas(); i++) {
            List<Double> fila = tabla.getRowAt(i).getFila();
            int cluster = obtenerClusterMasCercano(fila);

            if (!clusters.containsKey(cluster)) {
                List<Row> lista = new ArrayList<>();
                lista.add(tabla.getRowAt(i));
                clusters.put(cluster, lista);
            }
            clusters.get(cluster).add(new Row(fila));
        }

        return clusters;
    }

    public Distance getDistance() {
        return distance;
    }

    public void setDistance(Distance distance) {
        this.distance = distance;
    }
}