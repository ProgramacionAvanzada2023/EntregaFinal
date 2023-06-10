package Controlador;

import ClasesDistancias.DistanceEnum;
import ClasesPrincipales.AlgorithmEnum;

public interface Controlador {
    void crearAlgoritmo(AlgorithmEnum algoritmo, DistanceEnum tipoDistancia) throws Exception;

}
