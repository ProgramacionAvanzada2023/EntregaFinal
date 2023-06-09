package ClasesPrincipales;

import java.util.List;

public class RowWithLabel extends Row{
    private int numberClass;

    public RowWithLabel(List<Double> fila) {
        super(fila);
    }

    public int getNumberClass() {
        return this.numberClass;
    }

    public void setNumberClass(int numberClass) {
        this.numberClass = numberClass;
    }

}
