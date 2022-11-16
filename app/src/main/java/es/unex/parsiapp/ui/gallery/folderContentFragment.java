package es.unex.parsiapp.ui.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import es.unex.parsiapp.AppExecutors;
import es.unex.parsiapp.ListAdapterPost;
import es.unex.parsiapp.MenuLateralActivity;
import es.unex.parsiapp.R;
import es.unex.parsiapp.databinding.FragmentFolderContentBinding;
import es.unex.parsiapp.model.Carpeta;
import es.unex.parsiapp.model.Post;
import es.unex.parsiapp.roomdb.ParsiDatabase;
import es.unex.parsiapp.twitterapi.SingleTweet;
import es.unex.parsiapp.twitterapi.TwitterService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class folderContentFragment extends Fragment {
    private List<Post> listposts;
    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.twitter.com/2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    private String bearerTokenApi = "AAAAAAAAAAAAAAAAAAAAAN17jAEAAAAARPbZdHUXnMf%2F1qOKDcvaADYaD8Y%3DCJ2WH2ItpWhqKEvdwIz7hWu6qnUU9UlbYe0LEQtd7E7EfvJRU8";
    private FragmentFolderContentBinding binding;
    private long idCarpetaSeleccionada;

    public folderContentFragment(long id){
        this.idCarpetaSeleccionada = id;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        folderContentViewModel folderContentViewModel =
                new ViewModelProvider(this).get(folderContentViewModel.class);

        binding = FragmentFolderContentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        showTweetsFromFolder(root);
        return root;
    }


    public void showTweetsFromFolder(View root){
        // Conversion a lista de Posts de los tweets recibidos
        Carpeta carpeta = (Carpeta) getActivity().getIntent().getSerializableExtra("ContenidoCarpeta");
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                // Declaracion de la instancia de la BD
                ParsiDatabase database = ParsiDatabase.getInstance(getActivity());
                List<Post> listaPostDB;
                listposts = new ArrayList<Post>();

                listaPostDB = database.getCarpetaDao().getAllPostsFromCarpeta(idCarpetaSeleccionada);

                TwitterService twitterService = retrofit.create(TwitterService.class);

                for(Post p: listaPostDB){
                    twitterService.tweetFromID(p.getId(),"Bearer " + bearerTokenApi).enqueue(new Callback<SingleTweet>() {
                        @Override
                        public void onResponse(Call<SingleTweet> call, Response<SingleTweet> response) {
                            System.out.println(response.errorBody());
                            SingleTweet tweet = response.body();
                            // Conversion a lista de Posts de los tweets recibidos
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
                    });
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}