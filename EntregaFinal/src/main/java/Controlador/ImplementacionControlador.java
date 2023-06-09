package Controlador;

import ClasesDistancias.DistanceEnum;
import ClasesPrincipales.AlgorithmEnum;
import Modelo.ImplementacionModelo;
import Vista.ImplementacionVista;

public class ImplementacionControlador implements Controlador{
	private ImplementacionVista vista;
	private ImplementacionModelo modelo;

	public ImplementacionVista getVista() {
		return vista;
	}

	public void setVista(ImplementacionVista vista) {
		this.vista = vista;
	}

	public ImplementacionModelo getModelo() {
		return modelo;
	}

	public void setModelo(ImplementacionModelo modelo) {
		this.modelo = modelo;
	}

	@Override
	public void elegirAlgoritmoAndDistancia(AlgorithmEnum algoritmo, DistanceEnum tipoDistancia) throws Exception {
		modelo.elegirAlgoritmoAndDistancia(algoritmo,tipoDistancia);
	}

}
