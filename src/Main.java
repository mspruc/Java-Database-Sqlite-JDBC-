import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        String url = "jdbc:sqlite:C:\\Users\\Frede\\IdeaProjects\\databases\\StudentDatabase.db";
        DatabaseModel SDB = new DatabaseModel(url);
        DatabaseController control = new DatabaseController(SDB);
        DatabaseView view = new DatabaseView(control);
        control.setView(view);
        primaryStage.setTitle("Class, Student Finder");
        primaryStage.setScene(new Scene(view.asParent(),600,475));
        primaryStage.show();
    }
    public static void main(String[] args){
        launch(args);
    }
}
