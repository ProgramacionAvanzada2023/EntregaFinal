package ClasesPrincipales;

import java.util.ArrayList;
import java.util.List;

public class CSVLabeledFileReader extends CSVUnlabeledFileReader{

    public CSVLabeledFileReader(){
        super();
        super.tabla = (TableWithLabels) new TableWithLabels();
    }

    @Override
    void processData(String data) {
        String[] listaValores = getCurrentLine().split(SEPARADOR);
        List<Double> dimensiones = new ArrayList<>();
        for (int i = 0; i < super.tabla.getHeaders().size() -1; i++ ) {
            dimensiones.add(Double.parseDouble(listaValores[i]));
        }
        RowWithLabel fila = new RowWithLabel(dimensiones);

        String clase = listaValores[super.tabla.getHeaders().size() -1];
        ((TableWithLabels) super.tabla).getClasificacion().putIfAbsent(clase,((TableWithLabels) super.tabla).getClasificacion().size()+1);
        int numFlor = ((TableWithLabels) super.tabla).getClasificacion().get(clase);
        fila.setNumberClass(numFlor);

        super.tabla.addFilas(fila);
    }
}
