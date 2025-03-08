package com.example.data.util

import com.example.data.dto.PhotoItemDto
import com.example.data.network.model.Photo
import com.example.data.network.model.PhotoSearchDetail
import com.example.data.network.model.PhotoSearchResponse
import com.example.testfeature.util.fakeFirstPhotoFarm
import com.example.testfeature.util.fakeFirstPhotoId
import com.example.testfeature.util.fakeFirstPhotoOwner
import com.example.testfeature.util.fakeFirstPhotoSecret
import com.example.testfeature.util.fakeFirstPhotoServer
import com.example.testfeature.util.fakeFirstPhotoTitle
import com.example.testfeature.util.fakeSecondPhotoFarm
import com.example.testfeature.util.fakeSecondPhotoId
import com.example.testfeature.util.fakeSecondPhotoOwner
import com.example.testfeature.util.fakeSecondPhotoSecret
import com.example.testfeature.util.fakeSecondPhotoServer
import com.example.testfeature.util.fakeSecondPhotoTitle


internal const val fakePage = 1
internal const val fakePages = 10
internal const val fakePerPage = 100
internal const val fakeTotal = 1000

internal const val fakeFirstPhotoIsPublic = 1
internal const val fakeFirstPhotoIsFriend = 0
internal const val fakeFirstPhotoIsFamily = 1

internal const val fakeSecondPhotoIsPublic = 0
internal const val fakeSecondPhotoIsFriend = 1
internal const val fakeSecondPhotoIsFamily = 0

internal const val fakeStatus = "ok"
internal const val fakeInvalidStatus = "fail"

internal val fakeFirstPhoto = Photo(
    id = fakeFirstPhotoId,
    owner = fakeFirstPhotoOwner,
    secret = fakeFirstPhotoSecret,
    server = fakeFirstPhotoServer,
    farm = fakeFirstPhotoFarm,
    title = fakeFirstPhotoTitle,
    isPublic = fakeFirstPhotoIsPublic,
    isFriend = fakeFirstPhotoIsFriend,
    isFamily = fakeFirstPhotoIsFamily
)

internal val fakeSecondPhoto = Photo(
    id = fakeSecondPhotoId,
    owner = fakeSecondPhotoOwner,
    secret = fakeSecondPhotoSecret,
    server = fakeSecondPhotoServer,
    farm = fakeSecondPhotoFarm,
    title = fakeSecondPhotoTitle,
    isPublic = fakeSecondPhotoIsPublic,
    isFriend = fakeSecondPhotoIsFriend,
    isFamily = fakeSecondPhotoIsFamily
)

internal val fakePhotoSearchDetail = PhotoSearchDetail(
    page = fakePage,
    pages = fakePages,
    perPage = fakePerPage,
    total = fakeTotal,
    photos = listOf(fakeFirstPhoto, fakeSecondPhoto)
)

internal val fakePhotoSearchResponse = PhotoSearchResponse(
    photoSearchDetail = fakePhotoSearchDetail,
    status = fakeStatus
)

val fakeFirstPhotoItemDto = PhotoItemDto(
    title = fakeFirstPhotoTitle,
    isPublic = fakeFirstPhotoIsPublic,
    isFriend = fakeFirstPhotoIsFriend,
    isFamily = fakeFirstPhotoIsFamily,
    farm = fakeFirstPhotoFarm,
    server = fakeFirstPhotoServer,
    id = fakeFirstPhotoId,
    secret = fakeFirstPhotoSecret,
    owner = fakeFirstPhotoOwner
)

val fakeSecondPhotoItemDto = PhotoItemDto(
    title = fakeSecondPhotoTitle,
    isPublic = fakeSecondPhotoIsPublic,
    isFriend = fakeSecondPhotoIsFriend,
    isFamily = fakeSecondPhotoIsFamily,
    farm = fakeSecondPhotoFarm,
    server = fakeSecondPhotoServer,
    id = fakeSecondPhotoId,
    secret = fakeSecondPhotoSecret,
    owner = fakeSecondPhotoOwner
)

val fakePhotoItemDtoList = listOf(fakeFirstPhotoItemDto, fakeSecondPhotoItemDto)

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