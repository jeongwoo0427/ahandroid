package com.example.yournote;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import static android.provider.LiveFolders.INTENT;

public class activity_managekeyword extends AppCompatActivity {

    SpinnerListAdapter adapter;
    Spinner spinner;




    YourNoteDBMan dbman;
    SQLiteDatabase database;




    Button btnComplete;
    EditText etKeyword,etSummary;



    public static String md;
    public static String currentNo;


    String selectedCatId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_managekeyword);



        btnComplete = (Button)findViewById(R.id.btnComplete);
        etKeyword = (EditText)findViewById(R.id.etKeyWord);
        etSummary = (EditText)findViewById(R.id.etSummary);
        spinner = (Spinner)findViewById(R.id.spcate);

        DisplaySpinner();


        if(md=="modify") {
            getDetials();
        }

        btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(etKeyword.getText().toString().trim().equals("")) {

                    Toast.makeText(activity_managekeyword.this,"키워드를 입력하세요..데이터 꼬여요..",Toast.LENGTH_LONG).show();
                    return;
                }

                if(md == "add"){


                    addKeyWord();
                }

                if(md =="modify")
                {
                    modifyKeyWord();
                }




            }
        });




        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                CategoryDo cDo = (CategoryDo)adapter.getItem(position);

                selectedCatId = cDo.getNo();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }




    private void DisplaySpinner(){

        YourNoteDBMan dbman = new YourNoteDBMan(this);
        SQLiteDatabase db = dbman.getReadableDatabase();



        adapter = new SpinnerListAdapter();

        String qry = "SELECT * FROM category ORDER BY id ASC";

        Cursor cursor = db.rawQuery(qry,null);


        while(cursor.moveToNext()){

            adapter.addItem(String.valueOf(cursor.getInt(0)),cursor.getString(1));
        }

        spinner.setPrompt("카테고리를 선택하세요..");


        spinner.setAdapter(adapter);


        dbman.close();
        db.close();







    }


    private void addKeyWord(){

        int no;
        int importance;
        String categoryid = "1";
        String keyword,summary;


        //나중에 수정 필요
        importance = 1;
        categoryid = selectedCatId.trim();
        keyword = etKeyword.getText().toString().trim();
        summary = etSummary.getText().toString().trim();



        dbman = new YourNoteDBMan(this);
        database = dbman.getWritableDatabase();



        String sql = "INSERT INTO keyword(importance, categoryid, keyword, summary) VALUES("+importance+","+categoryid+",'"+keyword+"','"+summary+"');";

        database.execSQL(sql);



       Cursor cur = database.rawQuery("SELECT last_insert_rowid()",null);



       //새로 생성한 레코드의 ID값을 가져온다
       cur.moveToFirst();

            no = cur.getInt(0);

            dbman.close();
            database.close();


            Intent data = new Intent();
            data.putExtra("newNo",String.valueOf(no));

           setResult(RESULT_OK,data);

            finish();

           // Toast.makeText(activity_managekeyword.this,"에러발생",Toast.LENGTH_SHORT).show();






    }

    private void modifyKeyWord(){


        int importance;
        String categoryid;

        String keyword,summary;


        //나중에 수정 필요
        importance = 1;
        categoryid = selectedCatId.trim();
        keyword = etKeyword.getText().toString().trim();
        summary = etSummary.getText().toString().trim();




        dbman = new YourNoteDBMan(this);
        database = dbman.getWritableDatabase();



        //String sql = "INSERT INTO keyword(importance, categoryid, keyword, summary) VALUES("+importance+","+categoryid+",'"+keyword+"','"+summary+"');";

        String sql = "UPDATE keyword SET importance="+importance+", categoryid="+categoryid+", keyword='"+keyword+"', summary='"+summary+"' WHERE id="+currentNo+";";

        database.execSQL(sql);




        //새로 생성한 레코드의 ID값을 가져온다


        dbman.close();
        database.close();


        Intent data = new Intent();
        data.putExtra("newNo",String.valueOf(currentNo));

        setResult(RESULT_OK,data);

        finish();

        // Toast.makeText(activity_managekeyword.this,"에러발생",Toast.LENGTH_SHORT).show();




    }


    private void getDetials(){
        dbman = new YourNoteDBMan(this);
        database = dbman.getReadableDatabase();


        String sql = "SELECT * FROM keyword WHERE id="+currentNo;


        Cursor cur = database.rawQuery(sql,null);



        while(cur.moveToNext()){

            String keyword = cur.getString(3);

            String summary = cur.getString(4);



            etKeyword.setText(keyword);

            etSummary.setText(summary);


        }






        dbman.close();
        database.close();
    }


}
