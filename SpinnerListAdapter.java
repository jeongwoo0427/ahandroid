package com.example.yournote;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class SpinnerListAdapter extends BaseAdapter{


    ArrayList<CategoryDo> arrycate = new ArrayList<CategoryDo>();



    @Override
    public int getCount() {
        return arrycate.size();
    }

    @Override
    public Object getItem(int position) {
        return arrycate.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {



        final Context context = parent.getContext();



        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        convertView = inflater.inflate(R.layout.spinner_item,parent,false);

        TextView tvcate = (TextView)convertView.findViewById(R.id.txtcate);


        CategoryDo cDo = arrycate.get(position);
        tvcate.setText(cDo.getText());
        tvcate.setTag(cDo.getNo());




        return convertView;
    }



    public void addItem(String no, String categoryname){


        CategoryDo cDo = new CategoryDo();

        cDo.setcNo(no);
        cDo.setText(categoryname);

        arrycate.add(cDo);



    }

}
