package application.controller;
import com.jfoenix.controls.JFXAlert;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
public class AppVideo {

	private double oldheight = 0;
	private double oldwidth = 0;
	
	
    /* BORDERPANE EXTERIOR */
    @FXML
    private BorderPane rootborderpane;
	
	
	/* BOTONES DE LA TOPBAR */
    @FXML
    private JFXButton minimize;

    @FXML
    private JFXButton maximize;
    
    @FXML
    private JFXButton close;

    
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
    
    // Contenedor de la ventana de login
    @FXML
    private VBox loginview;
    
    @FXML
    private JFXButton login_button;

    @FXML
    private JFXTextField login_nick;
    
    @FXML
    private JFXPasswordField login_pass;

    // Manejo de eventos
    
    @FXML
    void openlogin(ActionEvent event) {
    	Node old_front = stackpane.getChildren().get(stackpane.getChildren().size() - 1);
    	old_front.setDisable(true);
    	old_front.setVisible(false);
    	
    	loginview.setDisable(false);
    	loginview.setVisible(true);
    	loginview.toFront();
    	
    }

    @FXML
    void openexplorar(ActionEvent event) {
    	
    	Node old_front = stackpane.getChildren().get(stackpane.getChildren().size() - 1);
    	old_front.setDisable(true);
    	old_front.setVisible(false);
    	old_front.toBack();
    	
    	testwindow.setDisable(false);
    	testwindow.setVisible(true);
    	testwindow.toFront();
    	//testwindow.toFront();
    	//rootborderpane.setCenter(null);
    }

    @FXML
    void openmislistas(ActionEvent event) {

    }

    @FXML
    void openrecientes(ActionEvent event) {

    }

    @FXML
    void opennuevalista(ActionEvent event) {

    }

    @FXML
    void openpremium(ActionEvent event) {

    }
    
    @FXML
    void login_enter(ActionEvent event) {
    	
    	if (login_nick.getText().equals("") || login_pass.getText().equals("")) {
            String title = "Login inválido";         
            String content = "El nickname y/o la contraseña no puede ser un campo vacío";       
            JFXDialogLayout dialogContent = new JFXDialogLayout();
          
            dialogContent.setHeading(new Text(title));       
            dialogContent.setBody(new Text(content));
            dialogContent.setStyle("-fx-font: 14 system;");
            
            JFXButton close = new JFXButton("Cerrar");          
            close.setStyle("-fx-background-color: #f6444f; -fx-text-fill: #FFFFFF; -fx-font: 14 system;");
            close.setPrefSize(100, 25);
            close.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
            
            dialogContent.setActions(close);
            
            JFXDialog dialog = new JFXDialog(stackpane, dialogContent, JFXDialog.DialogTransition.BOTTOM);
            
            close.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent __) {
                    dialog.close();
                }
            });
            dialog.show();
    	} else {
    		//TODO: la parte graciosa jeje
    	}
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
    		stg.setWidth(oldwidth);
    		stg.setHeight(oldheight);
    		stg.setMaximized(false);
    	} else {
    		oldwidth = stg.getWidth();
    		oldheight = stg.getHeight();
    		stg.setMaximized(true);
    	}
    }

    @FXML
    void closeWindow(ActionEvent event) {
    	((Stage)((JFXButton)event.getSource()).getScene().getWindow()).close();
    }
}
