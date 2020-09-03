package com.fabiojunior.eventsapp.data.repository

import com.fabiojunior.eventsapp.data.api.EventsServices
import com.fabiojunior.eventsapp.data.model.CheckIn
import com.fabiojunior.eventsapp.data.model.Event
import retrofit2.Call
import retrofit2.Response

class DataRepository(private val eventsServices: EventsServices) {

    /**
     * Call the event route by retrofit
     */
    fun getEvents(onEventData: OnEventData) {
        eventsServices.getEvents().enqueue(object : retrofit2.Callback<List<Event>> {
            override fun onResponse(call: Call<List<Event>>, response: Response<List<Event>>) {
                onEventData.onSuccess((response.body() as List<Event>))
            }

            override fun onFailure(call: Call<List<Event>>, t: Throwable) {
                onEventData.onFailure()
            }
        })
    }

    /**
     * Call the check in route by retrofit
     */
    fun checkIn(checkIn: CheckIn, onCheckin: OnCheckin) {
        eventsServices.checkIn(checkIn).enqueue(object : retrofit2.Callback<CheckIn> {
            override fun onResponse(call: Call<CheckIn>, response: Response<CheckIn>) {
                if (response.isSuccessful) onCheckin.onSuccess()
                else onCheckin.onFailure(response.message())
            }

            override fun onFailure(call: Call<CheckIn>, t: Throwable) {
                t.message?.let { onCheckin.onFailure(it) }
            }
        })
    }

    /**
     * Event callback
     */
    interface OnEventData {
        fun onSuccess(data: List<Event>)
        fun onFailure()
    }

    /**
     * Event check in
     */
    interface OnCheckin {
        fun onSuccess()
        fun onFailure(message: String)
    }
}