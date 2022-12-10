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

import es.unex.parsiapp.model.Carpeta;
import es.unex.parsiapp.model.Columna;
import es.unex.parsiapp.model.Post;
import es.unex.parsiapp.roomdb.PostDao;
import es.unex.parsiapp.twitterapi.PostNetworkDataSource;
import es.unex.parsiapp.util.AppExecutors;

public class PostRepository {
    private static final String LOG_TAG = PostRepository.class.getSimpleName();

    // For Singleton instantiation
    private static PostRepository sInstance;
    private final PostDao mPostDao;
    private final PostNetworkDataSource mPostNetworkDataSource;
    private final AppExecutors mExecutors = AppExecutors.getInstance();
    private final MutableLiveData<Columna> columnaFilterLiveData = new MutableLiveData<>();
    private final MutableLiveData<Long> carpetaFilterLiveData = new MutableLiveData<>();
    private final Map<Columna, Long> lastUpdateTimeMillisMap = new HashMap<>();
    private static final long MIN_TIME_FROM_LAST_FETCH_MILLIS = 30000;

    private PostRepository(PostDao postDao, PostNetworkDataSource postNetworkDataSource){
        mPostDao = postDao;
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

    public synchronized static PostRepository getInstance(PostDao dao, PostNetworkDataSource nds){
        Log.d(LOG_TAG, "Getting the repository");
        if (sInstance == null) {
            sInstance = new PostRepository(dao, nds);
            Log.d(LOG_TAG, "Made new repository");
        }
        return sInstance;
    }

    public void setColumna(final Columna c, String max_posts){
        columnaFilterLiveData.setValue(c);
        AppExecutors.getInstance().diskIO().execute(() -> {
            if(isFetchNeeded(c)){
                doFetchPosts(c, max_posts);
            }
        });
    }

    public void setCarpeta(final Carpeta c){
        carpetaFilterLiveData.setValue(c.getIdDb());
    }

    public void doFetchPosts(Columna c, String max_posts){
        Log.d(LOG_TAG, "Fetching posts from API");
        System.out.println("Fetching posts from API");
        AppExecutors.getInstance().diskIO().execute(() -> {
            mPostDao.deleteAllPostsWithoutCarpeta();
            mPostNetworkDataSource.fetchPosts(c, max_posts);
            lastUpdateTimeMillisMap.put(c, System.currentTimeMillis());
        });
    }

    public LiveData<List<Post>> getCurrentPostsHome() {
        System.out.println("OBTENIENDO TODOS LOS POSTS DE HOME");
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
                return mPostDao.getAllPostsFromCarpetaLiveData(input);
            }
        });
    }

    private boolean isFetchNeeded(Columna c){
        return true;
    }
}
