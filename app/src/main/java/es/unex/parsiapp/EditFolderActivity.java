package es.unex.parsiapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import es.unex.parsiapp.model.Carpeta;
import es.unex.parsiapp.roomdb.ParsiDatabase;

public class EditFolderActivity extends AppCompatActivity {

    private String oldFolderName; // Nombre anterior de la carpeta
    private long idfolder = 0; // ID de la carpeta a editar

    /* Metodos de Callback */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_folder);
        this.oldFolderName = this.getIntent().getStringExtra("foldername");
        this.idfolder = this.getIntent().getLongExtra("idfolder", idfolder);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Se obtiene el EditText y se asigna el texto al nombre antiguo
        EditText fname = findViewById(R.id.folderEditedName);
        fname.setText(this.oldFolderName);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ParsiDatabase.getInstance(this).close();
    }

    /* Metodos adicionales */
    // Accion a realizar al pulsar el boton de confirmar tras editar
    public void onConfirmEditFolderButton(View v){
        // Obtencion nuevo nombre
        EditText fname = findViewById(R.id.folderEditedName);
        String folderName = fname.getText().toString();

        // Si el nombre no esta vacio, edita la carpeta y pasa a MenuLateralActivity
        if(folderName.length() > 0) {
            // Editar carpeta
            editFolder(folderName, this.idfolder);
            Intent myIntent = new Intent(EditFolderActivity.this, MenuLateralActivity.class);
            // Lanzar actividad MenuLateralActivity
            startActivity(myIntent);
        }
    }

    // Edita una carpeta (le asigna un nuevo nombre) con un determinado ID
    public void editFolder(String folderName, long idfolder){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                // Declaracion de la instancia de la BD
                ParsiDatabase database = ParsiDatabase.getInstance(EditFolderActivity.this);
                // Se obtiene la carpeta
                Carpeta c = database.getCarpetaDao().getFolder(idfolder);
                // Cambio de nombre y update en BD
                c.setNombre(folderName);
                database.getCarpetaDao().update(c);
            }
        });
    }
}