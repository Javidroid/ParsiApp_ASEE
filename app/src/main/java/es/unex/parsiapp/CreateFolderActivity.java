package es.unex.parsiapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import es.unex.parsiapp.model.Carpeta;
import es.unex.parsiapp.roomdb.ParsiDatabase;

public class CreateFolderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_folder);
    }

    public void onConfirmCreateFolderButton(View v){
        EditText fname = findViewById(R.id.folderName);
        createFolder(fname.getText().toString());
        Intent myIntent = new Intent(CreateFolderActivity.this, MenuLateralActivity.class);
        startActivity(myIntent);
    }

    public void createFolder(String folderName){
        Carpeta c = new Carpeta(folderName);
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                // Declaracion de la instancia de la BD
                ParsiDatabase database = ParsiDatabase.getInstance(CreateFolderActivity.this);

                // Operaciones BD
                for(Carpeta c: database.getCarpetaDao().getAll()){System.out.println(c.getNombre());}
                database.getCarpetaDao().insert(c);
                System.out.println("CARPETA CREADA EXITOSAMENTE. TOTAL CARPETAS = " + database.getCarpetaDao().getAll().size());
            }
        });
    }

    @Override
    protected void onDestroy() {
        ParsiDatabase.getInstance(this).close();
        super.onDestroy();
    }
}