package com.example.networkapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import org.json.JSONObject

// TODO (1: Fix any bugs)
// TODO (2: Add function saveComic(...) to save comic info when downloaded
// TODO (3: Automatically load previously saved comic when app starts)

class MainActivity : AppCompatActivity() {

    private lateinit var requestQueue: RequestQueue
    lateinit var titleTextView: TextView
    lateinit var descriptionTextView: TextView
    lateinit var numberEditText: EditText
    lateinit var showButton: Button
    lateinit var comicImageView: ImageView
    private var searchHistory: MutableList<Int> = mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestQueue = Volley.newRequestQueue(this)

        titleTextView = findViewById<TextView>(R.id.comicTitleTextView)
        descriptionTextView = findViewById<TextView>(R.id.comicDescriptionTextView)
        numberEditText = findViewById<EditText>(R.id.comicNumberEditText)
        showButton = findViewById<Button>(R.id.showComicButton)
        comicImageView = findViewById<ImageView>(R.id.comicImageView)
loadSavedComic()

        showButton.setOnClickListener {
            downloadComic(numberEditText.text.toString())
        }

    }

    // Fetches comic from web as JSONObject
    private fun downloadComic (comicId: String) {
        val url = "https://xkcd.com/$comicId/info.0.json"
        requestQueue.add (
            JsonObjectRequest(url
                ,{image ->
                    showComic(image)
                    saveComic(image)}


                ,{}
            )
        )
    }

    // Display a comic for a given comic JSON object
    private fun showComic (comicObject: JSONObject) {
        titleTextView.text = comicObject.getString("title")
        descriptionTextView.text = comicObject.getString("alt")
        Picasso.get().load(comicObject.getString("img")).into(comicImageView)
    }

    // Implement this function
    private fun saveComic(comicObject: JSONObject) {
        val sharedPreferences = getSharedPreferences("comics", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("title", comicObject.getString("title"))
        editor.putString("description", comicObject.getString("alt"))
        editor.putString("imageUrl", comicObject.getString("img"))
        editor.apply()

    }

    fun loadSavedComic(){
        var sharedPreferences = getSharedPreferences("comics", MODE_PRIVATE)
        val title = sharedPreferences.getString("title", "")
        val description = sharedPreferences.getString("description", "")
        val imageUrl = sharedPreferences.getString("imageUrl", "")

        if (!title.isNullOrEmpty() && !description.isNullOrEmpty() && !imageUrl.isNullOrEmpty()){
            titleTextView.text = title
            descriptionTextView.text = description
            Picasso.get().load(imageUrl).into(comicImageView)
        } else{
            Toast.makeText(this,"No saved comic found", Toast.LENGTH_SHORT).show()
        }
    }


}