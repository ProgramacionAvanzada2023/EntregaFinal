package ClasesPrincipales;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SongRecSys {
    private RecSys recsys;

    private List<String> names;

    public SongRecSys(String method) throws Exception {
        String sep = System.getProperty("file.separator");
        String ruta = "src/main/java/recsys";

        // File names (could be provided as arguments to the constructor to be more general)
        Map<String,String> filenames = new HashMap<>();
        filenames.put("knn"+"train",ruta+sep+"songs_train.csv");
        filenames.put("knn"+"test",ruta+sep+"songs_test.csv");
        filenames.put("kmeans" +"train",ruta+sep+"songs_train_withoutnames.csv");
        filenames.put("kmeans" +"test",ruta+sep+"songs_test_withoutnames.csv");

        // Algorithms
        Map<String, Algorithm> algorithms = new HashMap<>();
        algorithms.put("knn",new KNN());
        algorithms.put("kmeans",new KMeans(15, 200, 4321));

        // Tables
        Map<String, Table> tables = new HashMap<>();
        String [] stages = {"train", "test"};
        //Csv csv = new Csv();
        for (String stage : stages) {
            CSVLabeledFileReader csv = new CSVLabeledFileReader();

            csv.readTableFromSource(filenames.get("knn" + stage));
            TableWithLabels tablaKnn = (TableWithLabels) csv.tabla;
            tables.put("knn" + stage,tablaKnn);

            csv = new CSVLabeledFileReader();
            csv.readTableFromSource(filenames.get("kmeans" + stage));
            TableWithLabels tablaKmeans = (TableWithLabels) csv.tabla;
            tables.put("kmeans" + stage,tablaKmeans);
        }

        // Names of items
        this.names = readNames(ruta+sep+"songs_test_names.csv");

        // Start the RecSys
        this.recsys = new RecSys(algorithms.get(method));
        this.recsys.train(tables.get(method+"train"));
        this.recsys.run(tables.get(method+"test"), names);

        // Given a liked item, ask for a number of recomendations
        String liked_name = "Lootkemia";
        List<String> recommended_items = this.recsys.recommend(liked_name,5);

        // Display the recommendation text (to be replaced with graphical display with JavaFX implementation)
        reportRecommendation(liked_name,recommended_items);
    }

    public List<String> readNames(String fileOfItemNames) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileOfItemNames));
        String line;
        List<String> names = new ArrayList<>();

        while ((line = br.readLine()) != null) {
            names.add(line);
        }
        br.close();
        return names;
    }

    private void reportRecommendation(String liked_name, List<String> recommended_items) {
        System.out.println("If you liked \""+liked_name+"\" then you might like:");
        for (String name : recommended_items)
        {
            System.out.println("\t * "+name);
        }
    }

    public static void main(String[] args) throws Exception {
        new SongRecSys("knn");
        new SongRecSys("kmeans");
    }

    public List<String> getNames() {
        return names;
    }

    public RecSys getRecsys() {
        return recsys;
    }

    public void setRecsys(RecSys recsys) {
        this.recsys = recsys;
    }
}