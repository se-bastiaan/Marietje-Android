package com.digitalisma.boilerplate.ui.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.digitalisma.boilerplate.R;
import com.digitalisma.boilerplate.data.model.Person;
import com.digitalisma.boilerplate.injection.component.ActivityComponent;
import com.digitalisma.boilerplate.ui.base.BaseActivity;
import com.digitalisma.boilerplate.ui.other.ViewModifier;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.digitalisma.boilerplate.injection.module.DeveloperSettingsModule.MAIN_ACTIVITY_VIEW_MODIFIER;

public class MainActivity extends BaseActivity implements MainView {

    @Inject
    MainPresenter presenter;
    @Inject
    PersonsAdapter personsAdapter;
    @Inject
    @Named(MAIN_ACTIVITY_VIEW_MODIFIER)
    ViewModifier viewModifier;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    /**
     * Return an Intent to start this Activity.
     */
    public static Intent getStartIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }

    @SuppressLint("InflateParams") // In this case it's ok
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(viewModifier.modify(getLayoutInflater().inflate(R.layout.activity_main, null)));
        ButterKnife.bind(this);

        recyclerView.setAdapter(personsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        presenter.attachView(this);
        presenter.loadPersons();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        presenter.detachView();
    }

    // MVP View methods implementation

    @Override
    public void showPersons(List<Person> persons) {
        personsAdapter.setPersons(persons);
        personsAdapter.notifyDataSetChanged();
    }

    @Override
    public void showError() {
        Snackbar.make(recyclerView, R.string.error_loading_persons, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showPersonsEmpty() {
        personsAdapter.setPersons(Collections.<Person>emptyList());
        personsAdapter.notifyDataSetChanged();
        Snackbar.make(recyclerView, R.string.empty_persons, Snackbar.LENGTH_LONG).show();
    }

    // End MVP View methods implementation

    @Override
    protected void injectActivity(ActivityComponent component) {
        component.inject(this);
    }

}
