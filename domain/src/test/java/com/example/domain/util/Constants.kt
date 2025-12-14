package com.example.domain.util

import com.example.data.repository.model.Photo
import com.example.domain.model.toDomainModel
import com.example.testfeature.util.fakeFirstPhotoOwner
import com.example.testfeature.util.fakeFirstPhotoTitle
import com.example.testfeature.util.fakeSecondPhotoOwner
import com.example.testfeature.util.fakeSecondPhotoTitle


const val fakeFirstPhotoIsPublicInt = true
const val fakeFirstPhotoIsFriendInt = false
const val fakeFirstPhotoIsFamilyInt = false

const val fakeSecondPhotoIsPublicInt = false
const val fakeSecondPhotoIsFriendInt = true
const val fakeSecondPhotoIsFamilyInt = false

const val fakeFirstPhotoIsPublic = true
const val fakeFirstPhotoIsFriend = false
const val fakeFirstPhotoIsFamily = false

const val fakeSecondPhotoIsPublic = false
const val fakeSecondPhotoIsFriend = true
const val fakeSecondPhotoIsFamily = false

val fakeFirstPhoto = Photo(
    title = fakeFirstPhotoTitle,
    isPublic = fakeFirstPhotoIsPublicInt,
    isFriend = fakeFirstPhotoIsFriendInt,
    isFamily = fakeFirstPhotoIsFamilyInt,
    owner = fakeFirstPhotoOwner,
    url = "https://farm9999.staticflickr.com/fakeServer/123456789_1c27664791.jpg",
)

val fakeSecondPhoto = Photo(
    title = fakeSecondPhotoTitle,
    isPublic = fakeSecondPhotoIsPublicInt,
    isFriend = fakeSecondPhotoIsFriendInt,
    isFamily = fakeSecondPhotoIsFamilyInt,
    url = "https://farm8888.staticflickr.com/fakeServer2/987654321_2d38764802.jpg",
    owner = fakeSecondPhotoOwner,
)
val fakePhotoDtoList = listOf(fakeFirstPhoto, fakeSecondPhoto)
val fakeSearchedPhotos = fakePhotoDtoList.toDomainModel()
