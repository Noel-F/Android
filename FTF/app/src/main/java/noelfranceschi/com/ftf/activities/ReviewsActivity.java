package noelfranceschi.com.ftf.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.ArrayList;

import noelfranceschi.com.ftf.R;
import noelfranceschi.com.ftf.adapter.ReviewAdapter;
import noelfranceschi.com.ftf.data.DataService;
import noelfranceschi.com.ftf.model.FoodTruck;
import noelfranceschi.com.ftf.model.FoodTruckReview;
import noelfranceschi.com.ftf.view.ItemDecorator;

/**
 * Created by noelfranceschi on 10/13/17.
 */

public class ReviewsActivity extends AppCompatActivity {

    //: Vars
    private FoodTruck foodTruck;
    private ArrayList<FoodTruckReview> reviews = new ArrayList<>();
    private ReviewAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);

        foodTruck = getIntent().getParcelableExtra(FoodTrucksListActivity.EXTRA_ITEM_TRUCK);
        ReviewInterface listener = new ReviewInterface() {
            @Override
            public void success(Boolean success) {
                if (success) {
                    setUpRecycler();
                    if (reviews.size() == 0) {
                        Toast.makeText(getBaseContext(), "No reviews for this Food Truck!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };

        reviews = DataService.getInstance().downloadReviews(this, foodTruck, listener);

    }

    public interface ReviewInterface {
        void success(Boolean success);
    }

    private void setUpRecycler() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_reviews);
        recyclerView.setHasFixedSize(true);
        adapter = new ReviewAdapter(reviews);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getBaseContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new ItemDecorator(0,0,0, 10));
    }
}