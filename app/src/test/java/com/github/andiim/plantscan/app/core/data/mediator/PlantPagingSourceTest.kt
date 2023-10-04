package com.github.andiim.plantscan.app.core.data.mediator

import androidx.paging.PagingSource
import androidx.paging.testing.TestPager
import com.github.andiim.plantscan.app.core.data.utils.PlantFactory
import com.github.andiim.plantscan.app.core.domain.usecase.PlantUseCase
import com.github.andiim.plantscan.app.core.firestore.FakeFirestoreSource
import com.github.andiim.plantscan.app.core.firestore.model.toDocument
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Test

class PlantPagingSourceTest {
    private val plantFactory = PlantFactory()
    private val mockPlants = listOf(
        plantFactory.createPlantItem("Test"),
        plantFactory.createPlantItem("Test"),
        plantFactory.createPlantItem("Test")
    )
    private val fakeSource = FakeFirestoreSource().apply {
        mockPlants
            .map { it.toDocument() }
            .forEach { plant -> addPlant(plant) }
    }

    @Test
    fun loadReturnsPageWhenSuccessFulLoadOfItemKeyedData() = runTest {
        val pagingSource = PlantPagingSource(fakeSource)
        val pager = TestPager(PlantUseCase.getDefaultPageConfig(), pagingSource)
        val result = pager.refresh() as PagingSource.LoadResult.Page

        assertThat(result.data)
            .containsExactlyElementsIn(mockPlants)
            .inOrder()

    }
}
