package es.unex.parsiapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import es.unex.parsiapp.model.Post;
import es.unex.parsiapp.roomdb.ParsiDatabase;

public class tweetDetailsActivity extends AppCompatActivity {
    private ShapeableImageView userImage;
    private TextView nombre, userName, tweet, tweetID;
    private ImageButton share, save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tweet);

        Post item = (Post) getIntent().getSerializableExtra("Post");
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
        save.setTag(R.string.idSave, item.getId());
    }

    public void compartirPostTweet(View v){
        // Accion de compartir
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, "Poner aqui enlace del tweet");
        intent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(intent, null);
        startActivity(shareIntent);
    }

    public void addPostToCarpetaTweet(View v){
        // TODO cambiar carpeta_id por la carpeta seleccionada
        long carpeta_id = 1;

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                // Declaracion de la instancia de la BD
                ParsiDatabase database = ParsiDatabase.getInstance(tweetDetailsActivity.this);

                // Obtencion del ID del post
                ImageButton imgButton = (ImageButton) v;
                String post_id = (String) imgButton.getTag(R.string.idSave);

                // Insertar post
                Post p = new Post(post_id, carpeta_id);
                database.getPostDao().insert(p);
            }
        });
    }
}