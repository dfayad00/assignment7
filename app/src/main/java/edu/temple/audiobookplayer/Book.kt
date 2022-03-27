package edu.temple.audiobookplayer

import android.os.Parcel
import android.os.Parcelable
import org.json.JSONObject

// Simple data class
data class Book(val id: Int, val title: String, val author: String, val coverURL: String) : Parcelable {

    constructor(book: JSONObject) : this(book.getInt("id"), book.getString("title"), book.getString("author"), book.getString("cover_url"))

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(author)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Book> {
        override fun createFromParcel(parcel: Parcel): Book {
            return Book(parcel)
        }

        override fun newArray(size: Int): Array<Book?> {
            return arrayOfNulls(size)
        }
    }
}
