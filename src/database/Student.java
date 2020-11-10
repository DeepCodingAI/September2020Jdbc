package database;

public class Student {
    private String stName;
    private String stID;
    private String stDOB;
    private String stGrade;

    public Student(){}
    public Student(String stName, String stID, String stDOB) {
        this.stName = stName;
        this.stID = stID;
        this.stDOB = stDOB;
    }
    public Student(String stName, String stID, String stDOB, String stGrade) {
        this.stName = stName;
        this.stID = stID;
        this.stDOB = stDOB;
        this.stGrade = stGrade;
    }
    public String getStName() {
        return stName;
    }

    public void setStName(String stName) {
        this.stName = stName;
    }

    public String getStID() {
        return stID;
    }

    public void setStID(String stID) {
        this.stID = stID;
    }

    public String getStDOB() {
        return stDOB;
    }

    public void setStDOB(String stDOB) {
        this.stDOB = stDOB;
    }

    public String getStGrade() {
        return stGrade;
    }
    public void setStGrade(String stGrade) {
        this.stGrade = stGrade;
    }
}
