package es.unex.parsiapp.ui.gallery;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import es.unex.parsiapp.AppExecutors;
import es.unex.parsiapp.CreateFolderActivity;
import es.unex.parsiapp.ListAdapterFolder;
import es.unex.parsiapp.ListAdapterPost;
import es.unex.parsiapp.MenuLateralActivity;
import es.unex.parsiapp.R;
import es.unex.parsiapp.databinding.FragmentColeccionBinding;
import es.unex.parsiapp.model.Carpeta;
import es.unex.parsiapp.model.Post;
import es.unex.parsiapp.roomdb.ParsiDatabase;

public class ColeccionFragment extends Fragment {

    private List<Carpeta> listCarpeta;
    private FragmentColeccionBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        GalleryViewModel galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);

        binding = FragmentColeccionBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        showCarpetas(root);

        return root;
    }

    public void showCarpetas(View root){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                // Declaracion de la instancia de la BD
                ParsiDatabase database = ParsiDatabase.getInstance(getActivity());

                listCarpeta = database.getCarpetaDao().getAll();
                for(Carpeta c: listCarpeta){
                    System.out.println("----------> CARPETA " + c.getNombre());
                }
            }
        });
        if(listCarpeta != null) {
            ListAdapterFolder listAdapter = new ListAdapterFolder(listCarpeta, root.getContext());
            RecyclerView recyclerView = root.findViewById(R.id.listRecyclerView);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
            recyclerView.setAdapter(listAdapter);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}