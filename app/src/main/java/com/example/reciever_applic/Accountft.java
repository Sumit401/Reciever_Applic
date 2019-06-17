package com.example.reciever_applic;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class Accountft extends Fragment {
    String mobile;
    String url="https://safesteps.in/android2/accdetailsstudent.php";
    ImageView accountpic;
    TextView accountname,accountclass,accountdob,accountadmission,accountcontact,accountguardian,accountaddress;
    String name,address,admission,dob,stu_class,pic,guardian;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.account,container,false);
        accountname=view.findViewById(R.id.accountname);
        accountclass=view.findViewById(R.id.accountclass);
        accountdob=view.findViewById(R.id.accountdob);
        accountadmission=view.findViewById(R.id.accountadmissionno);
        accountcontact=view.findViewById(R.id.accountcontact);
        accountguardian=view.findViewById(R.id.accountguardian);
        accountaddress=view.findViewById(R.id.accountaddress);
        accountpic=view.findViewById(R.id.accountpic);

        SharedPreferences preferences = getContext().getSharedPreferences("Login", Context.MODE_PRIVATE);
        mobile = preferences.getString("Mobile", null);

        SharedPreferences preferences1=getContext().getSharedPreferences("Account",Context.MODE_PRIVATE);
        name=preferences1.getString("Name",null);
        address=preferences1.getString("Address",null);
        admission=preferences1.getString("Admission",null);
        dob=preferences1.getString("DOB",null);
        stu_class=preferences1.getString("Class",null);
        guardian=preferences1.getString("Guardian",null);
        pic=preferences1.getString("Pic",null);

        if (name==null && address==null && admission==null && dob==null && stu_class==null && guardian==null && pic==null) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("mobile", mobile);
                Getdata getdata = new Getdata();
                getdata.execute(jsonObject.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else {
            accountname.setText(name);
            accountclass.setText(stu_class);
            accountcontact.setText(mobile);
            accountdob.setText(dob);
            accountaddress.setText(address);
            accountguardian.setText(guardian);
            accountadmission.setText(admission);
            accountcontact.setText(mobile);
            byte[] decodedString = Base64.decode(pic, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            accountpic.setImageBitmap(decodedByte);
        }
        return view;
    }

    private class Getdata extends AsyncTask<String,String,String> {
        ProgressDialog progressDialog=new ProgressDialog(getContext());
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Please Wait");
            progressDialog.setTitle("Loading");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            JSONObject jsonObject=JsonFunction.GettingData(url,params[0]);
            if (jsonObject==null)
                return "NULL";
            else
                return jsonObject.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.equalsIgnoreCase("NULL")){
                Toast.makeText(getContext(),"No Internet",Toast.LENGTH_SHORT).show();
                try {
                    JSONObject jsonObject=new JSONObject();
                    jsonObject.put("mobile",mobile);
                    Getdata getdata=new Getdata();
                    getdata.execute(jsonObject.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            progressDialog.dismiss();
            try {
                JSONObject jsonObject=new JSONObject(s);
                if (jsonObject.getString("success").equalsIgnoreCase("success")){
                    name=jsonObject.getString("name");
                    stu_class=jsonObject.getString("class");
                    dob=jsonObject.getString("dob");
                    address=jsonObject.getString("address");
                    admission=jsonObject.getString("admission");
                    guardian=jsonObject.getString("guardian");
                    pic=jsonObject.getString("pic");

                    accountname.setText(name);
                    accountclass.setText(stu_class);
                    accountdob.setText(dob);
                    accountaddress.setText(address);
                    accountguardian.setText(guardian);
                    accountadmission.setText(admission);
                    accountcontact.setText(mobile);

                    byte[] decodedString = Base64.decode(pic, Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    accountpic.setImageBitmap(decodedByte);

                    SharedPreferences preferences2=getContext().getSharedPreferences("Account",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor=preferences2.edit();
                    editor.putString("Name",name);
                    editor.putString("Address",address);
                    editor.putString("Admission",admission);
                    editor.putString("DOB",dob);
                    editor.putString("Class",stu_class);
                    editor.putString("Guardian",guardian);
                    editor.putString("Pic",pic);
                    editor.apply();

                }else {
                    Toast.makeText(getContext(),"Error",Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }
}
