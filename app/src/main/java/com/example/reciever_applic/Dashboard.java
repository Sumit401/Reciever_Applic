package com.example.reciever_applic;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class Dashboard extends Fragment {
    RecyclerView recyclerView;
    ArrayList<String> TitleArray=new ArrayList<>();
    ArrayList<String> IdArray=new ArrayList<>();
    ArrayList<String> UrlArray=new ArrayList<>();
    TextView textView;
    String mobile;
    String url="https://safesteps.in/android2/getattendence.php";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.dashoard,container,false);
        recyclerView=view.findViewById(R.id.recycler);

        SharedPreferences preferences= Objects.requireNonNull(getContext()).getSharedPreferences("Login", Context.MODE_PRIVATE);
        mobile=preferences.getString("Mobile",null);
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("mobile",mobile);
            jsonObject.put("action","get_data");
            LoadData loadData=new LoadData();
            loadData.execute(jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return view;
    }

    @SuppressLint("StaticFieldLeak")
    private class LoadData extends AsyncTask<String,String,String> {

        ProgressDialog progressDialog=new ProgressDialog(getContext());
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Please Wait");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            JSONObject jsonObject=JsonFunction.GettingData(url,params[0]);
            return jsonObject.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.equalsIgnoreCase("NULL")){
                Toast.makeText(getContext(),"No Internet",Toast.LENGTH_SHORT).show();
            }else {
                progressDialog.dismiss();

                try {
                    JSONObject j1=new JSONObject(s);

                    String res=j1.getString("response");
                    if(res.equalsIgnoreCase("success")) {

                        JSONArray jsonArray = j1.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject j2 = jsonArray.getJSONObject(i);
                            String status = j2.getString("status");
                            String date = j2.getString("date");

                            if (status.equalsIgnoreCase("1"))
                                status="Present";
                            else
                                status="Absent";

                            TitleArray.add(date);
                            IdArray.add(status);
                            UrlArray.add(String.valueOf(i+1));
                        }

                        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext(),
                                LinearLayoutManager.VERTICAL,false);
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setAdapter(new RecycleAdp(getContext(),TitleArray,UrlArray,IdArray));
                    }else {
                        textView.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
