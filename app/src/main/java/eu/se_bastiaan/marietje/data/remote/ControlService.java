package eu.se_bastiaan.marietje.data.remote;

import eu.se_bastiaan.marietje.data.model.Empty;
import eu.se_bastiaan.marietje.data.model.Queue;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;

public interface ControlService {

    /**
     * Get new CSRF token
     * @return {@link Response}
     */
    @GET("/")
    Observable<String> csrf();

    /**
     * Get the queue with the current playing song and those following
     * @return {@link Queue}
     */
    @GET("api/queue")
    Observable<Queue> queue();

    /**
     * Skip current playing song on the server
     * @return {@link Empty}
     */
    @GET("api/skip")
    Observable<Empty> skip();

    /**
     * Move specified song up in the queue
     * @param songId Id of the song
     * @return {@link Empty}
     */
    @FormUrlEncoded
    @POST("api/moveup")
    Observable<Empty> moveUp(@Field("id") long songId);

    /**
     * Move specified song down in the queue
     * @param songId Id of the song
     * @return {@link Empty}
     */
    @FormUrlEncoded
    @POST("api/movedown")
    Observable<Empty> moveDown(@Field("id") long songId);

    /**
     * Cancel specified song in queue
     * @param songId Id of the song
     * @return {@link Empty}
     */
    @FormUrlEncoded
    @POST("api/cancel")
    Observable<Empty> cancel(@Field("id") long songId);

    /**
     * Request specified song in queue
     * @param songId Id of the song
     * @return {@link Empty}
     */
    @FormUrlEncoded
    @POST("api/request")
    Observable<Empty> request(@Field("id") long songId);

    /**
     * Decrease server playback volume
     * @return {@link Empty}
     */
    @GET("api/volumedown")
    Observable<Empty> volumeDown();

    /**
     * Inscrease server playback volume
     * @return {@link Empty}
     */
    @GET("api/volumeup")
    Observable<Empty> volumeUp();

}
