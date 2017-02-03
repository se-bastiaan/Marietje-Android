package com.digitalisma.boilerplate.data.model;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

@AutoValue
public abstract class Picture implements Parcelable {
    public abstract String thumbnail();

    public static Picture create(String thumbnail) {
        return new AutoValue_Picture(thumbnail);
    }

    public static TypeAdapter<Picture> typeAdapter(Gson gson) {
        return new AutoValue_Picture.GsonTypeAdapter(gson);
    }
}
