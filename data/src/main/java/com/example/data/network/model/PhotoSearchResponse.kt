package com.example.data.network.model

import com.google.gson.annotations.SerializedName

internal data class PhotoSearchResponse(
    @SerializedName("photos") val photoSearchDetail: PhotoSearchDetail,
    @SerializedName("stat") val status: String,
    @SerializedName("code") val errorCode: Int?
)

internal data class PhotoSearchDetail(
    @SerializedName("page") val page: Int,
    @SerializedName("pages") val pages: Int,
    @SerializedName("perpage") val perPage: Int,
    @SerializedName("total") val total: Int,
    @SerializedName("photo") val photos: List<Photo>
)

internal data class Photo(
    @SerializedName("id") val id: String,
    @SerializedName("owner") val owner: String,
    @SerializedName("secret") val secret: String,
    @SerializedName("server") val server: String,
    @SerializedName("farm") val farm: Long,
    @SerializedName("title") val title: String,
    @SerializedName("ispublic") val isPublic: Int,
    @SerializedName("isfriend") val isFriend: Int,
    @SerializedName("isfamily") val isFamily: Int
)
