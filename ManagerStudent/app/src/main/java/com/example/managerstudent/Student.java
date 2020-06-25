package com.example.managerstudent;

public class Student {
    private String MSSV;
    private String Name;
    private String DateOfBirth;
    private String Email;
    private String Address;

    public Student(String MSSV, String name, String dateOfBirth, String email, String address) {
        this.MSSV = MSSV;
        Name = name;
        DateOfBirth = dateOfBirth;
        Email = email;
        Address = address;
    }

    public Student() {
    }

    public String getMSSV() {
        return MSSV;
    }

    public void setMSSV(String MSSV) {
        this.MSSV = MSSV;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDateOfBirth() {
        return DateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        DateOfBirth = dateOfBirth;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }
}
