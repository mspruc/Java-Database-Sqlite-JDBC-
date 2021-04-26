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
        String query = "Select ID, Name, Grade From InClass where InClass.ClassID = ?";
        String query2 = "Select AverageGrade from Class where Class.ClassID = ?";
        PreparedStatement preparedStatement = model.conn.prepareStatement(query);
        PreparedStatement preparedStatement2 = model.conn.prepareStatement(query2);
        preparedStatement.setString(1,input); //Sets the input as the "?" parameter
        preparedStatement2.setString(1,input);
        ResultSet rs = preparedStatement.executeQuery();
        textArea.appendText(input);
        while(rs.next()){
            int ID = rs.getInt("ID");
            String name = rs.getString("Name");
            float Grade = rs.getFloat("Grade");
            textArea.appendText("\n ID: " + ID +" Name: " + name + " Grade: " + Grade);
        }
        rs = preparedStatement2.executeQuery();
        while(rs.next()){
            textArea.appendText("\n Average grade for the class: " + rs.getFloat("AverageGrade"));
        }
    }

    public void viewStudent(String input, TextArea textArea) throws SQLException{
        textArea.clear();
        String query = "Select Name,StudentID,Zipcode,Origin,Semester From Student where Student.StudentID = ?";
        PreparedStatement preparedStatement = model.conn.prepareStatement(query);
        preparedStatement.setString(1,input); //Sets the input as the "?" parameter
        ResultSet rs = preparedStatement.executeQuery();
        while(rs.next()){
            String name = rs.getString("Name");
            int StudentID = rs.getInt("StudentID");
            int Zipcode = rs.getInt("Zipcode");
            String Origin = rs.getString("Origin");
            String Semester = rs.getString("Semester");
            textArea.appendText("\n ID: " + StudentID +" Name: " + name + " Origin: " + Origin + " Zipcode: " + Zipcode + " Semester: " + Semester);
        }

    }


    /*public void HandlePrintStudent(String name, TextArea txtArea){
        txtArea.clear();
        txtArea.appendText("Name \n");
        model.PreparedStmtFindStudentQuert();
        ArrayList<Student> names = model.FindStudent(name);
        for(int i = 0; i<names.size(); i++){
            txtArea.appendText(i+ " : " + names.get(i).name);
        }
    }

    public void HandlePrintClass(String name, TextArea txtArea){
        txtArea.clear();
        txtArea.appendText("Name \n");
        model.PreparedStmtFindClassQuert();
        ArrayList<Course> course = model.FindClass(name);
        for(int i = 0; i<course.size(); i++){
            txtArea.appendText(i+ " : " + course.get(i).name);
        }
    }*/
    }


