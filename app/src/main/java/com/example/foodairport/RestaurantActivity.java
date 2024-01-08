package com.example.foodairport;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodairport.Adapters.FoodAdapter;
import com.example.foodairport.ApiServices.ApiController;
import com.example.foodairport.Dao.CartDao;
import com.example.foodairport.Databases.CartDatabase;
import com.example.foodairport.FoodModel.Datum;
import com.example.foodairport.FoodModel.Food;
import com.example.foodairport.MyEntity.CartData;
import com.jpardogo.android.googleprogressbar.library.GoogleProgressBar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantActivity extends AppCompatActivity implements ListItemClickListener {

    RecyclerView rc_foods;
    ImageView Logo;
    TextView r_name, r_status;
    FoodAdapter foodAdapter;
    List<Datum> foodDataList;
    List<Datum> foodByRestaurant;

    Dialog dialog;
    GoogleProgressBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);
        rc_foods = findViewById(R.id.rc_food);
        Logo = findViewById(R.id.r_top_img);
        r_name = findViewById(R.id.r_top_name);
        r_status = findViewById(R.id.r_top_status);
        statusBarColor();

        bar = findViewById(R.id.progress_bar2);
        bar.setVisibility(View.GONE);


        rc_foods.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rc_foods.addItemDecoration(new DividerItemDecoration(rc_foods.getContext(), DividerItemDecoration.VERTICAL));

        Intent intent = getIntent();
        String name = intent.getStringExtra("res_name");
        String url = intent.getStringExtra("res_pic");
        int id = intent.getIntExtra("res_id", 0);
        Boolean status = intent.getBooleanExtra("res_status", true);

        r_name.setText(name);
        if (status == true)
            r_status.setText("Open now");
        if (status == false) {
            r_status.setTextColor(getResources().getColor(R.color.red));
            r_status.setText("Closed");
        }
        Picasso.get().load(url).into(Logo);

        foodDataList = new ArrayList<>();
        foodByRestaurant = new ArrayList<>();

        dialog = new Dialog(RestaurantActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        bar.setVisibility(View.VISIBLE);
        dialog.show();

        Call<Food> foodCall = ApiController.getInstance().getApi().getFoods();
        foodCall.enqueue(new Callback<Food>() {
            @Override
            public void onResponse(Call<Food> call, Response<Food> response) {


                Food fd = response.body();
                foodDataList = fd.getData();
                GetFoodByRestaurant(id);
                foodAdapter = new FoodAdapter(status, foodByRestaurant, RestaurantActivity.this::onListItemClick, getApplicationContext());
                rc_foods.setAdapter(foodAdapter);

                dialog.dismiss();
                bar.setVisibility(View.GONE);


            }

            @Override
            public void onFailure(Call<Food> call, Throwable t) {

            }

        });


    }

    public void statusBarColor() {
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.orange)); //status bar or the time bar at the top (see example image1)

            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.actionBarBackground)); // Navigation bar the soft bottom of some phones like nexus and some Samsung note series  (see example image2)
        }
    }

    public void GetFoodByRestaurant(int restaurantId) {
        foodByRestaurant.clear();
        for (int i = 0; i < foodDataList.size(); i++) {
            if (foodDataList.get(i).getAttributes().getRestaurant().getData().getId() == restaurantId)
                foodByRestaurant.add(foodDataList.get(i));

        }

    }


    @Override
    public void onListItemClick(int clickedItemIndex) {
        int id = foodByRestaurant.get(clickedItemIndex).getId();
        String name = foodByRestaurant.get(clickedItemIndex).getAttributes().getFoodName();
        String url = ApiController.url + foodByRestaurant.get(clickedItemIndex).getAttributes().getFoodPhoto().getData().getAttributes().getUrl();
        Log.e("in res ctivity ", url);
        int price = foodByRestaurant.get(clickedItemIndex).getAttributes().getFoodPrice();
        int quantity = foodByRestaurant.get(clickedItemIndex).getAttributes().getFoodQuantity();
        String Rname = foodByRestaurant.get(clickedItemIndex).getAttributes().getRestaurant().getData().getAttributes().getName();
        String RId = foodByRestaurant.get(clickedItemIndex).getAttributes().getRestaurant().getData().getAttributes().getRestaurantId();
        ExistExecutor(id, new CartData(id, name, url, price, quantity, Rname, RId));

    }

    public void ExistExecutor(int id, CartData cartdata) {
        CartDatabase cartDatabase = CartDatabase.getInstance(getApplicationContext());
        CartDao cartDao = cartDatabase.CartDao();
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                // do in background task
                Boolean bn = cartDao.is_exist(id);


                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        // it will act like postexcecute()
                        if (bn == false) {
                            InsertExecutor(cartdata, cartDao);

                        } else {
                            Toast.makeText(getApplicationContext(), "already in cart", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
    }

    public void InsertExecutor(CartData cartData, CartDao cartDao) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                // do in background task
                cartDao.insert(cartData);

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        // it will act like postexcecute()
                    }
                });
            }
        });
    }
}