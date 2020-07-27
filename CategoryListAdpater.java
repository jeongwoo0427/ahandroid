package com.example.yournote;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CategoryListAdpater extends BaseAdapter {


    TextView tvCategory;

    ArrayList<CategoryDo> arryCategoryDo = new ArrayList<CategoryDo>();

    @Override
    public int getCount() {
        return arryCategoryDo.size();
    }

    @Override
    public Object getItem(int position) {
        return arryCategoryDo.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final int pos = position;

        final Context context = parent.getContext();


            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.categorylistview_item,parent,false);


            tvCategory = (TextView) convertView.findViewById(R.id.tvCategory);

            CategoryDo cDo = arryCategoryDo.get(position);
            tvCategory.setText(cDo.getText());





        return convertView;


    }

    public void addItem(String no, String categoryName){


        CategoryDo cDo = new CategoryDo();

        cDo.setcNo(no);
        cDo.setText(categoryName);

        arryCategoryDo.add(cDo);


        keywordDo kDo = new keywordDo();



    }


}
