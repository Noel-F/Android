package noelfranceschi.com.ftf.model;

/**
 * Created by noelfranceschi on 10/13/17.
 */

public class FoodTruckReview {

    //: Vars
    private String id = "";
    private String title = "";
    private String text = "";

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public FoodTruckReview(String id, String title, String text) {
        this.id = id;
        this.title = title;
        this.text = text;
    }
}
