package ClasesPrincipales;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CSVUnlabeledFileReader extends ReaderTemplate {
    protected BufferedReader bufferLectura;
    public Table tabla;
    public CSVUnlabeledFileReader(){
        super();
        this.tabla = new Table();
    }
    @Override
    void openSource(String source) {
        try {
            bufferLectura = (new BufferedReader(new FileReader(source)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    void processHeaders(String headers) {
        List<String> cabecera = Arrays.stream(headers.split(SEPARADOR)).collect(Collectors.toList());
        tabla.setHeaders(cabecera);
    }
    @Override
    void processData(String data) {
        if(data.isEmpty()) return;
        List<Double> listaCampos = Arrays.stream(data.split(SEPARADOR))
                .map(Double::parseDouble)
                .collect(Collectors.toList());
        Row fila = new Row(listaCampos);
        tabla.addFilas(fila);
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

    // comprueba si hay más datos; en nuestro caso, si hay más línea(s) en elfichero CSV
    @Override
    boolean hasMoreData(){
        return getCurrentLine() != null;
    }
    @Override
    String getNextData() throws IOException {
        return bufferLectura.readLine();
    }
}