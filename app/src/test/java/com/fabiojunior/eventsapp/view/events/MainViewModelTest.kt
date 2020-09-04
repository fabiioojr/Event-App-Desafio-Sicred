package com.fabiojunior.eventsapp.view.events

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.fabiojunior.eventsapp.data.model.Coupon
import com.fabiojunior.eventsapp.data.model.Event
import com.fabiojunior.eventsapp.data.model.People
import com.fabiojunior.eventsapp.data.repository.DataRepository
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
class MainViewModelTest {
    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private lateinit var repository: DataRepository
    private lateinit var mainViewModel: MainEventsViewModel

    private val dataExpectedCoupons = listOf(
        Coupon("1", "1", 16, "https://teste.com"),
        Coupon("2", "2", 43, "https://teste2.com")
    )

    private val dataExpectedEvents = listOf(
        Event(
            "1",
            "Bazar de Artigos de Nerd",
            1570002524,
            "Temos de Yoda aos Power Rangers, não perca! :)",
            "https://teste.com",
            54.00,
            "-79.677",
            "-64.545", listOf(People("1", "1", "Fábio")),
            listOf(Coupon("1", "1", 16))
        ), Event(
            "2",
            "Workshop de Miojo Gourmet",
            137000416443,
            "Aprenda a fazer além de um miojo de 3 minutos...",
            "https://teste2.com",
            56.00,
            "-69.040",
            "-42,877", listOf(People("2", "2", "João")),
            listOf(Coupon("2", "2", 43))
        )
    )

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        Dispatchers.setMain(TestCoroutineDispatcher())
        repository = mock()
        whenever(runBlocking { repository.getEvents() }).thenReturn(dataExpectedEvents)
        mainViewModel = MainEventsViewModel(repository)
    }

    @Test
    fun `Upon receiving the data, the event list is set in the livedata`() {
        runBlocking {

            whenever(runBlocking { repository.getEvents() }).thenReturn(dataExpectedEvents)
            mainViewModel = MainEventsViewModel(repository)

            val events = mainViewModel.eventsLiveData
            mainViewModel.getEventsData()

            val observer = mock() as Observer<List<Event>>
            events.observeForever(observer)

            Assert.assertEquals(events.value, dataExpectedEvents)
        }
    }

    @Test
    fun `When receiving the Events, map the coupons`() {
        runBlocking {
            mainViewModel = MainEventsViewModel(repository)

            mainViewModel.getEventsData()

            val coupons = mainViewModel.couponsLiveData
            mainViewModel.getEventsData()

            val observer = mock() as Observer<List<Coupon>>
            coupons.observeForever(observer)
            Assert.assertEquals(coupons.value, dataExpectedCoupons)
        }
    }

    @Test
    fun `When receiving receive an error, it is passed in livedata`() {
        runBlocking {
            whenever(runBlocking { repository.getEvents() }).thenReturn(null)

            val error = mainViewModel.onEventError
            mainViewModel.getEventsData()

            Assert.assertTrue(error.value!!)
        }
    }
}