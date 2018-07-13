package jonathan.mapview.com.ride;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by PAUL on 5/28/2018.
 */

public class ProductsAdapter2  extends RecyclerView.Adapter<ProductsAdapter2.ProductViewHolder> {
    private Context mCtx;
    private List<Product2> productList;
    private String TAG="Available";



    public ProductsAdapter2(Context mCtx, List<Product2> productList) {
        this.mCtx = mCtx;
        this.productList = productList;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.viewrequests, null);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        Product2 product2 = productList.get(position);


        holder.pickups.setText(product2.getPickup());
       holder.totalrequests.setText(product2.getTotalrequests());

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView pickups, totalrequests;
        Button routepreview, navigation;
        ImageView verified;

        public ProductViewHolder(View itemView) {
            super(itemView);
           pickups=itemView.findViewById(R.id.textView3);
            totalrequests = itemView.findViewById(R.id.textView4);
        routepreview=itemView.findViewById(R.id.button6);
        navigation=itemView.findViewById(R.id.button7);
        verified=itemView.findViewById(R.id.imageView);

          //routepreview.setOnClickListener(this);
          navigation.setOnClickListener(this);
          routepreview.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.button7:
                    String mypick = pickups.getText().toString();
                   /* Intent intent=new Intent(mCtx, paul.mapview.com.ride.navigation.class);
                    intent.putExtra("data",mypick);
                    mCtx.startActivity(intent);
                    try {
                        finalize();
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                    */
                    if (mypick.equals("CIT")) {

                        Intent intent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://maps.google.com/maps?daddr=0.3313643,32.5705791"));
                        mCtx.startActivity(intent);
                        try {
                            finalize();
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                        }


                    } else if (mypick.equals("CEDAT")) {

                        Intent intent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://maps.google.com/maps?daddr=0.3363741,32.5643178"));
                        mCtx.startActivity(intent);
                        try {
                            finalize();
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                        }


                    } else if (mypick.equals("GENDER")) {

                        Intent intent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://maps.google.com/maps?daddr=0.3347163,32.5687659"));
                        mCtx.startActivity(intent);
                        try {
                            finalize();
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                        }


                    } else if (mypick.equals("CONAS")) {

                        Intent intent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://maps.google.com/maps?daddr=0.3359957,32.5659087"));
                        mCtx.startActivity(intent);
                        try {
                            finalize();
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                        }


                    } else if (mypick.equals("CHUSS")) {

                        Intent intent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://maps.google.com/maps?daddr=0.332639,32.5678526"));
                        mCtx.startActivity(intent);
                        try {
                            finalize();
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                        }


                    }
                    else if (mypick.equals("EDUCATION")) {

                        Intent intent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://maps.google.com/maps?daddr=0.3293741,32.5677601"));
                        mCtx.startActivity(intent);
                        try {
                            finalize();
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                        }


                    }
                    else if (mypick.equals("SCHOOL OF LAW")) {

                        Intent intent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://maps.google.com/maps?daddr=0.3284769,32.569708"));
                        mCtx.startActivity(intent);
                        try {
                            finalize();
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                        }


                    }
                    break;
                case R.id.button6:
                    SQLiteHandler db=new SQLiteHandler(mCtx);

                    String pick = pickups.getText().toString();
                    //Toast.makeText(mCtx,pick,Toast.LENGTH_LONG).show();
                    // Fetching user details from SQLite
                    HashMap<String, String> user = db.getUserDetails();

                   String shuttlename = user.get("name");
                  //  Toast.makeText(mCtx,shuttlename,Toast.LENGTH_LONG).show();
                    delete( shuttlename, pick);




            }

         /*
            Intent intent=new Intent(mCtx,MapsActivity2.class);
           String my=textViewTitle.getText().toString();
           intent.putExtra("data",my);
            mCtx.startActivity(intent);
            try {
                finalize();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            */
        }
        private void delete(final String shuttlename,final String pick){
            final String tag_string_req = "req_login";
            String url="http://shuttlealert.exoticugandatoursandtravel.com/driver/updaterequest.php";

            StringRequest strReq = new StringRequest(com.android.volley.Request.Method.POST,
                    url, new  Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    String data=response.toString();
                   // Toast.makeText(mCtx,"success",Toast.LENGTH_SHORT).show();
                    Log.d(tag_string_req,"returned data"+data);


                    try {
                        //converting the string to json array object


                        JSONArray array = new JSONArray(response);

                        //traversing through all the object
                        for (int i = 0; i < array.length(); i++) {
                            //getting product object from json array
                            JSONObject product = array.getJSONObject(i);
                            String message =product.getString("message");
                            //Toast.makeText(mCtx,message,Toast.LENGTH_LONG).show();
                            if (message.equals("success")){
                                verified.setVisibility(View.VISIBLE);


                                new android.app.AlertDialog.Builder(mCtx).setMessage("successfully updated").setCancelable(false).
                                        setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int id) {
                                               dialog.dismiss();
                                            }
                                        })

                                        .show();




                            }
                            else
                            {



                            }
                            // String name=product.getString("name");
                            //String age=product.getString("age");
                            // Toast.makeText(getApplication(),"returned name is:"+name,Toast.LENGTH_LONG).show();
                            // Toast.makeText(getApplication(),"returned age is:"+age,Toast.LENGTH_LONG).show();


                        }




                    } catch (JSONException e) {
                        // JSON error
                        e.printStackTrace();
                        Toast.makeText(mCtx, "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();

                    }

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "server error: " + error.getMessage());
                    Toast.makeText(mCtx,
                            error.getMessage(), Toast.LENGTH_LONG).show();
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    // Posting parameters to request url.
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("pickup", pick);
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
}
