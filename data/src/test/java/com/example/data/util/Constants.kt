package com.example.data.util

import com.example.data.repository.model.Photo
import com.example.data.network.dto.PhotoDto
import com.example.data.network.dto.PhotoSearchDetailDto
import com.example.data.network.dto.PhotoSearchResponseDto
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

internal val fakeFirstPhotoDto = PhotoDto(
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

internal val fakeSecondPhotoDto = PhotoDto(
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

internal val fakePhotoSearchDetailDto = PhotoSearchDetailDto(
    page = fakePage,
    pages = fakePages,
    perPage = fakePerPage,
    total = fakeTotal,
    photosDto = listOf(fakeFirstPhotoDto, fakeSecondPhotoDto)
)

internal val fakePhotoSearchResponseDto = PhotoSearchResponseDto(
    photoSearchDetailDto = fakePhotoSearchDetailDto,
    status = fakeStatus
)

val fakeFirstPhoto = Photo(
    title = fakeFirstPhotoTitle,
    isPublic = fakeFirstPhotoIsPublic  == 1,
    isFriend = fakeFirstPhotoIsFriend == 1,
    isFamily = fakeFirstPhotoIsFamily == 1,
    owner = fakeFirstPhotoOwner,
    url = "https://farm9999.staticflickr.com/fakeServer/123456789_1c27664791.jpg",
)

val fakeSecondPhoto = Photo(
    title = fakeSecondPhotoTitle,
    isPublic = fakeSecondPhotoIsPublic == 1,
    isFriend = fakeSecondPhotoIsFriend == 1,
    isFamily = fakeSecondPhotoIsFamily == 1,
    url = "https://farm8888.staticflickr.com/fakeServer2/987654321_2d38764802.jpg",
    owner = fakeSecondPhotoOwner,
)

val fakePhotoItemDtoList = listOf(fakeFirstPhoto, fakeSecondPhoto)

internal val fakePhotoSearchDetailDtoForInvalidStatus = PhotoSearchDetailDto(
    page = fakePage,
    pages = fakePages,
    perPage = fakePerPage,
    total = fakeTotal,
    photosDto = emptyList()
)


internal val fakePhotoSearchResponseDtoWithInvalidStatus = PhotoSearchResponseDto(
    photoSearchDetailDto = fakePhotoSearchDetailDtoForInvalidStatus,
    status = fakeInvalidStatus,
    errorCode = 116
)