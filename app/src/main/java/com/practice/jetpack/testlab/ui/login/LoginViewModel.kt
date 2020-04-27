package com.practice.jetpack.testlab.ui.login

import androidx.core.util.PatternsCompat
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.practice.jetpack.androidtask.api.response.LoginResponse
import com.practice.jetpack.testlab.AbstractViewModel
import com.practice.jetpack.testlab.api.request.LoginRequest
import com.practice.jetpack.testlab.repo.LoginRepo
import com.practice.jetpack.testlab.schedular.BaseSchedulerProvider
import com.practice.jetpack.testlab.utility.Constants
import retrofit2.Response

class LoginViewModel(private val loginRepo: LoginRepo,
                     private val schedulerProvider: BaseSchedulerProvider) : AbstractViewModel() {

    val emailAddress = MutableLiveData<String>("")
    val password = MutableLiveData<String>("")
    var loginResponse = MutableLiveData<Response<LoginResponse>>()
    var loginStarted = MutableLiveData<Boolean>()
    private var isValidEmail: Boolean = false
    private var isValidPass: Boolean = false
    var isNetworkAvailable: Boolean = true

    // To verify if username and Password are in expected format
    val dataValid = MediatorLiveData<Boolean>().apply {
        addSource(emailAddress) {
            isValidEmail = isEmailValid(it)
            // Only true when Email and Pass are in expected Format
            value = isValidEmail && isValidPass
        }
        addSource(password) {
            isValidPass = isPassWordValid(it)
            // Only true when Email and Pass are in expected Format
            value = isValidEmail && isValidPass
        }
    }

    // Is Password in expected format
    fun isPassWordValid(password: String?): Boolean {
        val regEx = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#\$%!\\-_?&])(?=\\S+\$).{8,16}".toRegex()
        return password?.matches(regEx) == true
    }

    // Is Email in expected format
    fun isEmailValid(email: String?): Boolean =
        PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()

    fun onLoginClick(){
        loginStarted.value = true
        onLogin(emailAddress.value, password.value)
    }

    fun onLogin(email: String?, password: String?) {
        var url = ""
        when(validateCredentials(email, password)){
            Constants.SUCCESS_CODE -> url = Constants.SUCCESS_URL
            Constants.INCORRECT_CODE -> url = Constants.INCORRECT_URL
            Constants.INVALID_CODE -> url =  Constants.INVALID_URL
        }
        launch {
            loginRepo.login(getLoginRequest(), url)
                .observeOn(schedulerProvider.ui())
                .subscribeOn(schedulerProvider.io())
                .subscribe{
                    loginResponse.value = it
                    loginStarted.value = false
                }
        }
    }

    private fun getLoginRequest(): LoginRequest {
        val loginRequest = LoginRequest()
        loginRequest.username = emailAddress.toString()
        loginRequest.password = password.toString()
        return loginRequest
    }

    fun validateCredentials(emailAddress: String?, password: String?): Int {
        return if(!isNetworkAvailable) {
            Constants.INVALID_CODE
        } else if(emailAddress?.equals(Constants.validUsername, false) == true &&
            password?.equals(Constants.validPassword, false) == true){
            Constants.SUCCESS_CODE
        } else {
            Constants.INCORRECT_CODE
        }
    }

//    fun checkNetwork(){
//        isNetworkAvailable = MyApplication().isNetworkAvailable()
//    }
}
