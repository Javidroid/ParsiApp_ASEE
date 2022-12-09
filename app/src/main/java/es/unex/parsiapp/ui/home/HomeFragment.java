package es.unex.parsiapp.ui.home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.List;

import es.unex.parsiapp.AppExecutors;
import es.unex.parsiapp.ListAdapterPost;
import es.unex.parsiapp.PostRepository;
import es.unex.parsiapp.R;
import es.unex.parsiapp.databinding.FragmentHomeBinding;
import es.unex.parsiapp.model.Columna;
import es.unex.parsiapp.model.Post;
import es.unex.parsiapp.roomdb.ParsiDatabase;
import es.unex.parsiapp.ui.tweetDetailsActivity;
import es.unex.parsiapp.twitterapi.PostNetworkDataSource;

public class HomeFragment extends Fragment {

    private List<Post> listposts; // Lista de posts en Home
    private FragmentHomeBinding binding; // Binding
    private PostRepository mRepository;
    private View rootV;
    private SwipeRefreshLayout refresh;

    // --- Métodos de Callback ---

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        // Obtener view
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Obtención del mRepository
        mRepository = PostRepository.getInstance(ParsiDatabase.getInstance(getContext()).getPostDao(), PostNetworkDataSource.getInstance());
        mRepository.getCurrentPostsHome().observe(getViewLifecycleOwner(), this::onPostsLoaded);

        this.rootV = root;

        // Mostrar tweets
        showTweetsFromColumna(root);

        // Refresh
        refresh = (SwipeRefreshLayout) root.findViewById(R.id.swipeRefreshLayout);
        refresh.setColorSchemeResources(R.color.white);
        refresh.setProgressBackgroundColorSchemeResource(R.color.blueParsi);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showTweetsFromColumna(rootV);
                refresh.setRefreshing(false);
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // --- Otros métodos ---

    // Muestra los tweets realizando una llamada a la API
    public void showTweetsFromColumna(View root){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String max_posts = sharedPreferences.getString("max_posts", "20");

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                // Declaracion de la instancia de la BD
                ParsiDatabase database = ParsiDatabase.getInstance(getContext());

                // Obtencion columna actual
                Columna c = database.getColumnaDao().getColumnaActual();

                if (c != null) {
                    AppExecutors.getInstance().mainThread().execute(new Runnable() {
                        @Override
                        public void run() {
                            mRepository.setColumna(c, max_posts);
                            TextView t = (TextView) root.findViewById(R.id.addColumn);
                            t.setVisibility(View.INVISIBLE);
                        }
                    });
                }
            }
        });
    }

    public void onPostsLoaded(List<Post> posts){
        System.out.println("ACCEDIENDO A ONPOSTSLOADED");
        listposts = posts;
        // Actualizar vista
        ListAdapterPost listAdapter = new ListAdapterPost(listposts, rootV.getContext(), new ListAdapterPost.OnItemClickListener() {
            @Override
            public void onItemClick(Post item) {
                showPost(item, rootV);
            }
        });
        RecyclerView recyclerView = rootV.findViewById(R.id.listRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(rootV.getContext()));
        recyclerView.setAdapter(listAdapter);
    }

    public void showPost(Post item, View root){
        Intent intent = new Intent(root.getContext(), tweetDetailsActivity.class);
        intent.putExtra("Post", item);
        intent.putExtra("Saved", 0);
        startActivity(intent);
    }

}