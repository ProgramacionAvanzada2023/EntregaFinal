package Controlador;

import ClasesDistancias.DistanceEnum;
import ClasesPrincipales.AlgorithmEnum;

import java.util.List;

public interface Controlador {
    void elegirAlgoritmoAndDistancia(AlgorithmEnum algoritmo, DistanceEnum tipoDistancia) throws Exception;

}
