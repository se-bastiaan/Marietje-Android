package eu.se_bastiaan.marietje.data.model;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

@AutoValue
public abstract class PlaylistSong implements Parcelable {

    @SerializedName("id")
    public abstract long objectId();
    public abstract Song song();
    @SerializedName("can_move_down")
    public abstract boolean canMoveDown();
    @SerializedName("requested_by")
    public abstract String requester();

    public static PlaylistSong.Builder builder() {
        return new AutoValue_PlaylistSong.Builder();
    }

    public static TypeAdapter<PlaylistSong> typeAdapter(Gson gson) {
        return new AutoValue_PlaylistSong.GsonTypeAdapter(gson);
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract PlaylistSong.Builder objectId(long objectId);
        public abstract PlaylistSong.Builder song(Song song);
        public abstract PlaylistSong.Builder canMoveDown(boolean canMoveDown);
        public abstract PlaylistSong.Builder requester(String requester);
        public abstract PlaylistSong build();
    }

}
