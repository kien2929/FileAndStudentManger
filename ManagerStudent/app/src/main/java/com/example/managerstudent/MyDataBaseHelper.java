package com.example.managerstudent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MyDataBaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "SQLite";

    // Database version
    private static final int DATABASE_VERSION = 1;

    // Database name
    private static final String DATABASE_NAME = "Student_Manager.db";

    // Table name : Student
    private static final String TABLE_STUDENT = "Student";

    private static final String STUDENT_ID = "Student_Id";
    private static final String STUDENT_NAME = "Student_Name";
    private static final String STUDENT_MSSV = "Student_Mssv";
    private static final String STUDENT_DATE = "Student_Date";
    private static final String STUDENT_EMAIL = "Student_Email";
    private static final String STUDENT_ADDRESS = "Student_Address";

    public MyDataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String script = "CREATE TABLE " + TABLE_STUDENT + " ("
                + STUDENT_ID + " INTEGER PRIMARY KEY,"
                + STUDENT_NAME + " TEXT,"
                + STUDENT_MSSV + " TEXT,"
                + STUDENT_DATE + " TEXT,"
                + STUDENT_EMAIL + " TEXT,"
                + STUDENT_ADDRESS + " TEXT)";
        db.execSQL(script);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENT);

        // Create tables again
        onCreate(db);
    }

    public void addStudent(Student student){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(STUDENT_NAME, student.getName());
        values.put(STUDENT_MSSV, student.getMSSV());
        values.put(STUDENT_DATE, student.getDateOfBirth());
        values.put(STUDENT_EMAIL, student.getEmail());
        values.put(STUDENT_ADDRESS, student.getAddress());

        db.insert(TABLE_STUDENT, null, values);
        db.close();
    }

    public List<Student> getAllNote(){
        List<Student> studentList = new ArrayList<Student>();

        // Select query
        String selectQuery = "SELECT * FROM " + TABLE_STUDENT;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Student student = new Student();
                student.setName(cursor.getString(1));
                student.setMSSV(cursor.getString(2));
                student.setDateOfBirth(cursor.getString(3));
                student.setEmail(cursor.getString(4));
                student.setAddress(cursor.getString(5));
                studentList.add(student);
            } while (cursor.moveToNext());
        }

        // return note list
        return studentList;
    }

    public void createDefault(){
        Student student1 = new Student("20160723", "Pham Ngoc Du", "29/11/1998",
                "ngocdu12a1@gmail.com", "Thai Binh");
        Student student2 = new Student("20161234", "Pham Ngoc Duong", "08/02/2008",
                "ngocduong12a1@gmail.com", "Ha Noi");

        this.addStudent(student1);
        this.addStudent(student2);
    }

    public List<Student> search(String data){
        String script = String.format("SELECT * FROM %s WHERE (%s LIKE '%%%s%%') OR (%s LIKE '%%%s%%')",
                TABLE_STUDENT, STUDENT_NAME, data, STUDENT_MSSV, data);
        Log.d(TAG, "search: " + script);

        List<Student> studentList = new ArrayList<Student>();

        // Select query

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(script, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Student student = new Student();
                student.setName(cursor.getString(1));
                student.setMSSV(cursor.getString(2));
                student.setDateOfBirth(cursor.getString(3));
                student.setEmail(cursor.getString(4));
                student.setAddress(cursor.getString(5));
                studentList.add(student);
            } while (cursor.moveToNext());
        }

        // return note list
        return studentList;
    }

    public void delete(String mssv){
        String script = String.format("DELETE FROM %s WHERE %s LIKE %s", TABLE_STUDENT, STUDENT_MSSV, mssv);
        Log.d(TAG, "search: " + script);

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(script);
    }
}
