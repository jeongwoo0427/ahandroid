package com.example.yournote;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


//DB을 열람할 수 있게끔 도와주는 클래스
public class YourNoteDBMan extends SQLiteOpenHelper {

    static String dbName = "noteDB";
    static int dbVersion = 9;

    public YourNoteDBMan(Context context) {
        super(context, dbName, null, dbVersion);
        //이 클래스를 불러줄때 데이터베이스 이름이랑 버전을 정의해줌
    }

    //데이터베이스 최초 실행시 테이블 만들어주는 쿼리문들
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE keyword(id INTEGER PRIMARY KEY AUTOINCREMENT, importance INTEGER, categoryid INTEGER, keyword TEXT, summary TEXT);";
        db.execSQL(sql);

         sql = " CREATE TABLE category( id INTEGER PRIMARY KEY AUTOINCREMENT, categoryname TEXT); ";
        db.execSQL(sql);

        //초기 DB 세팅
        sql = " INSERT INTO category(categoryname) VALUES('전체'); ";
        db.execSQL(sql);

        sql= " INSERT INTO category(categoryname) VALUES('영어'); ";
        db.execSQL(sql);

        sql = " INSERT INTO category(categoryname) VALUES('프로그래밍 용어'); ";
        db.execSQL(sql);

        sql = " INSERT INTO keyword(keyword,importance,categoryid,summary) VALUES('켜는 것은 메모장', 1,1,'안녕하세요! 이 어플 개발자입니다. 간단한 사용 설명을 드리자면, 먼저 좌측상단에 키워드 가져오기 버튼을 누르면 카테고리 관리하는 버튼이 나옵니다 거기서 원하는 카테고리를 추가한 후 이 화면 상단에 추가를 누르시면 키워드와 카테고리를 정한 후 설명을 입력하실 수 있습니다. 만약 이 글을 보기 싫드시다면 우측 상단에 삭제 버튼을 과감하게 누르시면 됩니다 ^^');  ";
        db.execSQL(sql);

    }

    //버전이 올라갈때 기존 테이블 지워주는 쿼리문들
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE keyword";

        String sql2 = " DROP TABLE category ";

        db.execSQL(sql);
        db.execSQL(sql2);

    onCreate(db);
    }
}
