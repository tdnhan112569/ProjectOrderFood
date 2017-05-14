package com.example.android.navigationdrawerexample;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * Created by TranDaiNhan on 5/12/2017.
 */

public class FragmentHoaDon extends Fragment {
    public static String GET_CONTENT = "";
    
    Context context;

//    public static FragmentHoaDon newInstance(Context context) {
//
//        Bundle args = new Bundle();
//        args.putSerializable(GET_CONTENT, (Serializable) context);
//        FragmentHoaDon fragment = new FragmentHoaDon();
//        fragment.setArguments(args);
//        return fragment;
//    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hoadon,container,false);
        ListView listView = (ListView) view.findViewById(R.id.lstDanhSachMatHangMua);

        return view;
    }
}
