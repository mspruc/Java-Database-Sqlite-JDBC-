import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;

public class DatabaseView {

    DatabaseController control;
    private GridPane StartView;
    Button exitButton = new Button("Exit");
    Button PrintButton = new Button("Print");
    Button ClearButton = new Button("Clear Choices");
    Button InsertButton = new Button("Insert Grade");
    Label StudentLbl = new Label("Select Student:");
    Label ClassLbl = new Label("Select Class:");
    Label GradeLbl = new Label("Select Grade you want to insert:");
    TextArea PrintText = new TextArea();
    ComboBox<Integer> StudentCombo = new ComboBox<>();
    ComboBox<String> ClassCombo = new ComboBox<String>();
    ComboBox<Float> GradeCombo = new ComboBox<>();


    public DatabaseView(DatabaseController control){
        this.control = control;
        CreateAndConfigure();
    }

    private void CreateAndConfigure(){
        StartView = new GridPane();
        StartView.setMinSize(500,400);
        StartView.setPadding(new Insets(10,10,10,10));
        StartView.setVgap(5);
        StartView.setHgap(1);
        StartView.add(StudentLbl,1,1);
        StartView.add(StudentCombo,15,1);
        StartView.add(ClassLbl,1,3);
        StartView.add(ClassCombo,15,3);
        StartView.add(GradeLbl,1,5);
        StartView.add(GradeCombo,15,5);
        StartView.add(PrintButton,15,6);
        StartView.add(ClearButton,13,6);
        StartView.add(InsertButton, 11,6);
        StartView.add(PrintText,1,7,15,7);
        StartView.add(exitButton,20,15);

        ObservableList<Integer> studentList = control.getStudent();
        StudentCombo.setItems(studentList);

        ObservableList<String> classList = control.getClassname();
        ClassCombo.setItems(classList);

        ArrayList<Float> grades = new ArrayList<>();
        grades.add((float)-3);
        grades.add((float)0);
        grades.add((float)0.2);
        grades.add((float)4);
        grades.add((float)7);
        grades.add((float)10);
        grades.add((float)12);
        ObservableList<Float> gradeList = FXCollections.observableList(grades);
        GradeCombo.setItems(gradeList);


    }

    public Parent asParent() {
        return StartView;
    }
}
