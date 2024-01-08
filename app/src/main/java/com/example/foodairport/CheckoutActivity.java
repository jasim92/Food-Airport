package com.example.foodairport;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.ContextCompat;

import com.example.foodairport.ApiServices.ApiController;
import com.example.foodairport.Dao.CartDao;
import com.example.foodairport.Databases.CartDatabase;
import com.example.foodairport.MyEntity.CartData;
import com.example.foodairport.PostModels.PostOrder;
import com.example.foodairport.RequestBody.DataBody;
import com.example.foodairport.RequestBody.ReqBody;
import com.jpardogo.android.googleprogressbar.library.GoogleProgressBar;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckoutActivity extends AppCompatActivity {
    ImageView back_btn;
    Spinner s_gate;
    AppCompatButton place_order;
    AppCompatEditText c_name, c_phone, c_pnr;

    String gatesArray[] = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14",
            "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28"};

    String gate, terminal;
    Dialog dialog;
    GoogleProgressBar bar;
    TextView terminal_info;

    String foodName, restaurantName, restaurantId;
    int foodPrice, foodQuantity;
    ImageView qrButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        back_btn = findViewById(R.id.back_button3);
        s_gate = findViewById(R.id.spinner_gate);
        place_order = findViewById(R.id.place_order);
        c_name = findViewById(R.id.customer_name);
        c_pnr = findViewById(R.id.customer_pnr);
        c_phone = findViewById(R.id.customer_phone);
        terminal_info = findViewById(R.id.terminal_info_checkout);
        qrButton = findViewById(R.id.qr_image);

        statusBarColor();
        bar = findViewById(R.id.progress_bar);
        bar.setVisibility(View.GONE);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CheckoutActivity.this, MainActivity.class));
            }
        });

        String foodStatus = "not Ready";
        Boolean confirmByRestaurant = false;

        selectGate();
        checkTerminal();
        getRoomFoodData();

        place_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(CheckoutActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCanceledOnTouchOutside(false);

                //send data to restaurant database and central database
                String customerName = c_name.getText().toString();
                String customerPhone = c_phone.getText().toString();
                String customerAddress = "Gate Number: " + gate + " in terminal: " + terminal;
                if (c_name.length() == 0) {
                    c_name.setError("enter name");
                } else if (c_phone.length() == 0) {
                    c_phone.setError("enter phone number");
                } else if (!customerName.equals("") && !customerPhone.equals("")) {
                    bar.setVisibility(View.VISIBLE);
                    dialog.show();
                    postMyOrder(customerName, customerPhone, customerAddress
                            , foodPrice, foodQuantity, foodName, restaurantName, restaurantId, foodStatus, confirmByRestaurant);
                    DeleteCartItemExecutor();
                }

                dialog.dismiss();

            }
        });

        qrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CheckoutActivity.this, ScannerActivity.class));
            }
        });

        //----------------putting data in edit box after scanning boarding pass----------
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String pnr = intent.getStringExtra("pnr");
        c_name.setText(name);
        c_pnr.setText(pnr);
        //-----------------------------------------------------------------------------------------
    }

    private void DeleteCartItemExecutor() {
        CartDatabase cartDatabase = CartDatabase.getInstance(getApplicationContext());
        CartDao cartDao = cartDatabase.CartDao();

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                // do in background task
                cartDao.delete();

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        // it will act like postexcecute()
//                        cartDataArrayList.remove(pos);
//                        notifyDataSetChanged();
//                        updatePrice();

                    }
                });

            }
        });
    }

    public void getRoomFoodData() {
        CartDatabase cartDatabase = CartDatabase.getInstance(getApplicationContext());
        CartDao cartDao = cartDatabase.CartDao();
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                // do in background task
                List<CartData> cartDataList = cartDao.getAllItems();
                for (int i = 0; i < cartDataList.size(); i++) {
                    foodName = cartDataList.get(i).getName();
                    foodPrice = cartDataList.get(i).getPrice();
                    foodQuantity = cartDataList.get(i).getQuantity();
                    restaurantName = cartDataList.get(i).getrName();
                    restaurantId = cartDataList.get(i).getrId();
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //postExecute(UI Thread)

                    }
                });
            }
        });
    }

    public void postMyOrder(String Cname, String Cphone, String Caddress, int Fprice, int Fquantity
            , String Fname, String Rname, String Rid, String Fstatus, Boolean confirmByR) {
        //I modify my parameters according to API (How my Strapi API's accepting response)
        ReqBody reqBody = new ReqBody();
        reqBody.setCustomerName(Cname);
        reqBody.setPhone(Cphone);
        reqBody.setAddress(Caddress);
        reqBody.setPrice(Fprice);
        reqBody.setQuantity(Fquantity);
        reqBody.setFoodName(Fname);
        reqBody.setRestaurantName(Rname);
        reqBody.setRestaurantId(Rid);
        reqBody.setFoodStatus(Fstatus);
        reqBody.setConfirmByRestaurant(confirmByR);

        DataBody dataBody = new DataBody(reqBody);

        Call<PostOrder> call = ApiController.getInstance().getApi().order(dataBody);
        call.enqueue(new Callback<PostOrder>() {
            @Override
            public void onResponse(Call<PostOrder> call, Response<PostOrder> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CheckoutActivity.this, "Your order has been placed", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(CheckoutActivity.this, OrderStatusActivity.class));
//                    DeleteItemExecutor(restaurantId);

                }

            }

            @Override
            public void onFailure(Call<PostOrder> call, Throwable t) {
                Toast.makeText(CheckoutActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void selectGate() {
        ArrayAdapter<String> gateAdapter = new ArrayAdapter<>(CheckoutActivity.this,
                android.R.layout.simple_spinner_item, gatesArray);
        gateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s_gate.setAdapter(gateAdapter);
        s_gate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                gate = adapterView.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                gate = adapterView.getItemAtPosition(0).toString();
            }
        });

    }

    private void checkTerminal() {
        SharedPreferences sp = getSharedPreferences("terminal", MODE_PRIVATE);
        //to check if there is any key named "terminal" in the file if yes then setting data in to edit box
        if (sp.contains("terminal")) {
            terminal = sp.getString("terminal", "");
            terminal_info.setText(terminal);
        }
    }

    public void statusBarColor() {
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.orange)); //status bar or the time bar at the top (see example image1)

            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.actionBarBackground)); // Navigation bar the soft bottom of some phones like nexus and some Samsung note series  (see example image2)
        }
    }

}