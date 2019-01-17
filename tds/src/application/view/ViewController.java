package application.view;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import javax.swing.ImageIcon;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import application.controller.AppVideo;
import application.model.Etiqueta;
import application.model.Filtro;
import application.model.ListaVideos;
import application.model.Usuario;
import application.model.Video;
import javafx.animation.FadeTransition;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Node;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;

import com.jfoenix.controls.JFXMasonryPane;
import com.jfoenix.controls.JFXNodesList;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import tds.video.VideoWeb;

public class ViewController implements Initializable {
	
	////////////////////////////
	/* ATRIBUTOS Y CONSTANTES */
	////////////////////////////

	private AppVideo controller;
	private boolean isProfileOpen;
	private static VideoWeb videoWeb;
	private boolean areOptionsOpened;
	private boolean showDeleteNotification;
	private boolean showEditNotification;
	private boolean editPlayListMode;
    private Timer globalTimer;
    
    private final static int DIALOG_BUTTON_WIDTH = 100;
	private final static int DIALOG_BUTTON_HEIGHT = 25;
	private final static String DIALOG_LABEL_STYLE = "-fx-text-fill: #000000;"
														+ " -fx-font: 14 system;";
	/* STACKPANE GLOBAL PARA JFXDIALOGS */
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
    private JFXButton mislistas, premium, explorar, recientes, login;
    
    /* STACKPANE PARA CONTENER TODAS LAS VENTANAS */
    @FXML
    private StackPane stackpane;  
    
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
    private ScrollPane exploreScroll;
    @FXML
    private JFXMasonryPane exploreContent; // Contenedor de los vídeos buscados
    @FXML
    private TextField exploreTitle;
    @FXML
    private JFXButton exploreSearch, exploreClear;
    @FXML 
    private JFXListView<JFXCheckBox> exploreTags;	// Para visualizar todas las etiquetas disponibles
    @FXML
    private ComboBox<String> exploreFilters;
    @FXML
    private Label exploreFilterTag;
 
    /* VENTANA DE MIS LISTAS */
    @FXML
    private BorderPane myListsView; // Contenedor de la vista de listas
    @FXML
    private HBox myListsMainSideBar, myListsSecondarySideBar;
    @FXML
    private TextField myListsTitle, myListsNewListTitle;
    @FXML
    private ScrollPane myListsSearchView;
    @FXML
    private JFXMasonryPane myListsContent;
    @FXML
    private ComboBox<String> myListsComboBox;
    @FXML
    private JFXNodesList myListsOptions;
    @FXML
    private JFXButton myListsSearch, myListsClear, myListExpand, myListsEdit, myListsPlay, myListsDelete, myListsNew, myListsAccept, myListsCreatePDF;
    @FXML
    private JFXListView<Label> myListsList;
    
    /* VENTANA DE RECIENTES */
    @FXML
    private GridPane recentView; // Contenedor de la vista de recientes
    @FXML
    private JFXListView<Label> recentVideosList, recentTopTenList;
    @FXML
    private JFXButton recentPlayRecentVideos, recentPlayTopTen;
    @FXML
    private VBox recentTopTenContainer;
    
    /* VENTANA DDE PREMIUM */ 
    @FXML
    private BorderPane premiumView; // Contenedor de la vista de premium
    @FXML
    private JFXButton premiumActivate;
    @FXML
    private Label premiumLabel;

	/////////////////
	/* CONSTRUCTOR */
	/////////////////
    
	public ViewController() {
		controller = AppVideo.getUnicaInstancia();
		videoWeb = new VideoWeb();
		isProfileOpen = false;	
		showDeleteNotification = true;
		showEditNotification = true;
		editPlayListMode = false;
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// Este método se invoca después de cargar todos los elementos de JavaFX
		assignEnterKeyToButton(loginView, loginButton);
		
		assignEnterKeyToButton(registerView, registerRegister);
		//assignKeyToButton(rootBorderPane, KeyCode.ESCAPE, registerCancel);
		
		//assignEnterKeyToButton(exploreView, exploreSearch);
		//assignEnterKeyToButton(myListsView, myListsAccept);
		
		// Abrimos la primera ventana
		openLoginView(null);
	}

    ///////////////////////
    /* MANEJO DE EVENTOS */
    ///////////////////////
	
	/* ABRIR VENTANAS */
    @FXML
    void openLoginView(ActionEvent event) {
    	// Abrimos una ventana u otra en función del estado
    	if (!isProfileOpen) {
        	// Ocultamos el elemento que hubiese en el frente
        	Node oldFront = stackpane.getChildren().get(stackpane.getChildren().size() - 1);
        	oldFront.setDisable(true);
        	oldFront.setVisible(false);
        	
    		// Limpiamos los elementos de la vista
    		loginNick.clear();
    		loginPassword.clear();
    		
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
    void openExplorarView(ActionEvent event) {	
    	// Ocultamos el elemento que hubiese en el frente
    	Node oldFront = stackpane.getChildren().get(stackpane.getChildren().size() - 1);
    	oldFront.setDisable(true);
    	oldFront.setVisible(false);
    	
    	// Cargamos los filtros y etiquetas
    	loadTags(controller.getListaEtiquetas());
		loadFilters();
		if (controller.getUsuarioActual().isPremium()) {
			exploreFilterTag.setDisable(false);
			exploreFilters.setDisable(false);		
		} else {
			exploreFilterTag.setDisable(true);
			exploreFilters.setDisable(true);
		}
		
		// Limpiamos la búsqueda anterior, si la hubiese
		exploreContent.getChildren().clear();
		
    	// Traemos la ventana al frente y la hacemos visible
    	exploreView.setDisable(false);
    	exploreView.setVisible(true);
    	exploreView.toFront();
    	
    	exploreScroll.setFitToHeight(true);
    	
    	fadeIn(exploreView);
    }

    @FXML
    void openMislistasView(ActionEvent event) {
    	// Ocultamos el elemento que hubiese en el frente
    	Node oldFront = stackpane.getChildren().get(stackpane.getChildren().size() - 1);
    	oldFront.setDisable(true);
    	oldFront.setVisible(false);
    	   	
    	// Limpiamos todos los elementos de la vista y cargamos las listas
		myListsContent.getChildren().clear();
		myListsNew.setDisable(false);
		myListsEdit.setDisable(true);
		myListsPlay.setDisable(true);
		myListsDelete.setDisable(true);
		
		myListsTitle.setDisable(true);
		myListsSearch.setDisable(true);
		myListsClear.setDisable(true);
		myListsTitle.clear();
		
		myListsList.getItems().clear();
		myListsComboBox.getSelectionModel().clearSelection();
		
		myListsSecondarySideBar.setDisable(true);
		myListsSecondarySideBar.setVisible(false);
		
		myListsMainSideBar.setDisable(false);
		myListsMainSideBar.setVisible(true);
		
		myListsSearchView.setFitToHeight(true);
		
		editPlayListMode = false;
		loadMyListsComboBox();
		
		// Comprobamos si el usuario es premium para activar o no la funcionalidad premium
		if (controller.getUsuarioActual().isPremium()) {
			myListsCreatePDF.setDisable(false);
		} else {
			myListsCreatePDF.setDisable(true);
		}	
		
    	// Traemos la ventana al frente y la hacemos visible
    	myListsView.setDisable(false);
    	myListsView.setVisible(true);
    	myListsView.toFront();
		
    	fadeIn(myListsView);
    }

    @FXML
    void openRecientesView(ActionEvent event) {
		// Ocultamos el elemento que hubiese en el frente
		Node oldFront = stackpane.getChildren().get(stackpane.getChildren().size() - 1);
		oldFront.setDisable(true);
		oldFront.setVisible(false);
		
		// Cargamos las dos listas de vídeos
		loadVideosToList(controller.getRecientes(), recentVideosList);
		if (controller.getUsuarioActual().isPremium()) {
			recentTopTenContainer.setDisable(false);	
		} else {
			recentTopTenContainer.setDisable(true);
		}
		// Cargamos la lista de top 10 vídeos independientemente de si el usuario es premium o no
		// Principalmente para motivar al usuario a convertirse en premium
		loadVideosToList(controller.getTopten(), recentTopTenList); 
			
		// Traemos la ventana al frente y la hacemos visible
		recentView.setDisable(false);
		recentView.setVisible(true);
		recentView.toFront();
		
		fadeIn(recentView);
    }

    @FXML
    void openPremiumView(ActionEvent event) {
    	// Ocultamos el elemento que hubiese en el frente
    	Node oldFront = stackpane.getChildren().get(stackpane.getChildren().size() - 1);
    	oldFront.setDisable(true);
    	oldFront.setVisible(false);
    	
    	if (controller.getUsuarioActual().isPremium()) {
    		premiumActivate.setDisable(true);
    		premiumLabel.setText("¡Gracias por activar premium!");
    	} else {
    		premiumActivate.setDisable(false);
    		premiumLabel.setText("¡Por un módico precio de 0.00€*!");
    	}
    	
    	// Traemos la ventana al frente y la hacemos visible
    	premiumView.setDisable(false);
    	premiumView.setVisible(true);
    	premiumView.toFront();
    	
    	fadeIn(premiumView);
    }
        	
    @FXML
    void openRegisterView(MouseEvent event) {
    	// Ocultamos el elemento que hubiese en el frente
    	Node oldFront = stackpane.getChildren().get(stackpane.getChildren().size() - 1);
    	oldFront.setDisable(true);
    	oldFront.setVisible(false);
    	
    	// Limpiamos todos los elementos de la ventana
    	registerNick.clear();
    	registerPassword.clear();
    	registerPasswordRepeat.clear();
    	registerName.clear();
    	registerSurname.clear();
    	registerDatePicker.setValue(null);
    	registerEmail.clear();   
    	hideRegisterLabels();
    	
    	// Traemos la ventana al frente y la hacemos visible
    	registerView.setDisable(false);
    	registerView.setVisible(true);
    	registerView.toFront(); 	
    	
    	fadeIn(registerView);
    }

    void openProfileView() {
		// Ocultamos el elemento que hubiese en el frente
		Node oldFront = stackpane.getChildren().get(stackpane.getChildren().size() - 1);
		oldFront.setDisable(true);
		oldFront.setVisible(false);
		
    	// Actualizamos al usuario actual por si se han producido cambios
		Usuario usuarioActual = controller.getUsuarioActual();
		isProfileOpen = true;
		
		// Actualizamos elementos de la vista de perfil
		profileNick.setText(usuarioActual.getLogin());
		profileEmail.setPromptText(usuarioActual.getEmail());
		profileDatePicker.setPromptText(usuarioActual.getFechaNac().toString());
		profileTitle.setText("Bienvenido, " + usuarioActual.getNombre());

		// Limpiamos los elementos a introducir
		profileEmail.clear();
		profileDatePicker.setValue(null);
		
		// Hacemos la vista visible
		profileView.setVisible(true);
		profileView.setDisable(false);
		profileView.toFront();
		fadeIn(profileView);
	}
      
    /* FUNCIONALIDAD VENTANA LOGIN */
    @FXML
    // Validación de la cuenta introducida en el login
    void loginEnter(ActionEvent event) {
    	boolean valid = true;
    	// Comprobamos que ambos campos hayan sido introducidos
    	// Si alguno no lo está, activamos su etiqueta de "*Este campo obligatorio"
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
    			openRecientesView(event);
    			
    			setProfileFunctionsTo(true); // Cambiamos el botón de login por el de mi perfil
    			setUserFunctionsTo(true); // Desbloqueamos la funcionalidad disponible para usuarios
    			
    			checkForUserBirthday();
    		} else {
    			// Login inválido
    			showDialog("Login inválido", "El nombre y/o la contraseña son incorrectas");
    		}
    	}
    }

    /* FUNCIONALIDAD VENTANA DE REGISTRO */
    @FXML
    // Registrar un usuario en el sistema
    void registerUser(ActionEvent event) {
    	// Comprobamos que ninguno de los campos obligatorios esté vacío.
    	// Si alguno lo está, activamos su etiqueta de "*Este campo obligatorio"
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
				openRecientesView(event);
				
				setProfileFunctionsTo(true); // Cambiamos el botón de login por el de mi perfil
				setUserFunctionsTo(true); // Desbloqueamos la funcionalidad disponible para usuarios
    		} else {
    			// Registro inválido (shouldn't happen)
    			showDialog("Registro inválido", "No se ha podido registrar al usuario");
    		}
    	}
    }
    
    @FXML
    void registerCancel(ActionEvent event) {
    	// Hace lo mismo que el evento openLoginView pero lo separamos por legibilidad y escalabilidad
    	openLoginView(event);
    }
    
    /* FUNCIONALIDAD VENTANA DE PERFIL */
    @FXML
    // Salir de la cuenta actualmente utilizada
    void profileLogout(ActionEvent event) {
    	controller.salirUsuario();
    	// Cambiamos el botón de mi perfil por el de login
    	setProfileFunctionsTo(false);
   	
    	// Bloqueamos la funcionalidad disponible para usuarios
    	setUserFunctionsTo(false);
		
    	openLoginView(event);
    }

    @FXML
    void loadVideos(ActionEvent event) {
    	FileChooser fileChooser = new FileChooser();
    	fileChooser.setTitle("Abrir fichero XML con vídeos");
    	try {
	    	File file = fileChooser.showOpenDialog(( (Node) event.getSource()).getScene().getWindow());
	    	controller.cargarVideos(file);
	    	showDialog("Boop!", "Vídeos cargados :)");
    	}
    	catch (Exception e) {
    		showDialog("Error", "No se ha elegido ningún fichero o el fichero indicado es inválido");
		}
    }
     
    @FXML
	// Actualizar todos los campos introducidos en la ventana de perfil.
    void profileUpdate(ActionEvent event) {
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
    
    /* FUNCIONALIDAD VENTANA DE EXPLORAR */
    @FXML
    // Busqueda de vídeos
    void exploreSearch(ActionEvent event) { 	
    	// Borramos el resultado de la busqueda anterior
    	exploreContent.getChildren().clear();
    	exploreScroll.setFitToHeight(false); // Permite el scroll vertical
    	
    	// Hecho esto, buscamos
    	Set<Video> videos = controller.buscarVideos(exploreTitle.getText(), getSelectedTags(), true);
    	for (Video video : videos) {
    		Label element = createVideoThumbnail(video, 200, 150);
    		element.getStyleClass().add("videothumbnail");
    		element.setOnMouseClicked(e -> {
			        if(e.getButton().equals(MouseButton.PRIMARY)){
			            if(e.getClickCount() == 2){
			            	// Si se hace doble click sobre la miniatura del vídeo, abrimos una ventana para reproducirlo
			            	showVideoDialog(video, "videoDialog");
			            }
			        }
    		});
    		exploreContent.getChildren().add(element);
    	}
    }    
    
    @FXML
    // Limpiar la búsqueda actual de vídeos
    void exploreClear(ActionEvent event) {
    	exploreContent.getChildren().clear();
    	// Desactivamos el scroll vertical temporalmente porque el contenedor no se redimensiona al limpiar sus elementos
    	exploreScroll.setFitToHeight(true); 
    }
    
    @FXML
    // Aplicar un filtro premium al usuario actual
    void chooseFilter(ActionEvent event) {
    	if (exploreFilters.getValue() == null) return; // Comprobamos que el evento no se haya disparado sobre un elemento vacío
    	Filtro selectedFilter = controller.getFiltro(exploreFilters.getSelectionModel().getSelectedItem());
    	controller.aplicarFiltro(selectedFilter);
    }

    @FXML
    void exploreTagsClicked(MouseEvent event) {
    	// Este evento no se corresponde con ninguna funcionalidad de la especificación
    	// Por la estructura del JFXListView y JFXCheckBox, es posible hacer click sobre la celda de la lista sin hacer click sobre el checkbox
    	// Por tanto, disparamos el checkbox independientemente
    	JFXCheckBox selectedTag = exploreTags.getSelectionModel().getSelectedItem();
    	if (selectedTag != null) exploreTags.getSelectionModel().getSelectedItem().fire();
    	
    	// Limpiamos el elemento seleccionado porque el .css es diabólico
    	// No conseguimos modificar el color del texto del elemento seleccionado ni a patadas
    	exploreTags.getSelectionModel().clearSelection();
    }

    /* FUNCIONALIDAD VENTANA DE MIS LISTAS */
    @FXML
    // Añadir un efecto visual al despliegue de las opciones del panel lateral de la vista
    void displayOptions(ActionEvent event) {
    	RotateTransition rt = new RotateTransition(Duration.millis(100), myListExpand);
    	if (!areOptionsOpened) {
        	rt.setFromAngle(0);
        	rt.setToAngle(90);
        	areOptionsOpened = true;
    	} else {
        	rt.setFromAngle(90);
        	rt.setToAngle(0);
        	areOptionsOpened = false;
    	}
    	rt.setAutoReverse(true);
    	rt.play();
    }
 
    @FXML
    // Proporcionamos al usuario la interfaz necesaria para añadir una nueva lista
    void addVideoList(ActionEvent event) {
    	// Ocultamos el panel lateral con las listas y las opciones
    	myListsMainSideBar.setDisable(true);
    	myListsMainSideBar.setVisible(false);
    	
    	// Limpiamos el contenedor para añadir una nueva lista
    	myListsNewListTitle.clear();
    	
    	// Mostramos el panel lateral secundario para añadir nuevas listas 	
    	myListsSecondarySideBar.setDisable(false);
    	myListsSecondarySideBar.setVisible(true);

    	fadeIn(myListsSecondarySideBar);
    }
   
    @FXML
    // Validamos la lista introducida
    void acceptNewList(ActionEvent event) {
    	// Si se ha introducido un título, lo registramos
    	// Si no se ha introducido ningún título, lo interpretamos como que el usuario no quiere crear ninguna lista
    	if (!myListsNewListTitle.getText().equals("")) {
    		if (controller.crearListaVideos(myListsNewListTitle.getText())) {
        		loadMyListsComboBox();
        		myListsComboBox.getSelectionModel().select(myListsNewListTitle.getText());   		
        		myListsEdit.setDisable(false);
        		myListsPlay.setDisable(false);
        		myListsDelete.setDisable(false);
    		}
    		else {
    			showDialog("Error al crear una lista", "Ya existe una lista con ese nombre.\nDeja de intentar rompernos el sistema, gracias.");
    		}
    	}
    	
    	// Ocultamos  el panel lateral secundario para añadir nuevas listas
    	myListsSecondarySideBar.setDisable(true);
    	myListsSecondarySideBar.setVisible(false);
    	
    	// Mostramos el panel lateral con las listas y las opciones
    	myListsMainSideBar.setDisable(false);
    	myListsMainSideBar.setVisible(true);
    	 	
    	fadeIn(myListsMainSideBar);
    }    

    @FXML
    // Elegir una lista de las listas disponibles
    void chooseList(ActionEvent event) {
    	// Cuando se elige una lista del comboBox con todas las listas de reproducción
    	// activamos sus controles y cargamos los vídeos de la lista
    	if (myListsComboBox.getValue() == null) return; // Evitamos que se dispare la funcionalidad cuando se limpia el elemento seleccionado
		myListsEdit.setDisable(false);
		myListsPlay.setDisable(false);
		myListsDelete.setDisable(false);
		loadListToListView(myListsComboBox.getSelectionModel().getSelectedItem());
    }
    
    @FXML
    // Borrar la lista de vídeos seleccionada
    void deleteVideoList(ActionEvent event) {
    	if (showDeleteNotification) {
        	// Generamos una ventana emergente para preguntar al usuario si está seguro de la operación
        	// Asimismo, incluimos un checkbox por si el usuario no quiere que vuelva a mostrarse el diálogo emergente
    		JFXDialogLayout dialogContent = new JFXDialogLayout();

    		// Título de la ventana emergente
    		dialogContent.setHeading(new Text("Borrar lista de vídeos"));
    		
    		// Contenido de la ventana emergente
    		VBox dialogBody = new VBox();
    		dialogBody.setSpacing(10);
    		
    		Text dialogMessage = new Text("¿Estás seguro de querer borrar la lista de vídeos seleccionada?");
    		JFXCheckBox dialogCheckBox = new JFXCheckBox("No volver a mostrar este mensaje");
    		dialogCheckBox.getStyleClass().add("myjfx-check-box");
    		
    		dialogBody.getChildren().addAll(dialogMessage, dialogCheckBox);
    		
    		dialogContent.setBody(dialogBody);
    		dialogContent.setStyle(DIALOG_LABEL_STYLE);

    		// Botones de la ventana emergente
    		JFXButton cancel = new JFXButton("Cancelar");
    		cancel.getStyleClass().addAll("jfxbutton", "jfxdialogbutton");
    		cancel.setPrefSize(DIALOG_BUTTON_WIDTH, DIALOG_BUTTON_HEIGHT);
    		cancel.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
    		
    		JFXButton accept = new JFXButton("Aceptar");
    		accept.getStyleClass().addAll("jfxbutton", "jfxdialogbutton");
    		accept.setPrefSize(DIALOG_BUTTON_WIDTH, DIALOG_BUTTON_HEIGHT);
    		accept.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
    		dialogContent.setActions(accept, cancel);

    		JFXDialog dialog = new JFXDialog(rootStackPane, dialogContent, JFXDialog.DialogTransition.BOTTOM);
    		
    		cancel.setOnAction(e -> {
    			showDeleteNotification = !dialogCheckBox.isSelected();
    			dialog.close();	
    		}); 
    		
    		accept.setOnAction(e -> {
    			showDeleteNotification = !dialogCheckBox.isSelected();
    			deleteVideoListAndRefresh(myListsComboBox.getSelectionModel().getSelectedItem()); 		
    			dialog.close();
    		});
		
    		dialog.show();
    	} else {
    		// Si el usuario ha elegido no volver a mostrar la ventana emergente, eliminamos la lista directamente
    		deleteVideoListAndRefresh(myListsComboBox.getSelectionModel().getSelectedItem());
    	}
    }
    
    @FXML
    // Editar la lista de vídeos seleccionada
    void editPlaylist(ActionEvent event) {
    	if (!editPlayListMode) {
	    	if (showEditNotification) {
	        	// Generamos una ventana emergente para preguntar al usuario si está seguro de la operación
	        	// Asimismo, incluimos un checkbox por si el usuario no quiere que vuelva a mostrarse el diálogo emergente
	    		JFXDialogLayout dialogContent = new JFXDialogLayout();
	
	    		// Título de la ventana emergente
	    		dialogContent.setHeading(new Text("Editar lista de vídeos"));
	    		
	    		// Contenido de la ventana emergente
	    		VBox dialogBody = new VBox();
	    		dialogBody.setSpacing(10);
	    		
	    		Label dialogMessage = new Label("Entrando el modo de edición.\n"
	    				+ "Haz doble click en cada vídeo para añadirlo en la lista. "
	    				+ "Asimismo, haz doble click sobre cada vídeo en la lista para eliminarlo.\n"
	    				+ "Para salir del modo de edición, vuelve a presionar el mismo botón.");
	    		dialogMessage.setWrapText(true);
	    		dialogMessage.setTextAlignment(TextAlignment.JUSTIFY);
	    		dialogMessage.setStyle(DIALOG_LABEL_STYLE);
	    		JFXCheckBox dialogCheckBox = new JFXCheckBox("No volver a mostrar este mensaje");;
	    		dialogCheckBox.getStyleClass().add("myjfx-check-box");
	    		
	    		dialogBody.getChildren().addAll(dialogMessage, dialogCheckBox);
	    		
	    		dialogContent.setBody(dialogBody);
	    		dialogContent.setStyle(DIALOG_LABEL_STYLE);
	
	    		// Botones de la ventana emergente
	    		JFXButton close = new JFXButton("Cerrar");
	    		close.getStyleClass().addAll("jfxbutton", "jfxdialogbutton");
	    		close.setPrefSize(DIALOG_BUTTON_WIDTH, DIALOG_BUTTON_HEIGHT);
	    		close.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
	    		dialogContent.setActions(close);
	    		
	    		JFXDialog dialog = new JFXDialog(rootStackPane, dialogContent, JFXDialog.DialogTransition.BOTTOM);
	    		
	    		close.setOnAction(e -> {
	    			showEditNotification = !dialogCheckBox.isSelected();
	    			dialog.close();	
	    		}); 
			
	    		dialog.show();    		
	    	} 
	    	// Desactivamos el resto de la funcionalidad mientras se está en el modo de edición
	    	myListsDelete.setDisable(true);
	    	myListsNew.setDisable(true);
	    	myListsPlay.setDisable(true);
	    	myListsCreatePDF.setDisable(true);
	    	editPlayListMode = true;
	    	
	    	myListsTitle.setDisable(false);
	    	myListsSearch.setDisable(false);
	    	myListsClear.setDisable(false);
	    	
    	} else {
    		// Volvemos a activar el resto de la funcionalidad de la misma ventana
	    	myListsDelete.setDisable(false);
	    	myListsNew.setDisable(false);
	    	myListsPlay.setDisable(false);
	    	myListsCreatePDF.setDisable(false);
	    	editPlayListMode = false;
	    	
	    	myListsTitle.setDisable(true);
	    	myListsSearch.setDisable(true);
	    	myListsClear.setDisable(true);
	    	myListsTitle.clear();
	    	myListsContent.getChildren().clear();
    	}	
    }    

    @FXML
    // Limpiar la búsqueda actual de vídeos
    void myListsClearVideos(ActionEvent event) {
    	myListsContent.getChildren().clear();
    	myListsSearchView.setFitToHeight(true); // Evita que la barra de scroll se quede
    }

    @FXML 
    // Realizar una búsqueda (sin etiquetas)
    void myListsSearchVideos(ActionEvent event) {
    	// Borramos el resultado de la busqueda anterior
    	myListsContent.getChildren().clear();
    	myListsSearchView.setFitToHeight(false);
    	
    	// Hecho esto, buscamos
    	Set<Video> videos = controller.buscarVideos(myListsTitle.getText(), null, false);
    	// Añadimos todos los vídeos encontrados al contenedor correspondiente
    	for (Video video : videos) {
    		Label element = createVideoThumbnail(video, 200, 150);
    		element.getStyleClass().add("videothumbnail");
    		element.setOnMouseClicked(e -> {
			        if(e.getButton().equals(MouseButton.PRIMARY)){
			            if(e.getClickCount() == 2){
			            	// Si se hace doble click sobre la miniatura del vídeo, lo añadimos a la lista
			            	addVideoToCurrentList(video);
			            }
			        }
    		});
    		myListsContent.getChildren().add(element);
    	}
    }

    @FXML
    void playVideoList(ActionEvent event) { 
		String listName = myListsComboBox.getSelectionModel().getSelectedItem();
		ListaVideos listToPlay = controller.getListaVideos(listName);  
		playAllVideosFromList(listToPlay);
    }
    
    @FXML
    // Manejo de la interacción con la lista de reproducción actual
    void playOrRemoveVideoFromList(MouseEvent event) {
    	// Si el evento se dispara sobre una lista vacía, no hacemos nada
		if (myListsList.getItems().isEmpty())
			return;
		Label label = myListsList.getSelectionModel().getSelectedItem();
    	// Si estamos en modo de edición, borramos el vídeo seleccionado con doble click
		if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
			if (editPlayListMode) {	
				controller.removeVideoDeLista(label.getId(), myListsComboBox.getSelectionModel().getSelectedItem());
				myListsList.getItems().remove(label);
				fadeIn(myListsList);
			} else {	
				// Sino, reproducimos el vídeo
				Video video = controller.getVideo(label.getId());
	        	showVideoDialog(video, "videoDialog");		
	    	}
		}
    }
    
    @FXML
    // Generar archivo PDF con todas las listas disponibles
    void generateListPDF(ActionEvent event) {
       	// Generamos una ventana emergente para preguntar el usuario el tiempo que quiere dejar entre vídeo y vídeo
    		JFXDialogLayout dialogContent = new JFXDialogLayout();

    		// Título de la ventana emergente
    		Text dialogTitle = new Text("Crear archivo PDF con todas las listas del usuario");
    		dialogTitle.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");
    		dialogContent.setHeading(dialogTitle);
    		
    		// Contenido de la ventana emergente
    		VBox dialogBody = new VBox();
    		dialogBody.setSpacing(10);
    		
    		Text dialogMessage = new Text("Esta funcionalidad es para usuarios premium, siéntete afortunado :^)\n\nIntroduzca el nombre del archivo a generar:");
    		
    		JFXTextField fileName = new JFXTextField();
    		fileName.getStyleClass().add("jfxtextfield");
    		fileName.setPrefWidth(170);
    		fileName.setMaxWidth(fileName.getPrefWidth());
    		
    		// Limitamos el nombre del fichero a 20 caracteres
    		UnaryOperator<Change> filter = change -> {
    		    if (change.getControlNewText().matches(".{0,20}")) {
    		        return change;
    		    }
    		    return null;
    		};
    		TextFormatter<String> textFormatter = new TextFormatter<>(filter);
    		
    		fileName.setTextFormatter(textFormatter);
    		
    		dialogBody.getChildren().addAll(dialogMessage, fileName);
    		
    		dialogContent.setBody(dialogBody);
    		dialogContent.setStyle(DIALOG_LABEL_STYLE);

    		// Botones de la ventana emergente
    		JFXButton cancel = new JFXButton("Cancelar");
    		cancel.getStyleClass().addAll("jfxbutton", "jfxdialogbutton");
    		cancel.setPrefSize(DIALOG_BUTTON_WIDTH, DIALOG_BUTTON_HEIGHT);
    		cancel.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
    		dialogContent.setActions(cancel);
    		
    		JFXButton accept = new JFXButton("Aceptar");
    		accept.getStyleClass().addAll("jfxbutton", "jfxdialogbutton");
    		accept.setPrefSize(DIALOG_BUTTON_WIDTH, DIALOG_BUTTON_HEIGHT);
    		accept.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
    		
    		dialogContent.setActions(accept, cancel);		
    		JFXDialog dialog = new JFXDialog(rootStackPane, dialogContent, JFXDialog.DialogTransition.BOTTOM);
    		
    		cancel.setOnAction(e -> dialog.close()); 
    		
    		accept.setOnAction(e -> {
    			dialog.close();	
    			if (!fileName.getText().equals("")) {
    				if (controller.crearPDF(fileName.getText())) {
    					showDialog("Boop!", "El fichero ha sido generado correctamente en el directorio de la aplicación :)");
    				} else {
    					showDialog("Error", "No se ha podido generar el fichero");
    				}
    			} else {
    				showDialog("Error", "No se puede crear un fichero sin nombre...");
    			}
    		});
    		dialog.show();    	
    }
    
    /* FUNCIONALIDAD VENTANA RECIENTES */   
    @FXML
    // Reproducir todos los vídeos de la lista de recientes
    void playRecentVideos(ActionEvent event) {
    	playAllVideosFromList(controller.getRecientes());
    }

    @FXML
    // Reproducir todos los vídeos de la lista de top ten
    void playTopTen(ActionEvent event) {
    	playAllVideosFromList(controller.getTopten());
    }

    @FXML
    // Reproducir un vídeo de alguna de las dos listas de la ventana de recientes
    void playVideoFromList(MouseEvent event) {	
    	if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
    		// Extraemos del evento la lista que ha sido presionada
    		@SuppressWarnings("unchecked")
			JFXListView<Label> list = (JFXListView<Label>) event.getSource();
    		// Extraemos el vídeo que ha sido seleccionado, si existe
        	Label label = list.getSelectionModel().getSelectedItem();
        	if (label == null)
        		return;
    		Video video = controller.getVideo(label.getId());
        	showVideoDialog(video, "videoDialog");	
        	// Refrescamos la lista de recientes
        	loadVideosToList(controller.getRecientes(), recentVideosList);
    	}
    }
    
    /* FUNCIONALIDAD VENTANA DE PREMIUM */
    @FXML
    void activatePremium(ActionEvent event) {
    	if (!controller.getUsuarioActual().isPremium()) {
    		controller.obtenerPremium();
    		showDialog("Premium activado", "*Le cobraremos el +0.25 a nuestra nota final en la convocatoria correspondiente");
    		premiumActivate.setDisable(true);
    		premiumLabel.setText("¡Gracias por activar premium!");
    	}
    }

    /* FUNCIONALIDAD BARRA SUPERIOR */
    @FXML
    void minimizeWindow(ActionEvent event) {
    	((Stage)((JFXButton)event.getSource()).getScene().getWindow()).setIconified(true);  	
    }

    @FXML
    void maximizeWindow(ActionEvent event) {
		Stage stg = ((Stage) ((JFXButton) event.getSource()).getScene().getWindow());
		// Si la ventana estaba maximizada, restauramos su tamaño anterior
		if (stg.isMaximized()) {
			stg.setMaximized(false);
		} else {
			// Sino, la maximizamos
			stg.setMaximized(true);
		}
	}

    @FXML
    // Cerrar la ventana y la aplicación
    void closeWindow(ActionEvent event) {
    	((Stage)((JFXButton)event.getSource()).getScene().getWindow()).close();
    	System.exit(0);
    }
  
    ////////////////////////////
    /* FUNCIONALIDAD AUXILIAR */
    ////////////////////////////

	// Método para hacer una transición de un nodo pasado de parámetro
	// La duración no está parametrizada pero podría parametrizarse también
	private void fadeIn(Node node) {
		FadeTransition ft = new FadeTransition(Duration.millis(200), node);
		ft.setFromValue(0.0);
		ft.setToValue(1.0);
		ft.play();
	}  

	// Generar un JFXDialog textual sobre la aplicación
	private void showDialog(String title, String content) {		
		JFXDialogLayout dialogContent = new JFXDialogLayout();

		// Título y cuerpo de la ventana emergente
		dialogContent.setHeading(new Text(title));
		dialogContent.setBody(new Text(content));
		dialogContent.setStyle(DIALOG_LABEL_STYLE);

		// Botones de la ventana emergente
		JFXButton close = new JFXButton("Cerrar");
		close.getStyleClass().addAll("jfxbutton", "jfxdialogbutton");
		close.setPrefSize(DIALOG_BUTTON_WIDTH, DIALOG_BUTTON_HEIGHT);
		close.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

		dialogContent.setActions(close);

		JFXDialog dialog = new JFXDialog(rootStackPane, dialogContent, JFXDialog.DialogTransition.BOTTOM);

		close.setOnAction(e -> dialog.close());
		dialog.show();
	}
	
    // Ocultar todas las labels de "*Este campo es obligatorio" de la vista del login
    private void hideLoginLabels() {
    	loginLabelNick.setVisible(false);
    	loginLabelPassword.setVisible(false);
    }
    
    // Ocultar todas las labels de "*Este campo es obligatorio" de la vista de registro
    private void hideRegisterLabels() {
    	registerLabelNick.setVisible(false);
    	registerLabelPassword.setVisible(false);
    	registerLabelPassRepeat.setVisible(false);
    	registerLabelName.setVisible(false);
    	registerLabelDate.setVisible(false);  	
    }
    
    // Activar o desactivar la funcionalidad del usuario
    private void setUserFunctionsTo(boolean value) {
		explorar.setDisable(!value);
		mislistas.setDisable(!value);
		recientes.setDisable(!value);
		premium.setDisable(!value);
    }
    
    // Indicar si el usuario está logeado o no
    private void setProfileFunctionsTo(boolean value) {
    	isProfileOpen = value;
    	
    	if (isProfileOpen) {
    		login.setText("Mi perfil");
    	} else {
    		login.setText("Log in");
    	}
    }
    
	// Generar y mostrar un JFXDialog con un vídeo pasado de parámetro. 
    // El diálogo tendrá de id el que se pasa de parámetro.
	private void showVideoDialog(Video video, String id) {		
		// Incrementamos el número de visitas del vídeo
		controller.reproducir(video.getURL());
		
		// Creamos los elementos del diálogo
		JFXDialogLayout dialogContent = new JFXDialogLayout();
		Text dialogTitle = new Text(video.getTitulo());
		dialogTitle.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");
		dialogContent.setHeading(dialogTitle);

		// Dado que el componente proporcionado es sobre Swing, necesitamos usar un contenedor especial
		SwingNode videoComponent = new SwingNode();
		videoComponent.setContent(videoWeb);
		
		// Lo metemos en un HBox para poder centrar únicamente el vídeo
		HBox videoComponentContainer = new HBox();
		videoComponentContainer.setAlignment(Pos.CENTER);
		videoComponentContainer.getChildren().add(videoComponent);
		
		// Contenedor de etiquetas
		FlowPane tags = new FlowPane();
		tags.setHgap(5.0);
		tags.setVgap(5.0);

		// Añadimos las etiquetas al contenedor
		for (Etiqueta tag : video.getEtiquetas()) {
			addTagToPane(tag, video.getURL(), tags);
		}

		// Contenedor de añadir nueva etiqueta
		HBox addTags = new HBox();
		addTags.setSpacing(10.0);
		
		// TextField para introducir una etiqueta
		JFXTextField addTagsTextField = new JFXTextField();
		addTagsTextField.setPromptText("Añadir etiqueta");
		addTagsTextField.getStyleClass().add("jfxtextfield");
		
		// Botón para añadir una etiqueta
		JFXButton add = new JFXButton("");
		add.getStyleClass().addAll("jfxbutton", "jfxdialogbutton", "addtagbutton");
		add.setPrefSize(30, 30);
		add.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
		add.setOnMouseClicked(e -> {
				// Separamos cada una de las etiquetas por espacios
				/* NOTA:
				 *	Esto se debe a que el servicio de persistencia separa las etiquetas por espacios
				 *	Evidentemente, la capa de la vista no debería de ser consciente de ello pero lo dejamos así por simplicidad de código
				 */
				String[] allTags = addTagsTextField.getText().split("\\s+");
				for (String i : allTags) {
					if (!i.equals("")) {
			            Etiqueta tag =  new Etiqueta(i);
			            boolean TagExisted = controller.containsEtiqueta(tag);
			            // Añadimos la etiqueta creada al sistema
			            if (controller.addEtiquetaVideo(tag, video.getURL())) {
			            	addTagToPane(tag, video.getURL(), tags);
			            	// Si la etiqueta no existía en ningún vídeo, la añadimos al panel con las etiquetas disponibles
			            	if (!TagExisted) {	
				        		exploreTags.getItems().add(createCheckBoxTag(tag.getNombre()));
			            	}
			            }
					}
				}
	            addTagsTextField.clear(); // Limpiamos la etiqueta introducida
			}
		);

		addTags.getChildren().addAll(addTagsTextField, add);
		
		// Número de reproducciones
		Text views = new Text("Reproducciones: " + video.getNumReproducciones());
		views.setStyle("-fx-font-size: 14; -fx-font-weight: bold;");
		// Contenedor del reproductor de vídeos
		VBox body = new VBox();
		body.setSpacing(10.0);
		body.getChildren().addAll(videoComponentContainer, tags, views, addTags);

		dialogContent.setBody(body);

		// Botones de la ventana emergente
		JFXButton close = new JFXButton("Cerrar");
		close.getStyleClass().addAll("jfxbutton", "jfxdialogbutton");
		close.setPrefSize(DIALOG_BUTTON_WIDTH, DIALOG_BUTTON_HEIGHT);
		close.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
		dialogContent.setActions(close);

		JFXDialog dialog = new JFXDialog(rootStackPane, dialogContent, JFXDialog.DialogTransition.CENTER);
		dialog.setId(id);
		dialog.setOverlayClose(false);		
		close.setOnAction(e -> {
				dialog.close();
				videoWeb.cancel();
				stopGlobalTimer(); // Paramos el temporizador global independientemente de si está siendo usado o no
			}
		);
		
		videoWeb.playVideo(video.getURL());
		dialog.show();
	}

	// Cargamos la lista de la ventana de explorar con todas las etiquetas pasadas de parámetro
	private void loadTags(Set<Etiqueta> savedTags) {
		exploreTags.setItems(savedTags.stream()
				.map(Etiqueta::getNombre) // Obtenemos el nombre de las etiquetas
				.map(t -> createCheckBoxTag(t)) // Creamos un checkbox a partir de estos nombres de etiqueta
				.collect(Collectors.toCollection(FXCollections::observableArrayList))); // Convertimos el conjunto en el formato esperado por el contenedor
	}
	
	// Crear un objeto JFXCheckBox para representar etiquetas a partir del título pasado de parámetro
	private JFXCheckBox createCheckBoxTag(String title) {
		JFXCheckBox tag = new JFXCheckBox();
		
		tag.setText(title);
		tag.setMaxWidth(Control.USE_PREF_SIZE);
		tag.getStyleClass().add("myjfx-check-box");
		
		return tag;
	}
	
	// Devuelve un conjunto con las etiquetas seleccionadas de la lista de etiquetas de la ventana de explorar
	private Set<Etiqueta> getSelectedTags() {
		Set<Etiqueta> selectedTags = new HashSet<Etiqueta>();
		for (JFXCheckBox checkbox : exploreTags.getItems()) {
			if (checkbox.isSelected()) selectedTags.add(new Etiqueta(checkbox.getText()));
		}
		return selectedTags;
	}
	
	// Añadir una etiqueta al contenedor pasado de parámetro
	// A partir de cada etiqueta se crea un label que contiene su título y la funcionalidad necesaria para poder borrarla
	// Necesitamos la URL del vídeo al que pertenece para poder eliminar la etiqueta posteriormente
	private boolean addTagToPane(Etiqueta tag, String URL, Pane tags) {
		Label l = new Label(tag.getNombre());
		l.getStyleClass().add("videotag");
		
		l.setOnMouseClicked(e -> {
			tags.getChildren().remove(l);
			controller.borrarEtiquetaVideo(tag, URL);
			// Si era el último vídeo con esa etiqueta, la borramos de la lista de etiquetas disponibles para la búsqueda
			if (!controller.containsEtiqueta(tag)) {
				// Como tenemos una lista de checkboxes no podemos buscar la etiqueta directamente por nombre
				// Recuperamos la label con la etiqueta y luego la borramos
				JFXCheckBox checkboxToDelete = null;
				for (JFXCheckBox checkbox : exploreTags.getItems()) {
					if (checkbox.getText().equals(tag.getNombre())) {
						checkboxToDelete = checkbox;											
					}
				}
				exploreTags.getItems().remove(checkboxToDelete);
			}
		});
		return tags.getChildren().add(l);
	}
	
	// Crear una miniatura de vídeo en nuestro formato personal
	// Contiene la miniatura y su título
	private Label createVideoThumbnail(Video video, int width, int height) {		
		Label label = new Label();
		label.setId(video.getURL());
		
		// Propiedades de la label que contiene la miniatura y el título del vídeo
		label.setMaxWidth(width);
		label.setMaxHeight(height);
		label.setPrefWidth(width);
		label.setPrefHeight(height);
		label.setAlignment(Pos.CENTER);
		label.setContentDisplay(ContentDisplay.TOP);
		label.setText(video.getTitulo());
		
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
		
		label.setGraphic(new ImageView(thumbnail));
		
		return label;
	}
	
	// Crear una miniatura en nuestro formato de menor tamaño
	private Label createSmallVideoThumbnail(Video video, int width, int height) {
		Label label = new Label();	
		label.setId(video.getURL());
		label.setMaxWidth(width);
		label.setMaxHeight(height);
		label.setPrefWidth(width);
		label.setPrefHeight(height);
		label.setAlignment(Pos.CENTER);
		label.setContentDisplay(ContentDisplay.TOP);
		label.setText(video.getTitulo());
		label.setStyle("-fx-font-weight: bold;");
		
		// Extraemos la miniatura del componente proporcionado
		// Hacemos una conversión de ImageIcon (Swing) a ImageView (JavaFX) con SwingFXUtils
		ImageIcon icon = videoWeb.getSmallThumb(video.getURL());
		BufferedImage bi = new BufferedImage(
			    icon.getIconWidth(),
			    icon.getIconHeight(),
			    BufferedImage.TYPE_INT_RGB);
		Graphics g = bi.createGraphics();
		icon.paintIcon(null, g, 0,0);
		g.dispose();
		Image thumbnail = SwingFXUtils.toFXImage(bi, null);
		label.setGraphic(new ImageView(thumbnail));
		return label;
		
	}
	// Carga las listas actuales del usuario al combobox que contendrá dichas listas
	private void loadMyListsComboBox() {
		List<ListaVideos> playlists = controller.getUsuarioActual().getListas();
		
		ObservableList<String> playlistsTitles = FXCollections.observableList(playlists.stream()
																			.map(ListaVideos::getNombre) // Obtenemos los títulos de las listas
																			.collect(Collectors.toList()) // Lo convertimos al formato esperado
																			); 
		myListsComboBox.setItems(playlistsTitles);
	}
	
	// Cargar la lista pasada de parámetro (su título) al JFXlistView del panel de mis listas
	private void loadListToListView(String title) {	
		ListaVideos list = controller.getListaVideos(title);
		if (list == null) return;
		
		myListsList.setItems(list.getVideos().stream() 
											.map(v -> createSmallVideoThumbnail(v, 120, 70)) // Creamos una label a partir del vídeo
											.collect(Collectors.toCollection(FXCollections::observableArrayList)));  // Convertimos al formato esperado

		// Si la lista no contiene ningún vídeo, no permitimos reproducir la lista
		if (myListsList.getItems().size() == 0) myListsPlay.setDisable(true);
			else myListsPlay.setDisable(false);
		
		fadeIn(myListsList);
	}
	
	// Cargar la lista pasada de parámetro a la lista especificada
	private void loadVideosToList(ListaVideos videos, JFXListView<Label> list) {
		list.setItems((videos.getVideos().stream()
				.map(v -> createVideoThumbnail(v, 120, 80)) // Creamos una label a partir del vídeo
				.peek(l -> l.setStyle("-fx-font-weight: bold")) // Le aplicamos un estilo concreto
				.collect(Collectors.toCollection(FXCollections::observableArrayList)))); // Convertimos al formado esperado
	}
	
	// Añadir vídeo a la lista actual de la ventana de mis listas
	private void addVideoToCurrentList(Video video) {
		// Comprobamos que el vídeo no esté ya en la lista
		if (controller.addVideoALista(video.getURL(), myListsComboBox.getSelectionModel().getSelectedItem())) {
			Label label = createSmallVideoThumbnail(video, 120, 70);
			myListsList.getItems().add(label);
			fadeIn(myListsList);
		}
		else showDialog("Error", "Este vídeo ya está en la lista");
	}
	
	// Reproducir todos los vídeos de la lista pasada de parámetros
	private void playAllVideosFromList(ListaVideos listToPlay) {
		// Si no hay vídeos, no hay nada que reproducir
		if (listToPlay.getVideos().size() == 0) return;
		
    	// Generamos una ventana emergente para preguntar el usuario el tiempo que quiere dejar entre vídeo y vídeo
		JFXDialogLayout dialogContent = new JFXDialogLayout();

		// Título de la ventana emergente
		Text dialogTitle = new Text("Reproducir lista de vídeos");
		dialogTitle.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");
		dialogContent.setHeading(dialogTitle);
		
		// Contenido de la ventana emergente
		VBox dialogBody = new VBox();
		dialogBody.setSpacing(10);
		
		Text dialogMessage = new Text("Introduzca el intervalo entre vídeo y vídeo (en segundos)");
		
		// TextField para introducir el intervalo de tiempo entre vídeo y vídeo
		JFXTextField interval = new JFXTextField();
		interval.getStyleClass().add("jfxtextfield");
		interval.setPrefWidth(50);
		interval.setMaxWidth(interval.getPrefWidth());
		
		// Permitimos únicamente números en el textfield
		UnaryOperator<Change> filter = change -> {
		    String text = change.getText();		    
		    if (text.matches("[0-9]*")) {
		        return change;
		    }
		    return null;
		};
		TextFormatter<String> textFormatter = new TextFormatter<>(filter);
		interval.setTextFormatter(textFormatter);
		
		dialogBody.getChildren().addAll(dialogMessage, interval);
		
		dialogContent.setBody(dialogBody);
		dialogContent.setStyle(DIALOG_LABEL_STYLE);

		// Botones de la ventana emergente
		JFXButton cancel = new JFXButton("Cancelar");
		cancel.getStyleClass().addAll("jfxbutton", "jfxdialogbutton");
		cancel.setPrefSize(DIALOG_BUTTON_WIDTH, DIALOG_BUTTON_HEIGHT);
		cancel.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
		dialogContent.setActions(cancel);
		
		JFXButton accept = new JFXButton("Aceptar");
		accept.getStyleClass().addAll("jfxbutton", "jfxdialogbutton");
		accept.setPrefSize(DIALOG_BUTTON_WIDTH, DIALOG_BUTTON_HEIGHT);
		accept.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
		
		dialogContent.setActions(accept, cancel);		
		JFXDialog dialog = new JFXDialog(rootStackPane, dialogContent, JFXDialog.DialogTransition.BOTTOM);
		
		cancel.setOnAction(e -> dialog.close()); 
		
		accept.setOnAction(e -> {
			dialog.close();		
			// No comprobamos que el texto incluya únicamente números porque ya hemos prohibido que se introduzca algo que no sea un número
			if (interval.getText().equals("") || Integer.parseInt(interval.getText()) == 0) {
				showDialog("Error", "No se ha introducido un intervalo o no es un intervalo válido");
			} else {
				// Obtenemos los vídeos de la lista a reproducir y mediante un timer vamos reproduciendo el siguiente vídeo
				// Utilizamos un constructor de copia puesto que en el caso de que reproduzcamos la lista de recientes, esta se va a modificar
				Iterator<Video> it = new LinkedList<Video>(listToPlay.getVideos()).iterator();
				showVideoDialog(it.next(), "videoDialog"); 
				
				// Creamos una tarea para nuestro temporizador global
				TimerTask timerTask = new TimerTask() {
		            @Override
		            public void run() {
		            	// Como estamos usando diálogos sobre toda la interfaz, miramos si se está reproduciendo un vídeo
		            	// Si cerramos el vídeo, el temporizador no se dará cuenta hasta su siguiente llamada	            	
		            	Node frontItem = rootStackPane.getChildren().get(rootStackPane.getChildren().size() - 1);
		            	if (frontItem.getId().equals("videoDialog")) {
		            		// Si se está reproduciendo un vídeo, lo cerramos y cargamos el siguiente
		            		((JFXDialog) frontItem).close();
		            		videoWeb.cancel();
		            		// Vemos si quedan vídeos por reproducir
		            		if (it.hasNext()) {
		            			// Puesto que un timer se ejecuta en un hilo diferente, no se puede modificar la aplicación JavaFX desde este
		            			// Tenemos que hacer una llamada a runLater para que sea posible mostar un diálogo de vídeo
		            			Platform.runLater(new Runnable() {
		            			    @Override
		            			    public void run() {
		            			    	showVideoDialog(it.next(), "videoDialog");
		            			    }
		            			});
		            		} 
		            	} else {
		            		// Sino, es que el usuario ha salido de la reproducción, por lo que finalizamos el timer
		            		stopGlobalTimer();
		            	}
		            }
		        };
				startGlobalTimer(timerTask, Integer.parseInt(interval.getText())*1000);
			}
		});
	
		dialog.show();    	
	}
	
	// Asigna a un botón una hotkey. View especifica el objeto visual sobre el que se aplicará (fuera
	// de ese objeto, no tendrá efecto.
	private static void assignKeyToButton(Node view, KeyCode key, ButtonBase button) {
		view.setOnKeyPressed(e -> {
            if (e.getCode() == key) {
            	System.out.println("Fired: " + button.getId());
                button.fire();
            }	       
	    });	
	}
	
	// Asigna a un botón una la hotkey INTRO. View especifica el objeto visual sobre el que se aplicará (fuera
	// de ese objeto, no tendrá efecto.
	private static void assignEnterKeyToButton(Node view, ButtonBase button) {
		assignKeyToButton(view, KeyCode.ENTER, button);
	}
	
	// Mostramos una ventana emergente felicitando al usuario si es premium y si es su cumpleaños
	private void checkForUserBirthday() {
		Usuario currentUser = controller.getUsuarioActual();
		if (currentUser.isPremium()) {
			LocalDate userBirthday = currentUser.getFechaNac();
			LocalDate today = LocalDate.now();
			if (userBirthday.getDayOfMonth() == today.getDayOfMonth() && userBirthday.getMonth().equals(today.getMonth())) {
				showDialog("¡Felicidades!", "Acorde a tus datos introducidos en el sistema, ¡hoy es tu cumpleaños!");
			}
		}
	}
	
	// Carga todos los filtros del sistema al combobox correspondiente
	private void loadFilters() {
		Set<String> filters = controller.getNombresFiltros();
		// Convertimos los elementos al formato esperado del ComboBox
		ObservableList<String> filterTitles = FXCollections.observableList(filters.stream()
				.collect(Collectors.toList())
				);

		exploreFilters.setItems(filterTitles);
		// Ponemos como seleccionado el filtro actualmente aplicado por el usuario
		exploreFilters.setValue(controller.getUsuarioActual().getFiltro().getNombre());
	}
	
	// Eliminamos la lista de vídeos pasada de parámetro y refrescamos todos los elementos y botones relacionados
	// de la ventana de mis listas
	private void deleteVideoListAndRefresh(String listTitle) {
    	controller.removeListaVideos(listTitle);      
    	
    	loadMyListsComboBox();    	
		myListsEdit.setDisable(true);
		myListsPlay.setDisable(true);
		myListsDelete.setDisable(true);
		myListsList.getItems().clear();	
	}
	
	// Manejo de la clase Timer en la aplicación, para reproducir listas de vídeos
	// Por la estructura de la aplicación, solo puede haber un timer ejecutándose a la vez
	private void startGlobalTimer(TimerTask task, int delay) {
		// Pseudo "singleton"
		if (globalTimer != null) {
			globalTimer.cancel();
		}
		globalTimer = new Timer();
		globalTimer.schedule(task, delay, delay);
	}
	
	private void stopGlobalTimer() {
		if (globalTimer != null) globalTimer.cancel();
		globalTimer = null;
	}
}	