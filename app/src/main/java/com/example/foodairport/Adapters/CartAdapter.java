package com.example.foodairport.Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodairport.Dao.CartDao;
import com.example.foodairport.Databases.CartDatabase;
import com.example.foodairport.MainActivity;
import com.example.foodairport.MyEntity.CartData;
import com.example.foodairport.R;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    List<CartData> cartDataArrayList;
    Context context;
    TextView cartValue;

    public CartAdapter(List<CartData> cartDataArrayList, TextView cartValue, Context context) {
        this.cartDataArrayList = cartDataArrayList;
        this.context = context;
        this.cartValue = cartValue;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartData cartData = cartDataArrayList.get(holder.getAdapterPosition());

        try {
            if (cartData.getName().length()<10)
                holder.itemName.setText(cartData.getName());
            else
                holder.itemName.setText(cartData.getName().substring(0, 10) + "...");

            holder.item_price.setText(String.valueOf(cartData.getPrice()));
            holder.item_qnt.setText(String.valueOf(cartData.getQuantity()));
            Picasso.get().load(cartData.getUrl()).into(holder.cartImg);
            Log.e("in adapter ", cartData.getUrl());
        } catch (Resources.NotFoundException e) {
            Log.e("exception: ", e.getMessage());
        }
        //--------------------------------------------------------------------------------------
        CartDatabase cartDatabase = CartDatabase.getInstance(context);
        CartDao cartDao = cartDatabase.CartDao();
        int id = cartData.getId();
        //-------------------------------------------------------------------------------------
        holder.deleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteItemExecutor(cartDao, id, holder.getAdapterPosition());
            }
        });
        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quantity = cartData.getQuantity();
                if (quantity<=5)
                    quantity++;
                cartData.setQuantity(quantity);

                UpdateItemExecutor(cartDao, id, quantity);
                notifyDataSetChanged();
                updatePrice();
            }
        });
        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quantity = cartData.getQuantity();
                if (quantity>1)
                    quantity--;
                cartData.setQuantity(quantity);

                UpdateItemExecutor(cartDao, id, quantity);
                notifyDataSetChanged();
                updatePrice();
            }
        });
    }

    private void DeleteItemExecutor(CartDao cartDao, int id, int pos) {

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                // do in background task
                cartDao.deleteById(id);

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        // it will act like postexcecute()
                        cartDataArrayList.remove(pos);
                        notifyDataSetChanged();
                        updatePrice();

                    }
                });

            }
        });
    }

    private void UpdateItemExecutor(CartDao cartDao, int id, int quantity)
    {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                // do in background task
                cartDao.updateById(id, quantity);

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //this will work as postExecute
                    }
                });
            }
        });
    }

    private void updatePrice() {
        int sum = 0;
        for (int i = 0; i < cartDataArrayList.size(); i++)
            sum = sum + (cartDataArrayList.get(i).getPrice()) * cartDataArrayList.get(i).getQuantity();

        cartValue.setText(sum + "/-");
    }

    @Override
    public int getItemCount() {
        return cartDataArrayList.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView cartImg, plus, minus, deleteItem;
        TextView itemName, item_qnt, item_price;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            cartImg = itemView.findViewById(R.id.cart_image);
            plus = itemView.findViewById(R.id.plus);
            minus = itemView.findViewById(R.id.minus);
            deleteItem = itemView.findViewById(R.id.delete_cart_item);
            itemName = itemView.findViewById(R.id.cart_item_name);
            item_qnt = itemView.findViewById(R.id.cart_item_quantity);
            item_price = itemView.findViewById(R.id.cart_item_price2);

        }
    }
}
