package eu.se_bastiaan.marietje.data.remote;

import eu.se_bastiaan.marietje.data.model.EmptyResponse;
import eu.se_bastiaan.marietje.data.model.QueueResponse;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;

public interface ControlService {

    /**
     * Get the queue with the current playing song and those following
     * @return {@link QueueResponse}
     */
    @GET("api/queue")
    Observable<QueueResponse> queue();

    /**
     * Skip current playing song on the server
     * @return {@link EmptyResponse}
     */
    @GET("api/skip")
    Observable<EmptyResponse> skip();

    /**
     * Move specified song up in the queue
     * @param songId Id of the song
     * @return {@link EmptyResponse}
     */
    @FormUrlEncoded
    @POST("api/moveup")
    Observable<EmptyResponse> moveUp(@Field("id") long songId);

    /**
     * Move specified song down in the queue
     * @param songId Id of the song
     * @return {@link EmptyResponse}
     */
    @FormUrlEncoded
    @POST("api/movedown")
    Observable<EmptyResponse> moveDown(@Field("id") long songId);

    /**
     * Cancel specified song in queue
     * @param songId Id of the song
     * @return {@link EmptyResponse}
     */
    @FormUrlEncoded
    @POST("api/cancel")
    Observable<EmptyResponse> cancel(@Field("id") long songId);

    /**
     * Request specified song in queue
     * @param songId Id of the song
     * @return {@link EmptyResponse}
     */
    @FormUrlEncoded
    @POST("api/request")
    Observable<EmptyResponse> request(@Field("id") long songId);

    /**
     * Decrease server playback volume
     * @return {@link EmptyResponse}
     */
    @GET("api/volumedown")
    Observable<EmptyResponse> volumeDown();

    /**
     * Inscrease server playback volume
     * @return {@link EmptyResponse}
     */
    @GET("api/volumeup")
    Observable<EmptyResponse> volumeUp();

}
