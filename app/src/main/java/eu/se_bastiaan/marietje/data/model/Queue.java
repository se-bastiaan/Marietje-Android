package eu.se_bastiaan.marietje.data.model;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import java.util.List;


@AutoValue
public abstract class Queue implements Parcelable {

    @SerializedName("started_at")
    public abstract long startedAt();
    @SerializedName("current_time")
    public abstract long currentTime();
    @SerializedName("current_song")
    public abstract PlaylistSong currentSong();
    @SerializedName("queue")
    public abstract List<PlaylistSong> queuedSongs();
    
    public static Queue.Builder builder() {
        return new AutoValue_Queue.Builder();
    }

    public static TypeAdapter<Queue> typeAdapter(Gson gson) {
        return new AutoValue_Queue.GsonTypeAdapter(gson);
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Queue.Builder startedAt(long startedAt);
        public abstract Queue.Builder currentTime(long currentTime);
        public abstract Queue.Builder currentSong(PlaylistSong song);
        public abstract Queue.Builder queuedSongs(List<PlaylistSong> queue);
        public abstract Queue build();
    }

}
