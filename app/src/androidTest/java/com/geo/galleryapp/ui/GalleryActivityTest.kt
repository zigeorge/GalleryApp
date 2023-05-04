package com.geo.galleryapp.ui

import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatEditText
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.MediumTest
import com.geo.galleryapp.R
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@MediumTest
@HiltAndroidTest
@OptIn(ExperimentalCoroutinesApi::class)
class GalleryActivityTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

//    @get:Rule
//    var coroutineTestRule = CoroutineTestRule()

//    @get:Rule
//    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var activityScenarioRule = ActivityScenarioRule(GalleryActivity::class.java)

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        hiltRule.inject()
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cancel()
    }

    @Test
    fun loadsTheDefaultResults() = runTest {
        assertImagesList()
        assertImagesListContainsEmptyListAtStart()
    }

    @Test
    fun searchTextChangeRefreshList() = runTest {
        assertImagesList()

        assertSearchText()

        assertClearButton()

        assertSwipeRefreshLayout()

        performSearchTextChange()
    }

    private fun assertImagesList() {
        onView(withId(R.id.images)).check { view: View, noViewFoundException: NoMatchingViewException? ->
            if (noViewFoundException != null) {
                throw noViewFoundException
            }
            val recyclerView = view as RecyclerView
            assertThat(recyclerView.adapter).isNotNull()
        }
    }

    private fun assertImagesListContainsEmptyListAtStart() {
        onView(withId(R.id.images)).check { view, noViewFoundException ->
            if (noViewFoundException != null) {
                throw noViewFoundException
            }
            val recyclerView = view as RecyclerView
            assertThat(recyclerView.adapter!!.itemCount).isEqualTo(0)
        }
    }

    private fun assertSearchText() {
        onView(withId(R.id.search_field)).check { view, noViewFoundException ->
            if (noViewFoundException != null) {
                throw noViewFoundException
            }
            val searchField = view as AppCompatEditText
            assertThat(searchField.text.toString()).isEqualTo("")
        }
    }

    private fun assertClearButton() {
        onView(withId(R.id.clear_icon)).check { view, noViewFoundException ->
            if (noViewFoundException != null) {
                throw noViewFoundException
            }
            val clearIcon = view as ImageView
            assertThat(clearIcon.visibility).isEqualTo(View.GONE)
        }
    }

    private fun assertSwipeRefreshLayout() {
        onView(withId(R.id.swipe_refresh)).check { view, noViewFoundException ->
            if (noViewFoundException != null) {
                throw noViewFoundException
            }
            val swipeRefreshLayout = view as SwipeRefreshLayout
            assertThat(swipeRefreshLayout.isRefreshing).isEqualTo(false)
        }
    }

    private fun performSearchTextChange() {
        onView(withId(R.id.search_field)).perform(typeText("nature"))
        onView(withId(R.id.clear_icon)).perform(click())
        onView(withId(R.id.search_field)).check { view, noViewFoundException ->
            if (noViewFoundException != null) {
                throw noViewFoundException
            }
            val searchView = view as AppCompatEditText
            assertThat(searchView.text.isNullOrEmpty()).isTrue()
        }
    }
}