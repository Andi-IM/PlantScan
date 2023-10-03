package com.github.andiim.plantscan.app.core.data.mediator

import androidx.paging.testing.asPagingSourceFactory
import com.github.andiim.plantscan.app.ui.utils.DataDummy

class FakePlantPagingSource {
    private val items = DataDummy.PLANTS
    private val pagingSourceFactory = items.asPagingSourceFactory()
    val pagingSource = pagingSourceFactory()
}
