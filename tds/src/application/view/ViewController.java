package application.view;

import java.awt.Graphics;
import java.awt.ScrollPane;
import java.awt.image.BufferedImage;
import java.util.Set;

import javax.swing.ImageIcon;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import application.controller.AppVideo;
import application.model.Usuario;
import application.model.Video;
import javafx.animation.FadeTransition;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import com.jfoenix.controls.JFXMasonryPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import tds.video.VideoWeb;
public class ViewController {

	private double oldHeight = 0;
	private double oldWidth = 0;
	private double xOffset = 0; 
	private double yOffset = 0;
	private AppVideo controller = AppVideo.getUnicaInstancia();
	boolean isProfileOpen = false;
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
    private JFXButton profileUpdate, profileLogout;
    
    /* VENTANA DE EXPLORAR */
    @FXML
    private BorderPane exploreView; // Contenedor de la vista de explorar
    @FXML
    private JFXMasonryPane exploreContent;
    @FXML
    private JFXTextField exploreTitle;
    @FXML
    private JFXButton exploreSearch, exploreClear;
    
    // Manejo de eventos  
    @FXML
    void openLoginView(ActionEvent event) {
    	// Ocultamos el elemento que hubiese en el frente
    	Node oldFront = stackpane.getChildren().get(stackpane.getChildren().size() - 1);
    	oldFront.setDisable(true);
    	oldFront.setVisible(false);
    	
    	// Abrimos una ventana u otra en función del estado
    	if (!isProfileOpen) {
        	loginView.setDisable(false);
        	loginView.setVisible(true);
        	loginView.toFront();
        	hideLoginLabels();
        	fadeIn(loginView);
    	} else {
    		profileView.setDisable(false);
        	profileView.setVisible(true);
        	profileView.toFront();
        	fadeIn(profileView);
    	}
    }

    @FXML
    void openExplorarView(ActionEvent event) {
    	// Ocultamos el elemento que hubiese en el frente
    	Node oldFront = stackpane.getChildren().get(stackpane.getChildren().size() - 1);
    	oldFront.setDisable(true);
    	oldFront.setVisible(false);
    	
    	exploreView.setDisable(false);
    	exploreView.setVisible(true);
    	exploreView.toFront();
    	fadeIn(exploreView);
    }

    @FXML
    void openMislistasView(ActionEvent event) {
    	//TODO
    }

    @FXML
    void openRecientesView(ActionEvent event) {
    	//TODO
    }

    @FXML
    void openNuevalistaView(ActionEvent event) {
    	//TODO
    }

    @FXML
    void openPremiumView(ActionEvent event) {
    	//TODO
    }
    
    @FXML
    void loginEnter(ActionEvent event) {
    	// Comprobamos que ambos campos hayan sido introducidos
    	if (loginNick.getText().equals("") || loginPassword.getText().equals("")) {
    		// Si no, mostramos la etiqueta de "*Campo obligatorio" correspondiente
    		if (loginNick.getText().equals("")) {
    			loginLabelNick.setVisible(true);
    		}
    		
    		if (loginPassword.getText().equals("")) {
    			loginLabelPassword.setVisible(true);
    		}
    	} else {
    		// Hay datos introducidos, intentamos identificarnos.
    		if (controller.verificarUsuario(loginNick.getText(), loginPassword.getText())) {
		
    			// Login válido
    			openProfileView();
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
    void openRegisterView(MouseEvent event) {
    	// Ocultamos el elemento que hubiese en el frente
    	Node oldFront = stackpane.getChildren().get(stackpane.getChildren().size() - 1);
    	oldFront.setDisable(true);
    	oldFront.setVisible(false);
    	// Traemos la ventana de registro al frente
    	registerView.setDisable(false);
    	registerView.setVisible(true);
    	registerView.toFront(); 	
    	// Ocultamos todas las etiquetas de "*Campo obligatorio" del registro
    	hideRegisterLabels();
    	fadeIn(registerView);
    }

    @FXML
    void registerCancel(ActionEvent event) {
    	//TODO: HACE LO MISMO QUE openLoginView pero lo dejo separado de mientras
    	// Ocultamos el elemento que hubiese en el frente
    	Node oldFront = stackpane.getChildren().get(stackpane.getChildren().size() - 1);
    	oldFront.setDisable(true);
    	oldFront.setVisible(false);
    	// Traemos la ventana de login al frente
    	loginView.setDisable(false);
    	loginView.setVisible(true);
    	loginView.toFront();
    	fadeIn(loginView);
    }
    

    @FXML
    void registerUser(ActionEvent event) {
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

    @FXML
    void minimizeWindow(ActionEvent event) {
    	((Stage)((JFXButton)event.getSource()).getScene().getWindow()).setIconified(true);  	
    }

    @FXML
	void maximizeWindow(ActionEvent event) {
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
    void closeWindow(ActionEvent event) {
    	((Stage)((JFXButton)event.getSource()).getScene().getWindow()).close();
    	System.exit(0);
    }
    
    @FXML
    void saveWindowPosition(MouseEvent event) {
        xOffset = event.getSceneX();
        yOffset = event.getSceneY();
    }
    
    @FXML
    void dragWindow(MouseEvent event) {
    	//System.out.println(event.getSource().toString());
    	
    	Region rg = (Region) event.getSource();
    	rg.getScene();
    	Stage stg = (Stage) rg.getScene().getWindow();
        stg.setX(event.getScreenX() - xOffset);
        stg.setY(event.getScreenY() - yOffset);
    }
    
	void openProfileView() {
		Usuario usuarioActual = controller.getUsuarioActual();
		isProfileOpen = true;
		// Escondemos el elemento anterior
		Node oldFront = stackpane.getChildren().get(stackpane.getChildren().size() - 1);
		oldFront.setDisable(true);
		oldFront.setVisible(false);

		// Hacemos la vista visible con una transición
		profileView.setVisible(true);
		profileView.setDisable(false);
		profileView.toFront();
		fadeIn(profileView);
		
		// Actualizamos elementos
		login.setText("Mi perfil");
		profileEmail.setPromptText(usuarioActual.getEmail());
		profileEmail.setText("");
		profileNick.setText(usuarioActual.getLogin());
		profileDatePicker.setPromptText(usuarioActual.getFechaNac().toString());
		profileDatePicker.setAccessibleText("");
		profileTitle.setText("Bienvenido, " + usuarioActual.getNombre());
	}
      
    @FXML
    void profileLogout(ActionEvent event) {
    	controller.salirUsuario();
    	isProfileOpen = false;
    	login.setText("Log in");
		explorar.setDisable(true);
		mislistas.setDisable(true);
		recientes.setDisable(true);
		nuevalista.setDisable(true);
    	openLoginView(null);
    }

    @FXML
    void profileUpdate(ActionEvent event) {
    	// Actualiza todos los campos introducidos.
    	// La comprobación de si un campo contiene información o no se delega al controlador.
    	// Asimismo, si la contraseña nueva no se escribe dos veces correctamente, no se realizan los cambios
    	if (profilePassword.getText().equals(profilePassRepeat.getText())) {
    		controller.modificarUsuarioActual(profileEmail.getText(), profilePassword.getText(), profileDatePicker.getValue());
    		//showDialog("Boop!", "Todos los cambios han sido actualizados :)");
    		openProfileView();
    	} else {
    		showDialog("Cambios inválidos", "Las contraseñas introducidas no coinciden");
    	}
    	
    	
    }

    @FXML
    void exploreSearch(ActionEvent event) {
    	//TODO
    	//controller.buscar(cadena)
    	Set<Video> videos = controller.buscar(exploreTitle.getText());
   
    	for (Video video : videos) {
    		Label element = new Label();
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
    		            	showVideoDialog(video);
    		            }
    		        }
    		    }
    		});
    		element.setContentDisplay(ContentDisplay.TOP);
    		element.setText(video.getTitulo());
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
    void exploreClear(ActionEvent event) {
    	exploreContent.getChildren().clear();
    }
    
    // Funcionalidad auxiliar
    
    // Ocultar todas las labels de "*Este campo es obligatorio" de la vista del login
    void hideLoginLabels() {
    	loginLabelNick.setVisible(false);
    	loginLabelPassword.setVisible(false);
    }
    
    // Ocultar todas las labels de "*Este campo es obligatorio" de la vista de registro
    void hideRegisterLabels() {
    	registerLabelNick.setVisible(false);
    	registerLabelPassword.setVisible(false);
    	registerLabelPassRepeat.setVisible(false);
    	registerLabelName.setVisible(false);
    	registerLabelDate.setVisible(false);
    	
    }
    
    // Generar un JFXDialog con un vídeo pasado de parámetro
    void showVideoDialog(Video video) {
        JFXDialogLayout dialogContent = new JFXDialogLayout(); 
        dialogContent.setHeading(new Text(video.getTitulo()));       
        SwingNode videoComponent = new SwingNode();
        videoComponent.setContent(videoWeb);
        
        VBox body = new VBox();
        body.setSpacing(5.0);
        body.getChildren().addAll(videoComponent, new Text("Reproducciones: " + video.getNumReproducciones()));
        dialogContent.setBody(body);
        dialogContent.setStyle("-fx-font: 14 system;");
        
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
                videoWeb.cancel();
            }
        });
        //controller.reproducir(video); <---- Descomentar cuando tengamos el cargador de vídeos
        									// Porque estoy injertando vídeos de momento y si los modifico puede que se líe
        videoWeb.playVideo(video.getURL());
        dialog.show();   	
    }
    
    // Generar un JFXDialog textual sobre la aplicación
    void showDialog(String title, String content) {    
        JFXDialogLayout dialogContent = new JFXDialogLayout(); 
        dialogContent.setHeading(new Text(title));       
        dialogContent.setBody(new Text(content));
        dialogContent.setStyle("-fx-font: 14 system;");
        
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
    void fadeIn(Node node) {
		FadeTransition ft = new FadeTransition(Duration.millis(200), node);
		ft.setFromValue(0.0);
		ft.setToValue(1.0);
		ft.play();
    }
}