package com.example.yournote;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends AppCompatActivity {


    Intent serviceIntent;

    public static final int NOTIFICATION_ID=1;
    YourNoteDBMan dbman;
    SQLiteDatabase database;
    Cursor cursor;

    TextView tvKeyText, tvKeyCategory, tvKeySummary,tvBar;

    Button btnNext,btnClose,btnDelete,btnSetting;


    String currentNo;



    private int REQUEST_KEYWORD =1;
    private int EDITED_KEYWORD=2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        viewSetting();



        getRandomDetails();



        registerScreenOnReceiver();


       onStartForegroundService();


       getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);

    }




    public void onStartForegroundService(){




        if(MyService.serviceIntent==null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                serviceIntent = new Intent(this, MyService.class);
                startForegroundService(serviceIntent);


            } else {
                startService(serviceIntent);
            }

        }else{
            serviceIntent = MyService.serviceIntent;


        }

    }




    private void registerScreenOnReceiver(){



    }


    private void viewSetting(){

        tvKeyText = (TextView)findViewById(R.id.tvKeyText);
        tvKeyCategory =(TextView)findViewById(R.id.tvKeyCategory);
        tvKeySummary = (TextView)findViewById(R.id.tvKeySummary);
        tvBar = (TextView)findViewById(R.id.tvbar);

        btnNext = findViewById(R.id.btnNext);
        btnClose = findViewById(R.id.btnClose);
        btnDelete = findViewById(R.id.btnDeleteKeyword);
        btnSetting = findViewById(R.id.btnSetting);


        long now= System.currentTimeMillis();
        Date date = new Date(now);

        SimpleDateFormat sdfnow = new SimpleDateFormat("yyyy년 MM월 dd일 HH:mm");

        tvBar.setText(sdfnow.format(date));






        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getRandomDetails();
            }
        });





        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.finish();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(MainActivity.this);
                dialogbuilder.setMessage("정말로 삭제하시겠습니까?");
                dialogbuilder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteKeyword();
                        getRandomDetails();
                        Toast.makeText(MainActivity.this,"삭제되었습니다",Toast.LENGTH_LONG).show();
                    }
                });

                dialogbuilder.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Toast.makeText(MainActivity.this,"다행이군요",Toast.LENGTH_LONG).show();
                    }
                });

                AlertDialog ad = dialogbuilder.create();
                ad.show();


            }
        });


    }


    public void openKeywordList(View view){
        Intent intent = new Intent(MainActivity.this, activity_bringkeyword.class);

        startActivityForResult(intent,REQUEST_KEYWORD);


    }


    @Override
    protected void onActivityResult(int recode,int resultCode,Intent data){

        super.onActivityResult(recode,resultCode,data);

        if(recode == REQUEST_KEYWORD){
        if(resultCode == RESULT_OK){
         // 테스트용코드
         //   Toast.makeText(MainActivity.this,"가져온값 ID= "+data.getStringExtra("no"),Toast.LENGTH_SHORT).show();
            getDetails(data.getStringExtra("no"));

        }
        }

        if(recode == EDITED_KEYWORD){
            if(resultCode == RESULT_OK){
             //테스트용 코드
             //   Toast.makeText(MainActivity.this,"가져온 ID값 = "+data.getStringExtra("newNo"),Toast.LENGTH_SHORT).show();
                getDetails(data.getStringExtra("newNo"));

            }
        }



    }


    //bringkeyword에서 가져온 keywordDo의 no값을 여기다 넣고 그 no값을 기반으로 한 쿼리문을 이용해 값을 찾아옴.
    private void getDetails(String no){

        dbman = new YourNoteDBMan(this);
        database = dbman.getReadableDatabase();


        currentNo = no;

        String sql = "SELECT keyword.keyword,category.categoryname,keyword.summary,keyword.importance FROM keyword INNER JOIN category ON keyword.categoryid = category.id WHERE keyword.id="+currentNo;


        cursor = database.rawQuery(sql,null);



        while(cursor.moveToNext()){

            String keyword = cursor.getString(0);
            String category = cursor.getString(1);
            String summary = cursor.getString(2);



            tvKeyText.setText(keyword);
            tvKeyCategory.setText("["+category+"]");
            tvKeySummary.setText(summary);


        }


        dbman.close();
        database.close();
    }


    public void getRandomDetails(){
        dbman = new YourNoteDBMan(this);
        database = dbman.getReadableDatabase();




        String sql = "SELECT keyword.keyword,category.categoryname,keyword.summary,keyword.id FROM keyword INNER JOIN category ON keyword.categoryid = category.id ORDER BY RANDOM() LIMIT 1";


        cursor = database.rawQuery(sql,null);



        while(cursor.moveToNext()){

            String keyword = cursor.getString(0);
            String category = cursor.getString(1);
            String summary = cursor.getString(2);
            currentNo = String.valueOf(cursor.getInt(3)).trim();



            tvKeyText.setText(keyword);
            tvKeyCategory.setText("["+category+"]");
            tvKeySummary.setText(summary);


        }


        dbman.close();
        database.close();

    }



    public void addKeyword(View view){
        Intent intent = new Intent(MainActivity.this, activity_managekeyword.class);


        activity_managekeyword.md ="add";

        startActivityForResult(intent,EDITED_KEYWORD);


    }

    public void modifyKeyword(View view){

        Intent intent = new Intent(MainActivity.this, activity_managekeyword.class);


        activity_managekeyword.md ="modify";
        activity_managekeyword.currentNo=currentNo;

        startActivityForResult(intent,EDITED_KEYWORD);



    }


    public void deleteKeyword(){

        dbman = new YourNoteDBMan(this);
        database = dbman.getReadableDatabase();

        String sql = "DELETE FROM keyword WHERE id="+currentNo;

        database.execSQL(sql);


        dbman.close();
        database.close();

    }


    public void settings(View view){

        Intent intent = new Intent(this,activity_Settings.class);

        startActivity(intent);


    }




}
