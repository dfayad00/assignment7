package edu.temple.audiobookplayer

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso
import edu.temple.audlibplayer.PlayerService

class BookDetailsFragment : Fragment() {

    private lateinit var titleTextView: TextView
    private lateinit var authorTextView: TextView
    private lateinit var coverImageView: ImageView
    private lateinit var playButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout = inflater.inflate(R.layout.fragment_book_details, container, false)

        titleTextView = layout.findViewById(R.id.titleTextView)
        authorTextView = layout.findViewById(R.id.authorTextView)
        coverImageView = layout.findViewById(R.id.bookCoverImageView)
        playButton = layout.findViewById(R.id.playButton)

        return layout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ViewModelProvider(requireActivity()).get(BooksViewModel::class.java)
            .getSelectedBook().observe(requireActivity()) {updateBook(it)}

        view.findViewById<Button>(R.id.playButton).setOnClickListener {
            val intent = Intent(activity, PlayerService::class.java)
            val connection = object: ServiceConnection {
                override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
                    //Toast.makeText(activity, "connected", Toast.LENGTH_SHORT).show()
                }

                override fun onServiceDisconnected(p0: ComponentName?) {
                    Toast.makeText(activity, "disconnected", Toast.LENGTH_SHORT).show()
                }
            }

            activity?.bindService(intent, connection, 0)
            activity?.startService(intent)
        }

    }

    private fun updateBook(book: Book?) {
        book?.run {
            titleTextView.text = title
            authorTextView.text = author
            Picasso.get().load(coverURL).into(coverImageView)
        }
    }
}