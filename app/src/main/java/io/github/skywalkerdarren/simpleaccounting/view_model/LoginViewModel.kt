package io.github.skywalkerdarren.simpleaccounting.view_model

import android.widget.EditText
import androidx.lifecycle.ViewModel
import io.github.skywalkerdarren.simpleaccounting.model.repository.OnlineRepository

class LoginViewModel(private val onlineRepo : OnlineRepository) : ViewModel() {
    fun login(username: String, password: String) {
        onlineRepo
    }

}