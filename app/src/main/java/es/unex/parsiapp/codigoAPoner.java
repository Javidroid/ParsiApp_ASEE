package es.unex.parsiapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import es.unex.parsiapp.model.Post;
import es.unex.parsiapp.roomdb.ParsiDatabase;
import es.unex.parsiapp.twitterapi.TweetResults;
import es.unex.parsiapp.twitterapi.TwitterService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class codigoAPoner extends AppCompatActivity {
    List<Post> listposts;
    private String bearerTokenApi = "AAAAAAAAAAAAAAAAAAAAAN17jAEAAAAARPbZdHUXnMf%2F1qOKDcvaADYaD8Y%3DCJ2WH2ItpWhqKEvdwIz7hWu6qnUU9UlbYe0LEQtd7E7EfvJRU8";


    public void init() {
        // --- API --- //
        // NO se pueden hacer llamadas a la API en el hilo principal
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.twitter.com/2/tweets/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TwitterService twitterService = retrofit.create(TwitterService.class);

        // Hacer un .enqueue es equivalente a llamarla en un hilo separado
        twitterService.tweetResults("Bearer " + bearerTokenApi).enqueue(new Callback<TweetResults>() {
            @Override
            public void onResponse(Call<TweetResults> call, Response<TweetResults> response) {
                TweetResults tweetResults = response.body();
                // Conversion a lista de Posts de los tweets recibidos
                listposts = new ArrayList<>();
                listposts = tweetResults.toPostList();

                ListAdapter listAdapter = new ListAdapter(listposts, codigoAPoner.this);
                RecyclerView recyclerView = findViewById(R.id.listRecyclerView);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(codigoAPoner.this));
                recyclerView.setAdapter(listAdapter);
            }

            @Override
            public void onFailure(Call<TweetResults> call, Throwable t) {
                t.printStackTrace();
            }
        });

        // --- BD --- //
        // NO se pueden hacer llamadas a la BD en el hilo principal
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                // Declaracion de la instancia de la BD
                ParsiDatabase database = ParsiDatabase.getInstance(codigoAPoner.this);

                // Operaciones BD

                // TODO Aqui borro ahora para quitar los cambios a la BD pero esto hay que quitarlo
                database.getPostDao().deleteAll();
                database.getCarpetaDao().deleteAll();
            }
        });
    }
}
