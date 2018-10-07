package com.liz.quanlysinhvien;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.liz.quanlysinhvien.entity.Student;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 10/7/2018.
 */

public class StudentDB extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "studentDB";
    private static final String TABLE_NAME = "students";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_STUDENT_NAME = "name";
    private static final String COLUMN_STUDENT_ID = "student_id";
    private static final String COLUMN_GENDER = "gender";
    private static final String COLUMN_MARK = "mark";
    private static final String GENDER_MALE = "Nam";
    private static final String GENDER_FEMALE = "Nữ";
    private static final int VERSION = 1;
    private Context mContext;

    public StudentDB(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_NAME + "( " +
                COLUMN_ID + " TEXT PRIMARY KEY ," +
                COLUMN_STUDENT_ID + " TEXT ," +
                COLUMN_STUDENT_NAME + " TEXT ," +
                COLUMN_GENDER + " TEXT ," +
                COLUMN_MARK + " TEXT)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(sql);
        onCreate(db);
    }

    public Student getStudentById(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " +
                COLUMN_STUDENT_ID + " = " +
                id;
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        String studentId = cursor.getString(1);
        String studentName = cursor.getString(2);
        boolean isMale = false;
        if (cursor.getString(3).equals(GENDER_MALE)) {
            isMale = true;
        }
        Double mark = Double.parseDouble(cursor.getString(4));
        Student student = new Student(studentId, studentName, isMale, mark);
        cursor.close();
        db.close();
        return student;
    }

    public void addStudent(Student student) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_STUDENT_ID, student.getStudentId());
        values.put(COLUMN_STUDENT_NAME, student.getStudentName());
        if (student.isMale()) {
            values.put(COLUMN_GENDER, GENDER_MALE);
        } else {
            values.put(COLUMN_GENDER, GENDER_FEMALE);
        }
        values.put(COLUMN_MARK, student.getAverageMark());
        db.insert(TABLE_NAME, null, values);
        Toast.makeText(mContext, "Insert successful", Toast.LENGTH_SHORT).show();
        db.close();
    }

    public void editStudent(Student student) {
        SQLiteDatabase db = this.getWritableDatabase();
        String id = student.getStudentId();
        String name = student.getStudentName();
        String gender;
        if(student.isMale()){
            gender = GENDER_MALE;
        }else{
            gender = GENDER_FEMALE;
        }
        Double mark = student.getAverageMark();
        String sql = "UPDATE "+TABLE_NAME+ " SET "+
                COLUMN_STUDENT_ID +" = '"+id+"', "+
                COLUMN_STUDENT_NAME+" = '"+name+"', "+
                COLUMN_GENDER+" = '"+gender+"', "+
                COLUMN_MARK+" = "+mark+ " WHERE "+
                COLUMN_STUDENT_ID+" = '"+id+"'";
        Log.d("TAG","Query : " + sql);
        db.rawQuery(sql,null);
        db.close();
        Log.d("TAG",student.toString());
    }

    public void deleteStudent(Student student) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_STUDENT_ID + " = ?",
                new String[]{String.valueOf(student.getStudentId())});
    }

    public List<Student> getAllStudent() {
        List<Student> mStudents = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                String id = cursor.getString(1);
                String name = cursor.getString(2);
                boolean isMale = false;
                if (cursor.getString(3).equals(GENDER_MALE)) {
                    isMale = true;
                }
                Double mark = Double.parseDouble(cursor.getString(4));
                Student student = new Student(id, name, isMale, mark);
                Log.d("TAG",student.toString());
                mStudents.add(student);
            } while (cursor.moveToNext());
        }
        db.close();
        return mStudents;
    }
}
