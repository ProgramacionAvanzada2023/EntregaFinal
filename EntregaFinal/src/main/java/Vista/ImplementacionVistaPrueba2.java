package Vista;/*package Vista;


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
		Label recommendation = new Label("Recommendation Type");
		RadioButton botonKmeans = new RadioButton("Algoritmo basado en Kmeans"); // Recommend based on song features
		RadioButton botonKnn = new RadioButton("Algoritmo basado en Knn"); //Recommend based on gussed genre
		ToggleGroup toggleRecommendation = new ToggleGroup();
		botonKnn.setToggleGroup(toggleRecommendation);
		botonKmeans.setToggleGroup(toggleRecommendation);
		VBox vboxAlgoritmo = new VBox(botonKnn, botonKmeans);
		vboxAlgoritmo.setSpacing(10);


		Label distancesTypes = new Label("Distance Type");
		RadioButton distanceEuclidean = new RadioButton("Euclidean");
		RadioButton distanceManhattan = new RadioButton("Manhattan");
		ToggleGroup toggleDistancias = new ToggleGroup();
		distanceEuclidean.setToggleGroup(toggleDistancias);
		distanceManhattan.setToggleGroup(toggleDistancias);
		HBox hboxDistances = new HBox(distanceEuclidean, distanceManhattan);
		hboxDistances.setSpacing(10);


		Label songTitles = new Label("Song Titles");
		ListView listaSongs = new ListView<>(FXCollections.observableArrayList(listaCanciones));
		listaSongs.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		Tooltip tooltip = new Tooltip("Doble click for recommendations based on this song" );
		listaSongs.setTooltip(tooltip);

		botonKnn.setOnAction( e -> { setTipoAlgoritmoSeleccionado(AlgorithmEnum.Knn);});
		botonKmeans.setOnAction( e -> { setTipoAlgoritmoSeleccionado(AlgorithmEnum.KMeans);});

		distanceEuclidean.setOnAction( e -> {
			if(getTipoAlgoritmoSeleccionado() != null) {
				try {
					controlador.elegirAlgoritmoAndDistancia(getTipoAlgoritmoSeleccionado(), DistanceEnum.EUCLIDEAN);
					setTipoDistanciaSeleccionado(DistanceEnum.EUCLIDEAN);
					listaSongs.setItems(FXCollections.observableArrayList(listaCanciones));
				} catch (Exception ex) {
					throw new RuntimeException(ex);
				}
			}else {
				System.err.println("Primero debe seleccionar el tipo de algoritmo");
			}
		});
		distanceManhattan.setOnAction( e -> {
			if(getTipoAlgoritmoSeleccionado() != null) {
				try {
					controlador.elegirAlgoritmoAndDistancia(getTipoAlgoritmoSeleccionado(), DistanceEnum.MANHATTAN);
					setTipoDistanciaSeleccionado(DistanceEnum.MANHATTAN);
					listaSongs.setItems(FXCollections.observableArrayList(listaCanciones));
				} catch (Exception ex) {
					throw new RuntimeException(ex);
				}
			}else {
				System.err.println("Primero debe seleccionar el tipo de algoritmo");
			}
		});

		Button botonRecommend = new Button("Recommend...");

		botonRecommend.setDisable(true);
		listaSongs.setOnMouseClicked(event -> {
			boolean restoCamposCompletos = toggleDistancias.getSelectedToggle() != null && toggleRecommendation.getSelectedToggle() != null;
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
			if(event.getButton() == MouseButton.PRIMARY && restoCamposCompletos && event.getClickCount() == 2){
				listaItemSelecionados = new ArrayList<>();
				listaItemSelecionados.add((String) listaSongs.getSelectionModel().getSelectedItem());
				recommendTitles();
			}
		});

		botonRecommend.setOnAction(actionEvent -> {
			if(getTipoDistanciaSeleccionado() != null && getTipoAlgoritmoSeleccionado() != null && !listaSongs.getSelectionModel().isEmpty()){
				listaItemSelecionados = listaSongs.getSelectionModel().getSelectedItems();
				recommendTitles();
			}
		});

		VBox vBox = new VBox(10, recommendation, vboxAlgoritmo, distancesTypes, hboxDistances, songTitles, listaSongs, botonRecommend);

		Scene scene = new Scene(vBox, 400, 600);
		stage.setScene(scene);
		stage.setTitle("Song Recommender");
		stage.show();
	}

	@Override
	public void recommendTitles(){
		List<String> listaCancionesParaRecomendar = new ArrayList<>();
		Label labelNumber = new Label("Number of recommendations:");
		Spinner<Integer> number = new Spinner<>();
		number.setEditable(true);
		number.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0));
		HBox boxCantidadRecomendaciones = new HBox(labelNumber,number);

		Label textoLabel = new Label(reportRecommendation(listaItemSelecionados));
		ListView listaOpciones = new ListView<>(FXCollections.observableArrayList(listaCancionesParaRecomendar));
		listaOpciones.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		VBox boxLista = new VBox(textoLabel,listaOpciones);

		Alert alerta = new Alert(Alert.AlertType.WARNING);
		alerta.setTitle("No recomendaciones disponibles");
		alerta.setContentText("No hay recomendaciones disponibles para el titulo " + listaItemSelecionados.toString());

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

		Button botonCerrar = new Button("Close");
		botonCerrar.setDisable(true);
		listaOpciones.setOnMouseClicked(event -> {
			System.err.println(listaOpciones.getSelectionModel().getSelectedItem());
			if(listaOpciones.getSelectionModel()!= null && listaOpciones.getSelectionModel().getSelectedItem() != null ){
				botonCerrar.setDisable(false);
			}
		});

		Button botonAtras = new Button("Atras");
		botonAtras.setOnAction(actionEvent -> {
			try {
				songRecommend();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		});
		HBox boxBotones = new HBox(botonAtras,botonCerrar);

		VBox vBox = new VBox(boxCantidadRecomendaciones, boxLista ,boxBotones);

		Scene scene = new Scene(vBox);
		stage.setScene(scene);
		stage.setTitle("Recommended titles");
		stage.show();

		botonCerrar.setOnAction(actionEvent -> {
			stage.close();
		});
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

		for (String canciones : listaItemSelecionados) {
			listaActualizada.addAll(modelo.listaCancionesRecomendadas(canciones, newValue));
		}

		if (listaActualizada.isEmpty()) {
			alerta.show();
			number.getEditor().setText(String.valueOf(valueFactory.getValue()));
		}
		listaOpciones.setItems(FXCollections.observableArrayList(listaActualizada));
	}

}*/
