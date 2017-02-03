package eu.se_bastiaan.marietje.data.model;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import java.util.List;


@AutoValue
public abstract class SongsResponse implements Parcelable {

    @SerializedName("per_page")
    public abstract long pageSize();
    @SerializedName("current_page")
    public abstract long currentPage();
    @SerializedName("last_page")
    public abstract long lastPage();
    public abstract List<Song> data();
    public abstract long total();

    public static SongsResponse.Builder builder() {
        return new AutoValue_SongsResponse.Builder();
    }

    public static TypeAdapter<SongsResponse> typeAdapter(Gson gson) {
        return new AutoValue_SongsResponse.GsonTypeAdapter(gson);
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract SongsResponse.Builder pageSize(long pageSize);
        public abstract SongsResponse.Builder currentPage(long currentPage);
        public abstract SongsResponse.Builder lastPage(long lastPage);
        public abstract SongsResponse.Builder total(long total);
        public abstract SongsResponse.Builder data(List<Song> data);
        public abstract SongsResponse build();
    }
}
