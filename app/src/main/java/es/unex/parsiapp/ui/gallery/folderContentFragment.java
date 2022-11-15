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

import java.util.List;

import es.unex.parsiapp.ListAdapterPost;
import es.unex.parsiapp.R;
import es.unex.parsiapp.databinding.FragmentFolderContentBinding;
import es.unex.parsiapp.model.Carpeta;
import es.unex.parsiapp.model.Post;



public class folderContentFragment extends Fragment {
    private List<Post> listposts;
    private FragmentFolderContentBinding binding;

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
        listposts = carpeta.getListaPosts();
        ListAdapterPost listAdapter = new ListAdapterPost(listposts, root.getContext());
        RecyclerView recyclerView = root.findViewById(R.id.listRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
        recyclerView.setAdapter(listAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}