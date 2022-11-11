
package es.unex.parsiapp.twitterapi;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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

}
