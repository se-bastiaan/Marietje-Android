package eu.se_bastiaan.marietje.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import paperparcel.PaperParcel


@PaperParcel
data class Queue(
        val startedAt: Long,
        val currentTime: Long,
        val currentSong: PlaylistSong,
        @SerializedName("queue") val queuedSongs: List<PlaylistSong>
) : Parcelable {
    companion object {
        @JvmField val CREATOR = PaperParcelQueue.CREATOR
    }

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        PaperParcelQueue.writeToParcel(this, dest, flags)
    }
}