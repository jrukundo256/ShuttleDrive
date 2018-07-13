package jonathan.mapview.com.ride;

/**
 * Created by PAUL on 5/28/2018.
 */

public class Product2 {
    //private int id;
   // private String title;
    private String pickup;
    private String totalrequests;




    public Product2( String pickup,String totalrequests) {
        // this.id = id;
        this.pickup=pickup;
        this.totalrequests=totalrequests;

       // this.title = title;



    }



    public String getPickup() {
        return pickup;
    }



    public String getTotalrequests(){
        return totalrequests;
    }

}
