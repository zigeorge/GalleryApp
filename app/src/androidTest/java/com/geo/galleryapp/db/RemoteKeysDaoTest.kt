package com.geo.galleryapp.db

import androidx.test.filters.SmallTest
import com.geo.galleryapp.repository.ImageDataFactory
import com.geo.galleryapp.repository.RemoteKeysFactory
import com.google.common.truth.Truth
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

@ExperimentalCoroutinesApi
@SmallTest
@HiltAndroidTest
class RemoteKeysDaoTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    @Named("test_gallery_db")
    lateinit var database: GalleryDb
    private lateinit var dao: RemoteKeysDao

    private lateinit var remoteKeysFactory: RemoteKeysFactory


    @Before
    fun setUp() {
        hiltRule.inject()
        dao = database.remoteKeysDao()
        remoteKeysFactory = RemoteKeysFactory()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun verifyInsertAll() = runTest {
        val list = remoteKeysFactory.fakeKeys()
        dao.insertAll(list)

        val count = dao.count()
        assertThat(count).isEqualTo(list.size)
    }

    @Test
    fun verifyRemoteKeysByRepoId() = runTest {
        val list = remoteKeysFactory.fakeKeys()
        dao.insertAll(list)

        val remoteKeys = dao.remoteKeysByRepoId(list[0].repoId)
        assertThat(remoteKeys?.repoId).isEqualTo(list[0].repoId)
    }

    @Test
    fun verifyRemoteKeysByRepoIdReturnsNullWhenKeyDoesntExist() = runTest {
        val remoteKeys = dao.remoteKeysByRepoId("13215")
        assertThat(remoteKeys).isNull()
    }

    @Test
    fun deleteRemovesAllDataFromTable() = runTest {
        dao.insertAll(remoteKeysFactory.fakeKeys())

        dao.delete()

        assertThat(dao.count()).isEqualTo(0)
    }

}