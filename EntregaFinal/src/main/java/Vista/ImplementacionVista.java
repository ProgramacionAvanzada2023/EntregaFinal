package Vista;

import ClasesDistancias.DistanceEnum;
import ClasesPrincipales.AlgorithmEnum;
import Controlador.ImplementacionControlador;
import Modelo.ImplementacionModelo;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ImplementacionVista implements Vista {
	private ImplementacionControlador controlador;
	private ImplementacionModelo modelo;
	private final Stage stage;
	private AlgorithmEnum tipoAlgoritmoSeleccionado;
	private DistanceEnum tipoDistanciaSeleccionado;
	private List<String> listaItemSelecionados = new ArrayList<>();
	private List<String> listaCanciones;

    public ImplementacionVista(final Stage stage) throws Exception {
		this.stage = stage;
    }
	public ImplementacionControlador getControlador() {
		return controlador;
	}
	public void setControlador(ImplementacionControlador controlador) {
		this.controlador = controlador;
	}

	public ImplementacionModelo getModelo() {
		return modelo;
	}
	public void setModelo(ImplementacionModelo modelo) {
		this.modelo = modelo;
	}

	public AlgorithmEnum getTipoAlgoritmoSeleccionado() {
		return tipoAlgoritmoSeleccionado;
	}
	public void setTipoAlgoritmoSeleccionado(AlgorithmEnum tipoAlgoritmoSeleccionado) {
		this.tipoAlgoritmoSeleccionado = tipoAlgoritmoSeleccionado;
	}
	public DistanceEnum getTipoDistanciaSeleccionado() {return tipoDistanciaSeleccionado; }
	public void setTipoDistanciaSeleccionado(DistanceEnum tipoDistanciaSeleccionado) {
		this.tipoDistanciaSeleccionado = tipoDistanciaSeleccionado;
	}

	public List<String> getListaItemSelecionados() {
		return listaItemSelecionados;
	}

	public void setListaItemSelecionados(List<String> listaItemSelecionados) {
		this.listaItemSelecionados = listaItemSelecionados;
	}

	@Override
	public void songRecommend() throws Exception {
		listaCanciones = new ArrayList<>();
		listaCanciones = modelo.getListaCanciones();
		VBox vBox = createVBox();
		Scene scene = new Scene(vBox, 400, 600);
		stage.setScene(scene);
		stage.setTitle("Song Recommender");
		stage.show();
	}

	private VBox createVBox() {
		Label recommendation = new Label("Recommendation Type");
		RadioButton botonKmeans = new RadioButton("Algoritmo basado en Kmeans"); // Recommend based on song features
		RadioButton botonKnn = new RadioButton("Algoritmo basado en Knn"); //Recommend based on gussed genre
		VBox vboxAlgoritmo = createAlgoritmoVBox(botonKmeans, botonKnn);

		Label distancesTypes = new Label("Distance Type");
		RadioButton distanceEuclidean = new RadioButton("Euclidean");
		RadioButton distanceManhattan = new RadioButton("Manhattan");
		HBox hboxDistances = createDistancesHBox(distanceEuclidean, distanceManhattan);

		Label songTitles = new Label("Song Titles");
		ListView<String> listaSongs = createListView();

		Button botonRecommend = createRecommendButton(listaSongs);

		setAlgoritmoButton(botonKnn, botonKmeans);
		setDistancesButton(distanceEuclidean, distanceManhattan, listaSongs);
		setListViewEventHandlers(listaSongs, botonRecommend);
		setRecommendButtonEventHandler(botonRecommend, listaSongs);

		VBox vBox = new VBox(10, recommendation, vboxAlgoritmo, distancesTypes, hboxDistances, songTitles, listaSongs, botonRecommend);
		return vBox;
	}

	private VBox createAlgoritmoVBox(RadioButton botonKmeans, RadioButton botonKnn) {
		ToggleGroup toggleRecommendation = new ToggleGroup();
		botonKnn.setToggleGroup(toggleRecommendation);
		botonKmeans.setToggleGroup(toggleRecommendation);
		VBox vboxAlgoritmo = new VBox(botonKnn, botonKmeans);
		vboxAlgoritmo.setSpacing(10);
		return vboxAlgoritmo;
	}

	private HBox createDistancesHBox(RadioButton distanceEuclidean, RadioButton distanceManhattan) {
		ToggleGroup toggleDistancias = new ToggleGroup();
		distanceEuclidean.setToggleGroup(toggleDistancias);
		distanceManhattan.setToggleGroup(toggleDistancias);
		HBox hboxDistances = new HBox(distanceEuclidean, distanceManhattan);
		hboxDistances.setSpacing(10);
		return hboxDistances;
	}

	private ListView<String> createListView() {
		ListView<String> listaSongs = new ListView<>(FXCollections.observableArrayList(listaCanciones));
		listaSongs.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		Tooltip tooltip = new Tooltip("Doble click for recommendations based on this song");
		listaSongs.setTooltip(tooltip);
		return listaSongs;
	}

	private Button createRecommendButton(ListView<String> listaSongs) {
		Button botonRecommend = new Button("Recommend...");
		botonRecommend.setDisable(true);
		return botonRecommend;
	}

	private void setAlgoritmoButton(RadioButton botonKnn, RadioButton botonKmeans) {
		botonKnn.setOnAction(e -> { setTipoAlgoritmoSeleccionado(AlgorithmEnum.Knn); });
		botonKmeans.setOnAction(e -> { setTipoAlgoritmoSeleccionado(AlgorithmEnum.KMeans); });
	}

	private void setDistancesButton(RadioButton distanceEuclidean, RadioButton distanceManhattan, ListView<String> listaSongs) {
		distanceEuclidean.setOnAction(e -> {
			updateAlgoritmoAndDistancia(DistanceEnum.EUCLIDEAN,listaSongs);
		});

		distanceManhattan.setOnAction(e -> {
			updateAlgoritmoAndDistancia(DistanceEnum.MANHATTAN, listaSongs);
		});
	}
	private void updateAlgoritmoAndDistancia(DistanceEnum tipoDistancia, ListView<String> listaSongs){
		if (getTipoAlgoritmoSeleccionado() != null) {
			try {
				controlador.elegirAlgoritmoAndDistancia(getTipoAlgoritmoSeleccionado(), tipoDistancia);
				setTipoDistanciaSeleccionado(tipoDistancia);
				listaSongs.setItems(FXCollections.observableArrayList(listaCanciones));
			} catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		} else {
			System.err.println("Primero debe seleccionar el tipo de algoritmo");
		}
	}

	private void setListViewEventHandlers(ListView<String> listaSongs, Button botonRecommend) {
		listaSongs.setOnMouseClicked(event -> {
			boolean restoCamposCompletos = getTipoDistanciaSeleccionado() != null && getTipoAlgoritmoSeleccionado() != null;
			if(event.getButton() == MouseButton.PRIMARY && restoCamposCompletos){
				botonRecommend.setDisable(false);
				if(listaSongs.getSelectionModel().getSelectedItems().isEmpty()){
					botonRecommend.setDisable(true);
				}
				String cancion = listaSongs.getSelectionModel().getSelectedItems().size() > 0 ? (String) listaSongs.getSelectionModel().getSelectedItems().get(0) : "";
				cancion += listaSongs.getSelectionModel().getSelectedItems().size() > 1 ?  "..." : "";
				String texto = "Recommend " + cancion;
				botonRecommend.setText(texto);
			}
			if(event.getButton() == MouseButton.PRIMARY && restoCamposCompletos && event.getClickCount() == 2 && restoCamposCompletos){
				listaItemSelecionados = new ArrayList<>();
				listaItemSelecionados.add((String) listaSongs.getSelectionModel().getSelectedItem());
				recommendTitles();
			}
		});
	}

	private void setRecommendButtonEventHandler(Button botonRecommend, ListView<String> listaSongs) {
		botonRecommend.setOnAction(e -> {
			if(getTipoDistanciaSeleccionado() != null && getTipoAlgoritmoSeleccionado() != null && !listaSongs.getSelectionModel().isEmpty()){
				listaItemSelecionados = listaSongs.getSelectionModel().getSelectedItems();
				recommendTitles();
			}
		});
	}

	private HBox createNumberRecommendationsHBox(Label labelNumber, Spinner<Integer> number) {
		number.setEditable(true);
		number.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0));
		HBox boxCantidadRecomendaciones = new HBox(labelNumber,number);
		return boxCantidadRecomendaciones;
	}

	private VBox createListOpciones(Label textoLabel, ListView listaOpciones, List<String> listaCancionesParaRecomendar) {
		listaOpciones.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		VBox boxLista = new VBox(textoLabel,listaOpciones);
		return boxLista;
	}

	private void setCantidadRecomendaciones(Spinner<Integer> number, Alert alerta, ListView listaOpciones){
		SpinnerValueFactory.IntegerSpinnerValueFactory valueFactory = (SpinnerValueFactory.IntegerSpinnerValueFactory) number.getValueFactory();
		valueFactory.setAmountToStepBy(1);
		number.getEditor().setOnAction(event -> {
			int newValue = Integer.parseInt(number.getEditor().getText());
			updateRecommendations(newValue, alerta, number, valueFactory, listaOpciones);
		});

		number.setOnMouseClicked(event -> {
			if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1) {
				int newValue = valueFactory.getValue();
				updateRecommendations(newValue, alerta, number, valueFactory, listaOpciones);
			}
		});
	}

	private void setListaOpciones(ListView listaOpciones, Button botonCerrar){
		listaOpciones.setOnMouseClicked(event -> {
			if(listaOpciones.getSelectionModel()!= null && listaOpciones.getSelectionModel().getSelectedItem() != null ){
				botonCerrar.setDisable(false);
			}
		});
	}

	private void setBotonAtrasAction(Button botonAtras){
		botonAtras.setOnAction(actionEvent -> {
			try {
				setTipoAlgoritmoSeleccionado(null);
				setTipoDistanciaSeleccionado(null);
				songRecommend();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		});
	}

	private void setBotonCerrarAction(Button botonCerrar, Stage stage){
		botonCerrar.setOnAction(actionEvent -> {
			stage.close();
		});
	}

	@Override
	public void recommendTitles(){
		List<String> listaCancionesParaRecomendar = new ArrayList<>();
		Label labelNumber = new Label("Number of recommendations:");
		Spinner<Integer> number = new Spinner<>();
		HBox boxCantidadRecomendaciones = createNumberRecommendationsHBox(labelNumber, number);

		Label textoLabel = new Label(reportRecommendation(listaItemSelecionados));
		ListView listaOpciones = new ListView<>(FXCollections.observableArrayList(listaCancionesParaRecomendar));
		VBox boxLista = createListOpciones(textoLabel, listaOpciones, listaCancionesParaRecomendar);

		Alert alerta = new Alert(Alert.AlertType.WARNING);
		alerta.setTitle("No recomendaciones disponibles");
		alerta.setContentText("No hay recomendaciones disponibles para el titulo " + listaItemSelecionados.toString());

		Button botonCerrar = new Button("Close");
		Button botonAtras = new Button("Atras");
		HBox boxBotones = new HBox(botonAtras,botonCerrar);

		setCantidadRecomendaciones(number, alerta, listaOpciones);
		setListaOpciones(listaOpciones, botonCerrar);
		setBotonAtrasAction(botonAtras);
		setBotonCerrarAction(botonCerrar, stage);

		VBox vBox = new VBox(boxCantidadRecomendaciones, boxLista ,boxBotones);

		Scene scene = new Scene(vBox);
		stage.setScene(scene);
		stage.setTitle("Recommended titles");
		stage.show();

	}

	@Override
	public void getListaCanciones() throws IOException {
		this.listaCanciones = modelo.getListaCanciones();
	}

	private String reportRecommendation(List<String> recommended_items) {
		String label = "If you liked \"";
		for (String name : recommended_items)
		{
			label += "\t * "+name;
		}
		label += "\" then you might like:";
		return label;
	}

	private void updateRecommendations(int newValue, Alert alerta, Spinner<Integer> number, SpinnerValueFactory.IntegerSpinnerValueFactory valueFactory, ListView listaOpciones) {
		List<String> listaActualizada = new ArrayList<>();

		for (String cancion : listaItemSelecionados) {
			listaActualizada.addAll(modelo.listaCancionesRecomendadas(cancion, newValue));
		}

		if (listaActualizada.isEmpty()) {
			alerta.show();
			number.getEditor().setText(String.valueOf(valueFactory.getValue()));
		}
		listaOpciones.setItems(FXCollections.observableArrayList(listaActualizada));
	}

}
