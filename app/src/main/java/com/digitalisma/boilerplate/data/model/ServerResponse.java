package com.digitalisma.boilerplate.data.model;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import java.util.List;

@AutoValue
public abstract class ServerResponse {

    public abstract List<Person> results();

    public static ServerResponse create(List<Person> persons) {
        return new AutoValue_ServerResponse(persons);
    }

    public static TypeAdapter<ServerResponse> typeAdapter(Gson gson) {
        return new AutoValue_ServerResponse.GsonTypeAdapter(gson);
    }

}
