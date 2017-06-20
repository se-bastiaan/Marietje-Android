package eu.se_bastiaan.marietje.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import paperparcel.PaperParcel
import java.util.*

@PaperParcel
data class Song(
        @SerializedName("id") val objectId: Long,
        val duration: Long,
        val title: String,
        val artist: String,
        @SerializedName("uploader_name") val uploader: String?
) : Parcelable {
    companion object {
        @JvmField val CREATOR = PaperParcelSong.CREATOR
    }

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        PaperParcelSong.writeToParcel(this, dest, flags)
    }

    val durationStr: String
        get() {
            val minutes = Math.floor((duration / 60).toDouble()).toLong()
            val seconds = duration % 60
            return String.format(Locale.ENGLISH, "%d:%02d", minutes, seconds)
        }
}
