package com.fabiojunior.eventsapp.view.events

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ethanhua.skeleton.RecyclerViewSkeletonScreen
import com.ethanhua.skeleton.Skeleton
import com.fabiojunior.eventsapp.R
import com.fabiojunior.eventsapp.data.model.Coupon
import com.fabiojunior.eventsapp.data.model.Event
import com.fabiojunior.eventsapp.data.model.Profile
import com.fabiojunior.eventsapp.view.adapters.CouponAdapter
import com.fabiojunior.eventsapp.view.adapters.DialogCoupon
import com.fabiojunior.eventsapp.view.adapters.EventsAdapter
import com.fabiojunior.eventsapp.view.base.BaseActivity
import com.fabiojunior.eventsapp.view.eventdetails.EventDetailActivity
import com.fabiojunior.eventsapp.view.register.RegisterActivity
import kotlinx.android.synthetic.main.activity_events.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainEventsActivity : BaseActivity(), EventsAdapter.EventClickListener,
    CouponAdapter.CouponClickListener, DialogCoupon.CallbackDialog {

    private val mainEventsVM by viewModel<MainEventsViewModel>()
    private lateinit var adapterEvents: EventsAdapter
    private lateinit var adapterCoupon: CouponAdapter
    private var profile: Profile? = null
    private var skeletonScreenCoupon: RecyclerViewSkeletonScreen.Builder? = null
    private var skeletonScreenEvent: RecyclerViewSkeletonScreen.Builder? = null

    override fun getLayout(): Int {
        return R.layout.activity_events
    }

    override fun init() {
        setupStatusBar()
        showError(false, "")

        if (intent.hasExtra(KEY_BUNDLE)) {
            val bundle = intent.getBundleExtra(KEY_BUNDLE)
            profile = bundle?.getParcelable(KEY_PROFILE)
            hello.text = getString(R.string.hello, profile?.name)
            email.text = profile?.email
        }

        prepareLists()
        setupListeners()
        getEventData()

        mainEventsVM
            .onEventError
            .observe(this, Observer {
                showError(
                    true,
                    getString(R.string.event_error)
                )
                refresh.isRefreshing = false
            })

        mainEventsVM
            .onEventLoading
            .observe(this, Observer {
                if (it) skeletonScreenEvent?.show();
            })

        mainEventsVM
            .onCouponLoading
            .observe(this, Observer {
                if (it) skeletonScreenCoupon?.show();
            })

        mainEventsVM
            .eventsLiveData
            .observe(this, Observer {
                it.let { eventsData ->
                    with(list_events) {
                        adapterEvents.updateList(eventsData as MutableList<Event>)
                        adapter = adapterEvents
                        refresh.isRefreshing = false
                    }
                }
            })

        mainEventsVM
            .couponsLiveData
            .observe(this, Observer {
                it.let { couponsData ->
                    with(list_coupons) {
                        adapterCoupon.updateList(couponsData as MutableList<Coupon>)
                        adapter = CouponAdapter(couponsData, this@MainEventsActivity)
                    }
                }
            })
    }

    /**
     * Get event data by model view
     */
    private fun getEventData() {
        GlobalScope.launch {
            mainEventsVM.getEventsData()
        }
    }

    /**
     * Setup listeners
     */
    private fun setupListeners() {
        logout.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        })

        refresh.setOnRefreshListener {
            GlobalScope.launch {
                withContext(Dispatchers.Main) {
                    mainEventsVM.getEventsData()
                }
            }
            showError(false, "")
        }
    }

    /**
     * Prepare lists
     */
    private fun prepareLists() {
        adapterEvents = EventsAdapter(mutableListOf(), this@MainEventsActivity)
        adapterCoupon = CouponAdapter(mutableListOf(), this@MainEventsActivity)

        skeletonScreenCoupon = Skeleton.bind(list_coupons)
            .adapter(adapterCoupon)
            .load(R.layout.item_skeleton_coupons)

        skeletonScreenEvent = Skeleton.bind(list_events)
            .adapter(adapterEvents)
            .load(R.layout.item_skeleton_events)

        with(list_events) {
            list_events.layoutManager =
                LinearLayoutManager(this@MainEventsActivity, RecyclerView.VERTICAL, false)
            list_events.adapter = adapter
            setHasFixedSize(true)
        }

        with(list_coupons) {
            list_coupons.layoutManager =
                LinearLayoutManager(this@MainEventsActivity, RecyclerView.HORIZONTAL, false)
            list_coupons.adapter = adapter
            setHasFixedSize(true)
        }
    }

    /**
     * Show error message
     */
    private fun showError(enabled: Boolean, message: String) {
        if (enabled) {
            error_message.visibility = View.VISIBLE
            error_text.text = message
        } else {
            error_message.visibility = View.GONE
        }
    }

    /**
     * Configure dialog coupon
     */
    private fun setupDialogCoupons(event: Event) {
        val dialogCoupon = DialogCoupon(event, this, this)
        dialogCoupon.show()
    }

    /**
     * Function for redirect to activity
     */
    private fun goToEventDetails(event: Event) {
        val bundle = Bundle().apply {
            putParcelable(KEY_EVENT, event)
            putParcelable(KEY_PROFILE, profile)
        }
        val intent = Intent(this, EventDetailActivity::class.java)
        intent.putExtra(KEY_BUNDLE, bundle)
        startActivity(intent)
    }

    override fun onEventClickListener(event: Event) {
        goToEventDetails(event)
    }

    override fun onCouponClickListener(coupon: Coupon) {
        coupon.eventId?.let {
            val event = adapterEvents.searchItem(it)
            event?.let { it1 -> setupDialogCoupons(it1) }
        }
    }

    override fun onClickDialogCoupon(event: Event) {
        goToEventDetails(event)
    }

    /**
     * Constants for intent
     */
    companion object {
        const val KEY_EVENT = "event"
        const val KEY_PROFILE = "profile"
        const val KEY_BUNDLE = "bundle"
    }
}