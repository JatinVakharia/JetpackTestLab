package com.practice.jetpack.testlab.interceptor

import com.google.gson.Gson
import com.practice.jetpack.androidtask.api.response.LoginResponse
import com.practice.jetpack.testlab.BuildConfig
import com.practice.jetpack.testlab.utility.Constants
import okhttp3.*

/* This interceptor is used to mock Responses, when API's are not ready to integrate.
* And this interceptor can only be used in Debug mode */

class LoginInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        if (BuildConfig.DEBUG) {
            val uri = chain.request().url().uri().toString()
            // We are providing different response depending upon URL
            val responseString = when {
                uri.endsWith(Constants.SUCCESS_URL) -> getSuccessResponse()
                uri.endsWith(Constants.INCORRECT_URL) -> getIncorrectResponse()
                uri.endsWith(Constants.INVALID_URL) -> getInvalidResponse()
                else -> ""
            }

            // We are providing different status code depending upon URL
            val code = when {
                uri.endsWith(Constants.SUCCESS_URL) -> 200
                uri.endsWith(Constants.INCORRECT_URL) -> 401
                uri.endsWith(Constants.INVALID_URL) -> 400
                else -> 404
            }

            return chain.proceed(chain.request())
                .newBuilder()
                .code(code)
                .protocol(Protocol.HTTP_2)
                .message(responseString)
                .body(
                    ResponseBody.create(
                        MediaType.parse("application/json"),
                    responseString.toByteArray()))
                .addHeader("content-type", "application/json")
                .build()
        } else {
            //just to be on safe side.
            throw IllegalAccessError("LoginInterceptor is only meant for Testing Purposes and " +
                    "bound to be used only with DEBUG mode")
        }
    }

    /* Dummy Success Response*/
    private fun getSuccessResponse(): String {
        val loginResponse = LoginResponse()
        loginResponse.token = "VwvYXBpXC9"
        return Gson().toJson(loginResponse)
    }

    /* Dummy Incorrect Credentials Response*/
    private fun getIncorrectResponse(): String {
        val loginResponse = LoginResponse()
        loginResponse.error = "invalid_credentials"
        loginResponse.description = "Email address and password is not a valid combination."
        return Gson().toJson(loginResponse)
    }

    /* Dummy InValid data Response*/
    private fun getInvalidResponse(): String {
        val loginResponse = LoginResponse()
        loginResponse.error = "bad_request"
        loginResponse.description = "Network communication error."
        return Gson().toJson(loginResponse)
    }


}