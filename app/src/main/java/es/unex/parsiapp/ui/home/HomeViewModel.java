package es.unex.parsiapp.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import es.unex.parsiapp.model.Columna;
import es.unex.parsiapp.model.Post;
import es.unex.parsiapp.repository.PostRepository;

public class HomeViewModel extends ViewModel {

    private final PostRepository mRepository;
    private final LiveData<List<Post>> mPosts;
    private Columna mColumna;
    private String mmax_posts;


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
        mRepository.doFetchPosts(mColumna, mmax_posts);
    }

    public LiveData<List<Post>> getPosts() {
        return mPosts;
    }
}