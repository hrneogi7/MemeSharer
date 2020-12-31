package com.example.memesharer

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class MainActivity : AppCompatActivity() {
    var currenturl:String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadmeme()
    }

    private fun loadmeme()
    {
        // Instantiate the RequestQueue.
        val progressbar=findViewById<ProgressBar>(R.id.progressBar)
        val nextButton=findViewById<Button>(R.id.nextButton)
        val shareButton=findViewById<Button>(R.id.shareButton)
        shareButton.isEnabled = false
        nextButton.isEnabled=false
        progressbar.visibility=View.VISIBLE

        val url = "https://meme-api.herokuapp.com/gimme"

        // Request a string response from the provided URL.
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url,null,
            Response.Listener { response ->
                currenturl=response.getString("url")
                val image=findViewById<ImageView>(R.id.memeImageView)
                Glide.with(this).load(currenturl).listener(object:RequestListener<Drawable>{
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                        progressbar.visibility=View.GONE
                        return false
                    }

                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {

                        progressbar.visibility=View.GONE
                        nextButton.isEnabled = true
                        shareButton.isEnabled=true
                        return false
                    }
                }).into(image)

            },
            Response.ErrorListener {
                progressbar.visibility = View.GONE
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show()

            })

        // Add the request to the RequestQueue.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    fun nextmeme(view: View) {
        loadmeme()
    }
    fun sharememe(view: View) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type="text/plain"
        intent.putExtra(Intent.EXTRA_TEXT,"Hey checkout this dankmemes link:$currenturl")
        val chooser=Intent.createChooser(intent,"Share this meme via ......")
        startActivity(chooser)
    }
}