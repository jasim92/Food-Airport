package com.example.foodairport.Databases;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.foodairport.Dao.CartDao;
import com.example.foodairport.MyEntity.CartData;

@Database(entities = {CartData.class}, version = 2,exportSchema = false)
public abstract class CartDatabase extends RoomDatabase {
    public static final String DATABASE_NAME = "cart";
    public abstract CartDao CartDao();
    private static volatile CartDatabase cartDatabase;
    public static synchronized CartDatabase getInstance(Context context)
    {
        if (cartDatabase==null)
        {
            cartDatabase = Room.databaseBuilder(context, CartDatabase.class,DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return cartDatabase;
    }
    public static Callback callback = new Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            new PopulateDbAsync(cartDatabase).execute();
        }
    };
    private static class PopulateDbAsync extends AsyncTask<Void,Void, Void>
    {
        private CartDao cartDao;

        public PopulateDbAsync(CartDatabase cartDatabase) {
            cartDao = cartDatabase.CartDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            cartDao.delete();
            return null;
        }
    }

    public static class InsertDbAsync extends AsyncTask<CartData, Void, Void>
    {
        private CartDao cartDao;

        public InsertDbAsync(CartDao cartDao) {
            this.cartDao = cartDao;
        }

        @Override
        protected Void doInBackground(CartData... cartData) {
            cartDao.insert(cartData[0]);
            return null;
        }
    }

    public static class IsExistAsync extends AsyncTask<Integer, Void, Boolean>
    {
        private CartDao cartDao;

        public IsExistAsync(CartDao cartDao) {
            this.cartDao = cartDao;
        }


        @Override
        protected Boolean doInBackground(Integer... integers) {
            Boolean bn = cartDao.is_exist(integers[0]);
            return bn;
        }

    }


}
