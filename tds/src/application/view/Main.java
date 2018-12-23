package application.view;
	
import javafx.application.Application;
//import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
//import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {
	//private double xOffset = 0; 
	//private double yOffset = 0;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("Root.fxml"));
			/*
		      root.setOnMousePressed(new EventHandler<MouseEvent>() {
		            public void handle(MouseEvent event) {
		                xOffset = event.getSceneX();
		                yOffset = event.getSceneY();
		            }
		        });
		        
		        //move around here
		        root.setOnMouseDragged(new EventHandler<MouseEvent>() {
		            public void handle(MouseEvent event) {
		                primaryStage.setX(event.getScreenX() - xOffset);
		                primaryStage.setY(event.getScreenY() - yOffset);
		            }
		        });
		    */
			Scene scene = new Scene(root,800,600);
			scene.getStylesheets().add(getClass().getResource("applicationred.css").toExternalForm());
			primaryStage.initStyle(StageStyle.TRANSPARENT);
			primaryStage.setScene(scene);
			primaryStage.setMinHeight(600);
			primaryStage.setMinWidth(800);
			ResizeHelper.addResizeListener(primaryStage);
			primaryStage.show();
			
		} catch(Exception e) {
			System.out.println("No se puedo cargar el archivo fxml");
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
}
