package eu.se_bastiaan.marietje.ui.main.queue;

import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.se_bastiaan.marietje.R;
import eu.se_bastiaan.marietje.data.local.PreferencesHelper;
import eu.se_bastiaan.marietje.data.model.PlaylistSong;
import eu.se_bastiaan.marietje.data.model.Queue;
import eu.se_bastiaan.marietje.data.model.Song;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.SongViewHolder> {

    private Timer timer;
    private long currentServerTime = 0;
    private long currentStartedAt = 0;
    private long timeLeft = 0;
    private long timeOffset = 0;
    private long playNextAt;
    private boolean isModerator = false;

    private Handler mainThreadHandler;
    private List<PlaylistSong> playlistSongs;
    private PlaylistAdapter.Listener listener;

    @Inject
    public PlaylistAdapter(PreferencesHelper preferencesHelper) {
        mainThreadHandler = new Handler(Looper.getMainLooper());
        playlistSongs = new ArrayList<>();
        isModerator = preferencesHelper.canCancel() || preferencesHelper.canSkip() || preferencesHelper.canMove();
    }

    public PlaylistAdapter(Handler mainThreadHandler) {
        this.mainThreadHandler = mainThreadHandler;
        playlistSongs = new ArrayList<>();
    }

    public void setListener(PlaylistAdapter.Listener listener) {
        this.listener = listener;
    }

    public void setQueue(Queue queue) {
        this.playlistSongs.clear();
        this.playlistSongs.add(queue.currentSong());
        this.playlistSongs.addAll(queue.queuedSongs());
        this.currentServerTime = queue.currentTime();
        this.currentStartedAt = queue.startedAt();

        long now = System.currentTimeMillis() / 1000;
        timeOffset = now - currentServerTime;
        this.playNextAt = currentStartedAt + timeOffset + queue.currentSong().song().duration();

    }

    @Override
    public PlaylistAdapter.SongViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_song, parent, false);
        return new PlaylistAdapter.SongViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final PlaylistAdapter.SongViewHolder holder, int position) {
        holder.bind();
    }

    @Override
    public int getItemCount() {
        return playlistSongs.size();
    }

    public class SongViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.text_title)
        TextView titleTextView;
        @BindView(R.id.text_artist)
        TextView artistTextView;
        @BindView(R.id.text_duration)
        TextView durationTextView;
        @BindView(R.id.text_requester)
        TextView requesterTextView;

        SongViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(view -> {
                if (listener != null) {
                    listener.onSongClicked(getAdapterPosition(), playlistSongs.get(getAdapterPosition()));
                }
            });
        }

        void startTimer() {
            if (timer != null) {
                timer.cancel();
                timer = null;
            }

            timeOffset = (System.currentTimeMillis() / 1000) - currentServerTime;
            timeLeft = playlistSongs.get(0).song().duration() - (currentServerTime - currentStartedAt) + timeOffset;
            timer = new Timer();
            timer.scheduleAtFixedRate(new UpdateTimeLeftTask(), 0, TimeUnit.SECONDS.toMillis(1));
        }

        void bind() {
            if (playNextAt > (System.currentTimeMillis() / 1000)) {
                if (getAdapterPosition() > 0) {
                    durationTextView.setText(playsAt());
                } else {
                    durationTextView.setText(timeLeftStr());
                    startTimer();
                }
            } else {
                durationTextView.setText("");
            }

            PlaylistSong playlistSong = playlistSongs.get(getAdapterPosition());
            requesterTextView.setText(playlistSong.requester());
            requesterTextView.setVisibility(View.VISIBLE);

            Song song = playlistSong.song();
            titleTextView.setText(TextUtils.isEmpty(song.title()) ? itemView.getResources().getString(R.string.songs_title_unknown) : song.title());
            artistTextView.setText(TextUtils.isEmpty(song.artist()) ? itemView.getResources().getString(R.string.songs_artist_unknown) : song.artist());

            boolean enabled = playlistSong.canMoveDown() || isModerator;
            itemView.setEnabled(enabled);
        }

        class UpdateTimeLeftTask extends TimerTask {
            @Override
            public void run() {
                if (getAdapterPosition() > 0) {
                    timer.cancel();
                    timer = null;
                    return;
                }

                mainThreadHandler.post(() -> durationTextView.setText(timeLeftStr()));
                timeLeft -= 1;
                if (timeLeft <= 0) {
                    timeLeft = 0;
                }
            }
        }

        private String timeLeftStr() {
            long minutes = (long) Math.floor(timeLeft / 60);
            long seconds = timeLeft % 60;
            return String.format(Locale.ENGLISH, "%d:%02d", minutes, seconds);
        }

        private String playsAt() {
            long startsAt = playNextAt;
            for (int i = 1; i < getAdapterPosition(); i++) {
                long duration = playlistSongs.get(i).song().duration();
                startsAt += duration;
            }

            Date date = new Date(startsAt * 1000);
            DateFormat formatter = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);

            return formatter.format(date);
        }
    }

    public interface Listener {
        void onSongClicked(int position, PlaylistSong playlistSong);
    }

}
