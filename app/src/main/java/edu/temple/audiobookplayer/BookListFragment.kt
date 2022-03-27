package edu.temple.audiobookplayer

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

private const val BOOK_LIST = "book_list"

class BookListFragment : Fragment() {
    private var bookList: BookList? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            bookList = it.getParcelable(BOOK_LIST)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_book_list, container, false)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val booksViewModel = ViewModelProvider(requireActivity()).get(BooksViewModel::class.java)

        // Using those sweet, sweet lambdas - but an onClickListener will do the job too
        val onClick : (Book) -> Unit = {
                // Update the ViewModel
                book: Book -> booksViewModel.setSelectedBook(book)
                // Inform the activity of the selection so as to not have the event replayed
                // when the activity is restarted
                (activity as BookSelectedInterface).bookSelected()
        }
        with (view as RecyclerView) {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = BookListAdapter (bookList!!, onClick)
        }

        // Observe updatedBooks flag and update RecyclerView adapter when changed
        booksViewModel.getUpdatedBookList().observe(requireActivity()){
            (view as RecyclerView).adapter?.notifyDataSetChanged()
        }

    }

    companion object {

        @JvmStatic
        fun newInstance(bookList: BookList) =
            BookListFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(BOOK_LIST, bookList)
                }
            }
    }

    class BookListAdapter (_bookList: BookList, _onClick: (Book) -> Unit) : RecyclerView.Adapter<BookListAdapter.BookViewHolder>() {
        private val bookList = _bookList
        private val onClick = _onClick

        class BookViewHolder (layout : View): RecyclerView.ViewHolder (layout) {
            val titleTextView : TextView = layout.findViewById(R.id.titleTextView)
            val authorTextView: TextView = layout.findViewById(R.id.authorTextView)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
            return BookViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.booklist_items_layout, parent, false))
        }

        // Bind the book to the holder along with the values for the views
        override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
            holder.titleTextView.text = bookList[position].title
            holder.authorTextView.text = bookList[position].author
            holder.titleTextView.setOnClickListener {
                onClick(bookList[position])
            }
        }

        override fun getItemCount(): Int {
            return bookList.size()
        }

    }

    interface BookSelectedInterface {
        fun bookSelected()
    }
}