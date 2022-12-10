package es.unex.parsiapp.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import es.unex.parsiapp.util.AppExecutors;
import es.unex.parsiapp.listadapter.ListAdapterPostSaved;
import es.unex.parsiapp.repository.PostRepository;
import es.unex.parsiapp.R;
import es.unex.parsiapp.model.Carpeta;
import es.unex.parsiapp.model.Post;
import es.unex.parsiapp.roomdb.ParsiDatabase;
import es.unex.parsiapp.twitterapi.PostNetworkDataSource;


public class folderContentActivity extends AppCompatActivity {
    private List<Post> listposts;
    ImageButton b;
    private PostRepository mRepository;

    // --- Métodos de Callback ---

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_folder_content);

        mRepository = PostRepository.getInstance(ParsiDatabase.getInstance(this).getPostDao(), PostNetworkDataSource.getInstance());
        mRepository.getCurrentPostsFolder().observe(this, this::onPostsLoaded);

        showContentFolder();
    }

    // --- Otros métodos ---

    // Muestra el contenido de la carpeta
    public void showContentFolder(){
        Carpeta item = (Carpeta) getIntent().getSerializableExtra("folderContent");
        mRepository.setCarpeta(item);
    }

    // Actualiza la UI con los posts
    public void onPostsLoaded(List<Post> posts){
        listposts = posts;

        // Actualizar vista
        ListAdapterPostSaved listAdapter = new ListAdapterPostSaved(listposts, folderContentActivity.this, new ListAdapterPostSaved.OnItemClickListener(){
            @Override
            public void onItemClick(Post item) {
                detailPostFromFolder(item);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.listRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(folderContentActivity.this));
        recyclerView.setAdapter(listAdapter);
    }

    // Ir a los detalles de un Post desde una carpeta
    public void detailPostFromFolder(Post item){
        Intent intent = new Intent(folderContentActivity.this, tweetDetailsActivity.class);
        intent.putExtra("Post", item);
        intent.putExtra("Saved", 1);
        startActivity(intent);
    }


    // Accion al pulsar el boton de "compartir post"
    public void compartirPost(View v){
        // Accion de compartir
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, "Poner aqui enlace del tweet");
        intent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(intent, null);
        startActivity(shareIntent);
    }


    // Accion al pulsar el boton de "eliminar post"
    public void deletePostFromCarpeta(View v){

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {

                // Declaracion de la instancia de la BD
                ParsiDatabase database = ParsiDatabase.getInstance(folderContentActivity.this);
                b = (ImageButton) v;
                AlertDialog.Builder popupFolders = new AlertDialog.Builder(folderContentActivity.this);

                popupFolders.setTitle("Borrar post de carpeta").setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Obtencion del ID del post
                        ImageButton imgButton = (ImageButton) v;
                        long post_id = (long) imgButton.getTag(R.string.idSaveDb);
                        AppExecutors.getInstance().diskIO().execute(new Runnable() {
                            @Override
                            public void run() {
                                database.getPostDao().delete(post_id);
                            }
                        });
                        Toast.makeText(folderContentActivity.this, "Se ha borrado con éxito.", Toast.LENGTH_SHORT).show();
                        showContentFolder();
                    }
                }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AppExecutors.getInstance().mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog alertDialog = popupFolders.create();
                        alertDialog.show();
                    }
                });
            }
        });
    }
}