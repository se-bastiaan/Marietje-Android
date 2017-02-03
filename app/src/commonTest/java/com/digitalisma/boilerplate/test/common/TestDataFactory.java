package com.digitalisma.boilerplate.test.common;

import com.digitalisma.boilerplate.data.model.Name;
import com.digitalisma.boilerplate.data.model.Person;
import com.digitalisma.boilerplate.data.model.Picture;

import java.util.ArrayList;
import java.util.List;

/**
 * Factory class that makes instances of data models with random field values.
 * The aim of this class is to help setting up test fixtures.
 */
public class TestDataFactory {

    public static Person makePerson(String uniqueSuffix) {
        return Person.builder()
                .setName(makeName(uniqueSuffix))
                .setEmail("email" + uniqueSuffix + "@example.com")
                .setPicture(makePicture(uniqueSuffix))
                .build();
    }

    public static List<Person> makeListPersons(int number) {
        List<Person> persons = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            persons.add(makePerson(String.valueOf(i)));
        }
        return persons;
    }

    public static Name makeName(String uniqueSuffix) {
        return Name.create("Name-" + uniqueSuffix, "Surname-" + uniqueSuffix);
    }

    public static Picture makePicture(String uniqueSuffix) {
        return Picture.create("https://randomuser.me/api/portraits/thumb/men/" + uniqueSuffix + ".jpg");
    }

}