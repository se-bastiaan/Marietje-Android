package eu.se_bastiaan.marietje.data.model;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

@AutoValue
public abstract class Permissions {

    @SerializedName("can_cancel")
    public abstract boolean canCancel();
    @SerializedName("can_skip")
    public abstract boolean canSkip();
    @SerializedName("can_move")
    public abstract boolean canMove();
    @SerializedName("can_control_volume")
    public abstract boolean canControlVolume();

    public static Permissions create(boolean cancel, boolean skip, boolean move, boolean controlVolume) {
        return new AutoValue_Permissions(cancel, skip, move, controlVolume);
    }

    public static TypeAdapter<Permissions> typeAdapter(Gson gson) {
        return new AutoValue_Permissions.GsonTypeAdapter(gson);
    }

}