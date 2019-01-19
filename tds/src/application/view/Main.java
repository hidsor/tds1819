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
	
	// Constantes
	private final static double SCENE_WIDTH = 800;
	private final static double SCENE_HEIGHT = 600;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("Root.fxml"));
			Scene scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);
			scene.getStylesheets().add(getClass().getResource("applicationred.css").toExternalForm());
			primaryStage.initStyle(StageStyle.TRANSPARENT);
			primaryStage.setScene(scene);
			primaryStage.setMinWidth(SCENE_WIDTH);
			primaryStage.setMinHeight(SCENE_HEIGHT);
			ResizeHelper.addResizeListener(primaryStage);
			primaryStage.show();
			
		} catch(Exception e) {
			System.out.println("No se pudo cargar la aplicación");
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
}
