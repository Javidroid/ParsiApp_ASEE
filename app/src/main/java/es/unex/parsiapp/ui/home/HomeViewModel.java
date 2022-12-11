package es.unex.parsiapp.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.unex.parsiapp.model.Columna;
import es.unex.parsiapp.model.Post;
import es.unex.parsiapp.repository.PostRepository;

public class HomeViewModel extends ViewModel {

    private final PostRepository mRepository;
    private final LiveData<List<Post>> mPosts;
    private Columna mColumna;
    private String mmax_posts;
    // Para limitar el refresh: Cooldown de 5 segundos
    private final Map<Columna, Long> lastUpdateTimeMillisMap = new HashMap<>();
    private static final long MIN_TIME_FROM_LAST_FETCH_MILLIS = 5000;


    public HomeViewModel(PostRepository repository) {
        mRepository = repository;
        mPosts = repository.getCurrentPostsHome();
    }

    public void setColumna(Columna columna, String max_posts){
        mColumna = columna;
        mmax_posts = max_posts;
        mRepository.setColumna(columna, mmax_posts);
    }

    public void onRefresh() {
        if (isRefreshNeeded(mColumna)){
            mRepository.doFetchPosts(mColumna, mmax_posts);
            lastUpdateTimeMillisMap.put(mColumna, System.currentTimeMillis());
        }
    }

    private boolean isRefreshNeeded(Columna c) {
        Long lastFetchTimeMillis = lastUpdateTimeMillisMap.get(c);
        lastFetchTimeMillis = lastFetchTimeMillis == null ? 0L : lastFetchTimeMillis;
        long timeFromLastFetch = System.currentTimeMillis() - lastFetchTimeMillis;
        return timeFromLastFetch > MIN_TIME_FROM_LAST_FETCH_MILLIS;
    }

    public LiveData<List<Post>> getPosts() {
        return mPosts;
    }
}