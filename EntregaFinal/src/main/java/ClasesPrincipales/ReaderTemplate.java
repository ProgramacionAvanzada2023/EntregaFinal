package ClasesPrincipales;

import java.io.IOException;

public abstract class ReaderTemplate {
    private String currentLine;
    public static final String SEPARADOR = ",";

    public ReaderTemplate(){}

    public String getCurrentLine() {
        return currentLine;
    }

    public void setCurrentLine(String currentLine) {
        this.currentLine = currentLine;
    }

    abstract void openSource(String source);
    abstract void processHeaders(String headers);
    abstract void processData(String data);
    abstract void closeSource();
    abstract  boolean hasMoreData();
    abstract String getNextData() throws IOException;

    public final Table readTableFromSource(String source) throws IOException {
        Table tabla = new Table();
        openSource(source);
        currentLine = getNextData();
        //Primero leemos la cabecera si el fichero no está vacío
        if (hasMoreData()) {
            processHeaders(currentLine);
            currentLine = getNextData();
        }
        while (hasMoreData()) {
            processData(currentLine);
            currentLine = getNextData();
        }
        closeSource();
        return tabla;
    }

}
