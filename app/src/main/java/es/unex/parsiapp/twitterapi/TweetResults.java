
package es.unex.parsiapp.twitterapi;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import es.unex.parsiapp.model.Post;

public class TweetResults {

    @SerializedName("data")
    @Expose
    private List<Datum> data = null;
    @SerializedName("includes")
    @Expose
    private Includes includes;
    @SerializedName("meta")
    @Expose
    private Meta meta;

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public TweetResults withData(List<Datum> data) {
        this.data = data;
        return this;
    }

    public Includes getIncludes() {
        return includes;
    }

    public void setIncludes(Includes includes) {
        this.includes = includes;
    }

    public TweetResults withIncludes(Includes includes) {
        this.includes = includes;
        return this;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public TweetResults withMeta(Meta meta) {
        this.meta = meta;
        return this;
    }

    /*
        Metodo para pasar un objeto TweetResults a una lista de Posts
     */
    public List<Post> toPostList(){
        int i = 0;
        List<Post> postList = new ArrayList<Post>();

        for(Datum d: this.data){
            Post p = new Post(d.getId());
            p.setAuthorId(d.getAuthorId());
            p.setContenido(d.getText());
            p.setAuthorUsername(this.includes.getUsers().get(i).getUsername());
            p.setProfilePicture(this.includes.getUsers().get(i).getProfileImageUrl());
            p.setTimestamp(d.getCreatedAt());

            postList.add(p);
            i++;
        }
        return postList;
    }

}
