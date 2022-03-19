package com.codepath.apps.restclienttemplate

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.CalendarContract
import android.text.Editable
import android.text.TextWatcher
import android.util.JsonReader
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.codepath.apps.restclienttemplate.models.Tweet
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers

class ComposeActivity : AppCompatActivity() {

    lateinit var etCompose: EditText
    lateinit var btnTweet: Button

    lateinit var client: TwitterClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compose)

        etCompose = findViewById(R.id.etTweetCompose)
        btnTweet = findViewById(R.id.btnTweet)

        client = TwitterApplication.getRestClient(this)

        // allow the user to see the character count as they compose a tweet
        var tvCounter = findViewById<TextView>(R.id.tvCounter)
        etCompose.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // Fires right as the text is being changed (even supplies the range of text)
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Fires right before text is changing
            }

            override fun afterTextChanged(s: Editable) {
                // Fires right after the text has changed
                val count = etCompose.text.toString()
                //tvCounter.setText(s.toString())

                //tvCounter.setText(s.length)

                if(count.length > 280) {
                    tvCounter.setTextColor(Color.RED)

                } else {
                    tvCounter.setTextColor(Color.BLACK)
                }
               tvCounter.text = s.length.toString()
            }
        })

        // handling user's click on the tweet button
        btnTweet.setOnClickListener {

            // grab content of edit text
            val tweetContent = etCompose.text.toString()

            // 1. make sure the tweet isn't empty
            if (tweetContent.isEmpty()) {
                Toast.makeText(this, "Empty tweets not allowed!", Toast.LENGTH_SHORT).show()
                // look into displaying snackbar message
            } else
            // 2. make sure the tweet doesn't exceed the character count
                if (tweetContent.length > 280) {
                    Toast.makeText(
                        this,
                        "Tweet is too long! Limit is 140 characters",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    //Toast.makeText(this, tweetContent, Toast.LENGTH_SHORT).show()

                    // make an api call to twitter to publish the tweet
                    client.publishTweet(tweetContent, object : JsonHttpResponseHandler() {

                        override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                            Log.e(TAG, "Successfully published tweet!")
                            // TODO send the tweet back to timeline activity

                            // get the tweet based on the response
                            val tweet = Tweet.fromJson(json.jsonObject)

                            // create an intent to pass back to timeline activity
                            val intent = Intent()
                            intent.putExtra("tweet", tweet)
                            setResult(RESULT_OK, intent)
                            // close out the compose activity
                            finish()
                        }

                        override fun onFailure(
                            statusCode: Int,
                            headers: Headers?,
                            response: String?,
                            throwable: Throwable?
                        ) {
                            Log.e(TAG, "Failed to publish tweet", throwable)
                        }

                    })
                }
        }
    }

    companion object {
        val TAG = "ComposeActivity"
    }
}