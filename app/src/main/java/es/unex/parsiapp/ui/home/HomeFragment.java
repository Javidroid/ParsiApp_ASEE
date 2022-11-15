package es.unex.parsiapp.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import es.unex.parsiapp.ListAdapterPost;
import es.unex.parsiapp.R;
import es.unex.parsiapp.databinding.FragmentHomeBinding;
import es.unex.parsiapp.model.Post;
import es.unex.parsiapp.twitterapi.SingleTweet;
import es.unex.parsiapp.twitterapi.TweetResults;
import es.unex.parsiapp.twitterapi.TwitterService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment {
    private List<Post> listposts;
    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.twitter.com/2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    private FragmentHomeBinding binding;
    private String bearerTokenApi = "AAAAAAAAAAAAAAAAAAAAAN17jAEAAAAARPbZdHUXnMf%2F1qOKDcvaADYaD8Y%3DCJ2WH2ItpWhqKEvdwIz7hWu6qnUU9UlbYe0LEQtd7E7EfvJRU8";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        showTweetsFromColumna(root);

        // Boton de Refresh
        Button b = (Button) root.findViewById(R.id.refresh_button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTweetsFromColumna(root);
            }
        });

        return root;
    }

    /* Para mostrar los tweets en la columna. Se realiza la llamada especificada
    * en twitterService.[llamada] */
    public void showTweetsFromColumna(View root){
        // --- API --- //
        // NO se pueden hacer llamadas a la API en el hilo principal
        TwitterService twitterService = retrofit.create(TwitterService.class);

        // Hacer un .enqueue es equivalente a llamarla en un hilo separado
        String query = "valorant"; // -> TODO Concepto a buscar, se debe de sacar del filtro EditText establecido al crear la columna

        twitterService.tweetsFromQuery(query,"Bearer " + bearerTokenApi).enqueue(new Callback<TweetResults>() {
            @Override
            public void onResponse(Call<TweetResults> call, Response<TweetResults> response) {
                TweetResults tweetResults = response.body();
                // Conversion a lista de Posts de los tweets recibidos
                listposts = tweetResults.toPostList();

                ListAdapterPost listAdapter = new ListAdapterPost(listposts, root.getContext());
                RecyclerView recyclerView = root.findViewById(R.id.listRecyclerView);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
                recyclerView.setAdapter(listAdapter);
            }

            @Override
            public void onFailure(Call<TweetResults> call, Throwable t) {
                t.printStackTrace();
            }
        });


        // Ejemplo de obtener un Tweet en base a su ID:
        /*
        twitterService.tweetFromID("1590012633410727937","Bearer " + bearerTokenApi).enqueue(new Callback<SingleTweet>() {
            @Override
            public void onResponse(Call<SingleTweet> call, Response<SingleTweet> response) {
                SingleTweet tweet = response.body();
                // Conversion a lista de Posts de los tweets recibidos
                listposts = new ArrayList<Post>();
                listposts.add(tweet.toPost());

                ListAdapterPost listAdapter = new ListAdapterPost(listposts, root.getContext());
                RecyclerView recyclerView = root.findViewById(R.id.listRecyclerView);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
                recyclerView.setAdapter(listAdapter);
            }

            @Override
            public void onFailure(Call<SingleTweet> call, Throwable t) {
                t.printStackTrace();
            }
        });*/
    }

    // Esto es un metodo dummy para acordarme de luego hacerlo
    public void addPostToCarpeta(){

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}