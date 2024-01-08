package com.example.foodairport;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodairport.Adapters.RestaurantAdapter;
import com.example.foodairport.ApiServices.ApiController;
import com.example.foodairport.Dao.CartDao;
import com.example.foodairport.Databases.CartDatabase;
import com.example.foodairport.FoodModel.Data__1;
import com.example.foodairport.FoodModel.Restaurant;
import com.example.foodairport.MyEntity.CartData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements ListItemClickListener {

    RecyclerView rc_restaurants;
    RestaurantAdapter restaurantAdapter;
    List<Data__1> drts;
    List<Data__1> restaurantsByTerminal;
    ImageView searchBtn, cart_btn;
    EditText searchFood;
    public TextView cart_item_count, terminaltxt;
    public int countItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rc_restaurants = findViewById(R.id.rc_restaurant);
        searchBtn = findViewById(R.id.search_btn);
        searchFood = findViewById(R.id.find_restaurant);
        cart_btn = findViewById(R.id.cart);
        cart_item_count = findViewById(R.id.cart_item_tv);
        terminaltxt = findViewById(R.id.terminal_info);

        statusBarColor();
        //------------------------------------------------------------
        checkTerminal();
        //----------------------------------------------------------------
        rc_restaurants.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rc_restaurants.addItemDecoration(new DividerItemDecoration(rc_restaurants.getContext(), DividerItemDecoration.VERTICAL));
        restaurantsByTerminal = new ArrayList<>();

        cart_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, CartActivity.class));
            }
        });

        getCartItemCount();

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchKey = searchFood.getText().toString();
                Toast.makeText(getApplicationContext(), searchKey + " Searching...", Toast.LENGTH_SHORT).show();
            }
        });

        Call<Restaurant> callRest = ApiController.getInstance().getApi().getRestaurants();
        callRest.enqueue(new Callback<Restaurant>() {
            @Override
            public void onResponse(Call<Restaurant> call, Response<Restaurant> response) {

                Restaurant rts = response.body();
                drts = rts.getData();
                SharedPreferences sp = getSharedPreferences("terminal", MODE_PRIVATE);
                if (sp.contains("terminal")) {
                    for (int i = 0; i < drts.size(); i++) {
                        if (drts.get(i).getAttributes().getTerminal() == Integer.valueOf(sp.getString("terminal", ""))) {
                            restaurantsByTerminal.add(drts.get(i));
                            restaurantAdapter = new RestaurantAdapter(getApplicationContext(), restaurantsByTerminal, MainActivity.this::onListItemClick);
                            rc_restaurants.setAdapter(restaurantAdapter);
                        }
                    }
                } else {
                    restaurantAdapter = new RestaurantAdapter(getApplicationContext(), drts, MainActivity.this::onListItemClick);
                    rc_restaurants.setAdapter(restaurantAdapter);
                }


            }

            @Override
            public void onFailure(Call<Restaurant> call, Throwable t) {

            }
        });
    }

    private void checkTerminal() {
        SharedPreferences sp = getSharedPreferences("terminal", MODE_PRIVATE);
        //to check if there is any key named "terminal" in the file if yes then setting data in to edit box
        if (sp.contains("terminal")) {
            terminaltxt.setText(sp.getString("terminal", ""));
        }
    }

    public void statusBarColor() {
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.orange)); //status bar or the time bar at the top (see example image1)

            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.actionBarBackground)); // Navigation bar the soft bottom of some phones like nexus and some Samsung note series  (see example image2)
        }
    }

    private void getCartItemCount() {
        CartDatabase cartDatabase = CartDatabase.getInstance(getApplicationContext());
        CartDao cartDao = cartDatabase.CartDao();
        getItemsExecutor(cartDao);
    }

    public void getItemsExecutor(CartDao cartDao) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                // do in background task
                List<CartData> cartDataCount = cartDao.getAllItems();
                for (int i = 0; i < cartDataCount.size(); i++) {
                    if (cartDataCount.get(i).getName().contains("Shuhoor Combo")) {
                        cartDao.deleteById(cartDataCount.get(i).getId());
                        cartDataCount.remove(i);

                    }
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        // it will act like postexcecute()
                        countItems = cartDataCount.size();
                        cart_item_count.setText("You have " + countItems + " items in your cart");
                    }
                });
            }
        });
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {


        Intent intent = new Intent(MainActivity.this, RestaurantActivity.class);
        String name = restaurantsByTerminal.get(clickedItemIndex).getAttributes().getName();
        String picturesurl = ApiController.url + restaurantsByTerminal.get(clickedItemIndex).getAttributes().getLogo().getData().getAttributes().getUrl();
        int id = restaurantsByTerminal.get(clickedItemIndex).getId();
        Boolean status = restaurantsByTerminal.get(clickedItemIndex).getAttributes().getStatus();
        intent.putExtra("res_name", name);
        intent.putExtra("res_pic", picturesurl);
        intent.putExtra("res_id", id);
        intent.putExtra("res_status", status);
        startActivity(intent);


    }

    @Override
    protected void onStop() {
        super.onStop();


    }
}