package eu.se_bastiaan.marietje.data.model;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

@AutoValue
public abstract class EmptyResponse {

    public static EmptyResponse create() {
        return new AutoValue_EmptyResponse();
    }

    public static TypeAdapter<EmptyResponse> typeAdapter(Gson gson) {
        return new AutoValue_EmptyResponse.GsonTypeAdapter(gson);
    }

}
