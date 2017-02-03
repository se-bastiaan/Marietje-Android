package eu.se_bastiaan.marietje.data.model;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import java.util.List;


@AutoValue
public abstract class QueueResponse implements Parcelable {

    @SerializedName("started_at")
    public abstract long startedAt();
    @SerializedName("current_time")
    public abstract long currentTime();
    @SerializedName("current_QueueResponse")
    public abstract PlaylistSong currentSong();
    @SerializedName("queue")
    public abstract List<PlaylistSong> queue();
    
    public static QueueResponse.Builder builder() {
        return new AutoValue_QueueResponse.Builder();
    }

    public static TypeAdapter<QueueResponse> typeAdapter(Gson gson) {
        return new AutoValue_QueueResponse.GsonTypeAdapter(gson);
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract QueueResponse.Builder startedAt(long startedAt);
        public abstract QueueResponse.Builder currentTime(long currentTime);
        public abstract QueueResponse.Builder currentSong(PlaylistSong song);
        public abstract QueueResponse.Builder queue(List<PlaylistSong> queue);
        public abstract QueueResponse build();
    }

}
