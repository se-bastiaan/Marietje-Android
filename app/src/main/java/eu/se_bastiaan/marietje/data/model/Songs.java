package eu.se_bastiaan.marietje.data.model;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import java.util.List;


@AutoValue
public abstract class Songs implements Parcelable {

    @SerializedName("per_page")
    public abstract long pageSize();
    @SerializedName("current_page")
    public abstract long currentPage();
    @SerializedName("last_page")
    public abstract long lastPage();
    public abstract List<Song> data();
    public abstract long total();

    public static Songs.Builder builder() {
        return new AutoValue_Songs.Builder();
    }

    public static TypeAdapter<Songs> typeAdapter(Gson gson) {
        return new AutoValue_Songs.GsonTypeAdapter(gson);
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Songs.Builder pageSize(long pageSize);
        public abstract Songs.Builder currentPage(long currentPage);
        public abstract Songs.Builder lastPage(long lastPage);
        public abstract Songs.Builder total(long total);
        public abstract Songs.Builder data(List<Song> data);
        public abstract Songs build();
    }
}
