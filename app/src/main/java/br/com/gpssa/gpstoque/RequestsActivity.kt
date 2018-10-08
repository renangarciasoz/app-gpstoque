package br.com.gpssa.gpstoque

import android.os.Bundle
import android.app.Activity

import kotlinx.android.synthetic.main.activity_requests.*

class RequestsActivity : DebugActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_requests)
    }

}
