package com.example.domain.model

import com.example.domain.util.fakePhotoDtoList
import com.example.testfeature.util.fakeFirstPhotoIsFamily
import com.example.testfeature.util.fakeFirstPhotoIsFriend
import com.example.testfeature.util.fakeFirstPhotoIsPublic
import com.example.testfeature.util.fakeFirstPhotoTitle
import com.example.testfeature.util.fakeSecondPhotoIsFamily
import com.example.testfeature.util.fakeSecondPhotoIsFriend
import com.example.testfeature.util.fakeSecondPhotoIsPublic
import com.example.testfeature.util.fakeSecondPhotoTitle
import io.kotest.matchers.shouldBe
import org.junit.Test


class DomainMapperTest {

    @Test
    fun `toDomainModel should transform PhotoItemDto list to PhotoItem list with correct generated url`() {

        val photoItemList = fakePhotoDtoList.toDomainModel()
        photoItemList.size shouldBe fakePhotoDtoList.size

        // Verify the first item
        val firstItem = photoItemList.first()

        firstItem.title shouldBe fakeFirstPhotoTitle
        firstItem.url shouldBe "https://farm9999.staticflickr.com/fakeServer/123456789_1c27664791.jpg"
        firstItem.isPublic shouldBe fakeFirstPhotoIsPublic
        firstItem.isFriend shouldBe fakeFirstPhotoIsFriend
        firstItem.isFamily shouldBe fakeFirstPhotoIsFamily

        // Verify the second item
        val secondItem = photoItemList[1]
        secondItem.title shouldBe fakeSecondPhotoTitle
        secondItem.url shouldBe "https://farm8888.staticflickr.com/fakeServer2/987654321_2d38764802.jpg"
        secondItem.isPublic shouldBe fakeSecondPhotoIsPublic
        secondItem.isFriend shouldBe fakeSecondPhotoIsFriend
        secondItem.isFamily shouldBe fakeSecondPhotoIsFamily
    }
}
