package jonathan.mapview.com.ride;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by PAUL on 6/26/2018.
 */

public class navigation extends AppCompatActivity {
    String pickup;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_login);
        Intent intent=getIntent();
        pickup=intent.getStringExtra("data");
        handle();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(this,ViewRequests.class);
        startActivity(intent);
        finish();
    }

    private void handle(){

if (pickup.equals("cit")){
    Intent intent=new Intent(Intent.ACTION_VIEW,
            Uri.parse("http://maps.google.com/maps?daddr=0.3313643,32.5705791"));
    startActivity(intent);



}






    }
}
