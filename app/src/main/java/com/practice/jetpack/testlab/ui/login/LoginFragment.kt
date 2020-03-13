package com.practice.jetpack.testlab.ui.login

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.CompoundButton
import androidx.core.widget.addTextChangedListener
import androidx.navigation.Navigation
import com.practice.jetpack.testlab.R
import kotlinx.android.synthetic.main.login_fragment.*

class LoginFragment : Fragment() {

    private val PRIVATE_MODE = 0
    private val DARK_THEME = "dark_theme"
    private var validEmail = false
    private var validPass = false

    companion object {
        fun newInstance() = LoginFragment()
    }

    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.login_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)

        // TODO: Use the ViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPef : SharedPreferences? = activity?.getSharedPreferences(DARK_THEME, PRIVATE_MODE)

        // checking current theme and adjusting toggle & theme
        if(sharedPef?.getBoolean(DARK_THEME, false) == true){
            theme_toggle.isChecked = true
            activity?.setTheme(R.style.AppThemeDark)
        } else {
            theme_toggle.isChecked = false
            activity?.setTheme(R.style.AppThemeLight)
        }

        invalidateLoginButton()

        // checking input values of username edittext
        username.addTextChangedListener( object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                validEmail = !p0?.trim().isNullOrEmpty()
                invalidateLoginButton()
            }
        })

        // checking input values of password edittext
        password.addTextChangedListener( object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                validPass = !p0?.trim().isNullOrEmpty()
                invalidateLoginButton()
            }
        })

        button_login.setOnClickListener(View.OnClickListener {
            it.hideKeyboard()
            Navigation.findNavController(view).navigate(R.id.action_loginFragment_pop_including_mainFragment)
        })

        // changing theme according to toggle state
        theme_toggle.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton, isChecked ->
            if(isChecked) {
                activity?.setTheme(R.style.AppThemeDark)
                sharedPef?.edit()?.putBoolean(DARK_THEME, true)?.apply()
            } else {
                activity?.setTheme(R.style.AppThemeLight)
                sharedPef?.edit()?.putBoolean(DARK_THEME, false)?.apply()
            }
        })

    }

    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    // set Login button state
    fun invalidateLoginButton(){
        button_login.isEnabled = validEmail && validPass
    }

}
