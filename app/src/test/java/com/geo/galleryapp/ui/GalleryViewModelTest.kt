package com.geo.galleryapp.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.*
import com.geo.galleryapp.api.FakeGalleryApiTest
import com.geo.galleryapp.repository.FakeGalleryDbRepositoryTest
import com.geo.galleryapp.repository.ImageDataFactoryTest
import com.geo.galleryapp.ui.adapters.ImagesAdapter
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GalleryViewModelTest {

    private val fakeGalleryApiTest = FakeGalleryApiTest()

//    @get:Rule
//    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: GalleryViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        fakeGalleryApiTest.addImageDataList(ImageDataFactoryTest().fakeImages())
        viewModel = GalleryViewModel(FakeGalleryDbRepositoryTest(fakeGalleryApiTest))
    }

    @After
    fun tearDownDispatcher() {
        Dispatchers.resetMain()
    }

    @Test
    fun `verify images receives imageData from repository as pagingData`() = runTest {
        val list = ImageDataFactoryTest().fakeImages(2)
        viewModel.images.collectLatest {
            val collectedData = it.collectData(testDispatcher)
            advanceUntilIdle()
            assertThat(collectedData).containsExactly(
                list[0],
                list[1]
            )
        }

//        val adapter = ImagesAdapter()

        // You need to launch here because submitData suspends forever while PagingData is alive
//        val job = launch {
//            viewModel.images.collectLatest {
//                adapter.submitData(it)
//            }
//        }

        // Do some stuff to trigger loads
//        advanceUntilIdle() // Let test dispatcher resolve everything

        // How to read from adapter state, there is also .peek() and .itemCount
//        assertThat(adapter.snapshot()).containsExactly(
//            list[0],
//            list[1]
//        )

        // We need to cancel the launched job as coroutines.test framework checks for leaky jobs
//        job.cancel()
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
suspend fun <T : Any> PagingData<T>.collectData(testDispatcher: TestDispatcher): List<T> {
    val dcb = object : DifferCallback {
        override fun onChanged(position: Int, count: Int) {}
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
    }
    val items = mutableListOf<T>()
    val dif = object : PagingDataDiffer<T>(dcb, testDispatcher) {

        override suspend fun presentNewList(
            previousList: NullPaddedList<T>,
            newList: NullPaddedList<T>,
            lastAccessedIndex: Int,
            onListPresentable: () -> Unit
        ): Int? {
            for (idx in 0 until newList.size)
                items.add(newList.getFromStorage(idx))
            onListPresentable()
            return null
        }
    }
    dif.collectFrom(this)
    return items
}