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

    public static void insertProfileIntoSqlTable(boolean drop,Student student,String tableName, String columnName1, String columnName2, String columnName3){
        try{
            connect =  connectToSqlDatabase();
            if(drop==true) {
                ps = connect.prepareStatement("DROP TABLE IF EXISTS `" + tableName + "`;");
                ps.executeUpdate();
                ps = connect.prepareStatement("CREATE TABLE " + tableName + " (" + columnName1 + " VARCHAR(20)," + columnName2 + " VARCHAR(20)," + columnName3 + " VARCHAR(20));");
                ps.executeUpdate();
            }
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
    public static void update(Student student,String tableName, String columnName1, String columnName2, String columnName3){
      try{
          connect = connectToSqlDatabase();
          ps = connect.prepareStatement("Update "+ tableName+ " SET "+columnName1+" =? ,"+columnName2+" =? ,"+columnName3
                                            +" =? Where "+columnName1+" = '"+student.getStName()+"'");
          ps.setString(1,student.getStName());
          ps.setString(2,student.getStID());
          ps.setString(3,student.getStDOB());
          ps.executeUpdate();
      } catch (IOException e) {
          e.printStackTrace();
      } catch (SQLException e) {
          e.printStackTrace();
      } catch (ClassNotFoundException e) {
          e.printStackTrace();
      }
    }

    public static void delete(String stName,String tableName, String columnName1){
        try{
            connect = connectToSqlDatabase();
            ps = connect.prepareStatement("Delete From "+ tableName+" Where "+columnName1+" = '"+stName+"'");
            ps.executeUpdate();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static List<Student> generateStProfile(){
        List<Student> list = new ArrayList<Student>();
        list.add(new Student("Prit","1122","01-23-1980"));
        list.add(new Student("Ali","2233","09-20-1989"));
        list.add(new Student("Popy","3344","08-03-1997"));
        list.add(new Student("Rathul","4455","11-28-1985"));
        list.add(new Student("Maithily","5566","02-27-1970"));
        list.add(new Student("Sujon","6677","03-21-1975"));
        list.add(new Student("Samir","7788","07-13-1960"));
        list.add(new Student("Rana","8899","6-09-1950"));

        return list;
    }
    public static void main(String[] args) {
        //delete("Prit","Trainee", "stName");
        update(new Student("Prit","1122","11-01-1999"),"Trainee","stName","stID","stDOB");
        List<Student> listGiven = generateStProfile();
        for(int i=0; i<listGiven.size(); i++) {
            insertProfileIntoSqlTable(false,listGiven.get(i), "Trainee", "stName", "stID", "stDOB");
        }
        List<Student> list = readStudentsProfile("Trainee");
        for(Student student:list){
            System.out.println(student.getStName()+ " "+ student.getStID()+ " "+ student.getStDOB());
        }
    }
}
