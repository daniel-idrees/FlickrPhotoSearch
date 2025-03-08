package com.example.domain.model

import com.example.domain.util.fakeFirstIsFamily
import com.example.domain.util.fakeFirstIsFriend
import com.example.domain.util.fakeFirstIsPublic
import com.example.domain.util.fakeFirstTitle
import com.example.domain.util.fakePhotoDtoList
import com.example.domain.util.fakeSecondIsFamily
import com.example.domain.util.fakeSecondIsFriend
import com.example.domain.util.fakeSecondIsPublic
import com.example.domain.util.fakeSecondTitle
import io.kotest.matchers.shouldBe
import org.junit.Test


class DomainMapperTest {

    @Test
    fun `toDomainModel should transform PhotoItemDto list to PhotoItem list with correct generated url`() {

        val photoItemList = fakePhotoDtoList.toDomainModel()
        photoItemList.size shouldBe fakePhotoDtoList.size

        // Verify the first item
        val firstItem = photoItemList.first()

        firstItem.title shouldBe fakeFirstTitle
        firstItem.url shouldBe "https://farm9999.staticflickr.com/fakeServer/123456789_1c27664791.jpg"
        firstItem.isPublic shouldBe fakeFirstIsPublic
        firstItem.isFriend shouldBe fakeFirstIsFriend
        firstItem.isFamily shouldBe fakeFirstIsFamily

        // Verify the second item
        val secondItem = photoItemList[1]
        secondItem.title shouldBe fakeSecondTitle
        secondItem.url shouldBe "https://farm8888.staticflickr.com/fakeServer2/987654321_2d38764802.jpg"
        secondItem.isPublic shouldBe fakeSecondIsPublic
        secondItem.isFriend shouldBe fakeSecondIsFriend
        secondItem.isFamily shouldBe fakeSecondIsFamily
    }
}
