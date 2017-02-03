package com.digitalisma.boilerplate.data.remote;

import com.digitalisma.boilerplate.data.model.ServerResponse;

import retrofit2.http.GET;
import rx.Observable;

public interface PersonsService {

    @GET("?inc=name,email,picture&results=10")
    Observable<ServerResponse> getPersons();

}
