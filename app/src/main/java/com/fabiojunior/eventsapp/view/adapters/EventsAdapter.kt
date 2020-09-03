package com.fabiojunior.eventsapp.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fabiojunior.eventsapp.R
import com.fabiojunior.eventsapp.data.model.Event
import kotlinx.android.synthetic.main.item_event.view.*
import java.text.SimpleDateFormat
import java.util.*

class EventsAdapter(
    private val events: MutableList<Event>,
    private val eventClickListener: EventClickListener
) :
    RecyclerView.Adapter<EventsAdapter.EventsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventsViewHolder {
        return EventsViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_event, parent, false)
        )
    }

    override fun getItemCount() = events.count()

    override fun onBindViewHolder(holder: EventsViewHolder, position: Int) {
        holder.bindView(events[position], eventClickListener)
    }

    /**
     * Update list
     */
    fun updateList(itemList: MutableList<Event>) {
        this.events.clear()
        this.events.addAll(itemList)
        notifyDataSetChanged()
    }

    /**
     * Search event in list by id
     */
    fun searchItem(idEvent: String): Event? {
        for (event in events) {
            if (event.id.equals(idEvent)) return event
        }
        return null
    }

    class EventsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val card = itemView.card
        private val icon = itemView.icon
        private val title = itemView.title
        private val date = itemView.email

        fun bindView(event: Event, eventClickListener: EventClickListener) {
            title.text = event.title
            date.text = SimpleDateFormat("dd/MM/yyy").format(event.date?.let { Date(it) })

            Glide.with(itemView.context)
                .load(event.image)
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_foreground)
                .into(icon)

            card.setOnClickListener {
                eventClickListener.onEventClickListener(event)
            }
        }
    }

    /**
     * Event callback
     */
    interface EventClickListener {
        fun onEventClickListener(event: Event)
    }
}