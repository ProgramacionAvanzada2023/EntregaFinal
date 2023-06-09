package ClasesPrincipales;

public enum AlgorithmEnum {
    Knn("knn"),
    KMeans("kmeans");

    private String descripcion;

    private AlgorithmEnum(String descripcion){
        this.descripcion = descripcion;
    }

    public String getDescripcion(){
        return descripcion;
    }
}
