package com.digitalisma.boilerplate.ui.main;

import com.digitalisma.boilerplate.data.model.Person;
import com.digitalisma.boilerplate.ui.base.MvpView;

import java.util.List;

public interface MainView extends MvpView {

    void showPersons(List<Person> persons);

    void showPersonsEmpty();

    void showError();

}
