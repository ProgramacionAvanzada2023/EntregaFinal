package ClasesDistancias;

public enum DistanceEnum {
    EUCLIDEAN("Euclidea"),
    MANHATTAN("Manhattan");

    private String descripcion;

    DistanceEnum(String descripcion){
        this.descripcion = descripcion;
    }

    public String getDescripcion(){
        return descripcion;
    }
}
