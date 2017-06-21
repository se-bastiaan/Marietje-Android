package eu.se_bastiaan.marietje.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import paperparcel.PaperParcel


@PaperParcel
data class Songs(
        @SerializedName("per_page") val pageSize: Long,
        val currentPage: Long,
        val lastPage: Long,
        val data: List<Song>,
        val total: Long
        ) : Parcelable {
    companion object {
        @JvmField val CREATOR = PaperParcelSongs.CREATOR
    }

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        PaperParcelSongs.writeToParcel(this, dest, flags)
    }
}