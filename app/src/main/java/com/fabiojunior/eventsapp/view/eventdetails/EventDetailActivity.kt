package com.fabiojunior.eventsapp.view.eventdetails

import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.fabiojunior.eventsapp.R
import com.fabiojunior.eventsapp.data.model.CheckIn
import com.fabiojunior.eventsapp.data.model.Event
import com.fabiojunior.eventsapp.data.model.Profile
import com.fabiojunior.eventsapp.view.adapters.DialogCheckin
import com.fabiojunior.eventsapp.view.base.BaseActivity
import com.fabiojunior.eventsapp.view.events.MainEventsActivity.Companion.KEY_BUNDLE
import com.fabiojunior.eventsapp.view.events.MainEventsActivity.Companion.KEY_EVENT
import com.fabiojunior.eventsapp.view.events.MainEventsActivity.Companion.KEY_PROFILE
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_event_detail.*
import kotlinx.android.synthetic.main.content_scrolling.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*


class EventDetailActivity : BaseActivity(), DialogCheckin.CallbackDialog, OnMapReadyCallback {
    private var profile: Profile? = null
    private var event: Event? = null
    private val detailEventsviewModel by viewModel<DetailsEventsViewModel>()
    private lateinit var dialogCheckin: DialogCheckin
    private lateinit var mapFragment: SupportMapFragment

    override fun getLayout(): Int {
        return R.layout.activity_event_detail
    }

    override fun init() {
        setSupportActionBar(toolbar)
        setupStatusBar()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        title = ""

        if (intent.hasExtra(KEY_BUNDLE)) {
            val bundle = intent.getBundleExtra(KEY_BUNDLE)
            event = bundle?.getParcelable(KEY_EVENT)
            profile = bundle?.getParcelable(KEY_PROFILE)
            event?.let { setupContent(it) }
        }

        mapFragment = (supportFragmentManager
            .findFragmentById(R.id.mapFragment) as SupportMapFragment?)!!
        mapFragment.getMapAsync(this)

        dialogCheckin = event?.let { it1 -> DialogCheckin(it1, this, this) }!!

        fab.setOnClickListener {
            GlobalScope.launch {
                withContext(Dispatchers.Main) {
                    dialogCheckin.show()
                }
            }
        }

        detailEventsviewModel
            .onResultCheckin
            .observe(this, Observer {
                if (it) {
                    dialogCheckin.setSuccess()
                } else {
                    dialogCheckin.setError()
                }
            })
    }

    /**
     * Setup event content
     */
    private fun setupContent(event: Event) {
        title_event.text = event.title
        id.text = "#${event.id}"
        price.text = "R$" + event.price
        date.text = SimpleDateFormat("dd/MM/yyy").format(event.date?.let { Date(event.date) })
        text_desc.text = event.description

        Glide.with(this)
            .load(event.image)
            .placeholder(R.drawable.ic_launcher_foreground)
            .error(R.drawable.ic_launcher_foreground)
            .into(image)
    }

    /**
     * Setup map with event location
     */
    override fun onMapReady(map: GoogleMap) {
        val latitude = event?.latitude?.toDoubleOrNull()
        val longitude = event?.longitude?.toDoubleOrNull()
        map.setMapStyle(
            MapStyleOptions.loadRawResourceStyle(
                this@EventDetailActivity, R.raw.style_map
            )
        )

        if (latitude != null && longitude != null) {
            map.addMarker(MarkerOptions().position(LatLng(latitude, longitude)).title(event?.title))
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(latitude, longitude), 18f))
        }
    }


    override fun onCheckinSuccess() {
        finish()
    }

    override fun onCheckinRequest() {
        GlobalScope.launch {
            withContext(Dispatchers.Main) {
                dialogCheckin.setLoading()
                val checkin = CheckIn(event?.id, profile?.name, profile?.email)
                detailEventsviewModel.checkIn(checkin)
            }
        }
    }

    override fun onCheckinCancel() {
    }

    override fun onCheckinError() {
    }

    /**
     * Share event in others app
     */
    private fun shareEvent() {
        event.let {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(
                Intent.EXTRA_TEXT,
                "Venha conferir esse incrível evento: " + event?.description
            )
            shareIntent.putExtra(Intent.EXTRA_TITLE, "Event App - " + event?.title)
            startActivity(Intent.createChooser(shareIntent, "Compartilhando evento..."))
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            R.id.share -> {
                shareEvent()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_event_detail, menu)
        var drawable = menu.findItem(R.id.share).icon

        drawable = DrawableCompat.wrap(drawable!!)
        DrawableCompat.setTint(drawable, ContextCompat.getColor(this, R.color.colorTextWhite))
        menu.findItem(R.id.share).icon = drawable
        return true
    }
}