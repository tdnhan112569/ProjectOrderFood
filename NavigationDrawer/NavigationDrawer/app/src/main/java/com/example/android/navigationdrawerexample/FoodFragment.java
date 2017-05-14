package com.example.android.navigationdrawerexample;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.android.navigationdrawerexample.Activity.MainActivity;

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

/**
 * Created by TranDaiNhan on 5/5/2017.
 */

public class FoodFragment extends Fragment {

    public static final String ARG_DANH_MUC_ID = "DMID";
    public static final String ARG_MON_AN_ADAPTER = "MAA";

    private String danhMucId;
    private MonAnAdapter monAnAdapter;


    public FoodFragment() {
    }


    public static FoodFragment newInstance(String danhMucId, MonAnAdapter monAnAdapter) {

        Bundle args = new Bundle();
        args.putString(FoodFragment.ARG_DANH_MUC_ID, danhMucId);
        args.putSerializable(ARG_MON_AN_ADAPTER, monAnAdapter);
        FoodFragment fragment = new FoodFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        danhMucId = getArguments().getString(ARG_DANH_MUC_ID);

        monAnAdapter = (MonAnAdapter) getArguments().getSerializable(ARG_MON_AN_ADAPTER);

        (new UpdateMonAnAdapterTask(danhMucId, monAnAdapter)).execute();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_food, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.lstMonAn);
        listView.setAdapter(monAnAdapter);

        Button buttonDatMua = (Button) rootView.findViewById(R.id.btnDatMua);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        return rootView;
    }
}

class UpdateMonAnAdapterTask extends AsyncTask<Void, Integer, ArrayList<MonAn>> {

    private ProgressDialog progressDialog;
    private String danhMucId;
    private MonAnAdapter monAnAdapter;


    public UpdateMonAnAdapterTask(String danhMucId, MonAnAdapter monAnAdapter) {
        this.danhMucId = danhMucId;
        this.monAnAdapter = monAnAdapter;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected ArrayList<MonAn> doInBackground(Void... params) {

        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {

            URL url = new URL(UrlList.GET_MON_AN + "/" + danhMucId);
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

            ArrayList<MonAn> monAnArrayList = new ArrayList<>();

            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int id = jsonObject.getInt("monAnId");
                String name = jsonObject.getString("tenMonAn");
                int tien = jsonObject.getInt("gia");
                String moTa = jsonObject.getString("moTa");
                MonAn monAn = new MonAn(id, name, tien, moTa);
                monAnArrayList.add(monAn);
            }
            return monAnArrayList;

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
    protected void onPostExecute(ArrayList<MonAn> monen) {
        super.onPostExecute(monen);
        monAnAdapter.monAnArrayList.clear();
        MainActivity.danhMucArrayList.get(Integer.valueOf(danhMucId) - 1).monAnArrayList.clear();
        MainActivity.danhMucArrayList.get(Integer.valueOf(danhMucId) - 1).monAnArrayList.addAll(monen);
        monAnAdapter.monAnArrayList.addAll(monen);
        monAnAdapter.notifyDataSetChanged();
    }
}


