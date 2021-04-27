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

    public DatabaseController(DatabaseModel model){ //Constructor
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

        //Give buttons their actions
        view.exitButton.setOnAction(e-> Platform.exit());
        view.ClearButton.setOnAction(e-> clearBoxes(view.StudentCombo, view.ClassCombo, view.GradeCombo));
        view.InsertButton.setOnAction(e-> {
            try {
                insertGrade(view.StudentCombo.getValue(),view.ClassCombo.getValue(),view.GradeCombo.getValue(),view.PrintText);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });

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

    //Create the values for the comboboxes in javaFX
    public ObservableList<Integer> getStudent(){
        ArrayList<Integer> IDs = model.SQLQueryStudentID();
        return FXCollections.observableList(IDs);
    }

    public ObservableList<String> getClassname(){
        ArrayList<String> Names = model.SQLQueryClass();
        return FXCollections.observableList(Names);
    }

    //Clears the choices in comboboxes
    public void clearBoxes(ComboBox student, ComboBox course, ComboBox grade){
        student.valueProperty().set(null);
        course.valueProperty().set(null);
        grade.valueProperty().set(null);
    }

    //Handling the print of a class
    public void viewClass(String input, TextArea textArea) throws SQLException{
        textArea.clear();
        String query = "Select InClass.ID, InClass.ClassID, InClass.name, InClass.Grade, Class.AverageGrade From InClass LEFT OUTER JOIN Class ON InClass.ClassID = Class.ClassID where InClass.ClassID = ?;";
        PreparedStatement preparedStatement = model.conn.prepareStatement(query);
        preparedStatement.setString(1,input); //Sets input on the place of "?" in query
        ResultSet rs = preparedStatement.executeQuery();
        textArea.appendText(input);
        String AverageGrade = rs.getString("AverageGrade");

        //Prints the values from the executed query
        while(rs.next()){
            int ID = rs.getInt("ID");
            String name = rs.getString("Name");
            String Grade = rs.getString("Grade");
            textArea.appendText("\n ID: " + ID +" Name: " + name + " Grade: " + Grade);
        }
        textArea.appendText("\n Average grade: " + AverageGrade); //We dont want to print it several times
    }

    //Handling the input of a grade, which updates the grade and the averagegrade
    public void insertGrade(Integer studentInput, String classInput, float gradeInput, TextArea textArea) throws SQLException {
        textArea.clear();
        String query = "UPDATE InClass SET grade = ? where InClass.ID = ? AND InClass.ClassID = ?";
        PreparedStatement preparedStatement = model.conn.prepareStatement(query);
        preparedStatement.setString(1, String.valueOf(gradeInput));
        preparedStatement.setString(2, String.valueOf(studentInput));
        preparedStatement.setString(3,classInput);
        preparedStatement.execute();
        textArea.appendText("If the student is in the class you have chosen we have done the following:");
        textArea.appendText("\n Inserted grade: " + gradeInput + " in class: " + classInput + " for student: " + studentInput);

        //Updates the average grade of the class
        query = "UPDATE Class SET AverageGrade = (Select AVG(Grade) from InClass WHERE InClass.ClassID = ?);";
        preparedStatement = model.conn.prepareStatement(query);
        preparedStatement.setString(1,classInput);
        preparedStatement.execute();

        //Updates the average grade of the student
        query = "UPDATE Student SET AverageGrade = (Select AVG(Grade) from InClass WHERE InClass.ID = ?);";
        preparedStatement = model.conn.prepareStatement(query);
        preparedStatement.setString(1, String.valueOf(studentInput));
        preparedStatement.execute();


    }

    //Handels the print of a student
    public void viewStudent(String input, TextArea textArea) throws SQLException{
        textArea.clear();
        String query = "Select Student.Name,Student.StudentID,Student.Zipcode,Student.Origin,Student.Semester, Student.AverageGrade, InClass.ClassID, InClass.Grade from Student LEFT OUTER JOIN InClass ON Student.StudentID = InClass.ID where Student.StudentID = ?;";
        PreparedStatement preparedStatement = model.conn.prepareStatement(query);
        preparedStatement.setString(1,input); //Sets the input as the "?" parameter
        ResultSet rs = preparedStatement.executeQuery();
        String name = rs.getString("Name");
        int StudentID = rs.getInt("StudentID");
        int Zipcode = rs.getInt("Zipcode");
        String Origin = rs.getString("Origin");
        String Semester = rs.getString("Semester");
        String averageGrade = rs.getString("AverageGrade");
        textArea.appendText("\n ID: " + StudentID +" \nName: " + name + " \nOrigin: " + Origin + " \nZipcode: " + Zipcode + " \nSemester: " + Semester + " \nAverage grade: " + averageGrade);

        while(rs.next()){
            String ClassID = rs.getString("ClassID");
            String Grade = rs.getString("Grade");
            textArea.appendText("\n Class: " + ClassID + " Grade: " + Grade);
        }

    }
}


