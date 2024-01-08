package com.example.foodairport.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodairport.ApiServices.ApiController;
import com.example.foodairport.CartActivity;

import com.example.foodairport.FoodModel.Datum;
import com.example.foodairport.ListItemClickListener;
import com.example.foodairport.R;
import com.example.foodairport.RestaurantActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder>{
    private List<Datum> foodList;
    public final ListItemClickListener itemClickListener;
    Context context;
    Boolean status;

    public FoodAdapter(Boolean status, List<Datum> foodList, ListItemClickListener itemClickListener, Context context) {
        this.foodList = foodList;
        this.itemClickListener = itemClickListener;
        this.context = context;
        this.status = status;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_items,parent,false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {

        Datum dm = foodList.get(position);
        String name  = dm.getAttributes().getFoodName();
        float price = Float.valueOf(dm.getAttributes().getFoodPrice());
        String url = ApiController.url +dm.getAttributes().getFoodPhoto()
                .getData().getAttributes().getUrl();
        holder.tv_name.setText(name);
        holder.tv_desc.setText(dm.getAttributes().getFoodDescription());
        holder.tv_price.setText("AED "+price);


        Picasso.get().load(url).into(holder.f_img);


        holder.addCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (status==false)
                {
                    Toast.makeText(context, "Restaurant is Closed, try after some time", Toast.LENGTH_SHORT).show();
                }
                if (status==true)
                {
                    holder.addCart.setVisibility(View.GONE);
                    holder.viewCart.setVisibility(View.VISIBLE);
                    itemClickListener.onListItemClick(position);
                }

            }
        });




    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public class FoodViewHolder extends RecyclerView.ViewHolder
    {
        ImageView f_img;
        TextView tv_name, tv_desc, tv_price;
        AppCompatButton addCart, viewCart;
        CardView f_card;
        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.f_name);
            tv_desc = itemView.findViewById(R.id.f_desc);
            tv_price = itemView.findViewById(R.id.f_price);
            f_img = itemView.findViewById(R.id.f_image);
            addCart = itemView.findViewById(R.id.add_to_cart);
            viewCart = itemView.findViewById(R.id.view_cart);
            f_card = itemView.findViewById(R.id.food_cardView);

            viewCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.getContext().startActivity(new Intent(view.getContext(), CartActivity.class));
                }
            });
        }
    }
}
