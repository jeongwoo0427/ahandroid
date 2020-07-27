package com.example.yournote;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class activity_bringcategory extends AppCompatActivity {

    YourNoteDBMan dbman;
    SQLiteDatabase database;
    CategoryListAdpater adapter;
    Cursor cursor;

    Button btnAddCategory;
    EditText etAddCategory;
    ListView categoryListView;


    String selectedNo;
    String selectedText;
    int md;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bringcategory);


        btnAddCategory = (Button)findViewById(R.id.btnCategoryAdd);
        etAddCategory = (EditText)findViewById(R.id.etAddCategory);

        categoryListView = (ListView)findViewById(R.id.categoryListView);

        select();






        categoryListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {




                CategoryDo cdo = (CategoryDo)adapter.getItem(position);


                selectedNo = cdo.getNo();
                selectedText = cdo.getText();

                if(selectedNo.equals("1")){
                    Toast.makeText(activity_bringcategory.this,"전체 카테고리는 수정하거나 삭제할 수 없습니다.",Toast.LENGTH_LONG).show();
                    return false;
                }


                //온크레이트로 다이얼로그 객체를 완전 초기화 시킨후 보여줌
                onCreateDialog(1).show();

                //처음에만 초기화하고 그다음부터 우려먹음;;
                //showDialog(1);




                return false;
            }
        });



        btnAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(etAddCategory.getText().toString().trim().equals("")) {

                    Toast.makeText(activity_bringcategory.this,"카테고리명을 입력하세요..오류나요..",Toast.LENGTH_LONG).show();
                    return;
                }

                btnAddCategory.setText("추가");


                if(md==2){

                    EditCategory();



                }else{
                    addNew();
                }

                md=1;

                select();

                etAddCategory.setText("");

            }
        });

    }









    protected Dialog onCreateDialog(int id){
        switch(id){
            case 1:

                final CharSequence[] items ={"수정","삭제"};
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("'"+selectedText+"' 에 대한 작업을 선택하세요");
                builder.setItems(items,new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if(which ==0){
                            //다이얼로그에서 수정을 누를 시
                            etAddCategory.setText(selectedText);
                            btnAddCategory.setText("완료");
                            md=2;
                        }else if(which ==1){
                            //다이얼로그에서 삭제를 눌를 시


                            androidx.appcompat.app.AlertDialog.Builder dialogbuilder = new androidx.appcompat.app.AlertDialog.Builder(activity_bringcategory.this);
                            dialogbuilder.setMessage("경고!! 카테고리 '"+selectedText+"' 와 관련된 모든 키워드가 사라집니다.. 정말로 삭제하시겠습니까?");
                            dialogbuilder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    deleteCategory();

                                    Toast.makeText(activity_bringcategory.this,"삭제되었습니다",Toast.LENGTH_LONG).show();
                                }
                            });

                            dialogbuilder.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    Toast.makeText(activity_bringcategory.this,"취소됐습니다.",Toast.LENGTH_LONG).show();
                                }
                            });

                            androidx.appcompat.app.AlertDialog ad = dialogbuilder.create();
                            ad.show();



                        }



                    }
                });

                AlertDialog alert =builder.create();
                return alert;
        }

        return null;

    }










    private void deleteCategory(){

        dbman = new YourNoteDBMan(this);
        database = dbman.getReadableDatabase();


        //카테고리와 관련된 키워드를 전부 삭제해버리고

        String sql = "DELETE FROM keyword WHERE categoryid="+selectedNo;

        database.execSQL(sql);

        //카테고리까지 삭제한다.
        sql = "DELETE FROM category WHERE id="+selectedNo;

        database.execSQL(sql);

        dbman.close();
        database.close();

        select();
    }










    private void addNew(){


        String categoryName= etAddCategory.getText().toString();


        dbman = new YourNoteDBMan(this);
        database = dbman.getWritableDatabase();

        String sql = "INSERT INTO category(categoryname) VALUES('"+categoryName+"');";

        database.execSQL(sql);



        //새로 생성한 레코드의 ID값을 가져온다

        dbman.close();
        database.close();



        // Toast.makeText(activity_managekeyword.this,"에러발생",Toast.LENGTH_SHORT).show();







    }


    private void EditCategory(){


        YourNoteDBMan dbmanager = new YourNoteDBMan(this);
        SQLiteDatabase db = dbman.getWritableDatabase();

        String sql = "UPDATE category SET categoryname = '"+etAddCategory.getText()+"' WHERE id="+selectedNo;

        db.execSQL(sql);



        dbman.close();
        database.close();

        Toast.makeText(activity_bringcategory.this,"수정완료 "+selectedText+" > "+etAddCategory.getText(),Toast.LENGTH_LONG).show();

        etAddCategory.setText("");

    }



    private void select(){


        dbman = new YourNoteDBMan(this);
        database = dbman.getReadableDatabase();
        adapter = new CategoryListAdpater();

        categoryListView.setAdapter(adapter);

        String sql = "SELECT * FROM category  ORDER BY id DESC";

        cursor = database.rawQuery(sql,null);


        //커서에 담은 테이블의 레코드 개수만큼 순회시키고 그만큼 리스트뷰에 추가시킨다.
        while(cursor.moveToNext()){
            int id = cursor.getInt(0);
            String keyword = cursor.getString(1);

            adapter.addItem(String.valueOf(id),keyword);

        }


        dbman.close();
        database.close();
    }


}
