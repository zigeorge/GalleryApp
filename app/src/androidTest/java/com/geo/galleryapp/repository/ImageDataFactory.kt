package com.geo.galleryapp.repository

import com.geo.galleryapp.models.Assets
import com.geo.galleryapp.models.Contributor
import com.geo.galleryapp.models.ImageData
import com.geo.galleryapp.models.Thumb
import java.util.concurrent.atomic.AtomicInteger

class ImageDataFactory {

    private var defaultId = 1994710325
    private var defaultContributorId = 301519563

    private fun create(id: Int, contributorId: Int): ImageData {
        val imageData = ImageData(
            assets = Assets(
                huge_thumb = Thumb(
                    height = 300,
                    url = "https://image.shutterstock.com/display_pic_with_logo/301519563/1994710325/stock-photo-relaxed-curly-haired-young-woman-lies-on-stomach-at-beach-sunbathes-wears-sunglasses-bathingsuit-1994710325.jpg",
                    width = 450
                ),
                large_thumb = Thumb(
                    height = 100,
                    url = "https://thumb9.shutterstock.com/thumb_large/301519563/1994710325/stock-photo-relaxed-curly-haired-young-woman-lies-on-stomach-at-beach-sunbathes-wears-sunglasses-bathingsuit-1994710325.jpg",
                    width = 150
                ),
                small_thumb = Thumb(
                    height = 67,
                    width = 100,
                    url = "https://thumb9.shutterstock.com/thumb_small/301519563/1994710325/stock-photo-relaxed-curly-haired-young-woman-lies-on-stomach-at-beach-sunbathes-wears-sunglasses-bathingsuit-1994710325.jpg"
                ),
                preview = Thumb(
                    height = 300,
                    width = 450,
                    url = "https://image.shutterstock.com/display_pic_with_logo/301519563/1994710325/stock-photo-relaxed-curly-haired-young-woman-lies-on-stomach-at-beach-sunbathes-wears-sunglasses-bathingsuit-1994710325.jpg"
                ),
                preview_1000 = Thumb(
                    height = 667,
                    width = 1000,
                    url = "https://ak.picdn.net/shutterstock/photos/1994710325/watermark_1000/f07b3d4123b2c8a6a2944f25efb6bd2a/preview_1000-1994710325.jpg"
                ),
                preview_1500 = Thumb(
                    height = 1000,
                    width = 1500,
                    url = "https://image.shutterstock.com/z/stock-photo-relaxed-curly-haired-young-woman-lies-on-stomach-at-beach-sunbathes-wears-sunglasses-bathingsuit-1994710325.jpg"
                )
            ),
            aspect = 1.4993,
            id = "$id",
            contributor = Contributor(
                id = "$contributorId"
            ),
            description = "Relaxed curly haired young woman lies on stomach at beach sunbathes wears sunglasses bathingsuit holds passport travels during summer holidays enjoys seaside resort has vacation at paradise.",
            image_type = "photo",
            media_type = "image",
            has_model_release = true
        )
        imageData.indexInResponse = -1
        return imageData
    }

    fun fakeImages(total: Int = 100): List<ImageData> {
        val list = ArrayList<ImageData>()
        repeat(total) {
            list.add(create(defaultId++, defaultContributorId++))
        }
        return list
    }
}
