package com.example.reciever_applic;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Objects;

public class MainActivity2 extends AppCompatActivity {

    int id1=0;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            switch (item.getItemId()) {
                case R.id.navigation_home: {
                    if (id1!=0){
                    MapsActivity map = new MapsActivity();
                    fragmentTransaction.replace(R.id.frameprofile, map);
                    fragmentTransaction.commit();
                    id1=0;
                    return true;
                    }
                    break;
                }
                case R.id.navigation_dashboard: {
                 if (id1!=1) {
                     Dashboard dashboard = new Dashboard();
                     fragmentTransaction.replace(R.id.frameprofile, dashboard);
                     fragmentTransaction.commit();
                     id1=1;
                     return true;
                    }
                 break;
                }
                case R.id.navigation_notifications: {
                    if (id1!=2) {
                        Accountft acc = new Accountft();
                        fragmentTransaction.replace(R.id.frameprofile, acc);
                        fragmentTransaction.commit();
                        id1=2;
                        return true;
                    }
                    break;
                }
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        FragmentTransaction ft=this.getSupportFragmentManager().beginTransaction();
        MapsActivity map=new MapsActivity();
        ft.replace(R.id.frameprofile,map);
        ft.commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.logout) {
            SharedPreferences settings = this.getSharedPreferences("Login", MODE_PRIVATE);
            settings.edit().clear().apply();
            final ProgressDialog progressDialog=new ProgressDialog(this);
            progressDialog.setMessage("Wait");
            progressDialog.setTitle("Logging Out");
            progressDialog.setCancelable(false);
            progressDialog.show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressDialog.dismiss();
                    Intent intent=new Intent(MainActivity2.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            },1000);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences preferences=this.getSharedPreferences("Account",MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.clear();
        editor.apply();
    }
}