package es.unex.parsiapp.roomdb;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import es.unex.parsiapp.model.Carpeta;
import es.unex.parsiapp.model.Post;

@Database(entities={Carpeta.class, Post.class}, version=1)
public abstract class ParsiDatabase extends RoomDatabase {
    private static ParsiDatabase instance;

    public static ParsiDatabase getInstance(Context context) {
        if(instance == null){
            instance = Room.databaseBuilder(context, ParsiDatabase.class, "parsi.db").build();
        }
        return instance;
    }

    public abstract CarpetaDao getCarpetaDao();
}
