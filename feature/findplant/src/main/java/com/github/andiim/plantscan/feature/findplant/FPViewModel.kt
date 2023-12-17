package com.github.andiim.plantscan.feature.findplant

import androidx.lifecycle.ViewModel
import com.algolia.instantsearch.android.paging3.Paginator
import com.algolia.instantsearch.android.paging3.searchbox.connectPaginator
import com.algolia.instantsearch.compose.searchbox.SearchBoxState
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.searchbox.SearchBoxConnector
import com.algolia.instantsearch.searchbox.connectView
import com.algolia.instantsearch.searcher.hits.HitsSearcher
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.IndexName
import com.github.andiim.plantscan.feature.findplant.model.PlantResult
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FPViewModel @Inject constructor() : ViewModel() {
    val searcher = HitsSearcher(
        applicationID = ApplicationID("H23OVSFHI8"),
        apiKey = APIKey("6ad963b56b299f7bb47f25f3b1441d13"),
        indexName = IndexName("prod_plants"),
    )

    // Search Box
    val searchBoxState = SearchBoxState()
    val searchBoxConnector = SearchBoxConnector(searcher)

    // Hits
    val hitsPaginator = Paginator(searcher) { it.deserialize(PlantResult.serializer()) }
    val connections = ConnectionHandler(searchBoxConnector)

    init {
        connections += searchBoxConnector.connectView(searchBoxState)
        connections += searchBoxConnector.connectPaginator(hitsPaginator)
    }

    override fun onCleared() {
        super.onCleared()
        searcher.cancel()
    }
}
