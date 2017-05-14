package com.example.android.navigationdrawerexample;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by TranDaiNhan on 5/4/2017.
 */

public class MonAnAdapter extends ArrayAdapter implements Serializable {

    Context context;
    ArrayList<MonAn> monAnArrayList;
    public static DonHangData danhSachDonHang;

    public MonAnAdapter(@NonNull Context context, @NonNull ArrayList<MonAn> objects) {
        super(context, 0, objects);
        this.context = context;
        monAnArrayList = objects;
        danhSachDonHang = new DonHangData();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final MonAn monAn = monAnArrayList.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_item_monan, parent, false);
        }

        TextView txtTen = (TextView) convertView.findViewById(R.id.txtTenMonAn);
        TextView txtMoTa = (TextView) convertView.findViewById(R.id.txtMoTa);



        final TextView txtGia = (TextView) convertView.findViewById(R.id.txtGia);
        final ImageView imageView = (ImageView) convertView.findViewById(R.id.imgMonAn);
        final Button button = (Button) convertView.findViewById(R.id.btnDatMua);


        String tien = String.valueOf(monAn.gia);

        String k = "";

        int count = 0;

        for(int i = tien.length() - 1; i >=0 ; i--) {

            k = k + tien.charAt(i); count ++;

            if(count == 3) {
                k = k + ".";
                count =0;
            }
        }

        k = new StringBuilder(k).reverse().toString();

        String hk = "Giá: " + k + " VNĐ";
        txtGia.setText(hk);
        txtTen.setText(monAn.tenMonAn);
        txtMoTa.setText("Thành phần: "+ monAn.moTo);


        new DownloadImageTask(imageView)
                .execute("http://lorempixel.com/400/400/food/");


        button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.layout_dialog_hoadon);
                dialog.setTitle("Chi tiết thanh toán");

                final TextView txtGiaDialog = (TextView) dialog.findViewById(R.id.txtThanhTien) ;
                Button buttonDown = (Button) dialog.findViewById(R.id.btnGiam);
                Button buttonUp = (Button) dialog.findViewById(R.id.btnTang);
                Button buttonMua = (Button) dialog.findViewById(R.id.btnMuaHang);
                Button buttonHuy = (Button) dialog.findViewById(R.id.btnHuy);
                final TextView editTextSoLuong = (TextView) dialog.findViewById(R.id.editTextGia);

                txtGiaDialog.setText(String.valueOf(monAn.gia));


                buttonDown.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int i = Integer.valueOf(editTextSoLuong.getText().toString());
                        if(i == 1 ||editTextSoLuong.getText().toString().equals("") ==true ) return;
                        editTextSoLuong.setText(String.valueOf(i-1));
                        txtGiaDialog.setText(String.valueOf((i - 1)  * monAn.gia));
                    }
                });


                buttonUp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(editTextSoLuong.getText().toString().equals("") ==true ) return;
                        int i = Integer.valueOf(editTextSoLuong.getText().toString());
                        editTextSoLuong.setText(String.valueOf(i+1));
                        txtGiaDialog.setText(String.valueOf((i + 1) * monAn.gia));
                    }
                });


                buttonHuy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                buttonMua.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ChiTietDonHangData chiTietDonHangData = new ChiTietDonHangData();
                        chiTietDonHangData.SoLuong = Integer.valueOf(editTextSoLuong.getText().toString());
                        chiTietDonHangData.MonAnId = monAn.monAnId;
                        danhSachDonHang.ChiTietDonHangDatas.add(chiTietDonHangData);
                        Toast.makeText(getContext(),monAn.monAnId+"",Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });

                dialog.show();

            }
        });

        return convertView;
    }
}

class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;

    public DownloadImageTask(ImageView bmImage) {
        this.bmImage = bmImage;
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        bmImage.setImageBitmap(result);
    }
}
