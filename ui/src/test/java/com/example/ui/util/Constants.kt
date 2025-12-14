package com.example.ui.util

import com.example.domain.model.SearchedPhoto
import com.example.domain.model.Visibility
import com.example.testfeature.util.fakeFirstPhotoTitle

const val testQuery = "testQuery"
const val anotherTestQuery = "anotherTestQuery"

val fakeSearchedPhoto = SearchedPhoto(
    title = fakeFirstPhotoTitle,
    url = "https://farm9999.staticflickr.com/fakeServer/123456789_1c27664791.jpg",
    visibility = Visibility.PUBLIC
)
val fakePhotoList = listOf(
    fakeSearchedPhoto
)
