package com.practice.jetpack.testlab.ui.login

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.practice.jetpack.androidtask.api.response.LoginResponse
import com.practice.jetpack.testlab.R
import com.practice.jetpack.testlab.databinding.LoginFragmentBinding
import kotlinx.android.synthetic.main.login_fragment.*
import org.koin.android.viewmodel.ext.android.viewModel

class LoginFragment : Fragment() {

    companion object {
        fun newInstance() = LoginFragment()
        const val PRIVATE_MODE = 0
        const val DARK_THEME = "dark_theme"
    }

    private val viewModel: LoginViewModel by viewModel()
    private lateinit var binding: LoginFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.login_fragment, container, false)

        // Bind layout with ViewModel
        binding.viewmodel = viewModel

        // LiveData needs the lifecycle owner
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        user_username.error = getString(R.string.emailNoValidHint)
        user_password.error = getString(R.string.passwordHintSignUp)

        val sharedPef : SharedPreferences? = activity?.getSharedPreferences(
            DARK_THEME,
            PRIVATE_MODE
        )

        // checking current theme and adjusting toggle & theme
        if(sharedPef?.getBoolean(DARK_THEME, false) == true){
            theme_toggle.isChecked = true
            activity?.setTheme(R.style.AppThemeDark)
        } else {
            theme_toggle.isChecked = false
            activity?.setTheme(R.style.AppThemeLight)
        }

        // Observer for ProgressBar
        viewModel.loginStarted.observe(viewLifecycleOwner, Observer { started ->
            if(started)
                login_progress.visibility = View.VISIBLE
            else
                login_progress.visibility = View.GONE
        })

        // Observer for Login Response
        viewModel.loginResponse.observe(viewLifecycleOwner, Observer { response ->
            view.hideKeyboard()
            val msg : String
            if(response.isSuccessful) {
                msg = "Login Successful"
                Navigation.findNavController(view)
                    .navigate(R.id.action_loginFragment_pop_including_mainFragment)
            }else {
                val loginResponse: LoginResponse =
                    Gson().fromJson<LoginResponse>(response.message(), LoginResponse::class.java)
                msg = loginResponse.description
            }

            val snack = Snackbar.make(view, msg, Snackbar.LENGTH_LONG)
            snack.show()
        })

        // changing theme according to toggle state
        theme_toggle.setOnCheckedChangeListener { _ , isChecked ->
            if(isChecked) {
                activity?.setTheme(R.style.AppThemeDark)
                sharedPef?.edit()?.putBoolean(DARK_THEME, true)?.apply()
            } else {
                activity?.setTheme(R.style.AppThemeLight)
                sharedPef?.edit()?.putBoolean(DARK_THEME, false)?.apply()
            }
        }

    }

    private fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}
