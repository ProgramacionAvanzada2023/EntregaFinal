package Modelo;

import ClasesDistancias.Distance;
import ClasesDistancias.DistanceEnum;
import ClasesDistancias.EuclideanDistance;
import ClasesDistancias.ManhattanDistance;
import ClasesPrincipales.*;
import Vista.ImplementacionVista;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImplementacionModelo implements Modelo{
    private String sep = System.getProperty("file.separator");
    private String ruta = "src/main/java/recsys";
    private ImplementacionVista vista;
    private Algorithm algoritmo;
    private RecSys recSys;
    private Map<String,String> filenames;
    private Map<String, Table> tables;

    public ImplementacionModelo() throws IOException {
        cargarDatosIniciales();
    }

    public ImplementacionVista getVista() {
        return vista;
    }

    public void setVista(ImplementacionVista vista) {
        this.vista = vista;
    }

    public Algorithm getAlgoritmo() {
        return algoritmo;
    }

    public void setAlgoritmo(Algorithm algorithm) {
        this.algoritmo = algorithm;
    }

    public RecSys getRecSys() {
        return recSys;
    }

    public void setRecSys(RecSys recSys) {
        this.recSys = recSys;
    }

    private void cargarDatosIniciales() throws IOException {
        this.filenames = new HashMap<>();
        filenames.put("knn"+"train",ruta+sep+"songs_train.csv");
        filenames.put("knn"+"test",ruta+sep+"songs_test.csv");
        filenames.put("kmeans" +"train",ruta+sep+"songs_train_withoutnames.csv");
        filenames.put("kmeans" +"test",ruta+sep+"songs_test_withoutnames.csv");

        // Tables
        this.tables = new HashMap<>();
        String [] stages = {"train", "test"};
        for (String stage : stages) {
            CSVLabeledFileReader csv = new CSVLabeledFileReader();
            csv.readTableFromSource(filenames.get("knn" + stage));
            TableWithLabels tablaKnn = (TableWithLabels) csv.tabla;
            this.tables.put("knn" + stage,tablaKnn);

            csv = new CSVLabeledFileReader();
            csv.readTableFromSource(filenames.get("kmeans" + stage));
            TableWithLabels tablaKmeans = (TableWithLabels) csv.tabla;
            this.tables.put("kmeans" + stage,tablaKmeans);
        }
    }

    @Override
    public void crearAlgoritmo(AlgorithmEnum algoritmoElegido, DistanceEnum tipoDistancia) throws Exception {
        Distance distance = null;
        switch (tipoDistancia){
            case EUCLIDEAN:
                distance = new EuclideanDistance();
                break;
            case MANHATTAN:
                distance = new ManhattanDistance();
                break;
        }

        switch (algoritmoElegido){
            case Knn:
                algoritmo = new KNN(distance);
                break;
            case KMeans:
                algoritmo = new KMeans(3, 20, 4321, distance);
        }
        this.recSys = new RecSys(algoritmo);
        recSys.train(tables.get(algoritmoElegido.getDescripcion()+"train"));
        recSys.run(tables.get(algoritmoElegido.getDescripcion()+"test"), getListaCanciones());
        vista.getListaCanciones();
    }

    @Override
    public List<String> listaCancionesRecomendadas(String itemSelecionado, int numeroRecomendacion) {
        return recSys.recommend(itemSelecionado, numeroRecomendacion);
    }
    @Override
    public List<String> getListaCanciones() throws IOException {
        return readNames(ruta+sep+"songs_test_names.csv");
    }

    private List<String> readNames(String fileOfItemNames) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileOfItemNames));
        String line;
        List<String> names = new ArrayList<>();

        while ((line = br.readLine()) != null) {
            names.add(line);
        }
        br.close();
        return names;
    }
}

