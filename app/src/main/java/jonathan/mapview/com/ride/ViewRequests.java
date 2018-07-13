package jonathan.mapview.com.ride;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.widget.Toolbar;
import android.support.v7.widget.RecyclerView;

import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by PAUL on 6/23/2018.
 */

public class ViewRequests extends AppCompatActivity {
    private String TAG="Available";
    //the recyclerview
    RecyclerView recyclerView;
    private SQLiteHandler db;
    private SessionManager session;
    String shuttlename;

    List<Product2> productList;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(this,Main2Activity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_main3);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        //initializing the productlist
        productList = new ArrayList<>();
        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        // Fetching user details from SQLite
        HashMap<String, String> user = db.getUserDetails();

        shuttlename = user.get("name");
        submitinfo(shuttlename);
        //getnames( shuttlename);

        android.support.v7.widget.Toolbar toolbar=findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("View Requests");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(ViewRequests.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void submitinfo(final String shuttlename) {

        final String tag_string_req = "req_login";
        String url="http://shuttlealert.exoticugandatoursandtravel.com/driver/getrequests.php";

        StringRequest strReq = new StringRequest(com.android.volley.Request.Method.POST,
                url, new  Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                String data=response.toString();
                Toast.makeText(getApplication(),"success",Toast.LENGTH_SHORT).show();
                Log.d(tag_string_req,"returned data"+data);


                try {
                    //converting the string to json array object


                    JSONArray array = new JSONArray(response);

                    //traversing through all the object
                    for (int i = 0; i < array.length(); i++) {
                        //getting product object from json array
                        JSONObject product = array.getJSONObject(i);
                       // String name=product.getString("name");
                       //String age=product.getString("age");
                        // Toast.makeText(getApplication(),"returned name is:"+name,Toast.LENGTH_LONG).show();
                        // Toast.makeText(getApplication(),"returned age is:"+age,Toast.LENGTH_LONG).show();


                        productList.add(new Product2(
                                product.getString("pickup"),
                                product.getString("mycount")));

                    }
                    ProductsAdapter2 adapter = new ProductsAdapter2(ViewRequests.this, productList);
                    recyclerView.setAdapter(adapter);



                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "server error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to request url.
                Map<String, String> params = new HashMap<String, String>();
                 params.put("shuttlename", shuttlename);
                // params.put("myage",myage);


                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
    private void getnames(final String shuttlename) {

        final String tag_string_req = "req_login";
        String url="http://192.168.43.236/driver/getnames.php";

        StringRequest strReq = new StringRequest(com.android.volley.Request.Method.POST,
                url, new  Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                String data=response.toString();
               // Toast.makeText(getApplication(),"success",Toast.LENGTH_SHORT).show();
                Log.d(tag_string_req,"returned data"+data);


                try {
                    //converting the string to json array object


                    JSONArray array = new JSONArray(response);

                    //traversing through all the object
                    for (int i = 0; i < array.length(); i++) {
                        //getting product object from json array
                        JSONObject product = array.getJSONObject(i);
                        String name=product.getString("studentname");
                        //String age=product.getString("age");
                         //Toast.makeText(getApplication(),"returned names:"+name,Toast.LENGTH_LONG).show();
                        // Toast.makeText(getApplication(),"returned age is:"+age,Toast.LENGTH_LONG).show();


                    }




                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "server error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to request url.
                Map<String, String> params = new HashMap<String, String>();
                params.put("shuttlename", shuttlename);
                // params.put("myage",myage);


                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
           // return true;
            new android.app.AlertDialog.Builder(this).setMessage("ARE YOU SURE YOU WANT TO EXIT").setCancelable(false)
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ViewRequests.this.finish();

                        }
                    }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            })
                    .show();

        }
        if (id==android.R.id.home){
            Intent intent=new Intent(this,Main2Activity.class);
            startActivity(intent);
            finish();


        }

        return super.onOptionsItemSelected(item);
    }

}
