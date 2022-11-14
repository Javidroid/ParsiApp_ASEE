package es.unex.parsiapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

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
import es.unex.parsiapp.roomdb.ParsiDatabase;

public class MenuLateralActivity extends AppCompatActivity{
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMenuLateralBinding binding;
    // Add a Carpeta Request Code
    private static final int ADD_CARPETA_REQUEST = 0;


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
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_menu_lateral);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        // --- BD --- //
        // NO se pueden hacer llamadas a la BD en el hilo principal
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                // Declaracion de la instancia de la BD
                ParsiDatabase database = ParsiDatabase.getInstance(MenuLateralActivity.this);

                // Operaciones BD

                List<Carpeta> carpetaList = database.getCarpetaDao().getAll();
                System.out.println("/// CARPETAS USUARIO ///");
                for(Carpeta c: carpetaList){
                    System.out.println(c.getNombre());
                }
            }
        });

        // --- Testeo en la UI ---
        // esto borradlo o lo que querais

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("Entered onActivityResult()");
        if (requestCode == ADD_CARPETA_REQUEST){
            if (resultCode == RESULT_OK){
                String folderName = data.getExtras().getString("foldername");
                createFolder(folderName);
            }
        }
    }

    public void onCreateFolderButton(View v){
        Intent intent = new Intent(MenuLateralActivity.this, CreateFolderActivity.class);
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

}