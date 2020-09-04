package com.fabiojunior.eventsapp

import com.fabiojunior.eventsapp.data.api.APIService
import com.fabiojunior.eventsapp.data.api.EventsServices
import com.fabiojunior.eventsapp.data.repository.DataRepository
import com.fabiojunior.eventsapp.data.repository.DataRepositoryInterface
import com.fabiojunior.eventsapp.data.repository.EventDataSourceInterface
import com.fabiojunior.eventsapp.data.repository.EventRemoteDataSource
import com.fabiojunior.eventsapp.view.eventdetails.DetailsEventsViewModel
import com.fabiojunior.eventsapp.view.events.MainEventsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Modules fot Koin
 */
private val apiServiceModule = module {
    single { APIService().eventApiService() }
}

private val repositoryModule = module {
    single<DataRepositoryInterface> { DataRepository(get()) }

}
private val dataSourceModule = module {
    single { EventRemoteDataSource(get()) }
}

val eventViewModelModule = module {
    viewModel {
        MainEventsViewModel(get())
    }
}

val detailsEventViewModelModule = module {
    viewModel {
        DetailsEventsViewModel(get())
    }
}

fun getEventModules() = listOf(
    eventViewModelModule,
    detailsEventViewModelModule,
    apiServiceModule,
    repositoryModule,
    dataSourceModule
)