/*
 * Copyright 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.navigationdrawerexample.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;

import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.navigationdrawerexample.ChiTietDonHangData;
import com.example.android.navigationdrawerexample.DanhMuc;
import com.example.android.navigationdrawerexample.DanhMucAdapter;
import com.example.android.navigationdrawerexample.DonHangData;
import com.example.android.navigationdrawerexample.FoodFragment;
import com.example.android.navigationdrawerexample.GsonHelper;
import com.example.android.navigationdrawerexample.KhachHang;
import com.example.android.navigationdrawerexample.MonAn;
import com.example.android.navigationdrawerexample.MonAnAdapter;
import com.example.android.navigationdrawerexample.R;
import com.example.android.navigationdrawerexample.UrlList;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class MainActivity extends Activity {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private ListView listView;
    private Button btnDatMua;

    //Url
    public static String URL_DANHMUC = "http://api-orderfood.azurewebsites.net/danhmuc/";

    // List
    public static ArrayList<DanhMuc> danhMucArrayList = new ArrayList<DanhMuc>();

    //Adapter
    public DanhMucAdapter danhMucAdapter;
    public MonAnAdapter monAnAdapter;

    //AsyncTask
    public JSONTask jsonTask;
    public JSONKiemTraKhachHang jsonKiemTraKhachHang;
    public String trangThaiDatHang = "";
    public boolean trangThaiKhachHang;
    public KhachHang khachHang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        jsonTask = (JSONTask) new JSONTask().execute(URL_DANHMUC);


        danhMucAdapter = new DanhMucAdapter(getApplicationContext(), danhMucArrayList);


        mTitle = mDrawerTitle = getTitle();
        // mPlanetTitles = new String[]{"1","2","3","4","5","6","7"};
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener
        mDrawerList.setAdapter(danhMucAdapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);


        monAnAdapter = new MonAnAdapter(this, new ArrayList<MonAn>());

        listView = (ListView) findViewById(R.id.lstMonAn);
        btnDatMua = (Button) findViewById(R.id.btnDatMua);



        if (savedInstanceState == null) {

        }


    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_buy).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        // Handle action buttons
        switch(item.getItemId()) {
            case R.id.action_buy:
                if(DonHangData.ChiTietDonHangDatas.size() == 0) {
                    Toast.makeText(getApplicationContext(),"Danh sách mua hàng rỗng",Toast.LENGTH_SHORT).show();
                    return true;
                }

                TelephonyManager tm = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
                String number = tm.getLine1Number();

                if(number.equals("")) {
                    number = "0949337629";
                }

                DonHangData.Sdt = number;

                new JSONKiemTraKhachHang().execute();

                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.setTitle("Thông tin mua hàng");
                dialog.setContentView(R.layout.fragment_hoadon);
                AdapterDanhSachDatHang adapterDanhSachDatHang = new AdapterDanhSachDatHang(getApplicationContext(), DonHangData.ChiTietDonHangDatas);
                ListView listView = (ListView) dialog.findViewById(R.id.lstDanhSachMatHangMua);
                listView.setAdapter(adapterDanhSachDatHang);


                Button buttonDatHang = (Button) dialog.findViewById(R.id.btnXacNhanMuaHang2);




                buttonDatHang.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(DonHangData.ChiTietDonHangDatas.size() == 0) {
                            dialog.dismiss();
                            return;
                        }

                        if(trangThaiKhachHang == false) {

                        }
                        else {

                            final Dialog dialog3 = new Dialog(MainActivity.this);
                            dialog3.setTitle("Thông tin mua hàng");
                            dialog3.setContentView(R.layout.layout_custom_thong_tin_nguoi_mua);
                            Button btnXacNhan = (Button) dialog3.findViewById(R.id.btnXacNhanDatMua2);
                            Button btnHuy = (Button) dialog3.findViewById(R.id.btnHuyDatMua2);
                            final EditText sdt = (EditText) dialog3.findViewById(R.id.txtSoDienThoai);
                            EditText diachi = (EditText) dialog3.findViewById(R.id.txtDiaChiGiaoNhan);
                            sdt.setText(DonHangData.Sdt);

                            btnXacNhan.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    dialog3.dismiss();
                                }
                            });
                            btnHuy.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog3.dismiss();
                                }
                            });
                            dialog3.show();
                        }
                    }
                });
                dialog.show();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {

        FoodFragment foodFragment = FoodFragment.newInstance(String.valueOf(danhMucArrayList.get(position).danhMucId), monAnAdapter);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, foodFragment).commit();


        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);
        setTitle(danhMucArrayList.get(position).tenDanhMuc);
        mDrawerLayout.closeDrawer(mDrawerList);


    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    // Json Danh Muc

    public class JSONTask extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("\t Xin vui lòng đợi trong giây lát");
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {

                URL url = new URL(params[0] + "getdanhmucs");
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                connection.setConnectTimeout(10);

                connection.setReadTimeout(10);

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();


                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                String jsonString = buffer.toString();


                JSONArray jsonArray = new JSONArray(jsonString);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    int id = jsonObject.getInt("danhMucId");
                    String name = jsonObject.getString("tenDanhMuc");
                    String hinh = jsonObject.getString("hinh");
                    DanhMuc danhMuc = new DanhMuc(id, name, hinh);
                    danhMucArrayList.add(danhMuc);
                }

                return jsonString;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            danhMucAdapter.notifyDataSetChanged();
            selectItem(0);
            progressDialog.dismiss();
        }
    }

    public String timTenMonAn(int id) {
        String a = "";
        for(int i = 0; i < danhMucArrayList.size(); i++) {
            for(int j = 0; j < danhMucArrayList.get(i).monAnArrayList.size(); j++) {
                if(danhMucArrayList.get(i).monAnArrayList.get(j).monAnId == id) {
                   a = danhMucArrayList.get(i).monAnArrayList.get(j).tenMonAn;
                    return a;
                }
            }
        }
        return a;
    }

    class AdapterDanhSachDatHang extends ArrayAdapter {

        Context context;
        ArrayList<ChiTietDonHangData> chiTietDonHangDataArrayList;

        public AdapterDanhSachDatHang(@NonNull Context context, @NonNull ArrayList<ChiTietDonHangData> objects) {
            super(context, 0, objects);
            this.context = context;
            this.chiTietDonHangDataArrayList = objects;
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            if(convertView == null) {
                convertView = LayoutInflater.from(this.context).inflate(R.layout.layout_item_danhsachdatmua,null);
            }

            final ChiTietDonHangData chiTietDonHangData = chiTietDonHangDataArrayList.get(position);

            TextView txtSoLuong = (TextView) convertView.findViewById(R.id.txtSoLuong);
            TextView txtTenMatHang = (TextView) convertView.findViewById(R.id.txtTenMatHang);
            txtSoLuong.setText(String.valueOf(chiTietDonHangData.SoLuong));

            String a = timTenMonAn(chiTietDonHangData.MonAnId);
            txtTenMatHang.setText(a);


            Button buttonXoa = (Button) convertView.findViewById(R.id.btnXoaThongTinDatHang);


            buttonXoa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chiTietDonHangDataArrayList.remove(position);
                    notifyDataSetChanged();
                }
            });


            Button buttonSua = (Button) convertView.findViewById(R.id.btnSuaThongTinDatHang);
            buttonSua.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog dialog2 = new Dialog(MainActivity.this);
                    dialog2.setContentView(R.layout.layout_edit_count_custom);
                    dialog2.setTitle("Thông tin chỉnh sửa");

                    final EditText editText = (EditText) dialog2.findViewById(R.id.editTextSoLuong);
                    Button buttonXN = (Button) dialog2.findViewById(R.id.btnXacNhanTTSua);
                    Button buttonHuy = (Button) dialog2.findViewById(R.id.btnHuyTTSua);
                    buttonHuy.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog2.dismiss();
                        }
                    });


                    buttonXN.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String a = editText.getText().toString();
                            if(a.equals("") == true || a.equals("0") ==true) {
                                dialog2.dismiss();
                            }
                            else {
                                chiTietDonHangDataArrayList.get(position).SoLuong = Integer.valueOf(editText.getText().toString());
                                notifyDataSetChanged();
                                dialog2.dismiss();
                            }
                        }
                    });

                    dialog2.show();
                }
            });
            return convertView;
        }
    }


    public class JSONDonHang extends AsyncTask<DonHangData, Void , String> {
        private ProgressDialog progressDialog;
        private final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Xin vui lòng đợi trong giây lát");
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(DonHangData... params) {
            try {
            OkHttpClient client = new OkHttpClient();
            RequestBody body = RequestBody.create(MEDIA_TYPE, GsonHelper.toJson(params[0]));
            Request request = new Request.Builder().url(UrlList.POST_DON_HANG).post(body).build();
                Response response = client.newCall(request).execute();
                return response.body().string();
            }catch (Exception e) {
                try {
                    throw e;
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            trangThaiDatHang = s;
        }
    }

    public class JSONKiemTraKhachHang extends  AsyncTask<Void , Void, String> {

        private ProgressDialog progressDialog;
        private OkHttpClient client;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Xin vui lòng đợi trong giây lát");
            progressDialog.setCancelable(true);
            progressDialog.show();
        }


        @Override
        protected String doInBackground(Void ... params) {
            try {
                String k = UrlList.GET_KHACH_HANG + "/" + DonHangData.Sdt;
                client = new OkHttpClient();
                 Request request = new Request.Builder()
                    .url(k)
                    .build();

            Response response;


                response = client.newCall(request).execute();
                return response.body().string();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if(s.equals("null")) {
                trangThaiKhachHang = false;
                progressDialog.dismiss();
                return;
            }
            else {
                trangThaiKhachHang = true;
                try {
                    JSONArray jsonArray = new JSONArray(s);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        khachHang.Sdt = jsonObject.getString("sdt");
                        khachHang.Ten = jsonObject.getString("ten");
                        khachHang.DiaChi = jsonObject.getString("diaChi");
                    }
                    DonHangData.Sdt = khachHang.Sdt;
                    progressDialog.dismiss();
                    return;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}

