package eu.se_bastiaan.marietje.data.model

import android.os.Parcel
import android.os.Parcelable
import paperparcel.PaperParcel

@PaperParcel
data class Permissions(
        val canCancel: Boolean = false,
        val canSkip: Boolean = false,
        val canMove: Boolean = false,
        val canControlVolume: Boolean = false
) : Parcelable {
    companion object {
        @JvmField val CREATOR = PaperParcelPermissions.CREATOR
    }

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        PaperParcelPermissions.writeToParcel(this, dest, flags)
    }
}