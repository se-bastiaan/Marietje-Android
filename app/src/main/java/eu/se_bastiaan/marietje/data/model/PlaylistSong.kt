package eu.se_bastiaan.marietje.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import paperparcel.PaperParcel

@PaperParcel
data class PlaylistSong(
        @SerializedName("id") val objectId: Long,
        val song: Song,
        val canMoveDown: Boolean,
        @SerializedName("requested_by") val requester: String
) : Parcelable {
    companion object {
        @JvmField val CREATOR = PaperParcelPlaylistSong.CREATOR
    }

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        PaperParcelPlaylistSong.writeToParcel(this, dest, flags)
    }
}