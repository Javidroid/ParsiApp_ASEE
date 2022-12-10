package es.unex.parsiapp.util;

import android.content.Context;

import es.unex.parsiapp.repository.PostRepository;
import es.unex.parsiapp.roomdb.ParsiDatabase;
import es.unex.parsiapp.twitterapi.PostNetworkDataSource;
import es.unex.parsiapp.ui.home.HomeViewModelFactory;

public class AppContainer {

    private ParsiDatabase database;
    private PostNetworkDataSource networkDataSource;
    public PostRepository repository;
    public HomeViewModelFactory factory;


    public AppContainer(Context context){
        database = ParsiDatabase.getInstance(context);
        networkDataSource = PostNetworkDataSource.getInstance();
        repository = PostRepository.getInstance(database.getPostDao(), networkDataSource);
        factory = new HomeViewModelFactory(repository);
    }

}
