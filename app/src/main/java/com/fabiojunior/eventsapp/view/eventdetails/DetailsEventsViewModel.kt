package com.fabiojunior.eventsapp.view.eventdetails

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fabiojunior.eventsapp.data.api.APIService
import com.fabiojunior.eventsapp.data.model.CheckIn
import com.fabiojunior.eventsapp.data.repository.DataRepository

class DetailsEventsViewModel() : ViewModel() {
    private val dataRepository: DataRepository = DataRepository(APIService.service())

    val onResultCheckin = MutableLiveData<Boolean>()

    /**
     * Function for checkin
     */
    fun checkIn(request: CheckIn) {
        dataRepository.checkIn(request, object : DataRepository.OnCheckin {
            override fun onSuccess() {
                onResultCheckin.postValue(true)
            }

            override fun onFailure(message: String) {
                onResultCheckin.postValue(false)
            }
        })
    }

}