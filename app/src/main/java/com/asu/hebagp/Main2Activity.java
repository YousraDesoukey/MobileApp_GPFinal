package com.asu.hebagp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
//import com.androidnetworking.common.Priority;
//import com.androidnetworking.error.ANError;
//import com.androidnetworking.interfaces.JSONObjectRequestListener;
//import com.jacksonandroidnetworking.JacksonParserFactory;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.callback.Callback;


public class Main2Activity extends AppCompatActivity {

    ImageView imageView;
    TextView KingName;
    TextView KingDesc;
    TextView trial;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("kingsinfo");
    String retrievedKingName = "Ay" ;
    String url = "http://192.168.1.110:8000/api/hiero/image/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Intent intent = getIntent();
        Bitmap bm = (Bitmap) intent.getParcelableExtra("pic");
        imageView = (ImageView) findViewById(R.id.imageView2);
        imageView.setImageBitmap(bm);
        KingName = findViewById(R.id.textView);
        KingDesc = findViewById(R.id.textView2);

        trial = (TextView) findViewById(R.id.textView4);
        trial.setText(imageconverter(bm));
        post();

        myRef.child(retrievedKingName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                if (snapshot.getKey().toString().equalsIgnoreCase(retrievedKingName)) {
                    String name = snapshot.child("name").getValue(String.class);
                    String desc = snapshot.child("desc").getValue(String.class);
                    KingName.setText(name);
                    KingDesc.setText(desc);
                }
            }
            @Override
            public void onCancelled(DatabaseError firebaseError) {
                Log.e("onCancelled", " cancelled");
            }
        });
//    final RequestQueue requestqueue = Volley.newRequestQueue(Main2Activity.this);
//    StringRequest stringrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
//        @Override
//        public void onResponse(String response) {
//            trial.setText(response);
//            requestqueue.stop();
//        }
//    }, new Response.ErrorListener() {
//        @Override
//        public void onErrorResponse(VolleyError error) {
//            trial.setText("ERROR");
//            error.printStackTrace();
//            requestqueue.stop();
//        }
//    });
//    requestqueue.add(stringrequest);

    }


    public String imageconverter(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imgBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgBytes, Base64.DEFAULT);
    }

    public void post(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplication(),"BRAVO",Toast.LENGTH_LONG).show();
                trial.setText(response.substring(1,response.length()-5));
                retrievedKingName=response.substring(1,response.length()-5);
                System.out.println(retrievedKingName);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplication(),error+"ERROR",Toast.LENGTH_LONG).show();
                System.out.println(error);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError{
                Map<String,String> params = new HashMap<String, String>();
                String x=trial.getText().toString();
                //System.out.println(x);
                params.put("data",x);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }


}






