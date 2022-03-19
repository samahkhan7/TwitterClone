package com.codepath.apps.restclienttemplate.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import org.json.JSONArray
import org.json.JSONObject

@Parcelize
// annotate the class with parcelize, move variables, add parcelable

class Tweet(var body: String = "", var createdAt: String = "", var user: User? = null) :
    Parcelable {

    companion object {
        // pass in a json object and turn that into a tweet object we can use

        fun fromJson(jsonObject: JSONObject): Tweet {

            val tweet = Tweet()
            tweet.body = jsonObject.getString("text")
            tweet.createdAt = jsonObject.getString("created_at")
            tweet.user = User.fromJson(jsonObject.getJSONObject("user"))
            return tweet
        }

        // takes a whole json array and converts it into a list of tweets that is then returned
        fun fromJsonArray(jsonArray: JSONArray): List<Tweet> {
            val tweets = ArrayList<Tweet>()
            for (i in 0 until jsonArray.length()) {
                tweets.add(fromJson(jsonArray.getJSONObject(i)))
            }
            return tweets
        }
    }

        fun getFormattedTimestamp(): String? {
            return TimeFormatter.getTimeDifference(createdAt)
         }
}