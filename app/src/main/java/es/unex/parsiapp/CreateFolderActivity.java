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
        String folderName = fname.getText().toString();
        Intent myIntent = new Intent(CreateFolderActivity.this, MenuLateralActivity.class);
        myIntent.putExtra("foldername", folderName);
        startActivityForResult(myIntent, 0);
    }

    @Override
    protected void onDestroy() {
        ParsiDatabase.getInstance(this).close();
        super.onDestroy();
    }
}