package com.dicoding.picodiploma.submission3.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "favorite")
data class Favorite(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,

    @ColumnInfo(name = "username")
    var username: String,

    @ColumnInfo(name = "avatar")
    var avatar: String,

    @ColumnInfo(name = "company")
    var company: String,

    @ColumnInfo(name = "location")
    var location: String,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "repository")
    var repository: String,

    @ColumnInfo(name = "followers")
    var followers: String,

    @ColumnInfo(name = "following")
    var following: String,
): Serializable
