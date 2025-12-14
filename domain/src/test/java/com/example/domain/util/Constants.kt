package com.example.domain.util

import com.example.data.repository.model.Photo
import com.example.data.repository.model.PhotoVisibility
import com.example.domain.model.toDomainModel
import com.example.testfeature.util.fakeFirstPhotoOwner
import com.example.testfeature.util.fakeFirstPhotoTitle
import com.example.testfeature.util.fakeSecondPhotoOwner
import com.example.testfeature.util.fakeSecondPhotoTitle


val fakeFirstPhoto = Photo(
    title = fakeFirstPhotoTitle,
    photoVisibility = PhotoVisibility.PUBLIC,
    owner = fakeFirstPhotoOwner,
    url = "https://farm9999.staticflickr.com/fakeServer/123456789_1c27664791.jpg",
)

val fakeSecondPhoto = Photo(
    title = fakeSecondPhotoTitle,
    photoVisibility = PhotoVisibility.FRIEND,
    url = "https://farm8888.staticflickr.com/fakeServer2/987654321_2d38764802.jpg",
    owner = fakeSecondPhotoOwner,
)
val fakePhotoDtoList = listOf(fakeFirstPhoto, fakeSecondPhoto)
val fakeSearchedPhotos = fakePhotoDtoList.toDomainModel()
