package edu.temple.audiobookplayer

import android.os.Parcel
import android.os.Parcelable
import org.json.JSONArray

const val BOOKLIST_KEY = "booklist_key"

class BookList() : Parcelable {

    private val bookList : MutableList<Book> by lazy {
        ArrayList()
    }

    constructor(parcel: Parcel) : this() {
        parcel.readParcelableList(bookList, null)
    }

    fun add(book: Book) {
        bookList.add(book)
    }

    operator fun get(index: Int) = bookList[index]

    fun size() = bookList.size

    fun populateBooks (books: JSONArray) {
        bookList.clear()
        for (i in 0 until books.length()) {
            bookList.add(Book(books.getJSONObject(i)))
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelableList(bookList, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BookList> {
        override fun createFromParcel(parcel: Parcel): BookList {
            return BookList(parcel)
        }

        override fun newArray(size: Int): Array<BookList?> {
            return arrayOfNulls(size)
        }
    }

}