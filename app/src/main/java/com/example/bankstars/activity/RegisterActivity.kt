package com.example.bankstars.activity

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.example.bankstars.R
import com.example.bankstars.User
import com.example.bankstars.repository.DatabaseUtil
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var RegisterName: EditText
    private lateinit var RegisterEmail: EditText
    private lateinit var RegisterCellfone: EditText
    private lateinit var RegisterPassword: EditText
    private lateinit var RegisterPasswordConfirmation: EditText
    private lateinit var RegisterSalvar: Button

    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        RegisterName = findViewById(R.id.register_edittext_name)
        RegisterEmail = findViewById(R.id.register_edittext_email)
        RegisterCellfone = findViewById(R.id.register_edittext_cellfone)
        RegisterPassword = findViewById(R.id.register_edittext_password)
        RegisterPasswordConfirmation = findViewById(R.id.register_edittext_password_confirmation)

        RegisterSalvar = findViewById(R.id.register_button_save)
        RegisterSalvar.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.register_button_save -> hendlerSaveAction()
        }
    }

    private fun hendlerSaveAction() {

        val name = RegisterName.text.trim()
        val email = RegisterEmail.text.trim()
        val fone = RegisterCellfone.text.trim()
        val password = RegisterPassword.text.trim()
        val passowordConfirmation = RegisterPasswordConfirmation.text.trim()

        if(isNameFilled(name) &&
            isEmailFilled(email) &&
            isFoneFilled(fone) &&
            isPasswordFilled(password) &&
            isPasswordConfirmationFilled(passowordConfirmation)){

            if(password == passowordConfirmation){

                val user = User(
                    name = name.toString(),
                    email = email.toString(),
                    fone = fone.toString(),
                    password = password.toString()
                )

                GlobalScope.launch {
                    val userDAO = DatabaseUtil.getInstance(applicationContext).getUserDAO()
                    userDAO.insert(user)

                    handler.post {
                        val dialog = AlertDialog.Builder(this@RegisterActivity)
                            .setTitle("Bank Stars")
                            .setMessage(" $name cadastrado com sucesso!")
                            .setCancelable(false)
                            .setPositiveButton("OK") {dialog, _ ->
                                dialog.dismiss()
                                finish()
                            }
                        dialog.show()
                    }
                }
            }else{
                RegisterPasswordConfirmation.error = "As senhas estão diferentes"
                return
            }
        }
    }

    private fun isPasswordConfirmationFilled(passowordConfirmation: CharSequence): Boolean {
        return if (passowordConfirmation.isBlank()) {
            RegisterPasswordConfirmation.error = "Este campo é obrogatório"
            false
        } else {
            true
        }
    }

    private fun isPasswordFilled(password: CharSequence): Boolean {
        return if (password.isBlank()) {
            RegisterPassword.error = "Este campo é obrogatório"
            false
        } else {
            true
        }
    }

    private fun isEmailFilled(email: CharSequence): Boolean {
        return if (email.isBlank()) {
            RegisterEmail.error = "Este campo é obrogatório"
            false
        } else {
            true
        }
    }

    private fun isFoneFilled(fone: CharSequence): Boolean {
        return if (fone.isBlank()) {
            RegisterCellfone.error = "Este campo é obrogatório"
            false
        } else {
            true
        }
    }

    private fun isNameFilled(name: CharSequence): Boolean {
        return if (name.isBlank()) {
            RegisterName.error = "Este campo é obrogatório"
            false
        } else {
            true
        }
    }
}