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

        public void CreateStatement() throws SQLException{
            this.stmt = conn.createStatement();
        }

        //Gets student ids from database with a query
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

        //Gets class names from database with a query
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


