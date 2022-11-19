package es.unex.parsiapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import es.unex.parsiapp.model.Carpeta;
import es.unex.parsiapp.roomdb.ParsiDatabase;
import es.unex.parsiapp.ui.gallery.folderContentFragment;

public class foldersActivity extends AppCompatActivity {
    private List<Carpeta> listCarpeta;
    private boolean bandera;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.folders_activity);

        bandera= true;

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                // Declaracion de la instancia de la BD
                ParsiDatabase database = ParsiDatabase.getInstance(foldersActivity.this);

                listCarpeta = database.getCarpetaDao().getAll();

                if(listCarpeta != null) {
                    ListAdapterFolder listAdapter = new ListAdapterFolder(listCarpeta,foldersActivity.this , new ListAdapterFolder.OnItemClickListener() {
                        @Override
                        public void onItemClick(Carpeta item) {
                            moveToFolderContent(item);
                        }
                    });
                    // La UI debe de ejecutarse en un mainThread (si no, peta)
                    AppExecutors.getInstance().mainThread().execute(new Runnable() {
                        @Override
                        public void run() {
                            RecyclerView recyclerView = findViewById(R.id.listRecyclerView);
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setLayoutManager(new LinearLayoutManager(foldersActivity.this));
                            recyclerView.setAdapter(listAdapter);
                        }
                    });
                }
            }
        });
    }
    /*
    public void borrarCarpeta(View v){
        System.out.println("/// Borrar Carpeta ///");
        Button b = (Button) v;
        long idFolder = (long) b.getTag(R.string.idDelete);
        // Obtener id de la carpeta
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                // Declaracion de la instancia de la BD
                ParsiDatabase database = ParsiDatabase.getInstance(getActivity());
                // Cambiar 0 por ID carpeta
                database.getCarpetaDao().deleteFolderByID(idFolder);
                // Actualizar vista
                View root = binding.getRoot();
                showCarpetas(root);
            }
        });
    }*/



    public void moveToFolderContent(Carpeta item){
        bandera= false;
        Fragment fragment = new folderContentFragment(item.getIdDb());
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.folderContainer, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    // Accion al pulsar boton de "crear carpeta"
    public void onCreateFolderButton(View v){
        Intent intent = new Intent(foldersActivity.this, CreateFolderActivity.class);
        // Se inicia la actividad CreateFolderActivity
        startActivity(intent);
    }

    // Accion al pulsar el boton de "editar carpeta"
    public void onEditFolderButton(View v){
        // Obtencion del nombre e ID de carpeta
        Button b = (Button) v;
        String folderName = (String) b.getTag(R.string.idEdit);
        long idFolder = (long) b.getTag(R.string.idFolder);

        // Se pasan el nombre e ID de la carpeta como Extras en el Intent
        Intent intent = new Intent(foldersActivity.this, EditFolderActivity.class);
        intent.putExtra("foldername", folderName);
        intent.putExtra("idfolder", idFolder);
        // Se inicia la actividad EditFolderActivity
        startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        System.out.println(bandera+"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        if (keyCode == event.KEYCODE_BACK && this.getClass() == foldersActivity.class && bandera) {
            Intent intent = new Intent(this, MenuLateralActivity.class);
            startActivity(intent);
        }
        bandera= true;
        return super.onKeyDown(keyCode, event);
    }
}