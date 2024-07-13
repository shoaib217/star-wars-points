package com.example.starwarpoints.data


import com.google.gson.annotations.SerializedName

data class PlayersItem(
    @SerializedName("icon")
    val icon: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    var totalMatchPlayed: Int
)