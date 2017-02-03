package eu.se_bastiaan.marietje.util;

import com.google.gson.TypeAdapterFactory;

@com.ryanharter.auto.value.gson.GsonTypeAdapterFactory
public abstract class GsonTypeAdapterFactory implements TypeAdapterFactory {
    public static TypeAdapterFactory create() {
        return new AutoValueGson_GsonTypeAdapterFactory();
    }
}
