package com.geo.galleryapp.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.geo.galleryapp.repository.GalleryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class GalleryViewModel @Inject constructor(
    private val repository: GalleryRepository
): ViewModel() {

    private val _searchTextState = MutableStateFlow("")

    val images = _searchTextState.flatMapLatest {
        repository.getImage(it)
    }
        .cachedIn(viewModelScope)

    fun searchImages(text: String) {
        _searchTextState.value = text
    }

}