package es.unex.parsiapp.roomdb;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import es.unex.parsiapp.model.Carpeta;
import es.unex.parsiapp.model.Post;

@Dao
public interface CarpetaDao {
    @Query("SELECT * FROM carpeta")
    public List<Carpeta> getAll();
    // TODO get posts by idcarpeta
    @Query("SELECT * FROM post WHERE carpetaid = :carpeta_id")
    public List<Post> getAllPostsFromCarpeta(long carpeta_id);
    @Insert
    public long insert(Carpeta item);
    @Query("DELETE FROM carpeta")
    public void deleteAll();
    @Update
    public int update(Carpeta item);
}
