package com.example.domain.util

import com.example.data.dto.PhotoItemDto
import com.example.domain.model.PhotoItem


const val fakeFirstTitle = "fakeTitle"
const val fakeFirstIsPublic = 1
const val fakeFirstIsFriend = 0
const val fakeFirstIsFamily = 1
const val fakeFirstId = "123456789"
const val fakeFirstOwner = "fakeOwner"
const val fakeFirstSecret = "1c27664791"
const val fakeFirstServer = "fakeServer"
const val fakeFirstFarm = 9999L

val fakeFirstPhotoItemDto = PhotoItemDto(
    title = fakeFirstTitle,
    isPublic = fakeFirstIsPublic,
    isFriend = fakeFirstIsFriend,
    isFamily = fakeFirstIsFamily,
    id = fakeFirstId,
    owner = fakeFirstOwner,
    secret = fakeFirstSecret,
    server = fakeFirstServer,
    farm = fakeFirstFarm
)


const val fakeSecondTitle = "fakeTitle2"
const val fakeSecondIsPublic = 0
const val fakeSecondIsFriend = 1
const val fakeSecondIsFamily = 0
const val fakeSecondId = "987654321"
const val fakeSecondOwner = "fakeOwner2"
const val fakeSecondSecret = "2d38764802"
const val fakeSecondServer = "fakeServer2"
const val fakeSecondFarm = 8888L


val fakeSecondPhotoItemDto = PhotoItemDto(
    title = fakeSecondTitle,
    isPublic = fakeSecondIsPublic,
    isFriend = fakeSecondIsFriend,
    isFamily = fakeSecondIsFamily,
    id = fakeSecondId,
    owner = fakeSecondOwner,
    secret = fakeSecondSecret,
    server = fakeSecondServer,
    farm = fakeSecondFarm
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