package com.example.data.util

import com.example.data.dto.PhotoItemDto
import com.example.data.network.model.Photo
import com.example.data.network.model.PhotoSearchDetail
import com.example.data.network.model.PhotoSearchResponse


internal const val fakePhotoId = "123456789"
internal const val fakePhotoOwner = "fakeOwner"
internal const val fakePhotoSecret = "1a2b3c4d5e"
internal const val fakePhotoServer = "fakeServer"
internal const val fakePhotoFarm = 9999L
internal const val fakePhotoTitle = "fakeTitle"
internal const val fakePhotoIsPublic = 1
internal const val fakePhotoIsFriend = 0
internal const val fakePhotoIsFamily = 1

internal const val fakePage = 1
internal const val fakePages = 10
internal const val fakePerPage = 100
internal const val fakeTotal = 1000

internal const val fakeStatus = "ok"
internal const val fakeInvalidStatus = "fail"

internal val fakePhoto = Photo(
    id = fakePhotoId,
    owner = fakePhotoOwner,
    secret = fakePhotoSecret,
    server = fakePhotoServer,
    farm = fakePhotoFarm,
    title = fakePhotoTitle,
    isPublic = fakePhotoIsPublic,
    isFriend = fakePhotoIsFriend,
    isFamily = fakePhotoIsFamily
)

internal val fakePhotoSearchDetail = PhotoSearchDetail(
    page = fakePage,
    pages = fakePages,
    perPage = fakePerPage,
    total = fakeTotal,
    photos = listOf(fakePhoto) // Include the fake Photo object
)

internal val fakePhotoSearchResponse = PhotoSearchResponse(
    photoSearchDetail = fakePhotoSearchDetail,
    status = fakeStatus
)

val fakePhotoItemDto =  PhotoItemDto(
    title = fakePhotoTitle,
    isPublic = fakePhotoIsPublic,
    isFriend = fakePhotoIsFriend,
    isFamily = fakePhotoIsFamily,
    farm = fakePhotoFarm,
    server = fakePhotoServer,
    id = fakePhotoId,
    secret = fakePhotoSecret,
    owner = fakePhotoOwner
)

val fakePhotoItemDtoList = listOf(fakePhotoItemDto)

internal val fakePhotoSearchDetailForInvalidStatus = PhotoSearchDetail(
    page = fakePage,
    pages = fakePages,
    perPage = fakePerPage,
    total = fakeTotal,
    photos = emptyList()
)

internal val fakePhotoSearchResponseWithInvalidStatus = PhotoSearchResponse(
    photoSearchDetail = fakePhotoSearchDetailForInvalidStatus,
    status = fakeInvalidStatus
)