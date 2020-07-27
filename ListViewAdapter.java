package com.example.yournote;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {

    private TextView tvKeyword,tvcate;

    public ArrayList<keywordDo>  arrKeyDo = new ArrayList<keywordDo>();


    @Override
    public int getCount() {
        return arrKeyDo.size();
    }


    //arrkeydo배열 중 선택한 포지션의 kdo객체 1개짜리를 반환
    @Override
    public Object getItem(int position) {
        return arrKeyDo.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        final Context context = parent.getContext();


        // 아래 코드를 주석처리한 이유는 리스트뷰를 스크롤할때 순서가 엉망진창이 됨..
        // 아마 화면에 안띄어진 부분은 null 로 인식이되고 띄어질때 not null 이 되는거 같다 그래서 화면에 안보여지는 순간 새로운 값들이 채워지는듯..
       // if(convertView ==null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.listview_item,parent,false);

            tvKeyword = (TextView) convertView.findViewById(R.id.tvkeyword);
            tvcate = (TextView)convertView.findViewById(R.id.tvcate);



            //아이템을 추가할때 kdo배열 중 포지션 한개를 가져와서
            //kdo 1개짜리 객체 새로 생성 후 거기다 아까 배열에서 가져온거 박아두고
            //kdo 1개짜리 객체에 저장된 값을 가져오는 것
            keywordDo kDo = arrKeyDo.get(position);
            tvKeyword.setText(kDo.getKeyword());
            tvcate.setText(kDo.getCategory()+"/");





       // }


        return convertView;
    }

    public void addItem(String no, String category,String keyword){


        keywordDo kDo = new keywordDo();

        kDo.setNo(no);
        kDo.setCategory(category);
        kDo.setKeyword(keyword);


        //배열에 아이템을 추가시키기 위해선 같은 형식의 오브젝트로 넣어야하니
        //1개짜리 kDo객체에 값들 넣고
        //1개짜리를 배열에 추가시킨다.
        arrKeyDo.add(kDo);


    }



}
