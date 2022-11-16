package es.unex.parsiapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class EditFolderActivity extends AppCompatActivity {

    private String oldFolderName;
    private long idfolder = 0;

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
        EditText fname = findViewById(R.id.folderEditedName);
        fname.setText(this.oldFolderName);
    }

    public void onConfirmEditFolderButton(View v){
        EditText fname = findViewById(R.id.folderEditedName);
        String folderName = fname.getText().toString();
        if(folderName.length() > 0) {
            Intent myIntent = new Intent(EditFolderActivity.this, MenuLateralActivity.class);
            myIntent.putExtra("editedfoldername", folderName);
            myIntent.putExtra("idfolder", this.idfolder);
            startActivity(myIntent);
        }
    }
}