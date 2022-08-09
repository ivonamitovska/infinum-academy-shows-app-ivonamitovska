package com.infinum.shows_ivona_mitovska.database.converter

import androidx.room.TypeConverter
import com.infinum.shows_ivona_mitovska.model.User

class Converters {

    @TypeConverter
    fun stringToUser(value: String?): User? {
        val data = value?.split(" ") ?: return null
        return User(data[0], data[1], data[2])
    }

    @TypeConverter
    fun userToString(user: User?): String {
        return user.toString()
    }
}