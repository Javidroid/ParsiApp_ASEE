package es.unex.parsiapp.roomdb;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import es.unex.parsiapp.model.Carpeta;

@Dao
public interface CarpetaDao {
    @Query("SELECT * FROM carpeta")
    public List<Carpeta> getAll();
    @Insert
    public long insert(Carpeta item);
    @Query("DELETE FROM carpeta")
    public void deleteAll();
    @Update
    public int update(Carpeta item);
}
