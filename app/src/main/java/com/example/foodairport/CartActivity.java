package com.example.foodairport;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodairport.Adapters.CartAdapter;
import com.example.foodairport.Dao.CartDao;
import com.example.foodairport.Databases.CartDatabase;
import com.example.foodairport.MyEntity.CartData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CartActivity extends AppCompatActivity {

    RecyclerView cart_rc;
    AppCompatButton checkout;
    ImageView back_button;
    TextView cartValue;
    List<CartData> cartDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        checkout = findViewById(R.id.checkout);
        back_button = findViewById(R.id.back_button2);
        cartValue = findViewById(R.id.total_cart_value);
        statusBarColor();
        cartDataList = new ArrayList<>();
        cartDataList.clear();
        getRoomData();

        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cartDataList.size()>0)
                {
                    Intent intent = new Intent(CartActivity.this, CheckoutActivity.class);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(CartActivity.this, "Add items to cart", Toast.LENGTH_SHORT).show();
                }

            }
        });

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(CartActivity.this, MainActivity.class));
            }
        });


    }
    public void statusBarColor()
    {
        if (Build.VERSION.SDK_INT >= 21)
        {
            getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.orange)); //status bar or the time bar at the top (see example image1)

            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.actionBarBackground)); // Navigation bar the soft bottom of some phones like nexus and some Samsung note series  (see example image2)
        }
    }

    private void getRoomData() {
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
                cartDataList = cartDao.getAllItems();
//                for (int i=0;i<cartDataList.size();i++)
//                {
//                    if (cartDataList.get(i).getName().contains("Shuhoor Combo"))
//                    {
//                        cartDao.deleteById(cartDataList.get(i).getId());
//                        cartDataList.remove(i);
//
//                    }
//                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        // it will act like postexcecute()
                        cart_rc = findViewById(R.id.rc_cart);
                        cart_rc.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        cart_rc.addItemDecoration(new DividerItemDecoration(cart_rc.getContext(), DividerItemDecoration.VERTICAL));
                        CartAdapter cartAdapter = new CartAdapter(cartDataList, cartValue, CartActivity.this);
                        cart_rc.setAdapter(cartAdapter);

                        int sum = 0;
                        for (int i = 0; i < cartDataList.size(); i++)
                            sum = sum + (cartDataList.get(i).getPrice()) * cartDataList.get(i).getQuantity();

                        cartValue.setText(sum + "/-");
                    }
                });
            }
        });


    }

}