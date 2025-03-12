package com.example.domain.util

import com.example.data.dto.PhotoItemDto
import com.example.domain.model.Photo
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


const val fakeFirstPhotoIsPublicInt = 1
const val fakeFirstPhotoIsFriendInt = 0
const val fakeFirstPhotoIsFamilyInt = 1

const val fakeSecondPhotoIsPublicInt = 0
const val fakeSecondPhotoIsFriendInt = 1
const val fakeSecondPhotoIsFamilyInt = 0

const val fakeFirstPhotoIsPublic = true
const val fakeFirstPhotoIsFriend = false
const val fakeFirstPhotoIsFamily = true

const val fakeSecondPhotoIsPublic = false
const val fakeSecondPhotoIsFriend = true
const val fakeSecondPhotoIsFamily = false

val fakeFirstPhotoItemDto = PhotoItemDto(
    title = fakeFirstPhotoTitle,
    isPublic = fakeFirstPhotoIsPublicInt,
    isFriend = fakeFirstPhotoIsFriendInt,
    isFamily = fakeFirstPhotoIsFamilyInt,
    id = fakeFirstPhotoId,
    owner = fakeFirstPhotoOwner,
    secret = fakeFirstPhotoSecret,
    server = fakeFirstPhotoServer,
    farm = fakeFirstPhotoFarm
)

val fakeSecondPhotoItemDto = PhotoItemDto(
    title = fakeSecondPhotoTitle,
    isPublic = fakeSecondPhotoIsPublicInt,
    isFriend = fakeSecondPhotoIsFriendInt,
    isFamily = fakeSecondPhotoIsFamilyInt,
    id = fakeSecondPhotoId,
    owner = fakeSecondPhotoOwner,
    secret = fakeSecondPhotoSecret,
    server = fakeSecondPhotoServer,
    farm = fakeSecondPhotoFarm
)
val fakePhotoDtoList = listOf(fakeFirstPhotoItemDto, fakeSecondPhotoItemDto)

val fakePhotoList = fakePhotoDtoList.map { photoDto ->
    with(photoDto) {
        Photo(
            title = title,
            url = "https://farm$farm.staticflickr.com/$server/${id}_$secret.jpg",
            isPublic = isPublic == 1,
            isFriend = isFriend == 1,
            isFamily = isFamily == 1
        )
    }
}