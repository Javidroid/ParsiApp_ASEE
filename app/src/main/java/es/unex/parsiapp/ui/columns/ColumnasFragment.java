package es.unex.parsiapp.ui.columns;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import es.unex.parsiapp.R;
import es.unex.parsiapp.databinding.FragmentColeccionBinding;
import es.unex.parsiapp.databinding.FragmentColumnasBinding;
import es.unex.parsiapp.model.Carpeta;
import es.unex.parsiapp.model.Columna;

public class ColumnasFragment extends Fragment {

    private List<Columna> listColumna;
    private FragmentColumnasBinding binding;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentColumnasBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}