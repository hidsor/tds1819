package application.view;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.swing.ImageIcon;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import application.controller.AppVideo;
import application.model.Etiqueta;
import application.model.Usuario;
import application.model.Video;
import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import com.jfoenix.controls.JFXMasonryPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import tds.video.VideoWeb;

public class ViewController {
	///////////////
	/* ATRIBUTOS */
	///////////////
	private double oldHeight = 0;
	private double oldWidth = 0;
	private double xOffset = 0; 
	private double yOffset = 0;
	private AppVideo controller = AppVideo.getUnicaInstancia();
	private boolean isProfileOpen = false;
	private static VideoWeb videoWeb = new VideoWeb();
	
	/* STACKPANE EXTERIOR PARA JFXDIALOG */
    @FXML
    private StackPane rootStackPane;
	
    /* BORDERPANE EXTERIOR */
    @FXML
    private BorderPane rootBorderPane;
	
	/* BOTONES DE LA TOPBAR */
    @FXML
    private JFXButton minimizeButton, maximizeButton, closeButton;
    
    /* BOTONES DE LA SIDEBAR */
    @FXML
    private JFXButton mislistas, premium, explorar, recientes, login, nuevalista;
    
    /* STACKPANE PARA CONTENER TODAS LAS VENTANAS */
    @FXML
    private StackPane stackpane;  
    @FXML
    private AnchorPane testwindow; 
    
    /* VENTANA DE LOGIN */ 
    @FXML
    private GridPane loginView; // Contenedor padre de la ventana de login
    @FXML
    private Label loginLabelNick, loginLabelPassword, registerLabel;  
    @FXML
    private JFXButton loginButton;
    @FXML
    private JFXTextField loginNick;   
    @FXML
    private JFXPasswordField loginPassword;
  
    /* VENTANA DE REGISTRO */
    @FXML
    private GridPane registerView; // Contenedor padre de la ventana de registro
    @FXML
    private Label registerLabelNick, registerLabelPassword, registerLabelPassRepeat, registerLabelName, registerLabelDate;   
    @FXML
    private JFXTextField registerNick, registerName, registerSurname, registerEmail;      
    @FXML
    private JFXPasswordField registerPassword, registerPasswordRepeat;
    @FXML
    private JFXDatePicker registerDatePicker;
    @FXML
    private JFXButton registerRegister, registerCancel;
   
    /* VENTANA DE PERFIL */
    @FXML
    private GridPane profileView; // Contenedor de la vista de perfil
    @FXML
    private Label profileNick, profileTitle;
    @FXML
    private JFXTextField profileEmail;
    @FXML
    private JFXDatePicker profileDatePicker;   
    @FXML
    private JFXPasswordField profilePassword, profilePassRepeat;
    @FXML
    private JFXButton profileUpdate, profileLogout, profileLoad;
    
    /* VENTANA DE EXPLORAR */
    @FXML
    private BorderPane exploreView; // Contenedor de la vista de explorar
    @FXML
    private JFXMasonryPane exploreContent;
    @FXML
    private JFXTextField exploreTitle;
    @FXML
    private JFXButton exploreSearch, exploreClear;
    @FXML 
    private JFXListView<String> tagsView = new JFXListView<String>();	// Para visualizar todas las etiquetas disponibles
    @FXML
    private JFXListView<String> searchTagsView = new JFXListView<String>(); // Visualizar nuestras etiquetas de búsqueda
    private Set<Etiqueta> searchTags = new HashSet<Etiqueta>();
    
    ///////////////////////
    /* MANEJO DE EVENTOS */
    ///////////////////////
    @FXML
    public void openLoginView(ActionEvent event) {
    	// Abrimos una ventana u otra en función del estado
    	if (!isProfileOpen) {
        	// Ocultamos el elemento que hubiese en el frente
        	Node oldFront = stackpane.getChildren().get(stackpane.getChildren().size() - 1);
        	oldFront.setDisable(true);
        	oldFront.setVisible(false);
        	
    		// Limpiamos los elementos de la vista
    		loginNick.setText("");
    		loginPassword.setText("");
    		
    		// Traemos la ventana al frente y la hacemos visible
        	loginView.setDisable(false);
        	loginView.setVisible(true);
        	loginView.toFront();
        	hideLoginLabels();
        	fadeIn(loginView);
    	} else {
    		openProfileView();
    	}
    }

    @FXML
    public void openExplorarView(ActionEvent event) {	
    	loadTags(controller.getListaEtiquetas());
    	
    	// Ocultamos el elemento que hubiese en el frente
    	Node oldFront = stackpane.getChildren().get(stackpane.getChildren().size() - 1);
    	oldFront.setDisable(true);
    	oldFront.setVisible(false);
    		
    	// Traemos la ventana al frente y la hacemos visible
    	exploreView.setDisable(false);
    	exploreView.setVisible(true);
    	exploreView.toFront();
    	fadeIn(exploreView);
    }

    @FXML
    public void openMislistasView(ActionEvent event) {
    	//TODO
    }

    @FXML
    public void openRecientesView(ActionEvent event) {
    	//TODO
    }

    @FXML
    public void openNuevalistaView(ActionEvent event) {
    	//TODO
    }

    @FXML
    public void openPremiumView(ActionEvent event) {
    	//TODO
    }
    
    @FXML
    // Validación de la cuenta introducida en el login
    public void loginEnter(ActionEvent event) {
    	boolean valid = true;
    	// Comprobamos que ambos campos hayan sido introducidos
    	if (loginNick.getText().equals("")) {
    		loginLabelNick.setVisible(true);
    		valid = false;
    	}
    	if (loginPassword.getText().equals("")) {
			loginLabelPassword.setVisible(true);
			valid = false;
    	}
    	if (valid) {
    		// Hay datos introducidos, intentamos identificarnos.
    		if (controller.verificarUsuario(loginNick.getText(), loginPassword.getText())) {
    			// Login válido
    			openProfileView();
    			// Desbloqueamos la funcionalidad disponible para usuarios
    			explorar.setDisable(false);
    			mislistas.setDisable(false);
    			recientes.setDisable(false);
    			nuevalista.setDisable(false);
    		} else {
    			// Login inválido
    			showDialog("Login inválido", "El nombre y/o la contraseña son incorrectas");
    		}
    	}
    }
    	
    @FXML
    public void openRegisterView(MouseEvent event) {
    	// Ocultamos el elemento que hubiese en el frente
    	Node oldFront = stackpane.getChildren().get(stackpane.getChildren().size() - 1);
    	oldFront.setDisable(true);
    	oldFront.setVisible(false);
    	
    	// Limpiamos todos los elementos de la ventana
    	registerNick.setText("");
    	registerPassword.setText("");
    	registerPasswordRepeat.setText("");
    	registerName.setText("");
    	registerSurname.setText("");
    	registerDatePicker.setValue(null);
    	registerEmail.setText("");    	
    	
    	// Traemos la ventana al frente y la hacemos visible
    	registerView.setDisable(false);
    	registerView.setVisible(true);
    	registerView.toFront(); 	
    	
    	hideRegisterLabels();
    	fadeIn(registerView);
    }

    @FXML
    public void registerCancel(ActionEvent event) {
    	//TODO: HACE LO MISMO QUE openLoginView pero lo dejo separado de mientras
    	/*
    	// Ocultamos el elemento que hubiese en el frente
    	Node oldFront = stackpane.getChildren().get(stackpane.getChildren().size() - 1);
    	oldFront.setDisable(true);
    	oldFront.setVisible(false);
    	
    	// Traemos la ventana al frente y la hacemos visible
    	loginView.setDisable(false);
    	loginView.setVisible(true);
    	loginView.toFront();
    	fadeIn(loginView);
    	*/
    	openLoginView(event);
    }
    

    @FXML
    // Registrar un usuario
    public void registerUser(ActionEvent event) {
    	// Comprobamos que ninguno de los campos obligatorios esté vacío.
    	// Si alguno lo está, activamos su etiqueta de "*Campo obligatorio"
    	boolean valid = true;
    	if (registerNick.getText().equals("")) {
    		registerLabelNick.setVisible(true);
    		valid = false;
    	}
    	if (registerPassword.getText().equals("")) {
    		registerLabelPassword.setVisible(true);
    		valid = false;
    	}
    	if (registerPasswordRepeat.getText().equals("")) {
    		registerLabelPassRepeat.setVisible(true);
    		valid = false;
    	}
    	if (registerName.getText().equals("")) {
    		registerLabelName.setVisible(true);
    		valid = false;
    	}
    	if (registerDatePicker.getValue() == null) {
    		registerLabelDate.setVisible(true);
    		valid = false;
    	}
    	// Comprobamos que ambas contraseñas sean la misma
    	if (valid && ! registerPassword.getText().equals(registerPasswordRepeat.getText())) {
    		valid = false;	
    		showDialog("Registro inválido", "Las contraseñas introducidas no coinciden");
    		
    	}
    	// Si todo ha ido bien, intentamos registrar al usuario
    	if (valid) {
			if (controller.registrarUsuario(registerNick.getText(), registerPassword.getText(), registerName.getText(),
					registerSurname.getText(), registerDatePicker.getValue(), registerEmail.getText())) {
    			// Registro válido
    			openProfileView();
    			// Desbloqueamos la funcionalidad disponible para usuarios
    			explorar.setDisable(false);
    			mislistas.setDisable(false);
    			recientes.setDisable(false);
    			nuevalista.setDisable(false);    			
    		} else {
    			// Registro inválido (shouldn't happen)
    			showDialog("Registro inválido", "No se ha podido registrar al usuario");
    		}
    	}
    }

    public void openProfileView() {
    	// Actualizamos al usuario actual por si se han producido cambios
		Usuario usuarioActual = controller.getUsuarioActual();
		isProfileOpen = true;
		
		// Ocultamos el elemento que hubiese en el frente
		Node oldFront = stackpane.getChildren().get(stackpane.getChildren().size() - 1);
		oldFront.setDisable(true);
		oldFront.setVisible(false);

		// Hacemos la vista visible
		profileView.setVisible(true);
		profileView.setDisable(false);
		profileView.toFront();
		fadeIn(profileView);
		
		// Actualizamos elementos de la vista de perfil
		login.setText("Mi perfil");
		profileNick.setText(usuarioActual.getLogin());
		profileEmail.setPromptText(usuarioActual.getEmail());
		profileDatePicker.setPromptText(usuarioActual.getFechaNac().toString());
		profileTitle.setText("Bienvenido, " + usuarioActual.getNombre());
		
		// Limpiamos los elementos a introducir
		profileEmail.setText("");
		profileDatePicker.setValue(null);
	}
      
    @FXML
    // Salir de la cuenta actualmente utilizada
    public void profileLogout(ActionEvent event) {
    	controller.salirUsuario();
    	isProfileOpen = false;
    	login.setText("Log in");
    	
    	// Bloqueamos la funcionalidad disponible para usuarios
		explorar.setDisable(true);
		mislistas.setDisable(true);
		recientes.setDisable(true);
		nuevalista.setDisable(true);
		
    	openLoginView(event);
    }

    @FXML
    public void loadVideos(ActionEvent event) {
    	//TODO: Componente cargador de vídeos
    }
     
    @FXML
	// Actualizar todos los campos introducidos en la ventana de perfil.
    public void profileUpdate(ActionEvent event) {
    	// La comprobación de si un campo contiene información o no se delega al controlador.
    	// Asimismo, si la contraseña nueva no se escribe dos veces correctamente, no se realizan los cambios
		if (profilePassword.getText().equals(profilePassRepeat.getText())) {
			controller.modificarUsuarioActual(profileEmail.getText(), profilePassword.getText(),
					profileDatePicker.getValue());
			// Actualizamos los cambios volviendo a abrir la ventana de perfil
    		openProfileView();
    	} else {
    		showDialog("Cambios inválidos", "Las contraseñas introducidas no coinciden");
    	}	
    }

    @FXML
    // Busqueda de vídeos
    public void exploreSearch(ActionEvent event) {
    	Set<Video> videos = controller.buscarVideos(exploreTitle.getText());
    	boolean ifExists = false;
    	for (Video video : videos) {
    		// Comprobamos que el vídeo que queremos mostrar no esté ya en la vista
    		for (Node child : exploreContent.getChildren()) {
    			Label l = (Label) child;
    			if (l.getText().equals(video.getTitulo())) {
    				ifExists = true;
    				break;
    			}
    		}
    		if (ifExists) continue;
    		
    		// Si no está, lo añadimos a la búsqueda actual
    		Label element = new Label();
    		
    		// Propiedades de la label que contiene la miniatura y el título del vídeo
    		element.setMaxWidth(200.0);
    		element.setMaxHeight(200.0);
    		element.setPrefWidth(200.0);
    		element.setPrefHeight(150.0);
    		element.setAlignment(Pos.CENTER);
    		element.setOnMouseEntered(e -> element.setStyle("-fx-background-color: #cfcfcf"));
    		element.setOnMouseExited(e -> element.setStyle(""));
    		element.setOnMouseClicked(new EventHandler<MouseEvent>() {
    		    @Override
    		    public void handle(MouseEvent mouseEvent) {
    		        if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
    		            if(mouseEvent.getClickCount() == 2){
    		            	// Si se hace doble click sobre la miniatura del vídeo, abrimos una ventana para reproducirlo
    		            	showVideoDialog(video);
    		            }
    		        }
    		    }
    		});
    		element.setContentDisplay(ContentDisplay.TOP);
    		element.setText(video.getTitulo());
    		
    		// Extraemos la miniatura del componente proporcionado
    		// Hacemos una conversión de ImageIcon (Swing) a ImageView (JavaFX) con SwingFXUtils
    		ImageIcon icon = videoWeb.getThumb(video.getURL());
    		BufferedImage bi = new BufferedImage(
    			    icon.getIconWidth(),
    			    icon.getIconHeight(),
    			    BufferedImage.TYPE_INT_RGB);
    		Graphics g = bi.createGraphics();
    		icon.paintIcon(null, g, 0,0);
    		g.dispose();
    		Image thumbnail = SwingFXUtils.toFXImage(bi, null);
    		element.setGraphic(new ImageView(thumbnail));
    		
    		exploreContent.getChildren().add(element);
    	}
    }    
    
    @FXML
    // Limpiar la búsqueda actual de vídeos
    public void exploreClear(ActionEvent event) {
    	exploreContent.getChildren().clear();
    }
    

    @FXML
    public void minimizeWindow(ActionEvent event) {
    	((Stage)((JFXButton)event.getSource()).getScene().getWindow()).setIconified(true);  	
    }

    @FXML
    public void maximizeWindow(ActionEvent event) {
		Stage stg = ((Stage) ((JFXButton) event.getSource()).getScene().getWindow());
		// Si la ventana estaba maximizada, restauramos su tamaño anterior
		if (stg.isMaximized()) {
			stg.setWidth(oldWidth);
			stg.setHeight(oldHeight);
			stg.setMaximized(false);
		} else {
			// Sino, la maximizamos y almacenamos el tamaño anterior
			oldWidth = stg.getWidth();
			oldHeight = stg.getHeight();
			stg.setMaximized(true);
		}
	}

    @FXML
    // Cerrar la ventana y la aplicación
    public void closeWindow(ActionEvent event) {
    	((Stage)((JFXButton)event.getSource()).getScene().getWindow()).close();
    	System.exit(0);
    }
    
    @FXML
    // TODO: NOT USED
    public void saveWindowPosition(MouseEvent event) {
        xOffset = event.getSceneX();
        yOffset = event.getSceneY();
    }
    
    @FXML
    //TODO: NOT USED
    public void dragWindow(MouseEvent event) {
    	Region rg = (Region) event.getSource();
    	rg.getScene();
    	Stage stg = (Stage) rg.getScene().getWindow();
        stg.setX(event.getScreenX() - xOffset);
        stg.setY(event.getScreenY() - yOffset);
    }
    
    ////////////////////////////  
    /* FUNCIONALIDAD AUXILIAR */
    ////////////////////////////  
    
    // Ocultar todas las labels de "*Este campo es obligatorio" de la vista del login
    public void hideLoginLabels() {
    	loginLabelNick.setVisible(false);
    	loginLabelPassword.setVisible(false);
    }
    
    // Ocultar todas las labels de "*Este campo es obligatorio" de la vista de registro
    public void hideRegisterLabels() {
    	registerLabelNick.setVisible(false);
    	registerLabelPassword.setVisible(false);
    	registerLabelPassRepeat.setVisible(false);
    	registerLabelName.setVisible(false);
    	registerLabelDate.setVisible(false);  	
    }
    
	// Generar un JFXDialog con un vídeo pasado de parámetro
	public void showVideoDialog(Video video) {
		JFXDialogLayout dialogContent = new JFXDialogLayout();

		dialogContent.setHeading(new Text(video.getTitulo()));

		SwingNode videoComponent = new SwingNode();
		videoComponent.setContent(videoWeb);

		// Contenedor de etiquetas
		HBox tags = new HBox();
		tags.setSpacing(5.0);

		// Añadimos las etiquetas al contenedor
		for (Etiqueta tag : video.getEtiquetas()) {
			Label l = new Label(tag.getNombre());
			l.setStyle(
					"-fx-font: 14 system; -fx-background-color: #efefef; -fx-padding : 2 5 2 5; -fx-font-weight: bold;");
			tags.getChildren().add(l);
		}

		// Contenedor de añadir nueva etiqueta
		HBox addTags = new HBox();
		addTags.setSpacing(10.0);

		Label addTagsText = new Label("Añadir nueva etiqueta:");
		addTagsText.setStyle("-fx-text-fill: #000000; -fx-font: 12 system; -fx-font-weight: bold;");

		JFXTextField addTagsTextField = new JFXTextField();
		addTagsTextField.setStyle("-jfx-focus-color: #f51827; -jfx-unfocus-color: #4d4d4d;");

		addTags.getChildren().addAll(addTagsText, addTagsTextField);

		// Contenedor del reproductor de vídeos
		VBox body = new VBox();
		body.setSpacing(5.0);
		body.getChildren().addAll(videoComponent, tags, addTags,
				new Text("Reproducciones: " + video.getNumReproducciones()));

		dialogContent.setBody(body);

		// Botones de la ventana emergente
		JFXButton close = new JFXButton("Cerrar");
		close.setStyle("-fx-background-color: #f6444f; -fx-text-fill: #FFFFFF; -fx-font: 14 system;");
		close.setPrefSize(100, 25);
		close.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

		JFXButton add = new JFXButton("Añadir");
		add.setStyle("-fx-background-color: #f6444f; -fx-text-fill: #FFFFFF; -fx-font: 14 system;");
		add.setPrefSize(100, 25);
		add.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

		dialogContent.setActions(add, close);

		JFXDialog dialog = new JFXDialog(rootStackPane, dialogContent, JFXDialog.DialogTransition.BOTTOM);

		close.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent __) {
				dialog.close();
				videoWeb.cancel();
			}
		});
		// controller.reproducir(video); <---- Descomentar cuando tengamos el cargador
		// de vídeos
		// Porque estoy injertando vídeos de momento y si los modifico puede que se líe
		videoWeb.playVideo(video.getURL());
		dialog.show();
	}

	// Generar un JFXDialog textual sobre la aplicación
	public void showDialog(String title, String content) {
		JFXDialogLayout dialogContent = new JFXDialogLayout();

		dialogContent.setHeading(new Text(title));
		dialogContent.setBody(new Text(content));
		dialogContent.setStyle("-fx-font: 14 system;");

		// Botones de la ventana emergente
		JFXButton close = new JFXButton("Cerrar");
		close.setStyle("-fx-background-color: #f6444f; -fx-text-fill: #FFFFFF; -fx-font: 14 system;");
		close.setPrefSize(100, 25);
		close.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

		dialogContent.setActions(close);

		JFXDialog dialog = new JFXDialog(rootStackPane, dialogContent, JFXDialog.DialogTransition.BOTTOM);

		close.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent __) {
				dialog.close();
			}
		});
		dialog.show();
	}

	// Método para hacer una transición de un nodo pasado de parámetro
	// La duración no está parametrizada pero podría parametrizarse también
	public void fadeIn(Node node) {
		FadeTransition ft = new FadeTransition(Duration.millis(200), node);
		ft.setFromValue(0.0);
		ft.setToValue(1.0);
		ft.play();
	}
	
	
	
	// Cargamos la lista con todas las etiquetas
	public void loadTags(Set<Etiqueta> etiquetasGuardadas) {
		tagsView.setItems(etiquetasGuardadas.stream()
				.map(Etiqueta::getNombre)
				.collect(Collectors.toCollection(FXCollections::observableArrayList)));
	}
	
	
	@FXML
	public boolean addSearchTag(MouseEvent event) {
		String tagName = tagsView.getSelectionModel().getSelectedItem();
		if (controller.addEtiquetaBusqueda(tagName)) {
			searchTagsView.getItems().add(tagName);
			return true;
		}
		return false;
	}
	
	@FXML
	public boolean removeSearchTag(MouseEvent event) {
		//TODO
		return true;
	}
	
	

}