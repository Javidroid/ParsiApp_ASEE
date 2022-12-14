package es.unex.parsiapp.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

import es.unex.parsiapp.util.AppExecutors;
import es.unex.parsiapp.R;
import es.unex.parsiapp.model.Carpeta;
import es.unex.parsiapp.model.Post;
import es.unex.parsiapp.roomdb.ParsiDatabase;

public class TweetDetailsActivity extends AppCompatActivity {

    private ShapeableImageView userImage;
    private TextView nombre, userName, tweet, tweetID;
    private ImageButton share, save;

    // --- Métodos de Callback ---

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tweet);

        Post item = (Post) getIntent().getSerializableExtra("Post");
        int sa = (int) getIntent().getSerializableExtra("Saved");
        userImage = findViewById(R.id.iconImageViewDetailTweet);
        nombre = findViewById(R.id.nameViewDetailTweet);
        userName = findViewById(R.id.userNameViewDetailTweet);
        tweet = findViewById(R.id.tweetViewDetailTweet);
        tweetID = findViewById(R.id.tweetID);
        share = findViewById(R.id.shareDetailTweet);
        save = findViewById(R.id.saveDetailTweet);

        Picasso.get()
                .load(item.getProfilePicture())
                .error(R.mipmap.ic_launcher_round)
                .fit()
                .into(userImage)
        ;
        nombre.setText(item.getAuthorUsername());
        userName.setText("@" + item.getAuthorUsername());
        tweet.setText(item.getContenido());
        tweetID.setText(item.getId());
        // Establece el ID del post en los botones de share y save
        share.setTag(R.string.idShare, item.getId());
        if(sa==1){
            save.setImageResource(R.drawable.saved);
        }
        save.setTag(R.string.idSave, item.getId());
    }

    public void addPostToCarpetaTweet(View v){
        final long[] folder_id = {1};

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {

                // Declaracion de la instancia de la BD
                ParsiDatabase database = ParsiDatabase.getInstance(TweetDetailsActivity.this);
                List<Carpeta> folders = database.getCarpetaDao().getAll();
                String[] nameFolders = new String[folders.size()];

                for(int i = 0; i < folders.size(); i++) {
                    nameFolders[i] = folders.get(i).getNombre();
                }

                save = (ImageButton) v;
                AlertDialog.Builder popupFolders = new AlertDialog.Builder(TweetDetailsActivity.this);

                popupFolders.setTitle("Seleccione una carpeta").setItems(nameFolders, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        savePostInFolder(v, folders, which, database);
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

    // Guarda un post
    public void savePostInFolder(View v, List<Carpeta> folders, int which, ParsiDatabase database){
        String data = "Se ha guardado en la carpeta " + folders.get(which).getNombre();
        Toast.makeText(TweetDetailsActivity.this, data, Toast.LENGTH_SHORT).show();
        long folder_id = folders.get(which).getIdDb();

        // Obtencion del ID del post
        ImageButton imgButton = (ImageButton) v;
        long post_id = (long) imgButton.getTag(R.string.idSaveDb);

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                Post pOld = database.getPostDao().getPost(post_id);
                Post p = new Post(pOld.getId(), folder_id);
                p.setContenido(pOld.getContenido());
                p.setTimestamp(pOld.getTimestamp());
                p.setAuthorUsername(pOld.getAuthorUsername());
                p.setProfilePicture(pOld.getProfilePicture());
                p.setAuthorId(pOld.getAuthorId());
                database.getPostDao().insert(p);
            }
        });
    }
}