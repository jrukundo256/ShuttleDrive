package jonathan.mapview.com.ride;

/**
 * Created by PAUL on 4/8/2018.
 */

public class Product {
    //private int id;
    private String title;
    private String description;
    private String image;

    public Product( String title, String description, String image) {
       // this.id = id;
        this.title = title;
        this.description = description;
        this.image=image;

    }



    public String getTitle() {
        return title;
    }

    public String getDescription()
    {
        return description;
    }

public String getImage(){
        return image;
}
}
