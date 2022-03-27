package edu.temple.audiobookplayer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.json.JSONArray

class BooksViewModel : ViewModel() {

    // Just a regular booklist
    // LiveData objects will not notify observer
    // if elements in a LiveData list change,
    // only if there is a change in the List object itself
    // So we have to find another way to facilitate the observer pattern
    // (see updatedBookList)
    val bookList: BookList by lazy {
        BookList()
    }


    // This item serves only as a notifier. We don't actually
    // care about the data it's storing. It's just a means to
    // have an observer be notified that something (new books have been added)
    // has happened
    private val updatedBookList : MutableLiveData<Int> by lazy {
        MutableLiveData()
    }

    private val book: MutableLiveData<Book> by lazy {
        MutableLiveData()
    }


    // The indirect observable for those that want to know when
    // the book list has changed
    fun getUpdatedBookList() : LiveData<out Any> {
        return updatedBookList
    }

    // When the booklist has changed, notify observers
    fun populateBooks(books: JSONArray) {
        bookList.populateBooks(books)
        notifyUpdatedBookList()
    }


    // A trivial update used to indirectly notify observers that the Booklist has changed
    fun notifyUpdatedBookList() {
        updatedBookList.value = updatedBookList.value?.plus(1)
    }

    fun getSelectedBook(): LiveData<Book> {
        return book
    }

    fun setSelectedBook(selectedBook: Book?) {
        this.book.value = selectedBook
    }
}