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

    public static MongoDatabase connectToMongoDB() {
        MongoClient mongoClient = new MongoClient();
        mongoDatabase = mongoClient.getDatabase("students");
        System.out.println("Database Connected");

        return mongoDatabase;
    }

    public String insertIntoMongoDB(List<Student> student, String profileName){
        MongoDatabase mongoDatabase = connectToMongoDB();
        MongoCollection myCollection = mongoDatabase.getCollection(profileName);
        boolean collectionExists = mongoDatabase.listCollectionNames()
                .into(new ArrayList<String>()).contains(profileName);
        if(collectionExists) {
            myCollection.drop();
        }
        for(int i=0; i<student.size(); i++){
            MongoCollection<Document> collection = mongoDatabase.getCollection(profileName);
            Document document = new Document().append("stName", student.get(i).getStName()).append("stID",
                    student.get(i).getStID()).append("stDOB",student.get(i).getStDOB()).append("stGrade", student.get(i).getStGrade());
            collection.insertOne(document);
        }
        return  "Student has been registered";
    }

    public static List<Student> readStudentListFromMongoDB(String profileName){
        List<Student> list = new ArrayList<Student>();
        Student student = new Student();
        MongoDatabase mongoDatabase = connectToMongoDB();
        MongoCollection<Document> collection = mongoDatabase.getCollection(profileName);
        BasicDBObject basicDBObject = new BasicDBObject();
        FindIterable<Document> iterable = collection.find(basicDBObject);
        for(Document doc:iterable){
            String stName = (String)doc.get("stName");
            student.setStName(stName);
            String stID = (String)doc.get("stID");
            student.setStName(stID);
            String stDOB = (String)doc.get("stDOB");
            student.setStName(stDOB);
            String stGrade = (String)doc.get("stGrade");
            student.setStGrade(stGrade);
            student = new Student(stName,stID,stDOB,stGrade);
            list.add(student);
            student = new Student();
        }
        return list;
    }

    public static void main(String [] args){
        List<Student> studentList = readStudentListFromMongoDB("profile");
        for(Student student:studentList){
            System.out.println(student.getStName()+ " "+ student.getStID()+" "+student.getStDOB()+" "+student.getStGrade());
        }
    }
}
