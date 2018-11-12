package application.controller;
import com.jfoenix.controls.JFXButton;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;

public class AppVideo {

	private double oldheight = 0;
	private double oldwidth = 0;
	
    @FXML
    private JFXButton minimize;

    @FXML
    private JFXButton maximize;

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
    private JFXButton close;

    @FXML
    private JFXButton nuevalista;


    @FXML
    void minimizeWindow(ActionEvent event) {
    	((Stage)((JFXButton)event.getSource()).getScene().getWindow()).setIconified(true);
    	
    }

    @FXML
    void maximizeWindow(ActionEvent event) {
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
