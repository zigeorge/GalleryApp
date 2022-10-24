package com.geo.galleryapp.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.geo.galleryapp.databinding.ActivityGalleryBinding
import com.geo.galleryapp.other.Constants.IMAGE_VIEW_TYPE
import com.geo.galleryapp.other.Constants.NETWORK_VIEW_TYPE
import com.geo.galleryapp.other.GlideApp
import com.geo.galleryapp.other.asMergedLoadStates
import com.geo.galleryapp.ui.adapters.ImageLoadStateAdapter
import com.geo.galleryapp.ui.adapters.ImagesAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter


@AndroidEntryPoint
class GalleryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGalleryBinding

    private lateinit var viewModel: GalleryViewModel

    private lateinit var imagesAdapter: ImagesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGalleryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[GalleryViewModel::class.java]
        initAdapter()
        initSearch()
        initSwipeToRefresh()
    }

    private fun initSearch() {
        binding.searchField.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO
                || actionId == EditorInfo.IME_ACTION_DONE
                || actionId == EditorInfo.IME_ACTION_SEARCH
                || actionId == EditorInfo.IME_ACTION_NEXT
            ) {
                updateImageListFromSearch()
                true
            } else {
                false
            }
        }
        binding.searchField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (binding.searchField.text.isNullOrEmpty()) {
                    binding.cancelIcon.visibility = GONE
                } else {
                    binding.cancelIcon.visibility = VISIBLE
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                updateImageListFromSearch()
            }
        })
        binding.cancelIcon.setOnClickListener {
            binding.searchField.setText("")
        }
    }

    private fun initSwipeToRefresh() {
        binding.swipeRefresh.setOnRefreshListener { imagesAdapter.refresh() }
    }

    private fun initAdapter() {
        imagesAdapter = ImagesAdapter(GlideApp.with(this))
        val loaderStateAdapter = ImageLoadStateAdapter(imagesAdapter)
        val gridLayoutManager = GridLayoutManager(this, 2)
//        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
//            override fun getSpanSize(position: Int): Int {
//                val viewType = imagesAdapter.getItemViewType(position)
//                Log.e("TYPE", "$viewType")
//                return if(viewType == NETWORK_VIEW_TYPE) 1
//                else 2
//            }
//        }
        binding.images.apply {
            this.layoutManager = gridLayoutManager
            this.setHasFixedSize(true)
            this.adapter = imagesAdapter.withLoadStateFooter(
                footer = loaderStateAdapter
            )
        }

        lifecycleScope.launchWhenCreated {
            viewModel.images.distinctUntilChanged().collectLatest {
                imagesAdapter.submitData(it)
            }
        }

        lifecycleScope.launchWhenCreated {
            imagesAdapter.loadStateFlow.collect { loadStates ->
                binding.swipeRefresh.isRefreshing =
                    loadStates.mediator?.refresh is LoadState.Loading
            }
        }

        lifecycleScope.launchWhenCreated {
            imagesAdapter.loadStateFlow
                // Use a state-machine to track LoadStates such that we only transition to
                // NotLoading from a RemoteMediator load if it was also presented to UI.
                .asMergedLoadStates()
                // Only emit when REFRESH changes, as we only want to react on loads replacing the
                // list.
                .distinctUntilChangedBy { it.refresh }
                // Only react to cases where REFRESH completes i.e., NotLoading.
                .filter { it.refresh is LoadState.NotLoading }
                // Scroll to top is synchronous with UI updates, even if remote load was triggered.
                .collect { binding.images.scrollToPosition(0) }
        }
    }

    private fun updateImageListFromSearch() {
        binding.searchField.text?.trim().toString().let {
            viewModel.searchImages(it)
        }
    }
}