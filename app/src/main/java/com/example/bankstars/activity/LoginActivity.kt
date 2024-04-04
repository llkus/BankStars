package com.example.bankstars.activity

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.bankstars.R
import com.example.bankstars.repository.DatabaseUtil
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var LoginEmail: EditText
    private lateinit var LoginPassword: EditText
    private lateinit var LoginRegister: Button
    private lateinit var LoginEntrar: Button

    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        LoginEmail = findViewById(R.id.login_edittext_email)
        LoginPassword = findViewById(R.id.login_edittext_senha)

        LoginRegister = findViewById(R.id.login_button_login)
        LoginRegister.setOnClickListener(this)

        LoginEntrar = findViewById(R.id.login_button_entrar)
        LoginEntrar.setOnClickListener(this)


    }

    override fun onClick(view: View?) {
        when (view?.id) {

            R.id.login_button_login -> {
                val it = Intent(this, RegisterActivity::class.java)
                startActivity(it)
            }

            R.id.login_button_entrar -> {
                val email = LoginEmail.text.toString()
                val password = LoginPassword.text.toString()

                if (email.isBlank()) {
                    LoginEmail.error = "Este campo é obrigatório"
                    return
                }

                if (password.isBlank()) {
                    LoginPassword.error = "Este campo é obrigatório"
                    return
                }

                GlobalScope.launch {
                    val userDAO = DatabaseUtil.getInstance(applicationContext).getUserDAO()
                    val user = userDAO.findByEmail(email)

                    if (user != null) {
                        if (user.password == password) {
                            val it = Intent(applicationContext, MainActivity::class.java)
                            it.putExtra("userId", user.id)
                            startActivity(it)
                            finish()
                        } else {
                            LoginEmail.text.clear()
                            LoginPassword.text.clear()

                            handler.post { showDialog("Email ou senha inválidos") }
                        }
                    } else {
                        handler.post { showDialog("Email ou senha inválidos") }
                    }
                }
            }
        }
    }

    private fun showDialog(message: String) {
        val dialog = AlertDialog.Builder(this)
            .setTitle("Bank Stars")
            .setMessage(message)
            .setCancelable(true)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
        dialog.show()
    }
}