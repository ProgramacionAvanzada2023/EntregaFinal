package ClasesPrincipales;

import java.util.List;

public interface Algorithm<Table, Object, Distance> {
    void train(Table table);
    Integer estimate(List<Double> listaData);
}
