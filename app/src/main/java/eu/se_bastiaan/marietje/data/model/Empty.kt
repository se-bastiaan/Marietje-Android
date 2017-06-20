package eu.se_bastiaan.marietje.data.model

import android.os.Parcel
import android.os.Parcelable
import paperparcel.PaperParcel

@PaperParcel
class Empty : Parcelable {
    companion object {
        @JvmField val CREATOR = PaperParcelEmpty.CREATOR
    }

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        PaperParcelEmpty.writeToParcel(this, dest, flags)
    }
}