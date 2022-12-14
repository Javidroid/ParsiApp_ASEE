package es.unex.parsiapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import es.unex.parsiapp.MyApplication;
import es.unex.parsiapp.ui.home.HomeViewModel;
import es.unex.parsiapp.util.AppContainer;
import es.unex.parsiapp.util.AppExecutors;
import es.unex.parsiapp.R;
import es.unex.parsiapp.model.Columna;
import es.unex.parsiapp.roomdb.ParsiDatabase;

public class CreateColumnActivity extends AppCompatActivity {

    private RadioButton radioButton; // Boton de tipo radio seleccionado actualmente
    private Columna editedColumn = null; // Columna que se esta editando
    private CreateColumnViewModel mViewModel;

    // --- Metodos de Callback ---

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_column);

        // Se obtiene el grupo de botones de tipo radio
        // Grupo de botones de tipo radio
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.api_call_type_selection);
        // Se obtiene el boton radio actualmente seleccionado
        this.radioButton = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());

        /* Si createColumn es true, se creara una nueva columna. Si es false se editara una columna
            ya existente y se cargaran los datos de esta en la IU */
        boolean createColumn = true;
        createColumn = getIntent().getBooleanExtra("create", createColumn);


        AppContainer appContainer = ((MyApplication) this.getApplication()).appContainer;
        mViewModel = new ViewModelProvider(this, (ViewModelProvider.Factory) appContainer.CCfactory).get(CreateColumnViewModel.class);

        mViewModel.getColumnBeingEdited().observe(this, columna -> {
            editedColumn = columna;
            setUIForEditColumn(editedColumn);
        });

        // Si se esta editando una columna, se prepara la IU para cargar los datos
        if (!createColumn){
            long id_columna = 1;
            id_columna = getIntent().getLongExtra("idcolumna", id_columna);
            mViewModel.setColumnBeingEdited(id_columna);
       }
    }

    // --- Otros métodos ---

    // Cambia la UI para editar una columna
    public void setUIForEditColumn(Columna c){
        // Obtencion de todos los inputs de la IU
        EditText column_name = (EditText) findViewById(R.id.columnName);
        EditText query_or_user = (EditText) findViewById(R.id.queryOrUser);
        RadioButton rb_query = (RadioButton) findViewById(R.id.query_selection);
        RadioButton rb_user = (RadioButton) findViewById(R.id.user_selection);
        Button b = (Button) findViewById(R.id.create_column_button);

        // Cambiar sus datos a los de la columna
        column_name.setText(c.getNombre());
        query_or_user.setText(c.getApiCall());
        if(c.getApiCallType() == Columna.ApiCallType.QUERY){
            rb_query.setChecked(true);
            rb_user.setChecked(false);
        } else if (c.getApiCallType() == Columna.ApiCallType.USER){
            rb_query.setChecked(false);
            rb_user.setChecked(true);
        }

        // Se cambia el boton de "Editar" a "Crear"
        b.setText("Editar");
    }

    /* Escucha que boton de radio esta seleccionado actualmente y lo asigna a this.radioButton */
    public void onRadioButtonClicked(View view) {
        // Comprobar si el boton esta actualmente seleccionado
        boolean checked = ((RadioButton) view).isChecked();

        // Comprobar que radio button ha sido seleccionado
        switch(view.getId()) {
            case R.id.query_selection:
                if (checked)
                    this.radioButton = (RadioButton) findViewById(R.id.query_selection);
                    break;
            case R.id.user_selection:
                if (checked)
                    this.radioButton = (RadioButton) findViewById(R.id.user_selection);
                    break;
        }
    }

    /* Accion a realizar al pulsar el boton de crear/editar tras establecer los parametros */
    public void onConfirmCreateColumnButton(View v){
        // Obtencion del nombre
        EditText cname = findViewById(R.id.columnName);
        String columnName = cname.getText().toString();
        // Obtencion de la busqueda
        EditText query = findViewById(R.id.queryOrUser);
        String selectedQuery = query.getText().toString();

        Columna.ApiCallType apiCallType;
        // Obtencion del tipo de columna (Query o Usuario)
        if (radioButton.getId() == R.id.query_selection){
            apiCallType = Columna.ApiCallType.QUERY;
        } else if (radioButton.getId() == R.id.user_selection){
            apiCallType = Columna.ApiCallType.USER;
        } else { return; }

        // Si el nombre y la busqueda no estan vacios
        if(columnName.length() > 0 && selectedQuery.length() > 0) {
            // Crea o edita la carpeta correspondiente
            if(this.editedColumn != null){
                editColumn(columnName, selectedQuery, apiCallType);
                finish();
            } else {
                createColumn(columnName, selectedQuery, apiCallType);
                Toast.makeText(CreateColumnActivity.this, "Se ha establecido " + columnName + " como columna actual", Toast.LENGTH_SHORT).show();
                Intent myIntent = new Intent(CreateColumnActivity.this, MenuLateralActivity.class);
                // Lanzar actividad MenuLateralActivity
                startActivity(myIntent);
            }
        }
    }

    /* Crea una nueva columna */
    public void createColumn(String columnName, String query, Columna.ApiCallType apiCallType){
        Columna c = new Columna(columnName, query);
        c.setApiCallType(apiCallType);
        c.setColumnaActual(true);
        mViewModel.createColumna(c);
    }

    /* Edita una columna existente */
    public void editColumn(String columnName, String query, Columna.ApiCallType apiCallType){
        this.editedColumn.setNombre(columnName);
        this.editedColumn.setApiCall(query);
        this.editedColumn.setApiCallType(apiCallType);
        mViewModel.editColumna(editedColumn);
    }
}