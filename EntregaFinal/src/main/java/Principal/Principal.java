package Principal;

import Controlador.ImplementacionControlador;
import Modelo.ImplementacionModelo;
import Vista.ImplementacionVista;
import javafx.application.Application;
import javafx.stage.Stage;

public class Principal extends Application {
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception{
		ImplementacionModelo modelo = new ImplementacionModelo();
		ImplementacionControlador controlador = new ImplementacionControlador();
		ImplementacionVista vista = new ImplementacionVista(stage);
		modelo.setVista(vista);
		vista.setControlador(controlador);
		vista.setModelo(modelo);
		controlador.setVista(vista);
		controlador.setModelo(modelo);
		vista.songRecommend();
	}
}
