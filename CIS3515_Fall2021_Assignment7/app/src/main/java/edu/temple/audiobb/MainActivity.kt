package edu.temple.audiobb

import android.app.SearchManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL


class MainActivity : AppCompatActivity(), BookListFragment.BookSelectedInterface {

    lateinit var requestQueue: RequestQueue

    private val isSingleContainer: Boolean by lazy {
        findViewById<View>(R.id.container2) == null
    }

    private val selectedBookViewModel: SelectedBookViewModel by lazy {
        ViewModelProvider(this).get(SelectedBookViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

         requestQueue = Volley.newRequestQueue(this)

        // Grab test data
        val bookList = getBookList()

        // If we're switching from one container to two containers
        // clear BookDetailsFragment from container1
        if (supportFragmentManager.findFragmentById(R.id.booklist_fragment) is BookDetailsFragment) {
            supportFragmentManager.popBackStack()
        }

        // If this is the first time the activity is loading, go ahead and add a BookListFragment
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.booklist_fragment, BookListFragment.newInstance(bookList))
                .commit()
        } else
        // If activity loaded previously, there's already a BookListFragment
        // If we have a single container and a selected book, place it on top
            if (isSingleContainer && selectedBookViewModel.getSelectedBook().value != null) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.booklist_fragment, BookDetailsFragment())
                    .setReorderingAllowed(true)
                    .addToBackStack(null)
                    .commit()
            }

        // If we have two containers but no BookDetailsFragment, add one to container2
        if (!isSingleContainer && supportFragmentManager.findFragmentById(R.id.container2) !is BookDetailsFragment)
            supportFragmentManager.beginTransaction()
                .add(R.id.container2, BookDetailsFragment())
                .commit()


        val searchButton: Button = findViewById(R.id.search_button)

        searchButton.setOnClickListener {
            onSearchRequested()
            lifecycleScope.launch(Dispatchers.Main) {
                if (Intent.ACTION_SEARCH == intent.action) {
                    intent.getStringExtra(SearchManager.QUERY)?.also { query ->
                        search(query)
                    }
                }
            }
        }
    }

    private fun search(query: String) {
        //val url = "https://kamorris.com/lab/cis3515/search.php?term=$query"

        requestQueue.add(
            JsonObjectRequest(Request.Method.GET,
                "https://kamorris.com/lab/cis3515/search.php?term=$query",
                null,
                {
                    jsonObject ->

                },
                {
                    Log.e("Response Error", it.toString())
                })
        )
    }

    private fun getBookList(): BookList {

        return BookList()
    }

    override fun onSearchRequested(): Boolean {
        startSearch(null, false, null, false)
        return super.onSearchRequested()
    }

    override fun onBackPressed() {
        // Backpress clears the selected book
        selectedBookViewModel.setSelectedBook(null)
        super.onBackPressed()
    }

    override fun bookSelected() {
        // Perform a fragment replacement if we only have a single container
        // when a book is selected

        if (isSingleContainer) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.booklist_fragment, BookDetailsFragment())
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .commit()
        }
    }
}