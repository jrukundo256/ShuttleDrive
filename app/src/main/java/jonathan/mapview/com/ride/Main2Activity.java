package jonathan.mapview.com.ride;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Main2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener ,OnMapReadyCallback ,LocationListener {
    private SessionManager session;
    private SQLiteHandler db;
    private GoogleMap mMap;
    private String TAG="Available";
    LocationManager locationManager;
    String currentlat;
    String currentlon;
    double targetlat;
    double targetlon;
    LatLng paul;
    String mylng;
    String mylat;
    String shuttlename;
    String resultValue;
    String pickup;
    Marker m = null;
    final Context c = this;
    Button test;
    SharedPreferences prefs = null;
    private ListView listView;
    private String names[] = {
            "Pending requests"

            // "Java Script",
            //"Wordpress"
    };

   // private String desc[]={"6"};
private String desc[]={"4"};

            //"Manage your content with Wordpress"



    private Integer imageid[] = {
            R.drawable.message,
            //R.drawable.campanyicon
            // R.drawable.cinemaicon,
            //R.drawable.clubicon
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
       BroadcastReceiver();
        desc[0]=resultValue;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        CustomList customList = new CustomList(this, names, desc,imageid);

       // desc=new String[20];
        //desc[0]=pickup;

        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(customList);
        Button getreq=findViewById(R.id.makereq);
      test=findViewById(R.id.mapping);



        getreq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Main2Activity.this,ViewRequests.class);
                startActivity(intent);
                finish();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               //Toast.makeText(getApplicationContext(),"You Clicked "+names[i],Toast.LENGTH_SHORT).show();
            }
        });

        setSupportActionBar(toolbar);
        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        getLocation();

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        // Fetching user details from SQLite
        HashMap<String, String> user = db.getUserDetails();

        shuttlename = user.get("name");
//Toast.makeText(getApplication(),shuttlename,Toast.LENGTH_LONG).show();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);

        }

    }
    // Launching the service
    public void BroadcastReceiver() {

        Intent i = new Intent(this, MyTestService.class);
        i.putExtra("foo", "bar");
        startService(i);
    }
    @Override
    protected void onResume() {
        super.onResume();
        // Register for the particular broadcast based on ACTION string
        IntentFilter filter = new IntentFilter(MyTestService.ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(testReceiver, filter);
        // or `registerReceiver(testReceiver, filter)` for a normal broadcast
    }
    // Define the callback for what to do when data is received
    private BroadcastReceiver testReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int resultCode = intent.getIntExtra("resultCode", RESULT_CANCELED);
            if (resultCode == RESULT_OK) {
                 resultValue = intent.getStringExtra("resultValue");
               // Toast.makeText(Main2Activity.this, resultValue, Toast.LENGTH_LONG).show();
test.setText(resultValue);

               // desc[0]=resultValue;
            }
        }
    };

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        prefs = this.getSharedPreferences("LatLng",MODE_PRIVATE);
        if((prefs.contains("Lat")) && (prefs.contains("Lng")))
        {
            mylat = prefs.getString("Lat","");
            mylng = prefs.getString("Lng","");
            LatLng l =new LatLng(Double.parseDouble(mylat),Double.parseDouble(mylng));
            mMap.addMarker(new MarkerOptions().position(l).title("last known location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(l,15f));
            mMap.addCircle(new CircleOptions().center(l).fillColor(0x110000FF).strokeWidth(1).strokeColor(0x110000FF));

        }



        // Add a marker in Sydney and move the camera
        //locationManager  = new Location(locationManager.getLastKnownLocation());
        // mMap.addMarker(new MarkerOptions().position(current).title("current location"));
        // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current,12f));
        // mMap.moveCamera(CameraUpdateFactory.newLatLng(current));

    }



    void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        targetlat = location.getLatitude();
        targetlon = location.getLongitude();
        currentlat = String.valueOf(targetlat);
        currentlon = String.valueOf(targetlon);
       // Toast.makeText(getApplication(),"lat is:"+currentlat,Toast.LENGTH_LONG).show();
       // Toast.makeText(getApplication(),"lon is:"+currentlon,Toast.LENGTH_LONG).show();
        submitinfo(shuttlename,currentlat,currentlon);

        Location current = new Location(location);
        // mMap.addMarker(new MarkerOptions().position(current).title("current location"));
LatLng curren=new LatLng(targetlat,targetlon);
        prefs.edit().putString("Lat",currentlat).commit();
        prefs.edit().putString("Lng",currentlon).commit();

       CameraPosition camPosition = new CameraPosition.Builder()
                .target(new LatLng(targetlat, targetlon)).zoom(15f).build();
        if (mMap != null){
            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(curren).title("current location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(curren,15f));
            mMap.addCircle(new CircleOptions().center(curren).radius(300).fillColor(0x110000FF).strokeWidth(1).strokeColor(0x110000FF));

           // mMap.animateCamera(CameraUpdateFactory
                 //   .newCameraPosition(camPosition));




        }

        else
        {


        }





    }
    @Override
    public void onProviderDisabled(String provider) {
        //Toast.makeText(Page.this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
        new android.app.AlertDialog.Builder(this).setMessage("For Better Results, Please Enable your Location").setCancelable(false).
                setPositiveButton("Okey!!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent=new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }
                })

                .show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){

        }else{
            new android.app.AlertDialog.Builder(this).setMessage("Location has been turned off, Please re-enable your Location").setCancelable(false).
                    setPositiveButton("Okey!!", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent=new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(intent);
                        }
                    })

                    .show();

        }

    }

    @Override
    public void onProviderEnabled(String provider) {

    }
    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(Main2Activity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
           // super.onBackPressed();
            new android.app.AlertDialog.Builder(this).setMessage("ARE YOU SURE YOU WANT TO EXIT").setCancelable(false)
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Main2Activity.this.finish();

                        }
                    }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            })
                    .show();

        }
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
                            Main2Activity.this.finish();

                        }
                    }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            })
                    .show();

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_shuttle) {
            LayoutInflater layoutInflaterAndroid = LayoutInflater.from(c);
            View mView = layoutInflaterAndroid.inflate(R.layout.user_input_dialog_box, null);
            AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(c);
            alertDialogBuilderUserInput.setView(mView);

            final EditText userInputDialogEditText = (EditText) mView.findViewById(R.id.userInputDialog);
            alertDialogBuilderUserInput
                    .setCancelable(false)
                    .setPositiveButton("send", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogBox, int id) {
                            // ToDo get user input here
                            String message =userInputDialogEditText.getText().toString();
                            submitmessage(shuttlename,message);
                           // Toast.makeText(getApplication(),message,Toast.LENGTH_LONG).show();
                        }
                    })

                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogBox, int id) {
                                    dialogBox.cancel();
                                }
                            });

            AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
            alertDialogAndroid.show();
            // Handle the camera action
        } else if (id == R.id.nav_profile) {

        } else if (id == R.id.nav_logout) {
            logoutUser();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void submitinfo(final String shuttlename,final String currentlat,final String currentlon) {

        final String tag_string_req = "req_login";
        String url="http://shuttlealert.exoticugandatoursandtravel.com/driver/location.php";

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
                        // String name=product.getString("name");
                        //String age=product.getString("age");
                        // Toast.makeText(getApplication(),"returned name is:"+name,Toast.LENGTH_LONG).show();
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
                params.put("currentlat",currentlat);
                params.put("currentlon",currentlon);
                // params.put("myage",myage);


                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
    private void submitmessage(final String shuttlename,final String message){
        final String tag_string_req = "req_login";
        String url="http://shuttlealert.exoticugandatoursandtravel.com/driver/message.php";

        StringRequest strReq = new StringRequest(com.android.volley.Request.Method.POST,
                url, new  Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                String data=response.toString();
              //  Toast.makeText(getApplication(),"success",Toast.LENGTH_SHORT).show();
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
                params.put("message", message);
                params.put("shuttlename",shuttlename);
                //params.put("currentlon",currentlon);
                // params.put("myage",myage);


                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }


}
