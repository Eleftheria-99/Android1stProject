package com.example.dit.hua.ergasia.firstactivity;

import java.io.Serializable;

public class DataContract implements Serializable {
    //One of the main principles of SQL databases is the schema: a formal declaration of how the database is organized.
    // The schema is reflected in the SQL statements that I use to create my database.
    // This class is a companion class, known as a contract class, which explicitly specifies the layout of my schema in a systematic and self-documenting way.
    int id;
    String fname;
    String lname;
    int age;

    public DataContract() {    //default constructor
    }

    //constructors
    public DataContract(int id, String fname, String lname, int age) {
        this.id = id;
        this.fname = fname;
        this.lname = lname;
        this.age = age;
    }

    public DataContract(String fname, String lname, int age) {
        this.fname = fname;
        this.lname = lname;
        this.age = age;
    }

    //setters and getters
    public int getAge() {
        return age;
    }

    public String getFname() {
        return fname;
    }

    public int getId() {
        return id;
    }

    public String getLname() {
        return lname;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }
}
