package com.example.ui.util

import com.example.domain.model.SearchedPhoto
import com.example.testfeature.util.fakeFirstPhotoTitle

const val testQuery = "testQuery"
const val anotherTestQuery = "anotherTestQuery"

const val fakePhotoIsPublic = true
const val fakePhotoIsFriend = false
const val fakePhotoIsFamily = true

val fakeSearchedPhoto = SearchedPhoto(
    title = fakeFirstPhotoTitle,
    url = "https://farm9999.staticflickr.com/fakeServer/123456789_1c27664791.jpg",
    isPublic = fakePhotoIsPublic,
    isFriend = fakePhotoIsFriend,
    isFamily = fakePhotoIsFamily
)
val fakePhotoList = listOf(
    fakeSearchedPhoto
)
