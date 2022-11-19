package es.unex.parsiapp.ui.columns;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import es.unex.parsiapp.AppExecutors;
import es.unex.parsiapp.ListAdapterColumna;
import es.unex.parsiapp.ListAdapterFolder;
import es.unex.parsiapp.MenuLateralActivity;
import es.unex.parsiapp.R;
import es.unex.parsiapp.databinding.FragmentColeccionBinding;
import es.unex.parsiapp.databinding.FragmentColumnasBinding;
import es.unex.parsiapp.model.Carpeta;
import es.unex.parsiapp.model.Columna;
import es.unex.parsiapp.roomdb.ParsiDatabase;

public class ColumnasFragment extends Fragment {

    private List<Columna> listColumna;
    private FragmentColumnasBinding binding;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentColumnasBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        showColumnas(root);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void showColumnas(View root){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                // Declaracion de la instancia de la BD
                ParsiDatabase database = ParsiDatabase.getInstance(getActivity());

                listColumna = database.getColumnaDao().getAll();
                for(Columna c : listColumna){
                    System.out.println("----------------------> " + c.getNombre());
                }

                if(listColumna != null) {
                    ListAdapterColumna listAdapter = new ListAdapterColumna(listColumna, root.getContext(), new ListAdapterColumna.OnItemClickListener() {
                        @Override
                        public void onItemClick(Columna item) {
                            item.setColumnaActual(true);
                            updateColumnaActual(item);
                            Toast.makeText(getContext(), "Se ha cambiado la columna actual a " + item.getNombre(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    // La UI debe de ejecutarse en un mainThread (si no, peta)
                    AppExecutors.getInstance().mainThread().execute(new Runnable() {
                        @Override
                        public void run() {
                            RecyclerView recyclerView = root.findViewById(R.id.listRecyclerView);
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
                            recyclerView.setAdapter(listAdapter);
                        }
                    });
                }

            }
        });
    }

    public void updateColumnaActual(Columna newC){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                ParsiDatabase database = ParsiDatabase.getInstance(getActivity());
                // Obtencion columna actual
                Columna oldC = database.getColumnaDao().getColumnaActual();
                if(oldC != null){
                    oldC.setColumnaActual(false);
                    database.getColumnaDao().update(oldC);
                }
                database.getColumnaDao().update(newC);
            }
        });
    }
}