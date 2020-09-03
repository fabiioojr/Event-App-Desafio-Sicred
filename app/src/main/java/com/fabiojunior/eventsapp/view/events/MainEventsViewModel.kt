package com.fabiojunior.eventsapp.view.events

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fabiojunior.eventsapp.data.api.APIService
import com.fabiojunior.eventsapp.data.model.Coupon
import com.fabiojunior.eventsapp.data.model.Event
import com.fabiojunior.eventsapp.data.repository.DataRepository

class MainEventsViewModel() : ViewModel() {
    private val dataRepository: DataRepository = DataRepository(APIService.service())
    private val listCoupons: MutableList<Coupon> = arrayListOf()
    val eventsLiveData: MutableLiveData<List<Event>> = MutableLiveData()
    val couponsLiveData: MutableLiveData<List<Coupon>> = MutableLiveData()
    val onEventLoading = MutableLiveData<Boolean>()
    val onCouponLoading = MutableLiveData<Boolean>()
    val onEventError = MutableLiveData<Boolean>()
    val onCouponError = MutableLiveData<Boolean>()

    /**
     * Logic for get events
     */
    fun getEventsData() {
        onEventLoading.postValue(true)
        onCouponLoading.postValue(true)

        dataRepository.getEvents(object : DataRepository.OnEventData {
            override fun onSuccess(data: List<Event>) {
                eventsLiveData.value = data
                getCoupons()
            }

            override fun onFailure() {
                onEventError.postValue(true)
                onCouponError.postValue(true)
            }
        })

        onEventLoading.postValue(false)
    }

    /**
     * Logic for get coupon
     */
    private fun getCoupons() {
        listCoupons.clear()

        onCouponLoading.postValue(true)
        eventsLiveData.value?.let {
            for (item in it) {
                item.cupons.let {
                    for (itemCoupon in item.cupons) {
                        itemCoupon.image = item.image
                        listCoupons.add(itemCoupon)
                    }
                }
                couponsLiveData.value = listCoupons
                onCouponLoading.postValue(false)
            }

            onEventLoading.postValue(false)
            onCouponLoading.postValue(false)
        }
    }
}