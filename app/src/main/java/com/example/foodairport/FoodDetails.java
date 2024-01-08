//package com.example.foodairport;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Looper;
//import android.util.Log;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.AppCompatButton;
//
//import com.example.foodairport.ApiServices.ApiController;
//import com.example.foodairport.Dao.CartDao;
//import com.example.foodairport.Databases.CartDatabase;
//import com.example.foodairport.MyEntity.CartData;
//
//import com.squareup.picasso.Picasso;
//
//import java.util.List;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//public class FoodDetails extends AppCompatActivity {
//
//    TextView foodName, restaurantName, restaurantStatus;
//    ImageView foodImage, restaurantLogo, backButton;
//    AppCompatButton addCart, viewCart;
//    List<Datum> popularRestaurantResponse;
//    List<CartData> cartDataList;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_food_details);
//        foodName = findViewById(R.id.food_name_2);
//        foodImage = findViewById(R.id.food_pic_2);
//        restaurantLogo = findViewById(R.id.restaurant_logo);
//        restaurantName = findViewById(R.id.restaurant_name);
//        restaurantStatus = findViewById(R.id.restaurant_status);
//        backButton = findViewById(R.id.back_button);
//        addCart = findViewById(R.id.add_to_cart);
//        viewCart = findViewById(R.id.view_cart);
//
//
//        backButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(FoodDetails.this, MainActivity.class));
//            }
//        });
//        Intent intent = getIntent();
//        String name = intent.getStringExtra("food_name");
//        String url = intent.getStringExtra("food_pic");
//        int id = intent.getIntExtra("food_id", 0);
//        int price = intent.getIntExtra("food_price", 0);
//        int quantity = 1;
//        foodName.setText(name);
//        Picasso.get().load(url).into(foodImage);
//
//
//        addCart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                addCart.setVisibility(View.GONE);
//                viewCart.setVisibility(View.VISIBLE);
//                ExistExecutor(id, new CartData(id, name, url, price, quantity));
//            }
//        });
//
//        viewCart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(FoodDetails.this, CartActivity.class));
//                Log.e("activity: ", "you are in cart activity");
//            }
//        });
//
//        Call<Popular> call = ApiController.getInstance().getApi().getPopularItemRestaurant();
//        call.enqueue(new Callback<Popular>() {
//            @Override
//            public void onResponse(Call<Popular> call, Response<Popular> response) {
//                Popular pm = response.body();
//                List<Datum> data = pm.getData();
//                popularRestaurantResponse = data;
//
////                        Picasso.get().load(ApiController.url + popularRestaurantResponse.get(id).getAttributes().getRestaurant().getData()
////                        .getAttributes().getLogo().getData().getAttributes().getUrl()).into(restaurantLogo);
////
////                restaurantName.setText(popularRestaurantResponse.get(id).getAttributes().getRestaurant().getData()
////                        .getAttributes().getName());
////
////                if (popularRestaurantResponse.get(id).getAttributes().getRestaurant().getData().getAttributes().getStatus() == true)
////                    restaurantStatus.setText("Open Now");
////                else {
////                    restaurantStatus.setTextColor(getResources().getColor(R.color.red));
////                    restaurantStatus.setText("Closed");
////                }
//
//            }
//
//            @Override
//            public void onFailure(Call<Popular> call, Throwable t) {
//
//            }
//        });
//
//
//    }
//
//    public void InsertExecutor(CartData cartData, CartDao cartDao) {
//        ExecutorService executorService = Executors.newSingleThreadExecutor();
//        Handler handler = new Handler(Looper.getMainLooper());
//        executorService.execute(new Runnable() {
//            @Override
//            public void run() {
//                // do in background task
//                cartDao.insert(cartData);
//                Log.e("executor: ", "Now, I am executing InsertExecutor");
//
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        // it will act like postexcecute()
//                    }
//                });
//            }
//        });
//    }
//
//    public void ExistExecutor(int id, CartData cartdata) {
//        CartDatabase cartDatabase = CartDatabase.getInstance(getApplicationContext());
//        CartDao cartDao = cartDatabase.CartDao();
//        ExecutorService executorService = Executors.newSingleThreadExecutor();
//        Handler handler = new Handler(Looper.getMainLooper());
//        executorService.execute(new Runnable() {
//            @Override
//            public void run() {
//                // do in background task
//                Boolean bn = cartDao.is_exist(id);
//                Log.e("executor: ", "Now, I am executing ExistExecutor");
//
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        // it will act like postexcecute()
//                        if (bn == false) {
//                            InsertExecutor(cartdata, cartDao);
//                            Log.e("executor: ", "Now, I am false");
//                        } else {
//                            Toast.makeText(getApplicationContext(), "already in cart", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//
//            }
//        });
//    }
//
//}