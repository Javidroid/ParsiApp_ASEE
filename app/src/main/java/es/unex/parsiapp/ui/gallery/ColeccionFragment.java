package es.unex.parsiapp.ui.gallery;

import android.content.Intent;
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

import es.unex.parsiapp.AppExecutors;
import es.unex.parsiapp.EditFolderActivity;
import es.unex.parsiapp.ListAdapterFolder;
import es.unex.parsiapp.R;
import es.unex.parsiapp.databinding.FragmentColeccionBinding;
import es.unex.parsiapp.model.Carpeta;
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

                if(listCarpeta != null) {
                    ListAdapterFolder listAdapter = new ListAdapterFolder(listCarpeta, root.getContext(), new ListAdapterFolder.OnItemClickListener() {
                        @Override
                        public void onItemClick(Carpeta item) {
                            moveToFolderContent(item, root);
                        }
                    });
                    RecyclerView recyclerView = root.findViewById(R.id.listRecyclerView);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
                    recyclerView.setAdapter(listAdapter);
                }
            }
        });
    }

    public void borrarCarpeta(View v){
        System.out.println("/// Borrar Carpeta ///");
        // Obtener id de la carpeta
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                // Declaracion de la instancia de la BD
                ParsiDatabase database = ParsiDatabase.getInstance(getActivity());
                // Cambiar 0 por ID carpeta
                database.getCarpetaDao().deleteFolderByID(0);
            }
        });
        // Actualizar vista
    }

    public void editarCarpeta(View v){
        Intent intent = new Intent(getActivity(), EditFolderActivity.class);
        // Añadir Extra con el nombre actual de la carpeta
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    public void moveToFolderContent(Carpeta item, View root){
        Intent intent = new Intent(root.getContext(), folderContentFragment.class);
        intent.putExtra("ContenidoCarpeta", item);
        startActivity(intent);
    }

}