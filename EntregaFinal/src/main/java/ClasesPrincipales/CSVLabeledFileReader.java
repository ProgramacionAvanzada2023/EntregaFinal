package ClasesPrincipales;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CSVLabeledFileReader extends CSVUnlabeledFileReader{
    public TableWithLabels table;
    private BufferedReader bufferLectura;
    public CSVLabeledFileReader(){
        super();
        this.table = new TableWithLabels();
    }
    void openSource(String source) {
        try {
            bufferLectura = (new BufferedReader(new FileReader(source)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    void processHeaders(String headers) {
        List<String> cabecera = Arrays.stream(headers.split(SEPARADOR)).toList();
        table.setHeaders(cabecera);
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
        table.getClasificacion().putIfAbsent(clase,table.getClasificacion().size()+1);
        int numFlor = table.getClasificacion().get(clase);
        fila.setNumberClass(numFlor);

        table.addFilas(fila);
    }

    @Override
    void closeSource(){
        if (bufferLectura != null) {
            try {
                bufferLectura.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // comprueba si hay más datos; en nuestro caso, si hay mas línea(s) en elfichero CSV
    @Override
    boolean hasMoreData(){
        return getCurrentLine() != null;
    }
    @Override
    String getNextData() throws IOException {
        return bufferLectura.readLine();
    }
}
