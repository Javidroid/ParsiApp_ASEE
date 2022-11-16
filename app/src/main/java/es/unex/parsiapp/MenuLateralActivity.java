package es.unex.parsiapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import es.unex.parsiapp.databinding.ActivityMenuLateralBinding;
import es.unex.parsiapp.model.Carpeta;
import es.unex.parsiapp.model.Post;
import es.unex.parsiapp.roomdb.ParsiDatabase;

public class MenuLateralActivity extends AppCompatActivity{
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMenuLateralBinding binding;

    /* Metodos de callback */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Vista
        binding = ActivityMenuLateralBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Support Action Bar
        setSupportActionBar(binding.appBarMenuLateral.toolbar);
        binding.appBarMenuLateral.toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_settings)
                .setOpenableLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_menu_lateral);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lateral, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_menu_lateral);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    protected void onDestroy() {
        ParsiDatabase.getInstance(this).close();
        super.onDestroy();
    }

    /* Metodos añadidos*/

    // Accion al pulsar boton de "crear carpeta"
    public void onCreateFolderButton(View v){
        Intent intent = new Intent(MenuLateralActivity.this, CreateFolderActivity.class);
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
        Intent intent = new Intent(MenuLateralActivity.this, EditFolderActivity.class);
        intent.putExtra("foldername", folderName);
        intent.putExtra("idfolder", idFolder);
        // Se inicia la actividad EditFolderActivity
        startActivity(intent);
    }

    // Accion al pulsar el boton de "compartir post"
    public void compartirPost(View v){
        // Accion de compartir
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, "Poner aqui enlace del tweet");
        intent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(intent, null);
        startActivity(shareIntent);
    }

    // Accion al pulsar el boton de "guardar post"
    public void addPostToCarpeta(View v){
        // TODO cambiar carpeta_id por la carpeta seleccionada
        long carpeta_id = 1;

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                // Declaracion de la instancia de la BD
                ParsiDatabase database = ParsiDatabase.getInstance(MenuLateralActivity.this);

                // Obtencion del ID del post
                ImageButton imgButton = (ImageButton) v;
                String post_id = (String) imgButton.getTag(R.string.idSave);

                // Insertar post
                Post p = new Post(post_id, carpeta_id);
                database.getPostDao().insert(p);
            }
        });
    }

}