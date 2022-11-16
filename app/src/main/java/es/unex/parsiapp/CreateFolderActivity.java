package es.unex.parsiapp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import es.unex.parsiapp.model.Carpeta;
import es.unex.parsiapp.roomdb.ParsiDatabase;

public class CreateFolderActivity extends AppCompatActivity {

    /* Metodos de Callback */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_folder);
    }

    @Override
    protected void onDestroy() {
        ParsiDatabase.getInstance(this).close();
        super.onDestroy();
    }

    // Accion a realizar al pulsar el boton de crear tras establecer el nombre
    public void onConfirmCreateFolderButton(View v){
        // Obtencion del nombre
        EditText fname = findViewById(R.id.folderName);
        String folderName = fname.getText().toString();
        // Si el nombre no esta vacio
        if(folderName.length() > 0) {
            // Crea la carpeta
            createFolder(folderName);
            Intent myIntent = new Intent(CreateFolderActivity.this, MenuLateralActivity.class);
            // Lanzar actividad MenuLateralActivity
            startActivity(myIntent);
        }
    }

    // Crea una nueva carpeta con nombre folderName
    public void createFolder(String folderName){
        Carpeta c = new Carpeta(folderName);

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                // Declaracion de la instancia de la BD
                ParsiDatabase database = ParsiDatabase.getInstance(CreateFolderActivity.this);
                // Insercion en BD
                long id = database.getCarpetaDao().insert(c);
                c.setIdDb(id);
            }
        });
    }
}