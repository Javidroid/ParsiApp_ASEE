package es.unex.parsiapp.repository;

import android.util.Log;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.unex.parsiapp.R;
import es.unex.parsiapp.model.Carpeta;
import es.unex.parsiapp.model.Columna;
import es.unex.parsiapp.model.Post;
import es.unex.parsiapp.roomdb.CarpetaDao;
import es.unex.parsiapp.roomdb.ColumnaDao;
import es.unex.parsiapp.roomdb.ParsiDatabase;
import es.unex.parsiapp.roomdb.PostDao;
import es.unex.parsiapp.twitterapi.PostNetworkDataSource;
import es.unex.parsiapp.ui.CreateColumnActivity;
import es.unex.parsiapp.util.AppExecutors;

public class PostRepository {
    private static final String LOG_TAG = PostRepository.class.getSimpleName();

    // For Singleton instantiation
    private static PostRepository sInstance;

    private final PostDao mPostDao;
    private final CarpetaDao mCarpetaDao;
    private final ColumnaDao mColumnaDao;

    private final LiveData<List<Carpeta>> carpetaList;
    private final LiveData<List<Columna>> columnaList;
    private LiveData<Columna> columnBeingEdited;


    private final PostNetworkDataSource mPostNetworkDataSource;
    private final AppExecutors mExecutors = AppExecutors.getInstance();

    // MutableLiveData
    private final MutableLiveData<Columna> columnaFilterLiveData = new MutableLiveData<>();
    private final MutableLiveData<Long> carpetaFilterLiveData = new MutableLiveData<>();
    private final MutableLiveData<Long> columnaBeingEditedFilterLiveData = new MutableLiveData<>();

    // Refresh
    private final Map<String, Long> lastUpdateTimeMillisMap = new HashMap<>();
    private static final long MIN_TIME_FROM_LAST_FETCH_MILLIS = 10000;

    private PostRepository(PostDao postDao, CarpetaDao folderDao, ColumnaDao colDao, PostNetworkDataSource postNetworkDataSource){
        mPostDao = postDao;
        mCarpetaDao = folderDao;
        mColumnaDao = colDao;

        carpetaList = mCarpetaDao.getAllLiveData();
        columnaList = mColumnaDao.getAllFromLiveData();

        mPostNetworkDataSource = postNetworkDataSource;

        // Live Data que obtiene los Posts de la red:
        LiveData<Post[]> networkData = mPostNetworkDataSource.getCurrentPosts();
        networkData.observeForever(newPostsFromNetwork -> {
            System.out.println("Detectados nuevos posts a través del Observer");
            mExecutors.diskIO().execute(() -> {
                if(newPostsFromNetwork.length > 0){
                    System.out.println("BORRANDO ANTIGUOS POSTS DE ROOM");
                    mPostDao.deleteAllPostsWithoutCarpeta();
                }
                List<Post> postList = Arrays.asList(newPostsFromNetwork);
                for(Post p: postList){
                    p.setIdCarpeta(-1);
                }
                postDao.bulkInsert(postList);
                Log.d(LOG_TAG, "New values inserted in Room");
            });
        });
    }

    public synchronized static PostRepository getInstance(PostDao pdao, CarpetaDao fdao, ColumnaDao cdao, PostNetworkDataSource nds){
        Log.d(LOG_TAG, "Getting the repository");
        if (sInstance == null) {
            sInstance = new PostRepository(pdao, fdao, cdao, nds);
            Log.d(LOG_TAG, "Made new repository");
        }
        return sInstance;
    }

    public void setColumna(String max_posts){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                Columna c = mColumnaDao.getColumnaActual();
                if(c != null){
                    AppExecutors.getInstance().mainThread().execute(new Runnable() {
                        @Override
                        public void run() {
                            columnaFilterLiveData.setValue(c);
                        }
                    });
                    if(isFetchNeeded(c)){
                        doFetchPosts(c, max_posts);
                    }
                }
            }
        });
    }

    public void setCarpeta(final Carpeta c){
        carpetaFilterLiveData.setValue(c.getIdDb());
    }

    public void setColumnaActual(Columna c){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                // Obtencion columna actual
                Columna oldC = mColumnaDao.getColumnaActual();
                if(oldC != null){
                    oldC.setColumnaActual(false);
                    mColumnaDao.update(oldC);
                }
                mColumnaDao.update(c);
            }
        });
    }

    public void setColumnBeingEdited(long id_columna){
        columnaBeingEditedFilterLiveData.setValue(id_columna);
        System.out.println("COLUMNA INTRODUCIDA EN MUTABLE");
    }

    public LiveData<Columna> getColumnBeingEdited(){
       System.out.println("ENTRANDO EN GETCOLUMN REPOSITORY");
       return Transformations.switchMap(columnaBeingEditedFilterLiveData, new Function<Long, LiveData<Columna>>() {
           @Override
           public LiveData<Columna> apply(Long input) {
               System.out.println("COLUMNA DEVUELTA ---> " + mColumnaDao.getColumnaLiveData(input).getValue().getNombre());
               return mColumnaDao.getColumnaLiveData(input);
           }
       });
    }

    public void doFetchPosts(Columna c, String max_posts){
        Log.d(LOG_TAG, "Fetching posts from API");
        System.out.println("Fetching posts from API");
        AppExecutors.getInstance().diskIO().execute(() -> {
            mPostDao.deleteAllPostsWithoutCarpeta();
            mPostNetworkDataSource.fetchPosts(c, max_posts);
            lastUpdateTimeMillisMap.put(c.getApiCall(), System.currentTimeMillis());
        });
    }

    public LiveData<List<Post>> getCurrentPostsHome() {
        System.out.println("OBTENIENDO TODOS LOS POSTS DE HOME (POST REPOSITORY)");
        return Transformations.switchMap(columnaFilterLiveData, new Function<Columna, LiveData<List<Post>>>() {
            @Override
            public LiveData<List<Post>> apply(Columna input) {
                return mPostDao.getAllLiveData();
            }
        });
    }

    public LiveData<List<Post>> getCurrentPostsFolder() {
        return Transformations.switchMap(carpetaFilterLiveData, new Function<Long, LiveData<List<Post>>>() {
            @Override
            public LiveData<List<Post>> apply(Long input) {
                System.out.println("OBTENIENDO TODOS LOS POSTS DE UNA CARPETA (POST REPOSITORY)");
                return mPostDao.getAllPostsFromCarpetaLiveData(input);
            }
        });
    }

    public LiveData<List<Carpeta>> getAllFolders(){
        return carpetaList;
    }

    public LiveData<List<Columna>> getAllColumnas(){
        return columnaList;
    }

    public void createColumna(Columna c){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                Columna oldC = mColumnaDao.getColumnaActual();
                if(oldC != null){
                    oldC.setColumnaActual(false);
                    mColumnaDao.update(oldC);
                }
                mColumnaDao.insert(c);
            }
        });
    }

    public void editColumna(Columna c){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mColumnaDao.update(c);
            }
        });
    }

    private boolean isFetchNeeded(Columna c){
        Long lastFetchTimeMillis = lastUpdateTimeMillisMap.get(c.getApiCall());
        lastFetchTimeMillis = lastFetchTimeMillis == null ? 0L : lastFetchTimeMillis;
        long timeFromLastFetch = System.currentTimeMillis() - lastFetchTimeMillis;
        return timeFromLastFetch > MIN_TIME_FROM_LAST_FETCH_MILLIS;
    }
}
