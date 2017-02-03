package com.digitalisma.boilerplate.util;

import com.google.gson.TypeAdapterFactory;

@com.ryanharter.auto.value.gson.GsonTypeAdapterFactory
public abstract class GsonTypeAdapterFactory implements TypeAdapterFactory {
    public static TypeAdapterFactory create() {
        return new AutoValueGson_GsonTypeAdapterFactory();
    }
}
