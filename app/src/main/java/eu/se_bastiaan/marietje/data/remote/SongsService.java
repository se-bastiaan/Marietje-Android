package eu.se_bastiaan.marietje.data.remote;

import eu.se_bastiaan.marietje.data.model.SongsResponse;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface SongsService {

    /**
     * Get list of songs on the server
     * @param page Parameter to select page
     * @param pageSize Parameter to determine a pages' length
     * @param searchQuery Parameter to search for songs by artist and/or title
     * @param uploaderQuery Parameter to search for songs by a specific uploader
     * @return {@link SongsResponse}
     */
    @GET("api/songs")
    Observable<SongsResponse> songs(@Query("page") long page, @Query("pagesize") long pageSize,
                                    @Query("all") String searchQuery, @Query("uploader") String uploaderQuery);

    /**
     * Get list of all the songs on the server that can be managed by the user
     * @param page Parameter to select page
     * @param pageSize Parameter to determine a pages' length
     * @param artistQuery Parameter to search for songs by artist
     * @param titleQuery Parameter to search for songs by title
     * @return {@link SongsResponse}
     */
    @GET("api/managesongs")
    Observable<SongsResponse> manageSongs(@Query("page") long page, @Query("pagesize") long pageSize,
                                          @Query("artist") String artistQuery, @Query("title") String titleQuery);

}
