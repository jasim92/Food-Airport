package com.example.foodairport.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodairport.ApiServices.ApiController;
import com.example.foodairport.FoodModel.Data__1;
import com.example.foodairport.ListItemClickListener;
import com.example.foodairport.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder>
{
    private List<Data__1> restaurantList;
    public final ListItemClickListener itemClickListener;
    Context context;

    public RestaurantAdapter(Context context, List<Data__1> restaurantList, ListItemClickListener itemClickListener) {
        this.restaurantList = restaurantList;
        this.itemClickListener = itemClickListener;
        this.context = context;
    }

    @NonNull
    @Override
    public RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurants_items, parent, false);
        return new RestaurantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantViewHolder holder, int position) {
        Data__1 restaurant = restaurantList.get(position);
        holder.nameTv.setText(restaurant.getAttributes().getName());

        Picasso.get().load(ApiController.url +restaurant.getAttributes().getLogo().getData().getAttributes().getUrl()
                ).into(holder.R_img);

        if (restaurant.getAttributes().getStatus()==true)
        {
            holder.statusTv.setText("Open");
        }
        if (restaurant.getAttributes().getStatus()==false)
        {
            holder.statusTv.setTextColor(Color.RED);
            holder.statusTv.setText("Closed");
        }

        holder.r_cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onListItemClick(position);
            }
        });


    }

    @Override
    public int getItemCount() {
        return restaurantList.size();
    }

    public class RestaurantViewHolder extends RecyclerView.ViewHolder
    {
        ImageView R_img, T_img;
        TextView nameTv, statusTv;
        CardView r_cardView;

        public RestaurantViewHolder(@NonNull View itemView) {
            super(itemView);
            R_img = itemView.findViewById(R.id.R_image);
            T_img = itemView.findViewById(R.id.R_type);
            nameTv = itemView.findViewById(R.id.R_name);
            statusTv = itemView.findViewById(R.id.R_status);
            r_cardView = itemView.findViewById(R.id.rest_cardView);
        }
    }
}
