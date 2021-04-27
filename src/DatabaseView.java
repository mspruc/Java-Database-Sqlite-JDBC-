import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
public class DatabaseView {

    DatabaseController control;
    private GridPane StartView;
    Button exitButton = new Button("Exit");
    Button PrintButton = new Button("Print");
    Button ClearButton = new Button("Clear Choices");
    Label StudentLbl = new Label("Select Student:");
    Label ClassLbl = new Label("Select Class:");
    TextArea PrintText = new TextArea();
    ComboBox<Integer> StudentCombo = new ComboBox<>();
    ComboBox<String> ClassCombo = new ComboBox<String>();

    public DatabaseView(DatabaseController control){
        this.control = control;
        CreateAndConfigure();
    }

    private void CreateAndConfigure(){
        StartView = new GridPane();
        StartView.setMinSize(300,200);
        StartView.setPadding(new Insets(10,10,10,10));
        StartView.setVgap(5);
        StartView.setHgap(1);
        StartView.add(StudentLbl,1,1);
        StartView.add(StudentCombo,15,1);
        StartView.add(ClassLbl,1,3);
        StartView.add(ClassCombo,15,3);
        StartView.add(PrintButton,15,6);
        StartView.add(ClearButton,13,6);
        StartView.add(PrintText,1,7,15,7);
        StartView.add(exitButton,20,15);

        ObservableList<Integer> studentList = control.getStudent();
        StudentCombo.setItems(studentList);

        ObservableList<String> classList = control.getClassname();
        ClassCombo.setItems(classList);
    }

    public Parent asParent() {
        return StartView;
    }
}
