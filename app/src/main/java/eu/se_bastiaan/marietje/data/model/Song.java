package eu.se_bastiaan.marietje.data.model;

import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

@AutoValue
public abstract class Song implements Comparable<Song>, Parcelable {

    @SerializedName("id")
    public abstract long objectId();
    public abstract long duration();
    public abstract String title();
    public abstract String artist();
    @SerializedName("uploader_name")
    public abstract String uploader();

    public static Song.Builder builder() {
        return new AutoValue_Song.Builder();
    }

    public static TypeAdapter<Song> typeAdapter(Gson gson) {
        return new AutoValue_Song.GsonTypeAdapter(gson);
    }

    @Override
    public int compareTo(@NonNull Song another) {
        return title().compareToIgnoreCase(title());
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Song.Builder objectId(long id);
        public abstract Song.Builder duration(long duration);
        public abstract Song.Builder title(String title);
        public abstract Song.Builder artist(String artist);
        public abstract Song.Builder uploader(String uploaderName);
        public abstract Song build();
    }

}
