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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMenuLateralBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_settings)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_menu_lateral);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        // TODO Esto es para crear carpetas. Es MUY cutre. Más cutre que Ponte.
        // Mejor cambiar la creación de carpetas a un Fragment y de ahí hacer las cosas
        // uwu
        long idSupp = 0;
        if(this.getIntent().getStringExtra("foldername") != null){
            createFolder(this.getIntent().getStringExtra("foldername"));
        } else if (this.getIntent().getStringExtra("editedfoldername") != null){
            long id = this.getIntent().getLongExtra("idfolder", idSupp);
            editFolder(this.getIntent().getStringExtra("editedfoldername"), id);
        }

        // --- BD --- //
        // NO se pueden hacer llamadas a la BD en el hilo principal
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                // Declaracion de la instancia de la BD
                ParsiDatabase database = ParsiDatabase.getInstance(MenuLateralActivity.this);

                // Operaciones BD
                // Muestro las carpetas el usuario (esto era para ver si funcionaba, se puede quitar)
                List<Carpeta> carpetaList = database.getCarpetaDao().getAll();
                System.out.println("/// CARPETAS USUARIO ///");
                for(Carpeta c: carpetaList){
                    System.out.println(c.getNombre() + " - " + c.getIdDb());
                }
            }
        });

        // --- Testeo en la UI ---
        // esto borradlo o lo que querais

    }

    @Override
    protected void onNewIntent(Intent intent) {
        System.out.println("-------> INTENT DATA: " + intent.getStringExtra("foldername"));
        super.onNewIntent(intent);
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


    public void onCreateFolderButton(View v){
        Intent intent = new Intent(MenuLateralActivity.this, CreateFolderActivity.class);
        startActivity(intent);
    }

    public void onEditFolderButton(View v){
        Button b = (Button) v;
        String folderName = (String) b.getTag(R.string.idEdit);
        long idFolder = (long) b.getTag(R.string.idFolder);

        Intent intent = new Intent(MenuLateralActivity.this, EditFolderActivity.class);
        intent.putExtra("foldername", folderName);
        intent.putExtra("idfolder", idFolder);
        startActivity(intent);
    }

    public void createFolder(String folderName){
        Carpeta c = new Carpeta(folderName);

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                // Declaracion de la instancia de la BD
                ParsiDatabase database = ParsiDatabase.getInstance(MenuLateralActivity.this);
                long id = database.getCarpetaDao().insert(c);
                System.out.println("CARPETA CON NOMBRE " + c.getNombre() + " CREADA EXITOSAMENTE. TOTAL CARPETAS = " + database.getCarpetaDao().getAll().size());
                c.setIdDb(id);
            }
        });
    }

    public void editFolder(String folderName, long idfolder){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                // Declaracion de la instancia de la BD
                ParsiDatabase database = ParsiDatabase.getInstance(MenuLateralActivity.this);
                Carpeta c = database.getCarpetaDao().getFolder(idfolder);
                c.setNombre(folderName);
                database.getCarpetaDao().update(c);
                System.out.println("CARPETA EDITADA EXITOSAMENTE");
            }
        });
    }

    public void compartirPost(View v){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, "Poner aqui enlace del tweet");
        intent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(intent, null);
        startActivity(shareIntent);
    }

    public void addPostToCarpeta(View v){
        long carpeta_id = 1;
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                // Declaracion de la instancia de la BD
                ParsiDatabase database = ParsiDatabase.getInstance(MenuLateralActivity.this);

                ImageButton imgButton = (ImageButton) v;
                String post_id = (String) imgButton.getTag(R.string.idSave);
                System.out.println("POST ID A SER GUARDADO: " + post_id);

                Post p = new Post(post_id, carpeta_id);
                database.getPostDao().insert(p);
            }
        });
    }

}