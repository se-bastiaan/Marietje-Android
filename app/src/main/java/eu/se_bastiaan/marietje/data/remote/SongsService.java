package eu.se_bastiaan.marietje.data.remote;

import eu.se_bastiaan.marietje.data.model.Songs;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

public interface SongsService {

    /**
     * Get list of songs on the server
     * @param page Parameter to select page
     * @param pageSize Parameter to determine a pages' length
     * @param searchQuery Parameter to search for songs by artist and/or title
     * @param uploaderQuery Parameter to search for songs by a specific uploader
     * @return {@link Songs}
     */
    @FormUrlEncoded
    @POST("api/songs")
    Observable<Songs> songs(@Field("page") long page, @Field("pagesize") long pageSize,
                            @Field("all") String searchQuery, @Field("uploader") String uploaderQuery);

    /**
     * Get list of all the songs on the server that can be managed by the user
     * @param page Parameter to select page
     * @param pageSize Parameter to determine a pages' length
     * @param artistQuery Parameter to search for songs by artist
     * @param titleQuery Parameter to search for songs by title
     * @return {@link Songs}
     */
    @FormUrlEncoded
    @POST("api/managesongs")
    Observable<Songs> manageSongs(@Field("page") long page, @Field("pagesize") long pageSize,
                                  @Field("artist") String artistQuery, @Field("title") String titleQuery);

}
