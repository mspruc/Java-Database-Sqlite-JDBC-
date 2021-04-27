import java.sql.*;
import java.util.ArrayList;

import static java.sql.DriverManager.getConnection;

public class DatabaseModel {
        Connection conn=null;
        String url;
        Statement stmt=null;
        PreparedStatement pstmt=null;
        ResultSet rs=null;

        DatabaseModel(String url){
            this.url = url;
        }

        public void connect() throws SQLException {
            conn=getConnection(url);
        }
        public void close() throws SQLException{
            if (conn != null){
                conn.close();
            }
        }

        public void CreateStatement() throws SQLException{
            this.stmt = conn.createStatement();
        }

        public void PreparedStmtFindStudentQuert(){
            String sql = "SELECT * From Student as S1 Where S1.Name = ?;";
            try{
                pstmt = conn.prepareStatement(sql);
            }catch(SQLException e){
                System.out.println(e.getMessage());
            }
        }

         public void PreparedStmtFindClassQuert(){
            String sql = "SELECT * From Class as C1 Where C1.Name = ?;";
            try{
                pstmt = conn.prepareStatement(sql);
            }catch(SQLException e){
                System.out.println(e.getMessage());
            }
        }


        public ArrayList<Course> FindClass(String name) {
            ArrayList<Course> course = new ArrayList<Course>();
            try {
                pstmt.setString(1, name);
                rs = pstmt.executeQuery();
                if (rs == null) {
                    System.out.println("No records fetched");
                }
                while (rs != null && rs.next()) {
                    course.add(new Course(rs.getString(1),rs.getFloat(2),rs.getArray(3),rs.getArray(4)));
                    System.out.println("Name: " + rs.getString(1) + "Average Grade: " + rs.getFloat(2) + "Students: " + rs.getArray(3) + "Teachers: " + rs.getArray(4));
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }return course;
        }


        public ArrayList<Student> FindStudent(String name){
            ArrayList<Student> students = new ArrayList<>();
            try{
                pstmt.setString(1,name);
                rs = pstmt.executeQuery();
                if(rs==null){
                    System.out.println("No records fetched.");
                }
                while (rs!=null && rs.next()){
                    students.add(new Student(rs.getString(1)));
                    System.out.println("Name: " + rs.getString(1));
                }
            }catch (SQLException e){
                System.out.println(e.getMessage());
            } return students;

        }

        public ArrayList<Integer> SQLQueryStudentID(){
            ArrayList<Integer> IDs = new ArrayList<>();
            String sql = "Select StudentID From Student;";
            try{
                rs = stmt.executeQuery(sql);
                while (rs != null && rs.next()){
                    int ID = rs.getInt(1);
                    IDs.add(ID);
                }
            }catch (SQLException e)
            {
                System.out.println(e.getMessage());
            }
            rs=null;
            return IDs;
        }


        public ArrayList<String> SQLQueryClass(){
            ArrayList<String> Names = new ArrayList<>();
            String sql = "Select ClassID From Class;";
            try{
                rs = stmt.executeQuery(sql);
                while (rs != null && rs.next()){
                    String name = rs.getString(1);
                    Names.add(name);
                }
            }catch (SQLException e)
            {
                System.out.println(e.getMessage());
            }
            rs=null;
            return Names;
        }


}

class Course{
    String name;
    float avgGrade;
    Array students;
    Array teachers;

    public Course(String name, float avgGrade, Array students, Array teachers){
        this.name = name;
        this.avgGrade = avgGrade;
        this.students = students;
        this.teachers = teachers;
    }
}

class Student{
    String name;
    float  avgGrade;
    String monkey;

    public Student(String name){
        this.name = name;
    }
}
