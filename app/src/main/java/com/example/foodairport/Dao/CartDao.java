package com.example.foodairport.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.foodairport.MyEntity.CartData;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Dao
public interface CartDao {
    @Query("select * from CartData")
    List<CartData> getAllItems();

    @Insert
    void insert(CartData cartData);

    @Query("select exists(select * from CartData where id = :cartId)")
    Boolean is_exist(int cartId);

    @Query("delete from cartdata where id = :cartId")
    void deleteById(int  cartId);

    @Query("delete from cartdata where id = :rId")
    void deleteByRestId(String  rId);

    @Query("delete from cartdata")
    void delete();

    @Query("update Cartdata set quantity= :quant where id = :cartId")
    void updateById(int cartId, int quant);
}
