package es.unex.parsiapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import es.unex.parsiapp.model.Carpeta;
import es.unex.parsiapp.model.Columna;
import es.unex.parsiapp.roomdb.ParsiDatabase;

public class CreateColumnActivity extends AppCompatActivity {

    private RadioGroup radioGroup;
    private RadioButton radioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_column);

        this.radioGroup = (RadioGroup) findViewById(R.id.api_call_type_selection);
        this.radioButton = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());

    }

    @Override
    protected void onDestroy() {
        ParsiDatabase.getInstance(this).close();
        super.onDestroy();
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.query_selection:
                if (checked)
                    this.radioButton = (RadioButton) findViewById(R.id.query_selection);
                    System.out.println("QUERY");
                    break;
            case R.id.user_selection:
                if (checked)
                    this.radioButton = (RadioButton) findViewById(R.id.user_selection);
                    System.out.println("USUARIO");
                    break;
        }
    }

    // Accion a realizar al pulsar el boton de crear tras establecer los parametros
    public void onConfirmCreateColumnButton(View v){
        // Obtencion del nombre
        EditText cname = findViewById(R.id.columnName);
        String columnName = cname.getText().toString();
        // Obtencion de la busqueda
        EditText query = findViewById(R.id.queryOrUser);
        String selectedQuery = query.getText().toString();


        // Si el nombre y la busqueda no estan vacios
        if(columnName.length() > 0 && selectedQuery.length() > 0) {
            // Crea la carpeta
            createColumn(columnName, selectedQuery);
            Intent myIntent = new Intent(CreateColumnActivity.this, MenuLateralActivity.class);
            // Lanzar actividad MenuLateralActivity
            startActivity(myIntent);
        }
    }

    // Crea una nueva carpeta con nombre folderName
    public void createColumn(String columnName, String query){
        Columna c = new Columna(columnName, query);
        if (radioButton.getId() == R.id.query_selection){
            c.setApiCallType(Columna.ApiCallType.QUERY);
        } else if (radioButton.getId() == R.id.user_selection){
            c.setApiCallType(Columna.ApiCallType.USER);
        } else { return; }

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                // Declaracion de la instancia de la BD
                ParsiDatabase database = ParsiDatabase.getInstance(CreateColumnActivity.this);
                // Insercion en BD
                long id = database.getColumnaDao().insert(c);
                c.setIdDb(id);
            }
        });
    }
}