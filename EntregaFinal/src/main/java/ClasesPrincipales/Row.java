package ClasesPrincipales;

import java.util.List;

public class Row {
    private List<Double> fila;

    public Row(List<Double> fila) {
        this.fila = fila;
    }

    public Row(){ super(); }
    public List<Double> getFila() {
        return fila;
    }

    public void setFila(List<Double> fila) {
        this.fila = fila;
    }
}
