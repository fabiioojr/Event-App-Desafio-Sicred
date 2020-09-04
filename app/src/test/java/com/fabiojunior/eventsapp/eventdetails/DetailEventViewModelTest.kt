package com.fabiojunior.eventsapp.eventdetails

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.fabiojunior.eventsapp.data.model.CheckIn
import com.fabiojunior.eventsapp.data.repository.DataRepository
import com.fabiojunior.eventsapp.view.eventdetails.DetailsEventsViewModel
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class DetailEventViewModelTest {
    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private lateinit var repository: DataRepository
    private lateinit var detailsEventsViewModel: DetailsEventsViewModel
    private val testCheckInRequest = CheckIn(
        "1", "Fábio Júnior", "fabiojunior@gmail.com"
    )

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        Dispatchers.setMain(TestCoroutineDispatcher())
        repository = mock()
    }

    @Test
    fun `Upon successful check-in, livedata is notified`() {
        runBlocking {

            whenever(runBlocking { repository.checkIn(testCheckInRequest) }).thenReturn(true)
            detailsEventsViewModel = DetailsEventsViewModel(repository)

            val checkinResult = detailsEventsViewModel.onResultCheckin
            detailsEventsViewModel.checkIn(testCheckInRequest)

            val observer = mock() as Observer<Boolean?>
            checkinResult.observeForever(observer)

            Assert.assertEquals(checkinResult.value, true)
        }
    }

    @Test
    fun `When trying to checkin and fail, livedata is notified`() {
        runBlocking {

            whenever(runBlocking { repository.checkIn(testCheckInRequest) }).thenReturn(false)
            detailsEventsViewModel = DetailsEventsViewModel(repository)

            val checkinResult = detailsEventsViewModel.onResultCheckin
            detailsEventsViewModel.checkIn(testCheckInRequest)

            val observer = mock() as Observer<Boolean?>
            checkinResult.observeForever(observer)

            Assert.assertEquals(checkinResult.value, false)
        }
    }
}