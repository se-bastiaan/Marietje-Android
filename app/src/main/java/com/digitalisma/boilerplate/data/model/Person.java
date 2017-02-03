package com.digitalisma.boilerplate.data.model;

import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

@AutoValue
public abstract class Person implements Comparable<Person>, Parcelable {
    public abstract Name name();
    public abstract String email();
    public abstract Picture picture();

    public static Builder builder() {
        return new AutoValue_Person.Builder();
    }

    public static TypeAdapter<Person> typeAdapter(Gson gson) {
        return new AutoValue_Person.GsonTypeAdapter(gson);
    }

    @Override
    public int compareTo(@NonNull Person another) {
        return name().first().compareToIgnoreCase(name().first());
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder setName(Name name);
        public abstract Builder setEmail(String email);
        public abstract Builder setPicture(Picture picture);
        public abstract Person build();
    }
}

