package es.unex.parsiapp.roomdb;

import androidx.lifecycle.LiveData;
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
    // SELECTS
    @Query("SELECT * FROM carpeta")
    public List<Carpeta> getAll();
    @Query("SELECT * FROM carpeta WHERE idDb = :carpeta_id")
    public Carpeta getFolder(long carpeta_id);
    @Query("SELECT * FROM post WHERE carpetaid = :carpeta_id")
    public List<Post> getAllPostsFromCarpeta(long carpeta_id);
    @Query("SELECT * FROM carpeta")
    public LiveData<List<Carpeta>> getAllLiveData();
    @Query("SELECT * FROM carpeta WHERE idDb = :carpeta_id")
    public LiveData<Carpeta> getFolderLiveData(long carpeta_id);

    // INSERT
    @Insert
    public long insert(Carpeta item);

    // DELETE
    @Query("DELETE FROM carpeta")
    public void deleteAll();
    @Query("DELETE FROM carpeta WHERE idDb = :carpeta_id")
    public void deleteFolderByID(long carpeta_id);

    //UPDATE
    @Update
    public int update(Carpeta item);
}
