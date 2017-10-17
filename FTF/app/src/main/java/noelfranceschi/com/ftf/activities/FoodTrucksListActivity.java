package noelfranceschi.com.ftf.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import noelfranceschi.com.ftf.R;
import noelfranceschi.com.ftf.adapter.FoodTruckAdapter;
import noelfranceschi.com.ftf.model.FoodTruck;
import noelfranceschi.com.ftf.view.ItemDecorator;


public class FoodTrucksListActivity extends AppCompatActivity {

    //: Vars
    private FoodTruckAdapter adapter;
    private ArrayList<FoodTruck> trucks = new ArrayList<>();





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_trucks_list);

        String url = "https://finalwebtest.com/api/v1/foodtruck";
        
        final ArrayList<FoodTruck> foodTruckList = new ArrayList<>();

        final JsonArrayRequest getTrucks = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                System.out.println(response.toString());

                try {
                    JSONArray foodTrucks = response;

                    for (int x = 0; x < foodTrucks.length(); x++) {
                        JSONObject foodTruck = foodTrucks.getJSONObject(x);
                        String name = foodTruck.getString("name");
                        String id = foodTruck.getString("_id");
                        String foodType = foodTruck.getString("foodtype");
                        Double avgCost = foodTruck.getDouble("avgCost");

                        JSONObject geometry = foodTruck.getJSONObject("geometry");
                        JSONObject coordinates = geometry.getJSONObject("coordinates");

                        Double latitude = coordinates.getDouble("lat");
                        Double longitude = coordinates.getDouble("long");

                        FoodTruck newFoodTruck = new FoodTruck(id, name, foodType, avgCost, latitude, longitude);
                        foodTruckList.add(newFoodTruck);
                    }

                } catch (JSONException e) {
                    Log.v("JSON", "EXC" + e.getLocalizedMessage());
                }

//                System.out.print("THIS IS THE FOOD TRUCK NAME: " + foodTruckList.get(0).getName());

                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_foodtruck);
                recyclerView.setHasFixedSize(true);
                adapter = new FoodTruckAdapter(foodTruckList);
                recyclerView.setAdapter(adapter);
                LinearLayoutManager layoutManager = new LinearLayoutManager(getBaseContext());
                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.addItemDecoration(new ItemDecorator(0,0,0, 10));


            }
        }, new Response.ErrorListener(){
           @Override
            public void onErrorResponse(VolleyError error) {
               Log.v("API", "Err" + error.getLocalizedMessage());
           }
        });

        Volley.newRequestQueue(this).add(getTrucks);
    }
}
