package es.unex.parsiapp.twitterapi;

import java.util.List;

import es.unex.parsiapp.model.Post;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface TwitterService {
    // TODO configurar para realizar las llamadas que se requieran
    @GET("https://api.twitter.com/2/tweets/search/recent?expansions=author_id&tweet.fields=author_id&media.fields=url&user.fields=name,username,profile_image_url&query=%23valorant&max_results=10")
    Call<TweetResults> tweetResults(@Header("Authorization") String authHeader);
}
