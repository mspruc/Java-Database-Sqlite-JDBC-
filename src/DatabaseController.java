import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

import java.sql.*;
import java.util.ArrayList;

import static java.sql.DriverManager.getConnection;

public class DatabaseController {
    DatabaseModel model;
    DatabaseView view;

    public DatabaseController(DatabaseModel model){
        this.model = model;
        try{
            model.connect();
            model.CreateStatement();

        }catch (SQLException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    public void setView(DatabaseView view){
        this.view = view;
        view.exitButton.setOnAction(e-> Platform.exit());
        view.ClearButton.setOnAction(e-> clearBoxes(view.StudentCombo, view.ClassCombo));

            EventHandler<ActionEvent> Print = e-> {
                try {
                    if(view.ClassCombo.getValue() !=null) {
                        viewClass(view.ClassCombo.getValue(), view.PrintText);
                    }
                    if(view.StudentCombo.getValue() != null) {
                        viewStudent(String.valueOf(view.StudentCombo.getValue()), view.PrintText);
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            };
            view.PrintButton.setOnAction(Print);
    }

    public ObservableList<Integer> getStudent(){
        ArrayList<Integer> IDs = model.SQLQueryStudentID();
        return FXCollections.observableList(IDs);
    }

    public ObservableList<String> getClassname(){
        ArrayList<String> Names = model.SQLQueryClass();
        return FXCollections.observableList(Names);
    }

    public void clearBoxes(ComboBox student, ComboBox course){
        student.valueProperty().set(null);
        course.valueProperty().set(null);
    }

    public void viewClass(String input, TextArea textArea) throws SQLException{
        textArea.clear();
        String query = "Select InClass.ID, InClass.ClassID, InClass.name, InClass.Grade, Class.AverageGrade From InClass LEFT OUTER JOIN Class ON InClass.ClassID = Class.ClassID where InClass.ClassID = ?;";
        PreparedStatement preparedStatement = model.conn.prepareStatement(query);
        preparedStatement.setString(1,input); //Sets the input as the "?" parameter
        ResultSet rs = preparedStatement.executeQuery();
        textArea.appendText(input);
        float AverageGrade = rs.getFloat("AverageGrade");
        while(rs.next()){
            int ID = rs.getInt("ID");
            String name = rs.getString("Name");
            float Grade = rs.getFloat("Grade");
            textArea.appendText("\n ID: " + ID +" Name: " + name + " Grade: " + Grade);
        }
        textArea.appendText("\n Average grade: " + AverageGrade);
    }

    public void viewStudent(String input, TextArea textArea) throws SQLException{
        textArea.clear();
        String query = "Select Student.Name,Student.StudentID,Student.Zipcode,Student.Origin,Student.Semester, InClass.ClassID, InClass.Grade from Student LEFT OUTER JOIN InClass ON Student.StudentID = InClass.ID where Student.StudentID = ?;";
        PreparedStatement preparedStatement = model.conn.prepareStatement(query);
        preparedStatement.setString(1,input); //Sets the input as the "?" parameter
        ResultSet rs = preparedStatement.executeQuery();
        String name = rs.getString("Name");
        int StudentID = rs.getInt("StudentID");
        int Zipcode = rs.getInt("Zipcode");
        String Origin = rs.getString("Origin");
        String Semester = rs.getString("Semester");
        textArea.appendText("\n ID: " + StudentID +" Name: " + name + " Origin: " + Origin + " Zipcode: " + Zipcode + " Semester: " + Semester);

        while(rs.next()){
            String ClassID = rs.getString("ClassID");
            float Grade = rs.getFloat("Grade");
            textArea.appendText("\n Class: " + ClassID + " Grade: " + Grade);
        }

    }
}


