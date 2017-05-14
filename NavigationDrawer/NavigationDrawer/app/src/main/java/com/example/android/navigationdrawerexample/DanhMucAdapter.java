package com.example.android.navigationdrawerexample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android.navigationdrawerexample.DanhMuc;
import com.example.android.navigationdrawerexample.R;

import java.util.ArrayList;

/**
 * Created by TranDaiNhan on 5/4/2017.
 */

public class DanhMucAdapter extends ArrayAdapter {

    Context context;
    ArrayList<DanhMuc> danhMucArrayList = new ArrayList<>();

    public DanhMucAdapter( Context context,  ArrayList<DanhMuc> objects) {
        super(context,0, objects);
        this.context = context;
        this.danhMucArrayList = objects;
    }


    @Override
    public View getView(int position,  View convertView,  ViewGroup parent) {
        DanhMuc danhMuc;
        danhMuc = danhMucArrayList.get(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.drawer_list_item,parent,false);
        }

        TextView txtName =  (TextView) convertView.findViewById(R.id.text1);

        txtName.setText(danhMuc.tenDanhMuc);

        return convertView;
    }
}
