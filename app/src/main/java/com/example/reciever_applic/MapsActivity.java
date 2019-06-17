package com.example.reciever_applic;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

public class MapsActivity extends Fragment implements OnMapReadyCallback {

    Marker marker;
    LatLng busloc;
    LatLng parentsloc = new LatLng(25.577766, 85.056102);
    GoogleMap mMap;
    MapView mMapView;
    int duration;
    String s1,s2;
    Polyline currentPolyline;
    int z=0,w=0;
    Handler handler;
    FloatingActionButton setcamera;
    Bitmap smallMarker=null;
    BitmapDrawable bitmapdraw;
    String url="https://safesteps.in/android2/locget.php";
    @SuppressLint("MissingPermission")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_maps,container,false);

        mMapView = view.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(this);
        SharedPreferences sharedPreferences=getContext().getSharedPreferences("Login",MODE_PRIVATE);
        s1=sharedPreferences.getString("Name",null);
        s2=sharedPreferences.getString("Mobile",null);

        setcamera=view.findViewById(R.id.setcamera);
        setcamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(busloc == null){
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(parentsloc, 18);
                    mMap.animateCamera(cameraUpdate);
                } else {
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(busloc, 18);
                    mMap.animateCamera(cameraUpdate);
                }
            }
        });
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                    try {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("name", s1);
                        jsonObject.put("mobile", s2);
                        // Get bus location from server
                        Getdata getdata = new Getdata();
                        getdata.execute(jsonObject.toString());
                        //check time between bus and parent's current location
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                handler.postDelayed(this,1000);
            }
        }, 1000);
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        z=0;
        w=0;
        mMapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        z=0;
        w=0;
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
        mMapView.onDestroy();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng cdg=new LatLng(30.7333,76.7794);
        mMap.addMarker(new MarkerOptions().position(parentsloc));
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(cdg, 50);
        mMap.animateCamera(cameraUpdate);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cdg,13));

           /* try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", s1);
                jsonObject.put("mobile", s2);
                // Get bus location from server
                Getdata getdata = new Getdata();
                getdata.execute(jsonObject.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }*/
    }

    private String getUrl(Context context,LatLng origin, LatLng dest) {
        if (origin!=null && dest !=null && context !=null) {
            // Origin of route
            String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

            // Destination of route
            String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

            // Building the parameters to the web service
            String parameters = str_origin + "&" + str_dest + "&departure_time=now";

            // Output format
            String output = "json";

            // Building the url to the web service

            return "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_key);
        }
        else
            return "Null";
    }

    @SuppressLint("StaticFieldLeak")
    private class Getdata extends AsyncTask<String,String,String> {
        @Override
        protected String doInBackground(String... params) {
            JSONObject jsonObject=JsonFunction.GettingData(url,params[0]);
            if (jsonObject==null){
                return "NULL";
            }
            else
                return jsonObject.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            if (s.equalsIgnoreCase("NULL")){
                Toast.makeText(getActivity(),"No Internet Connection",Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(s);
            int height = 130;
            int width = 60;
            try {
                bitmapdraw= (BitmapDrawable) Objects.requireNonNull(getContext()).getResources().getDrawable(R.mipmap.bustrack);
                Bitmap bi=bitmapdraw.getBitmap();
                smallMarker = Bitmap.createScaledBitmap(bi, width, height, false);
            }catch (NullPointerException e){
                e.getMessage();
            }

            try {
                JSONObject object=new JSONObject(s);
                String res=object.getString("response");
                if (res.equalsIgnoreCase("Success")) {
                    double lat1 = Double.parseDouble(object.getString("lati"));
                    double lng1 = Double.parseDouble(object.getString("longi"));
                    double lat2=Double.parseDouble(object.getString("oldlat"));
                    double lng2=Double.parseDouble(object.getString("oldlng"));
                    float brng=Float.parseFloat(object.getString("brng"));
                    busloc = new LatLng(lat1, lng1);

                    if (marker!=null) {
                        marker.remove();
                    }
                    if (smallMarker !=null)
                    marker=mMap.addMarker(new MarkerOptions().position(busloc).rotation(brng).flat(true).title("Bus").icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));
                    if(w<1){
                        w++;
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(busloc));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Geturl geturl=new Geturl();
            geturl.execute();
        }
    }
     @SuppressLint("StaticFieldLeak")
    private class Geturl extends AsyncTask<String,String,String>{
        @Override
        protected String doInBackground(String... strings) {
            JSONObject object=JsonFunction.GettingData(getUrl(getContext(),busloc,parentsloc),"");
            if (object==null){
                return "NULL";
            }
            else {
                return object.toString();
            }
        }

        @TargetApi(Build.VERSION_CODES.M)
        @Override
        protected void onPostExecute(String s) {
            if (s.equalsIgnoreCase("NULL")){

            }
            super.onPostExecute(s);
            try {
                JSONObject jsonObject=new JSONObject(s);
                JSONArray jsonArray=jsonObject.getJSONArray("routes");
                for (int i=0; i<jsonArray.length(); i++){
                    JSONObject jsonObject1=jsonArray.getJSONObject(i);
                    JSONArray jsonArray1=jsonObject1.getJSONArray("legs");
                    for (int j=0;j<jsonArray1.length(); j++){
                        JSONObject jsonObject2=jsonArray1.getJSONObject(j);
                        JSONObject jsonObject3=jsonObject2.getJSONObject("duration");
                        String k = jsonObject3.getString("value");
                        duration=Integer.parseInt(k);
                    }
                }


                if(duration<10 && z<1){
                    final Intent emptyIntent = new Intent();
                    PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 0, emptyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    NotificationManager notificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);

                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getContext())
                            .setSmallIcon(android.R.mipmap.sym_def_app_icon)
                            .setContentTitle("Bus is arriving")
                            .setContentText("Will reach you in "+duration/60+" minutes")
                            .setContentIntent(pendingIntent)
                            .setOnlyAlertOnce(true)
                            .setVibrate(new long[]{400,400,400,400});
                    try {
                        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                        Ringtone r = RingtoneManager.getRingtone(getContext().getApplicationContext(), notification);
                        r.play();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    notificationManager.notify(1, mBuilder.build());
                    z++;
                }

                PolylineOptions options=new PolylineOptions().width(4).color(Color.BLUE).geodesic(true);

                JSONObject obj1=new JSONObject(s);
                JSONArray arr1=obj1.getJSONArray("routes");

                for (int i=0;i<arr1.length();i++){

                    JSONObject obj2=arr1.getJSONObject(i);
                    JSONArray s1=obj2.getJSONArray("legs");

                    for (int j=0;j<s1.length();j++){
                        JSONObject obj3=s1.getJSONObject(j);
                        JSONArray arr2=obj3.getJSONArray("steps");

                        for (int k=0;k<arr2.length();k++){
                            JSONObject obj4=arr2.getJSONObject(k);
                            JSONObject obj5=obj4.getJSONObject("polyline");
                            String s2=obj5.getString("points");
                            //decoding points in polyline
                            List<LatLng> decode= PolyUtil.decode(s2);

                            for (int z=0; z<decode.size(); z++){
                                LatLng point = decode.get(z);
                                options.add(point);
                                }
                            }
                        }
                    }
                    //removing old polyline if exists.
                    if (currentPolyline != null)
                        currentPolyline.remove();
                    //setting new polyline
                    currentPolyline = mMap.addPolyline(options);

                } catch (JSONException e) {
                e.printStackTrace();
            }
            catch (NullPointerException e1){
                e1.printStackTrace();
            }
        }
    }


}