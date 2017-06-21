package eu.se_bastiaan.marietje.ui.main.request;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.se_bastiaan.marietje.R;
import eu.se_bastiaan.marietje.data.model.Song;

public class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.SongViewHolder> {

    private List<Song> songs;
    private Listener listener;

    @Inject
    public SongsAdapter() {
        songs = new ArrayList<>();
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void setSongs(List<Song> songs) {
        this.songs.clear();
        this.songs.addAll(songs);
    }

    public void addSongs(List<Song> songs) {
        this.songs.addAll(songs);
    }

    @Override
    public SongViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_song, parent, false);
        return new SongViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final SongViewHolder holder, int position) {
        Song song = songs.get(position);
        holder.titleTextView.setText(TextUtils.isEmpty(song.getTitle()) ? holder.itemView.getResources().getString(R.string.songs_title_unknown) : song.getTitle());
        holder.artistTextView.setText(TextUtils.isEmpty(song.getArtist()) ? holder.itemView.getResources().getString(R.string.songs_artist_unknown) : song.getArtist());
        holder.durationTextView.setText(song.getDurationStr());
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    class SongViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.text_title)
        TextView titleTextView;
        @BindView(R.id.text_artist)
        TextView artistTextView;
        @BindView(R.id.text_duration)
        TextView durationTextView;

        public SongViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(view -> {
                if (listener != null) {
                    listener.onSongClicked(getAdapterPosition(), songs.get(getAdapterPosition()));
                }
            });
        }
    }

    public interface Listener {
        void onSongClicked(int position, Song song);
    }

}
