package com.fabiojunior.eventsapp.view.register

import android.content.Intent
import android.os.Bundle
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import com.fabiojunior.eventsapp.R
import com.fabiojunior.eventsapp.data.model.Profile
import com.fabiojunior.eventsapp.view.base.BaseActivity
import com.fabiojunior.eventsapp.view.events.MainEventsActivity
import kotlinx.android.synthetic.main.activity_register.*
import java.util.regex.Pattern

class RegisterActivity : BaseActivity() {

    private val emailRegex = Pattern.compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    )

    override fun getLayout(): Int {
        return R.layout.activity_register
    }

    override fun init() {
        setupStatusBar()
        showError(false, "")

        login.setOnClickListener {
            showError(false, "")
            if (isNameValid(edt_name.text.toString())) {
                if (isEmail(edt_email.text.toString())) {
                    val profile = Profile(edt_name.text.toString(), edt_email.text.toString())
                    val bundle =
                        Bundle().apply { putParcelable(MainEventsActivity.KEY_PROFILE, profile) }
                    val intent = Intent(this, MainEventsActivity::class.java)
                    intent.putExtra(MainEventsActivity.KEY_BUNDLE, bundle)
                    startActivity(intent)
                    finish()

                } else {
                    showError(true, getString(R.string.invalid_email))
                }
            } else {
                showError(true, getString(R.string.invalid_name))
            }
        }
    }

    /**
     * Validate email
     */
    private fun isEmail(email: String) = emailRegex.matcher(email).matches()

    /**
     * Validate name
     */
    private fun isNameValid(name: String) = name.length > 3

    /**
     * Show error message
     */
    private fun showError(enabled: Boolean, message: String) {
        if (enabled) {
            error_message.visibility = VISIBLE
            error_text.text = message
        } else {
            error_message.visibility = INVISIBLE
        }
    }
}