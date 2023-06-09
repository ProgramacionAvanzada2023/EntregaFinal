package Modelo;

import ClasesDistancias.DistanceEnum;
import ClasesPrincipales.AlgorithmEnum;

import java.io.IOException;
import java.util.List;

public interface Modelo<E> {

    void elegirAlgoritmoAndDistancia(AlgorithmEnum algoritmoElegido, DistanceEnum tipoDistancia) throws Exception;
    List<String> listaCancionesRecomendadas(String itemSelecionado, int numeroRecomendacion);
    List<String> getListaCanciones() throws IOException;
}
