package com.example.data.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(kotlinx.serialization.InternalSerializationApi::class)
@Serializable
internal data class PhotoSearchResponseDto(
    @SerialName("photos") val photoSearchDetailDto: PhotoSearchDetailDto,
    @SerialName("stat") val status: String,
    @SerialName("code") val errorCode: Int? = null
)

@OptIn(kotlinx.serialization.InternalSerializationApi::class)
@Serializable
internal data class PhotoSearchDetailDto(
    val page: Int,
    val pages: Int,
    @SerialName("perpage") val perPage: Int,
    val total: Int,
    @SerialName("photo") val photosDto: List<PhotoDto>
)

@OptIn(kotlinx.serialization.InternalSerializationApi::class)
@Serializable
internal data class PhotoDto(
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
