package com.example.data.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(kotlinx.serialization.InternalSerializationApi::class)
@Serializable
internal data class PhotoSearchResponse(
    @SerialName("photos") val photoSearchDetail: PhotoSearchDetail,
    @SerialName("stat") val status: String,
    @SerialName("code") val errorCode: Int? = null
)

@OptIn(kotlinx.serialization.InternalSerializationApi::class)
@Serializable
internal data class PhotoSearchDetail(
    val page: Int,
    val pages: Int,
    @SerialName("perpage") val perPage: Int,
    val total: Int,
    @SerialName("photo") val photos: List<Photo>
)

@OptIn(kotlinx.serialization.InternalSerializationApi::class)
@Serializable
internal data class Photo(
    val id: String,
    val owner: String,
    val secret: String,
    val server: String,
    val farm: Long,
    val title: String,
    @SerialName("ispublic") val isPublic: Int,
    @SerialName("isfriend") val isFriend: Int,
    @SerialName("isfamily") val isFamily: Int
)
