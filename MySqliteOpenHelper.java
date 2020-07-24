package com.example.yournote;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


//DB을 열람할 수 있게끔 도와주는 클래스
public class MySqliteOpenHelper extends SQLiteOpenHelper {

    static String dbName = "testDb.db";
    static int dbVersion = 1;

    public YourNoteDBMan(Context context) {
        super(context, dbName, null, dbVersion);
        //이 클래스를 불러줄때 데이터베이스 이름이랑 버전을 정의해줌
    }

    //데이터베이스 최초 실행시 테이블 만들어주는 쿼리문들
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE keyword(id INTEGER PRIMARY KEY AUTOINCREMENT, keyword TEXT, summary TEXT);";
        db.execSQL(sql);

    }

    //버전이 올라갈때 기존 테이블 지워주는 쿼리문들
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE keyword";

        db.execSQL(sql);
   
        onCreate(db);
    }
}
