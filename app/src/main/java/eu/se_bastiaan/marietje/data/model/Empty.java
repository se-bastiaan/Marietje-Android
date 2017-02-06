package eu.se_bastiaan.marietje.data.model;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

@AutoValue
public abstract class Empty {

    public static Empty create() {
        return new AutoValue_Empty();
    }

    public static TypeAdapter<Empty> typeAdapter(Gson gson) {
        return new AutoValue_Empty.GsonTypeAdapter(gson);
    }

}
