package application.view;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
public class ViewController {

	private double oldHeight = 0;
	private double oldWidth = 0;
	private double xOffset = 0; 
	private double yOffset = 0;
	
	/* STACKPANE EXTERIOR PARA JFXDIALOG */
    @FXML
    private StackPane rootStackPane;
	
    /* BORDERPANE EXTERIOR */
    @FXML
    private BorderPane rootBorderPane;
	
	
	/* BOTONES DE LA TOPBAR */
    @FXML
    private JFXButton minimizeButton;

    @FXML
    private JFXButton maximizeButton;
    
    @FXML
    private JFXButton closeButton;

    
    /* BOTONES DE LA SIDEBAR */
    @FXML
    private JFXButton mislistas;

    @FXML
    private JFXButton premium;

    @FXML
    private JFXButton explorar;

    @FXML
    private JFXButton recientes;

    @FXML
    private JFXButton login;

    @FXML
    private JFXButton nuevalista;
    
    /* STACKPANE PARA CONTENER TODAS LAS VENTANAS */
    @FXML
    private StackPane stackpane;
    

    @FXML
    private AnchorPane testwindow; 
    
    /* VENTANA DE LOGIN */ 
    @FXML
    private VBox loginView; // Contenedor padre de la ventana de login
    
    @FXML
    private JFXButton loginButton;

    @FXML
    private JFXTextField loginNick;
    
    @FXML
    private JFXPasswordField loginPassword;
    
    @FXML
    private Label registerLabel;
    
    /* VENTANA DE REGISTRO */
    @FXML
    private VBox registerView; // Contenedor padre de la ventana de registro

    @FXML
    private JFXTextField registerNick;

    @FXML
    private JFXPasswordField registerPassword;

    @FXML
    private JFXPasswordField registerPasswordRepeat;

    @FXML
    private JFXTextField registerName;

    @FXML
    private JFXTextField registerSurname;

    @FXML
    private JFXDatePicker registerDatePicker;

    @FXML
    private JFXTextField registerEmail;

    @FXML
    private JFXButton registerRegister;

    @FXML
    private JFXButton registerCancel;
   
    
    // Manejo de eventos
    
    @FXML
    void openLoginView(ActionEvent event) {
    	Node oldFront = stackpane.getChildren().get(stackpane.getChildren().size() - 1);
    	oldFront.setDisable(true);
    	oldFront.setVisible(false);
    	
    	loginView.setDisable(false);
    	loginView.setVisible(true);
    	loginView.toFront();
    	
    }

    @FXML
    void openExplorarView(ActionEvent event) {
    	Node oldFront = stackpane.getChildren().get(stackpane.getChildren().size() - 1);
    	oldFront.setDisable(true);
    	oldFront.setVisible(false);
    	
    	testwindow.setDisable(false);
    	testwindow.setVisible(true);
    	testwindow.toFront();
    }

    @FXML
    void openMislistasView(ActionEvent event) {

    }

    @FXML
    void openRecientesView(ActionEvent event) {

    }

    @FXML
    void openNuevalistaView(ActionEvent event) {

    }

    @FXML
    void openPremiumView(ActionEvent event) {

    }
    
    @FXML
    void loginEnter(ActionEvent event) {
    	
    	if (loginNick.getText().equals("") || loginPassword.getText().equals("")) {
            String title = "Login inválido";         
            String content = "El nombre de usuario y/o la contraseña no puede ser un campo vacío";       
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
    	} else {
    		//TODO: la parte graciosa
    	}
    }
    	
    @FXML
    void openRegisterView(MouseEvent event) {
    	Node oldFront = stackpane.getChildren().get(stackpane.getChildren().size() - 1);
    	oldFront.setDisable(true);
    	oldFront.setVisible(false);
    	
    	registerView.setDisable(false);
    	registerView.setVisible(true);
    	registerView.toFront(); 	
    }

    @FXML
    void registerCancel(ActionEvent event) {
    	//TODO: Hace lo mismo que el método openLoginView pero lo separo por si queremmos hacer algo diferente luego
    	Node oldFront = stackpane.getChildren().get(stackpane.getChildren().size() - 1);
    	oldFront.setDisable(true);
    	oldFront.setVisible(false);
    	
    	loginView.setDisable(false);
    	loginView.setVisible(true);
    	loginView.toFront();
    }

    @FXML
    void registerUser(ActionEvent event) {
    	//TODO: Registrar un usuario
    }

    @FXML
    void minimizeWindow(ActionEvent event) {
    	((Stage)((JFXButton)event.getSource()).getScene().getWindow()).setIconified(true);
    	
    }

    @FXML
    void maximizeWindow(ActionEvent event) {
    	//System.out.println("boop");
    	Stage stg = ((Stage)((JFXButton)event.getSource()).getScene().getWindow());
    	
    	if (stg.isMaximized()) {
    		stg.setWidth(oldWidth);
    		stg.setHeight(oldHeight);
    		stg.setMaximized(false);
    	} else {
    		oldWidth = stg.getWidth();
    		oldHeight = stg.getHeight();
    		stg.setMaximized(true);
    	}
    }

    @FXML
    void closeWindow(ActionEvent event) {
    	((Stage)((JFXButton)event.getSource()).getScene().getWindow()).close();
    }
    
    @FXML
    void saveWindowPosition(MouseEvent event) {
    	//System.out.println("triggered");
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
}