package ClasesPrincipales;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableWithLabels extends Table {
    private Map<String, Integer> clasificacion;

    public TableWithLabels (){
        super();
        this.clasificacion = new HashMap<>();

        for (Row f : super.getListaFilas()) {
            RowWithLabel fila = new RowWithLabel(f.getFila());
            addFilas(fila);
        }
    }

    @Override
    public void addFilas(Row fila) {
        if (fila instanceof RowWithLabel) {
            super.addFilas(fila);
        } else {
            /*RowWithLabel filaConEtiqueta = ;*/
            /*RowWithLabel filaConEtiqueta = new RowWithLabel(fila.getFila());*/
            super.addFilas((RowWithLabel) fila);
        }
    }
    public Map<String, Integer> getClasificacion() {
        return clasificacion;
    }

    public void setClasificacion(Map<String, Integer> clasificacion) {
        this.clasificacion = clasificacion;
    }

    @Override
    public RowWithLabel getRowAt(int numeroLinea) {
        return (RowWithLabel) super.getRowAt(numeroLinea);
    }
}
