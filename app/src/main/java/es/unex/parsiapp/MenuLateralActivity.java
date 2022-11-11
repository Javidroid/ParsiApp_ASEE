package es.unex.parsiapp;

import android.os.Bundle;
import android.view.View;
import android.view.Menu;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import es.unex.parsiapp.databinding.ActivityMenuLateralBinding;
import es.unex.parsiapp.model.Carpeta;
import es.unex.parsiapp.model.Post;
import es.unex.parsiapp.roomdb.ParsiDatabase;
import es.unex.parsiapp.twitterapi.Datum;
import es.unex.parsiapp.twitterapi.TweetResults;
import es.unex.parsiapp.twitterapi.TwitterService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MenuLateralActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMenuLateralBinding binding;
    private String bearerTokenApi = "AAAAAAAAAAAAAAAAAAAAAN17jAEAAAAARPbZdHUXnMf%2F1qOKDcvaADYaD8Y%3DCJ2WH2ItpWhqKEvdwIz7hWu6qnUU9UlbYe0LEQtd7E7EfvJRU8";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMenuLateralBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMenuLateral.toolbar);
        binding.appBarMenuLateral.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_menu_lateral);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

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
                List<Post> postList = tweetResults.toPostList();

                for(Post p: postList){
                    System.out.println(p.toString());
                }
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
                ParsiDatabase database = ParsiDatabase.getInstance(MenuLateralActivity.this);

                // Operaciones BD

                // TODO Aqui borro ahora para quitar los cambios a la BD pero esto hay que quitarlo
                database.getPostDao().deleteAll();
                database.getCarpetaDao().deleteAll();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lateral, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_menu_lateral);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    protected void onDestroy() {
        ParsiDatabase.getInstance(this).close();
        super.onDestroy();
    }
}