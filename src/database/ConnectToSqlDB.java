package database;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ConnectToSqlDB {
    public static Connection connect = null;
    public static Statement statement = null;
    public static PreparedStatement ps = null;
    public static ResultSet resultSet = null;

    public static Properties loadLocalProperties() throws IOException {
        Properties prop = new Properties();
        InputStream ism = new FileInputStream("src/secret.properties");
        prop.load(ism);
        ism.close();
        return prop;
    }
    public static Properties loadCloudProperties() throws IOException {
        Properties prop = new Properties();
        InputStream ism = new FileInputStream("src/aws-rds-db.properties");
        prop.load(ism);
        ism.close();
        return prop;
    }

    public static Connection connectToSqlDatabase() throws IOException, ClassNotFoundException, SQLException {
        Properties properties = loadCloudProperties();
        String driverClass = properties.getProperty("MYSQLJDBC.driver");
        String url = properties.getProperty("MYSQLJDBC.url");
        String userName = properties.getProperty("MYSQLJDBC.userName");
        String password = properties.getProperty("MYSQLJDBC.password");
        Class.forName(driverClass);
        connect = DriverManager.getConnection(url,userName,password);
        return connect;
    }


    public static List<Student> readStudentsProfile(String tableName){
        List<Student> list = new ArrayList<>();
        Student student = null;
        try{
            Connection connection = connectToSqlDatabase();
            String query = "Select *From "+tableName;
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery(query);
            while(resultSet.next()){
                String stName = resultSet.getString("stName");
                String stID = resultSet.getString("stID");
                String stDOB = resultSet.getString("stDOB");
                student = new Student(stName, stID, stDOB);
                list.add(student);

            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return list;
    }

    public static void insertProfileIntoSqlTable(Student student,String tableName, String columnName1, String columnName2, String columnName3){
        try{
            connect =  connectToSqlDatabase();
            //ps = connect.prepareStatement("DROP TABLE IF EXISTS `"+tableName+"`;");
           // ps.executeUpdate();
            //ps = connect.prepareStatement("CREATE TABLE "+tableName+" ("+columnName1+" VARCHAR(20),"+columnName2+" VARCHAR(20),"+columnName3+" VARCHAR(20));");
            //ps.executeUpdate();
            ps = connect.prepareStatement("Insert Into "+ tableName+"("+columnName1+","+columnName2+","+columnName3+
                    ") Values(?,?,?)");
            ps.setString(1,student.getStName());
            ps.setString(2,student.getStID());
            ps.setString(3,student.getStDOB());
            ps.execute();


        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        insertProfileIntoSqlTable(new Student("ALI","9087","02-11-1980"),"Trainee","stName","stID","stDOB");
        List<Student> list = readStudentsProfile("Trainee");
        for(Student student:list){
            System.out.println(student.getStName()+ " "+ student.getStID()+ " "+ student.getStDOB());
        }
    }
}
