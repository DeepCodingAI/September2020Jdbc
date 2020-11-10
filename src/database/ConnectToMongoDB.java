package database;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class ConnectToMongoDB {

    public static MongoDatabase mongoDatabase = null;

    public static MongoDatabase connectToMongoDB(String dataBaseName){
        //./mongod --dbpath /Users/mrahman/develop/db/
        MongoClient mongoClient = new MongoClient();
        mongoDatabase = mongoClient.getDatabase(dataBaseName);
        System.out.println("MongoDB database is connected to "+ dataBaseName);
        return mongoDatabase;
    }

    public static String insertIntoMongoDB(List<Student> list, String dataBaseName, String collectionName){
        MongoDatabase mongoDatabase = connectToMongoDB(dataBaseName);
        for(int i=0; i<list.size(); i++){
            MongoCollection<Document> collGiven = mongoDatabase.getCollection(collectionName);
            Document document = new Document().append("stName",list.get(i).getStName())
                                              .append("stID",list.get(i).getStID())
                                              .append("stDOB",list.get(i).getStDOB())
                                              .append("stGrade",list.get(i).getStGrade());
            collGiven.insertOne(document);
        }
        return "Student has been registered";

    }

    public static List<Student> readStudentObject(String dataBasename,String collectionName){
        List<Student> list = new ArrayList<>();
        Student student = new Student();
        MongoDatabase mongoDatabase = connectToMongoDB(dataBasename);
        MongoCollection<Document> collection = mongoDatabase.getCollection(collectionName);
        BasicDBObject basicDBObject = new BasicDBObject();
        FindIterable<Document> iterable = collection.find(basicDBObject);
        for(Document document:iterable){
            String stName = (String)document.get("stName");
            String stID = (String)document.get("stID");
            String stDOB = (String)document.get("stDOB");
            String stGrade = (String)document.get("stGrade");
            student = new Student(stName,stID,stDOB,stGrade);
            list.add(student);
            student = new Student();
        }
        return list;
    }

    public static void main(String[] args) {
        List<Student> listOfStudent = new ArrayList<>();
        listOfStudent.add(new Student("Martin","2190","09-29-1999","B+"));
        listOfStudent.add(new Student("Jonson","4580","07-25-2099","A+"));
        insertIntoMongoDB(listOfStudent,"students","profile");
        List<Student> list = readStudentObject("students","profile");
        for(Student student:list){
            System.out.println(student.getStName()+ " "+student.getStID()+" "+student.getStDOB()+" "+student.getStGrade());
        }
    }

}
