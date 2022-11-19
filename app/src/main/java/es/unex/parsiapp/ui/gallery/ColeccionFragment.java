package es.unex.parsiapp.ui.gallery;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import es.unex.parsiapp.MenuLateralActivity;
import es.unex.parsiapp.databinding.FoldersActivityBinding;
import es.unex.parsiapp.foldersActivity;

public class ColeccionFragment extends Fragment {

    private FoldersActivityBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        GalleryViewModel galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);

        binding = FoldersActivityBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Intent intent = new Intent(root.getContext(), foldersActivity.class);
        startActivity(intent);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        Intent intent = new Intent(getActivity(), MenuLateralActivity.class);
        startActivity(intent);
    }
}