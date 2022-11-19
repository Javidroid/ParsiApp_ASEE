package es.unex.parsiapp.roomdb;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import es.unex.parsiapp.model.Columna;

@Dao
public interface ColumnaDao {
    @Query("SELECT * FROM columna")
    public List<Columna> getAll();
    @Insert
    public long insert(Columna c);
    @Query("DELETE FROM columna")
    public void deleteAll();
    @Query("DELETE FROM columna WHERE idDb = :columna_id")
    public void deleteColumnaByID(long columna_id);
    @Update
    public int update(Columna c);
}
