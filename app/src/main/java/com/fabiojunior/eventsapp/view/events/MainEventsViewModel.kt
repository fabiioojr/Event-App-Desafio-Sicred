package com.fabiojunior.eventsapp.view.events

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fabiojunior.eventsapp.data.model.Coupon
import com.fabiojunior.eventsapp.data.model.Event
import com.fabiojunior.eventsapp.data.repository.DataRepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainEventsViewModel(private val dataRepository: DataRepositoryInterface) : ViewModel() {
    val listCoupons: MutableList<Coupon> = arrayListOf()
    val eventsLiveData: MutableLiveData<List<Event>> = MutableLiveData()
    val couponsLiveData: MutableLiveData<List<Coupon>> = MutableLiveData()
    val onEventLoading = MutableLiveData<Boolean>()
    val onCouponLoading = MutableLiveData<Boolean>()
    val onEventError = MutableLiveData<Boolean>()
    val onCouponError = MutableLiveData<Boolean>()

    /**
     * Logic for get events
     */
    suspend fun getEventsData() {
        onEventLoading.postValue(true)
        onCouponLoading.postValue(true)

        val response = dataRepository.getEvents()

        if (response != null) {
            GlobalScope.launch {
                withContext(Dispatchers.Main) {
                    eventsLiveData.value = response
                    getCoupons()
                }
            }
        } else {
            onEventError.postValue(true)
            onCouponError.postValue(true)
        }
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