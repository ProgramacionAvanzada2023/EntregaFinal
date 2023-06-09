package ClasesPrincipales;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CSVLabeledFileReader extends CSVUnlabeledFileReader{

    public CSVLabeledFileReader(){
        super();
        super.table = (TableWithLabels) new TableWithLabels();
    }

    @Override
    void processData(String data) {
        String[] listaValores = getCurrentLine().split(SEPARADOR);
        List<Double> dimensiones = new ArrayList<>();
        for (int i = 0; i < table.getHeaders().size() -1; i++ ) {
            dimensiones.add(Double.parseDouble(listaValores[i]));
        }
        RowWithLabel fila = new RowWithLabel(dimensiones);

        String clase = listaValores[table.getHeaders().size() -1];
        ((TableWithLabels) table).getClasificacion().putIfAbsent(clase,((TableWithLabels) table).getClasificacion().size()+1);
        int numFlor = ((TableWithLabels) table).getClasificacion().get(clase);
        fila.setNumberClass(numFlor);

        table.addFilas(fila);
    }
}
