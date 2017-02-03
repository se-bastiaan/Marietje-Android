package com.digitalisma.boilerplate.ui.main;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.digitalisma.boilerplate.R;
import com.digitalisma.boilerplate.data.model.Person;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PersonsAdapter extends RecyclerView.Adapter<PersonsAdapter.PersonViewHolder> {

    private List<Person> persons;

    @Inject
    public PersonsAdapter() {
        persons = new ArrayList<>();
    }

    public void setPersons(List<Person> persons) {
        this.persons = persons;
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_person, parent, false);
        return new PersonViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final PersonViewHolder holder, int position) {
        Person person = persons.get(position);
        Glide.with(holder.photoImageView.getContext()).load(person.picture().thumbnail()).into(holder.photoImageView);
        holder.nameTextView.setText(String.format("%s %s",
                person.name().first(), person.name().last()));
        holder.emailTextView.setText(person.email());
    }

    @Override
    public int getItemCount() {
        return persons.size();
    }

    class PersonViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image_photo)
        ImageView photoImageView;
        @BindView(R.id.text_name)
        TextView nameTextView;
        @BindView(R.id.text_email)
        TextView emailTextView;

        public PersonViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
