package com.infinum.shows_ivona_mitovska.persistence

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import com.google.gson.Gson
import com.infinum.shows_ivona_mitovska.model.Token
import com.infinum.shows_ivona_mitovska.utils.Constants.USER_TOKEN_KEY
import java.io.ByteArrayOutputStream

class ShowPreferences(context: Context) {
    private val prefs = context.getSharedPreferences("ShowPreferences", Context.MODE_PRIVATE)

    fun getBoolean(key: String): Boolean = prefs.getBoolean(key, false)

    fun saveBoolean(key: String, content: Boolean) = prefs.edit().putBoolean(key, content).apply()

    fun getString(key: String): String? = prefs.getString(key, null)

    fun saveString(key: String, content: String) = prefs.edit().putString(key, content).apply()

    fun removeString(key: String) = prefs.edit().remove(key).apply()

    fun saveToken(token: Token) {
        val json = Gson().toJson(token)
        prefs.edit().putString(USER_TOKEN_KEY, json).apply()
    }

    fun getToken(): Token? {
        val json = prefs.getString(USER_TOKEN_KEY, null)
        return if (json == null) null else Gson().fromJson(json, Token::class.java)
    }

    fun deleteToken(){
        prefs.edit().remove(USER_TOKEN_KEY).apply()
    }

    fun saveImageToPrefs(key: String, image: Bitmap) {
        val baos = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val encodedImage: String = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT)
        prefs.edit().putString(key, encodedImage).apply()
    }

    fun getImageFromPrefs(key: String): Bitmap? {
        val encodedImage = prefs.getString(key, null)
        if (encodedImage != null) {
            val b: ByteArray = Base64.decode(encodedImage, Base64.DEFAULT)
            return BitmapFactory.decodeByteArray(b, 0, b.size)
        }
        return null
    }
}