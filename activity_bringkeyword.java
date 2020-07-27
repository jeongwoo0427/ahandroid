package com.example.yournote;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;



//키워드가져오는 액티비티를 대표적으로 담당하는 클래스
public class activity_bringkeyword extends AppCompatActivity {

    SpinnerListAdapter sadpater;
    YourNoteDBMan dbman;
    SQLiteDatabase database;
    Cursor cursor;
    ListViewAdapter adapter;


    ListView listview;
    Button btnManageCategory , btnRefresh;
    Spinner spinner;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bringkeyword);

        listview = (ListView)findViewById(R.id.listview);
        btnManageCategory = (Button)findViewById(R.id.btnManageCategory_brningM);


        spinner = (Spinner)findViewById(R.id.spCategory);
        btnRefresh =(Button)findViewById(R.id.btnRefreshKeyword);


        //테스트용
       // insert(1,1,"두번째 테스트","서머리를 표시하는 두번째 테스트 입니다.");




        select("1");

        DisplaySpinner();



        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                CategoryDo cdo = (CategoryDo)sadpater.getItem(position);



                select(cdo.getNo());


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //리스트뷰아이템 중 1개 선택 됐을때

        listview.setOnItemClickListener(new ListView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                //1개짜리 객체 선언 후 어댑터에서 배열아이템중 클릭한 포지션에 해당하는 1개짜리 객체를 가져오는 메서드
                keywordDo kDo = (keywordDo)adapter.getItem(position);



                Intent data = new Intent();

                data.putExtra("no",kDo.getNo());


                setResult(RESULT_OK,data);

                finish();

            }
        });



        btnManageCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Intent intent = new Intent(activity_bringkeyword.this, activity_bringcategory.class);

                startActivity(intent);


            }
        });



        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select("1");
                DisplaySpinner();
            }
        });


    }






    private void DisplaySpinner(){

        YourNoteDBMan dbmanager = new YourNoteDBMan(this);
        SQLiteDatabase sqldb = dbmanager.getReadableDatabase();



         sadpater= new SpinnerListAdapter();

        String qry = "SELECT * FROM category ORDER BY id ASC";

        Cursor curs= sqldb.rawQuery(qry,null);


        while(curs.moveToNext()){

            sadpater.addItem(String.valueOf(curs.getInt(0)),curs.getString(1));
        }

        spinner.setPrompt("카테고리를 선택하세요..");


        spinner.setAdapter(sadpater);

        dbmanager.close();
        sqldb.close();








    }



    //db도우미랑 실행기를 이용해서 입력쿼리문 실행하는 메소드
    private void insert(int importance, int categoryid, String keyword, String summary){

        dbman = new YourNoteDBMan(this);
        database = dbman.getWritableDatabase();

        String sql = "INSERT INTO keyword(importance, categoryid, keyword, summary) VALUES("+importance+","+categoryid+",'"+keyword+"','"+summary+"');";

        database.execSQL(sql);

        dbman.close();
        database.close();




    }


    //리스트뷰에 데이터들을 띄우기 위해 실행되는 메소드
    private void select(String SelectMode){


        dbman = new YourNoteDBMan(this);
        database = dbman.getReadableDatabase();
        adapter = new ListViewAdapter();

        String sql;


        if(SelectMode.equals("1"))
            sql = "SELECT keyword.id, category.categoryname,keyword.keyword FROM keyword INNER JOIN category ON keyword.categoryid = category.id  ORDER BY keyword.id DESC";

        else{
           sql = "SELECT keyword.id, category.categoryname,keyword.keyword FROM keyword INNER JOIN category ON keyword.categoryid = category.id WHERE category.id="+SelectMode+" ORDER BY keyword.id DESC";

        }

        cursor = database.rawQuery(sql,null);


        //커서에 담은 테이블의 레코드 개수만큼 순회시키고 그만큼 리스트뷰에 추가시킨다.
        while(cursor.moveToNext()){
            int id = cursor.getInt(0);
            String category = cursor.getString(1);
            String keyword = cursor.getString(2);

            adapter.addItem(String.valueOf(id),category,keyword);

        }

        listview.setAdapter(adapter);

        dbman.close();
        database.close();
    }


}
