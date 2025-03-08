package com.example.domain.util

import com.example.data.dto.PhotoItemDto
import com.example.domain.model.PhotoItem
import com.example.testfeature.util.fakeFirstPhotoFarm
import com.example.testfeature.util.fakeFirstPhotoId
import com.example.testfeature.util.fakeFirstPhotoIsFamily
import com.example.testfeature.util.fakeFirstPhotoIsFriend
import com.example.testfeature.util.fakeFirstPhotoIsPublic
import com.example.testfeature.util.fakeFirstPhotoOwner
import com.example.testfeature.util.fakeFirstPhotoSecret
import com.example.testfeature.util.fakeFirstPhotoServer
import com.example.testfeature.util.fakeFirstPhotoTitle
import com.example.testfeature.util.fakeSecondPhotoFarm
import com.example.testfeature.util.fakeSecondPhotoId
import com.example.testfeature.util.fakeSecondPhotoIsFamily
import com.example.testfeature.util.fakeSecondPhotoIsFriend
import com.example.testfeature.util.fakeSecondPhotoIsPublic
import com.example.testfeature.util.fakeSecondPhotoOwner
import com.example.testfeature.util.fakeSecondPhotoSecret
import com.example.testfeature.util.fakeSecondPhotoServer
import com.example.testfeature.util.fakeSecondPhotoTitle


val fakeFirstPhotoItemDto = PhotoItemDto(
    title = fakeFirstPhotoTitle,
    isPublic = fakeFirstPhotoIsPublic,
    isFriend = fakeFirstPhotoIsFriend,
    isFamily = fakeFirstPhotoIsFamily,
    id = fakeFirstPhotoId,
    owner = fakeFirstPhotoOwner,
    secret = fakeFirstPhotoSecret,
    server = fakeFirstPhotoServer,
    farm = fakeFirstPhotoFarm
)

val fakeSecondPhotoItemDto = PhotoItemDto(
    title = fakeSecondPhotoTitle,
    isPublic = fakeSecondPhotoIsPublic,
    isFriend = fakeSecondPhotoIsFriend,
    isFamily = fakeSecondPhotoIsFamily,
    id = fakeSecondPhotoId,
    owner = fakeSecondPhotoOwner,
    secret = fakeSecondPhotoSecret,
    server = fakeSecondPhotoServer,
    farm = fakeSecondPhotoFarm
)
val fakePhotoDtoList = listOf(fakeFirstPhotoItemDto, fakeSecondPhotoItemDto)

val fakePhotoList = fakePhotoDtoList.map { photoDto ->
    with(photoDto) {
        PhotoItem(
            title = title,
            url = "https://farm$farm.staticflickr.com/$server/${id}_$secret.jpg",
            isPublic = isPublic,
            isFriend = isFriend,
            isFamily = isFamily
        )
    }
}