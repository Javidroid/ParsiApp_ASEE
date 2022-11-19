package es.unex.parsiapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import es.unex.parsiapp.roomdb.ParsiDatabase;

public class CreateColumnActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_column);
    }

    @Override
    protected void onDestroy() {
        ParsiDatabase.getInstance(this).close();
        super.onDestroy();
    }

    // Accion a realizar al pulsar el boton de crear tras establecer los parametros
    public void onConfirmCreateColumnButton(View v){
        // Obtencion del nombre
        EditText cname = findViewById(R.id.columnName);
        String columnName = cname.getText().toString();
        // Obtencion de la busqueda

        /*
        // Si el nombre no esta vacio
        if(columnName.length() > 0) {
            // Crea la carpeta
            createFolder(folderName);
            Intent myIntent = new Intent(CreateFolderActivity.this, MenuLateralActivity.class);
            // Lanzar actividad MenuLateralActivity
            startActivity(myIntent);
        }*/
    }
}