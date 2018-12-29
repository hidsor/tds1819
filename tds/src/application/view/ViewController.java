package application.view;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
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
import application.model.ListaVideos;
import application.model.Usuario;
import application.model.Video;
import javafx.animation.FadeTransition;
import javafx.animation.RotateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import com.jfoenix.controls.JFXMasonryPane;
import com.jfoenix.controls.JFXNodesList;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import tds.video.VideoWeb;
import umu.tds.videos.ComponenteBuscadorVideos;

public class ViewController implements Initializable {
	////////////////////////////
	/* ATRIBUTOS Y CONSTANTES */
	////////////////////////////
	private double oldHeight = 0;
	private double oldWidth = 0;
	private double xOffset = 0; 
	private double yOffset = 0;
	private AppVideo controller;
	private ComponenteBuscadorVideos buscador;
	private boolean isProfileOpen;
	private static VideoWeb videoWeb;
    boolean areOptionsOpened;
	
	private final static String DIALOG_BUTTON_STYLE =	"-fx-background-color: #f6444f;"
														+ " -fx-text-fill: #FFFFFF;"
														+ " -fx-font: 14 system;"
														+ " -fx-font-weight: bold;";
	private final static String DIALOG_LABEL_STYLE = "-fx-text-fill: #000000;"
														+ " -fx-font: 14 system;";
	private final static String DIALOG_JFXTEXTFIELD_STYLE = "-jfx-focus-color: #f51827;"
														+ " -jfx-unfocus-color: #4d4d4d;"
														+ " -fx-font: 14 system;";

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
    private JFXButton mislistas, premium, explorar, recientes, login;
    
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
    private JFXMasonryPane exploreContent; // Contenedor de los v�deos buscados
    @FXML
    private TextField exploreTitle;
    @FXML
    private JFXButton exploreSearch, exploreClear;
    @FXML 
    private JFXListView<String> tagsView = new JFXListView<String>();	// Para visualizar todas las etiquetas disponibles
    @FXML
    private JFXListView<String> searchTagsView = new JFXListView<String>(); // Visualizar nuestras etiquetas de b�squeda utilizadas
 
    
    /* VENTANA DE NUEVA LISTA */
    @FXML
    private BorderPane myListsView; // Contenedor de la vista de listas
    @FXML
    private StackPane myListsCenterRegion, myListsRightRegion;
    @FXML
    private HBox myListsMainSideBar, myListsSecondarySideBar;
    @FXML
    private TextField myListsTitle, myListsNewListTitle;
    @FXML
    private JFXMasonryPane myListsContent;
    @FXML
    private ComboBox<String> myListsComboBox;
    @FXML
    private JFXNodesList myListsOptions;
    @FXML
    private JFXButton myListsSearch, myListsClear, myListExpand, myListsEdit, myListsPlay, myListsDelete, myListsNew;
    @FXML
    private JFXListView<Label> myListsList;
    
	/////////////////
	/* CONSTRUCTOR */
	/////////////////	
	public ViewController() {
		controller = AppVideo.getUnicaInstancia();
		buscador = new ComponenteBuscadorVideos();
		videoWeb = new VideoWeb();
    	buscador.addVideosListener(controller);
		isProfileOpen = false;	
	}
	
    ///////////////////////
    /* MANEJO DE EVENTOS */
    ///////////////////////
	
	/* ABRIR VENTANAS */
    @FXML
    public void openLoginView(ActionEvent event) {
    	// Abrimos una ventana u otra en funci�n del estado
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
    	exploreContent.getChildren().clear();
    	fadeIn(exploreView);
    }

    @FXML
    public void openMislistasView(ActionEvent event) {
    	
    	// Ocultamos el elemento que hubiese en el frente
    	Node oldFront = stackpane.getChildren().get(stackpane.getChildren().size() - 1);
    	oldFront.setDisable(true);
    	oldFront.setVisible(false);
    		
    	// Traemos la ventana al frente y la hacemos visible
    	myListsView.setDisable(false);
    	myListsView.setVisible(true);
    	myListsView.toFront();
    	
    	// Limpiamos todos los elementos de la vista y cargamos las listas
		myListsContent.getChildren().clear();
		loadMyListsComboBox();
		myListsEdit.setDisable(true);
		myListsPlay.setDisable(true);
		myListsDelete.setDisable(true);
		
		myListsComboBox.getSelectionModel().clearSelection();
		
    	fadeIn(myListsView);
    }

    @FXML
    public void openRecientesView(ActionEvent event) {
    	//TODO
    }

    @FXML
    public void openPremiumView(ActionEvent event) {
    	//TODO
    }
        	
    @FXML
    public void openRegisterView(MouseEvent event) {
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
    	
    	// Traemos la ventana al frente y la hacemos visible
    	registerView.setDisable(false);
    	registerView.setVisible(true);
    	registerView.toFront(); 	
    	
    	hideRegisterLabels();
    	fadeIn(registerView);
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
		profileEmail.clear();
		profileDatePicker.setValue(null);
	}
      
    /* FUNCIONALIDAD VENTANA LOGIN */
    @FXML
    // Validaci�n de la cuenta introducida en el login
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
    			// Login v�lido
    			openProfileView();
    			// Desbloqueamos la funcionalidad disponible para usuarios
    			explorar.setDisable(false);
    			mislistas.setDisable(false);
    			recientes.setDisable(false);
    		} else {
    			// Login inv�lido
    			showDialog("Login inv�lido", "El nombre y/o la contrase�a son incorrectas");
    		}
    	}
    }

    /* FUNCIONALIDAD VENTANA DE REGISTRO */
    @FXML
    // Registrar un usuario
    public void registerUser(ActionEvent event) {
    	// Comprobamos que ninguno de los campos obligatorios est� vac�o.
    	// Si alguno lo est�, activamos su etiqueta de "*Campo obligatorio"
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
    	// Comprobamos que ambas contrase�as sean la misma
    	if (valid && ! registerPassword.getText().equals(registerPasswordRepeat.getText())) {
    		valid = false;	
    		showDialog("Registro inv�lido", "Las contrase�as introducidas no coinciden");
    		
    	}
    	// Si todo ha ido bien, intentamos registrar al usuario
    	if (valid) {
			if (controller.registrarUsuario(registerNick.getText(), registerPassword.getText(), registerName.getText(),
					registerSurname.getText(), registerDatePicker.getValue(), registerEmail.getText())) {
    			// Registro v�lido
    			openProfileView();
    			// Desbloqueamos la funcionalidad disponible para usuarios
    			explorar.setDisable(false);
    			mislistas.setDisable(false);
    			recientes.setDisable(false);			
    		} else {
    			// Registro inv�lido (shouldn't happen)
    			showDialog("Registro inv�lido", "No se ha podido registrar al usuario");
    		}
    	}
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
    
    /* FUNCIONALIDAD VENTANA DE PERFIL */
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
		
    	openLoginView(event);
    }

    @FXML
    public void loadVideos(ActionEvent event) {
    	FileChooser fileChooser = new FileChooser();
    	fileChooser.setTitle("Abrir XML con videos");
    	try {
	    	File file = fileChooser.showOpenDialog(( (Node) event.getSource()).getScene().getWindow());
	    	buscador.buscarVideo(file.getAbsolutePath());
	    	showDialog("Boop!", "V�deos cargados :)");
    	}
    	catch (Exception e) {
    		showDialog("Error", "No se ha elegido ning�n fichero o el fichero indicado es inv�lido");
			//System.out.println("Error: Abra un archivo valido");
		}
    }
     
    @FXML
	// Actualizar todos los campos introducidos en la ventana de perfil.
    public void profileUpdate(ActionEvent event) {
    	// La comprobaci�n de si un campo contiene informaci�n o no se delega al controlador.
    	// Asimismo, si la contrase�a nueva no se escribe dos veces correctamente, no se realizan los cambios
		if (profilePassword.getText().equals(profilePassRepeat.getText())) {
			controller.modificarUsuarioActual(profileEmail.getText(), profilePassword.getText(),
					profileDatePicker.getValue());
			// Actualizamos los cambios volviendo a abrir la ventana de perfil
    		openProfileView();
    	} else {
    		showDialog("Cambios inv�lidos", "Las contrase�as introducidas no coinciden");
    	}	
    }

    /* FUNCIONALIDAD VENTANA DE EXPLORAR */
    @FXML
    // Busqueda de v�deos
    public void exploreSearch(ActionEvent event) {
    	// Borramos el resultado de la busqueda anterior
    	//exploreContent.getChildren().clear();
    	
    	// Hecho esto, buscamos
    	Set<Video> videos = controller.buscarVideos(exploreTitle.getText());
    	boolean ifExists = false;
    	for (Video video : videos) {
    		// Comprobamos que el v�deo que queremos mostrar no est� ya en la vista
    		for (Node child : exploreContent.getChildren()) {
    			Label l = (Label) child;
    			if (l.getText().equals(video.getTitulo())) {
    				ifExists = true;
    				break;
    			}
    		}
    		if (ifExists) continue;
    		
    		// Si no est�, lo a�adimos a la b�squeda actual
    		Label element = createVideoThumbnail(video, new EventHandler<MouseEvent>() {
    		    @Override
    		    public void handle(MouseEvent mouseEvent) {
    		        if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
    		            if(mouseEvent.getClickCount() == 2){
    		            	// Si se hace doble click sobre la miniatura del v�deo, abrimos una ventana para reproducirlo
    		            	controller.reproducir(video.getURL());
    		            	showVideoDialog(video);
    		            }
    		        }
    		    }
    		});
    		exploreContent.getChildren().add(element);
    	}
    }    
    
    @FXML
    // Limpiar la b�squeda actual de v�deos
    public void exploreClear(ActionEvent event) {
    	exploreContent.getChildren().clear();
    }
    /* FUNCIONALIDAD VENTANA DE MIS LISTAS */
 
    @FXML
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
    
    // Proporcionamos al usuario la interfaz necesaria para a�adir una nueva lista
    @FXML
    void addVideoList(ActionEvent event) {
    	// Ocultamos el panel lateral con las listas y las opciones
    	myListsMainSideBar.setDisable(true);
    	myListsMainSideBar.setVisible(false);
    	
    	// Mostramos el panel lateral secundario para a�adir nuevas listas
    	
    	myListsSecondarySideBar.setDisable(false);
    	myListsSecondarySideBar.setVisible(true);
    	
    	myListsNewListTitle.clear();
    	
    	fadeIn(myListsSecondarySideBar);
    }
    
    // Validamos la lista introducida
    @FXML
    void acceptNewList(ActionEvent event) {
    	
    	// Si se ha introducido un t�tulo, lo registramos
    	// Si no se ha introducido ning�n t�tulo, lo interpretamos como que el usuario no quiere crear ninguna lista
    	if (!myListsNewListTitle.getText().equals("")) {
    		controller.crearListaVideos(myListsNewListTitle.getText());
    		loadMyListsComboBox();
    		myListsComboBox.getSelectionModel().select(myListsNewListTitle.getText());   		
    		myListsEdit.setDisable(false);
    		myListsPlay.setDisable(false);
    		myListsDelete.setDisable(false);		
    	}
    	
    	// Ocultamos  el panel lateral secundario para a�adir nuevas listas
    	myListsSecondarySideBar.setDisable(true);
    	myListsSecondarySideBar.setVisible(false);
    	
    	// Mostramos el panel lateral con las listas y las opciones
    	myListsMainSideBar.setDisable(false);
    	myListsMainSideBar.setVisible(true);
    	 	
    	fadeIn(myListsMainSideBar);
    }    
    

    @FXML
    void chooseList(ActionEvent event) {
		myListsEdit.setDisable(false);
		myListsPlay.setDisable(false);
		myListsDelete.setDisable(false);
		loadListToListView(myListsComboBox.getSelectionModel().getSelectedItem());
    }
    
    @FXML
    void deleteVideoList(ActionEvent event) {
    	controller.removeListaVideos(myListsComboBox.getSelectionModel().getSelectedItem());
    	loadMyListsComboBox();
		myListsEdit.setDisable(true);
		myListsPlay.setDisable(true);
		myListsDelete.setDisable(true);
    	//TODO
    }
    
    @FXML
    void editPlaylist(ActionEvent event) {
    	//TODO
    }    

    @FXML
    void myListsClearVideos(ActionEvent event) {
    	//TODO
    }

    @FXML
    void myListsSearchVideos(ActionEvent event) {
    	//TODO
    }

    @FXML
    void playVideoList(ActionEvent event) {
    	//TODO
    }
    
    /* FUNCIONALIDAD BARRA SUPERIOR */
    @FXML
    public void minimizeWindow(ActionEvent event) {
    	((Stage)((JFXButton)event.getSource()).getScene().getWindow()).setIconified(true);  	
    }

    @FXML
    public void maximizeWindow(ActionEvent event) {
		Stage stg = ((Stage) ((JFXButton) event.getSource()).getScene().getWindow());
		// Si la ventana estaba maximizada, restauramos su tama�o anterior
		if (stg.isMaximized()) {
			stg.setWidth(oldWidth);
			stg.setHeight(oldHeight);
			stg.setMaximized(false);
		} else {
			// Sino, la maximizamos y almacenamos el tama�o anterior
			oldWidth = stg.getWidth();
			oldHeight = stg.getHeight();
			stg.setMaximized(true);
		}
	}

    @FXML
    // Cerrar la ventana y la aplicaci�n
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
 
    /* FUNCIONALIDAD AUXILIAR */  
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
    
	// Generar un JFXDialog con un v�deo pasado de par�metro
	public void showVideoDialog(Video video) {		
		
		JFXDialogLayout dialogContent = new JFXDialogLayout();

		dialogContent.setHeading(new Text(video.getTitulo()));

		SwingNode videoComponent = new SwingNode();
		videoComponent.setContent(videoWeb);

		// Contenedor de etiquetas
		HBox tags = new HBox();
		tags.setSpacing(5.0);

		// A�adimos las etiquetas al contenedor
		for (Etiqueta tag : video.getEtiquetas()) {
			addTagToPane(tag, tags);
		}

		// Contenedor de a�adir nueva etiqueta
		HBox addTags = new HBox();
		addTags.setSpacing(10.0);

		Label addTagsText = new Label("A�adir nueva etiqueta:");
		
		addTagsText.setStyle(DIALOG_LABEL_STYLE + " -fx-font-weight: bold;");

		JFXTextField addTagsTextField = new JFXTextField();
		addTagsTextField.setStyle(DIALOG_JFXTEXTFIELD_STYLE);

		addTags.getChildren().addAll(addTagsText, addTagsTextField);

		// Contenedor del reproductor de v�deos
		VBox body = new VBox();
		body.setSpacing(5.0);
		body.getChildren().addAll(videoComponent, tags, addTags,
				new Text("Reproducciones: " + video.getNumReproducciones()));

		dialogContent.setBody(body);

		// Botones de la ventana emergente
		JFXButton close = new JFXButton("Cerrar");
		close.setStyle(DIALOG_BUTTON_STYLE);
		close.setPrefSize(100, 25);
		close.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

		JFXButton add = new JFXButton("A�adir");
		add.setStyle(DIALOG_BUTTON_STYLE);
		add.setPrefSize(100, 25);
		add.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
		add.setOnMouseClicked(new EventHandler<MouseEvent>() {
    		    @Override
    		    public void handle(MouseEvent mouseEvent) {
	                Etiqueta tag =  new Etiqueta(addTagsTextField.getText());
	                if (controller.addEtiquetaVideo(tag, video)) {
	        			addTagToPane(tag, tags);     			
		        		tagsView.getItems().add(tag.getNombre());
	                }
	                addTagsTextField.clear();
    		    }
    		});

		dialogContent.setActions(add, close);

		JFXDialog dialog = new JFXDialog(rootStackPane, dialogContent, JFXDialog.DialogTransition.BOTTOM);

		close.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent __) {
				dialog.close();
				videoWeb.cancel();
			}
		});
		videoWeb.playVideo(video.getURL());
		dialog.show();
	}

	// Generar un JFXDialog textual sobre la aplicaci�n
	public void showDialog(String title, String content) {
		JFXDialogLayout dialogContent = new JFXDialogLayout();

		dialogContent.setHeading(new Text(title));
		dialogContent.setBody(new Text(content));
		dialogContent.setStyle(DIALOG_LABEL_STYLE);

		// Botones de la ventana emergente
		JFXButton close = new JFXButton("Cerrar");
		close.setStyle(DIALOG_BUTTON_STYLE);
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

	// M�todo para hacer una transici�n de un nodo pasado de par�metro
	// La duraci�n no est� parametrizada pero podr�a parametrizarse tambi�n
	public void fadeIn(Node node) {
		FadeTransition ft = new FadeTransition(Duration.millis(200), node);
		ft.setFromValue(0.0);
		ft.setToValue(1.0);
		ft.play();
	}
	
	// TODO: B�rralo si no lo usas
	// M�todo para hacer una transici�n hacia afuera de un nodo pasado de par�metro
	// La duraci�n no est� parametrizada pero podr�a parametrizarse tambi�n
	public void fadeOut(Node node) {
		FadeTransition ft = new FadeTransition(Duration.millis(200), node);
		ft.setFromValue(1.0);
		ft.setToValue(0.0);
		ft.play();
	}
	
	// Cargamos la lista con todas las etiquetas
	public void loadTags(Set<Etiqueta> savedTags) {
		tagsView.setItems(savedTags.stream()
				.map(Etiqueta::getNombre)
				.collect(Collectors.toCollection(FXCollections::observableArrayList)));
	}
	
	// A�adir una etiqueta a las etiquetas utilizadas para la b�squeda
	@FXML
	public boolean addSearchTag(MouseEvent event) {
		if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
			String tagName = tagsView.getSelectionModel().getSelectedItem();
			if (controller.addEtiquetaBusqueda(tagName)) {
				searchTagsView.getItems().add(tagName);
				return true;
			}
		}
		return false;
	}
	
	// Eliminar una etiqueta de las etiquetas utilizadas para la b�squeda
	@FXML
	public boolean removeSearchTag(MouseEvent event) {
		if (controller.isEtiquetasBusquedaEmpty())
			return false;
		if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
			String tagName = searchTagsView.getSelectionModel().getSelectedItem();
			controller.removeEtiquetaBusqueda(tagName);
			searchTagsView.getItems().remove(tagName);
			return true;
		}
		return false;
	}
	
	// A�adir una etiqueta a un contenedor pasado de par�metro
	private boolean addTagToPane(Etiqueta tag, Pane tags) {
		Label l = new Label(tag.getNombre());
		l.getStyleClass().add("videotag");
		return tags.getChildren().add(l);
	}
	
	// Crear una miniatura de v�deo en nuestro formato
	// Contiene la miniatura y su t�tulo junto al evento que se pase de par�metro
	private Label createVideoThumbnail(Video video, EventHandler<MouseEvent> e) {		
		Label label = new Label();
		
		// Propiedades de la label que contiene la miniatura y el t�tulo del v�deo
		label.setMaxWidth(200.0);
		label.setMaxHeight(150.0);
		label.setPrefWidth(200.0);
		label.setPrefHeight(150.0);
		label.setAlignment(Pos.CENTER);
		label.getStyleClass().add("videothumbnail");
		label.setOnMouseClicked(e);
		label.setContentDisplay(ContentDisplay.TOP);
		label.setText(video.getTitulo());
		
		// Extraemos la miniatura del componente proporcionado
		// Hacemos una conversi�n de ImageIcon (Swing) a ImageView (JavaFX) con SwingFXUtils
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
	// Carga las listas actuales del usuario al combobox que contendr� dichas listas
	private void loadMyListsComboBox() {
		List<ListaVideos> playlists = controller.getUsuarioActual().getListas();
		
		ObservableList<String> playlistsTitles = FXCollections.observableList(playlists.stream()
																			.map(ListaVideos::getNombre)
																			.collect(Collectors.toList())
																			);
		myListsComboBox.setItems(playlistsTitles);
	}
	
	// Cargar la lista pasada de par�metro al listView del panel de mis listas
	private void loadListToListView(String title) {
		ListaVideos list = controller.getListaVideos(title);
		
		if (list == null) return;

		EventHandler<MouseEvent> e = new EventHandler<MouseEvent>() {
		    @Override
		    public void handle(MouseEvent mouseEvent) {
		        if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
		            if(mouseEvent.getClickCount() == 2){
		            	System.out.println("clicked jeje");
		            }
		        }
		    }
		};
		myListsList.setItems(list.getVideos().stream()
											.map(v -> createVideoThumbnail(v, null))
											.collect(Collectors.toCollection(FXCollections::observableArrayList)));
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		//TODO: Si no metes nada quita el implements Initializable
	}
}