package com.geo.galleryapp.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.geo.galleryapp.models.ImageData
import com.geo.galleryapp.repository.GalleryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class GalleryViewModel @Inject constructor(
    private val repository: GalleryRepository
): ViewModel() {

    private val _searchTextState = MutableLiveData("")
//    val searchTextState: StateFlow<String> = _searchTextState

    val images = _searchTextState.asFlow().flatMapLatest {
        repository.getImage(it)
    }
        .cachedIn(viewModelScope)

//    val hudaiImage = repository.getHudaiImage()

//    fun getImages(): Flow<PagingData<ImageData>> {
//        return repository.getImage("").cachedIn(viewModelScope)
//    }

    fun searchImages(text: String) {
        _searchTextState.value = text
    }

}