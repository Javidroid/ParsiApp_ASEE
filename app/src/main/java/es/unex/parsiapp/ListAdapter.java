package es.unex.parsiapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

import es.unex.parsiapp.model.Post;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    private List<Post> mData;
    private LayoutInflater mInlfater;
    private Context context;

    public ListAdapter(List<Post> postList, Context context){
        this.mInlfater = LayoutInflater.from(context);
        this.context = context;
        this.mData = postList;
    }

    //Obtiene el numero de post que hay en una lista
    @Override
    public int getItemCount(){ return mData.size();}

    //Establece el diseño que tiene que tener cada post al mostrarse
    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = mInlfater.inflate(R.layout.list_tweet, null);
        return new ListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ListAdapter.ViewHolder holder, final int position) {
        holder.bindData(mData.get(position));
    }

    //Reestablece el contenidode la variable mData, es decir una nueva lista de posts
    public void setItems(List<Post> postList) { mData = postList;}


    public class ViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView userImage;
        TextView nombre, userName, time, tweet;

        ViewHolder(View itemView){
            super(itemView);
            userImage = itemView.findViewById(R.id.iconImageView);
            nombre = itemView.findViewById(R.id.nameView);
            userName = itemView.findViewById(R.id.userNameView);
            time = itemView.findViewById(R.id.timeView);
            tweet = itemView.findViewById(R.id.tweetView);
        }

        void bindData(@NonNull final Post item) {
            Picasso.get()
                    .load(item.getProfilePicture())
                    .into(userImage)
            ;
            nombre.setText(item.getAuthorUsername());
            userName.setText(item.getAuthorUsername());
            time.setText(item.getTimestamp());
            tweet.setText(item.getContenido());
        }
    }
}