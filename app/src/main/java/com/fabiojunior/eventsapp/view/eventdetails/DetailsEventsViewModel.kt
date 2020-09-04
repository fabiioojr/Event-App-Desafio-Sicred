package com.fabiojunior.eventsapp.view.eventdetails

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fabiojunior.eventsapp.data.model.CheckIn
import com.fabiojunior.eventsapp.data.repository.DataRepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailsEventsViewModel(private val dataRepository: DataRepositoryInterface) : ViewModel() {

    val onResultCheckin = MutableLiveData<Boolean>()

    /**
     * Function for checkin
     */
    fun checkIn(request: CheckIn) {
        GlobalScope.launch {
            withContext(Dispatchers.Main) {
                onResultCheckin.postValue(dataRepository.checkIn(request))
            }
        }
    }

}