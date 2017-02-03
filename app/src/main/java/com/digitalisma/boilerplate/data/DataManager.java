package com.digitalisma.boilerplate.data;

import com.digitalisma.boilerplate.data.local.PreferencesHelper;
import com.digitalisma.boilerplate.data.model.Person;
import com.digitalisma.boilerplate.data.model.ServerResponse;
import com.digitalisma.boilerplate.data.remote.PersonsService;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

@Singleton
public class DataManager {

    private final PersonsService personsService;
    private final PreferencesHelper preferencesHelper;

    @Inject
    public DataManager(PersonsService personsService, PreferencesHelper preferencesHelper) {
        this.personsService = personsService;
        this.preferencesHelper = preferencesHelper;
    }

    public PreferencesHelper getPreferencesHelper() {
        return preferencesHelper;
    }

    public Observable<List<Person>> getPersons() {
        return personsService.getPersons().map(ServerResponse::results).distinct();
    }

}
