package com.example.bankstars.activity

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import com.example.bankstars.R
import com.example.bankstars.Task
import com.example.bankstars.repository.DatabaseUtil
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TaskActivity : AppCompatActivity() {

    private lateinit var TaskName: EditText
    private lateinit var TaskDescription: EditText
    private lateinit var TaskTransactionPlus: CheckBox
    private lateinit var TaskTransactionMinus: CheckBox
    private lateinit var TaskButton: Button

    private val handler = Handler(Looper.getMainLooper())

    private var UserId = -1
    private var tId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)

        UserId = intent.getIntExtra("userId", -1)
        tId = intent.getIntExtra("taskId", -1)

        TaskName = findViewById(R.id.task_edittext_name)
        TaskDescription = findViewById(R.id.task_edittext_descripition)
        TaskTransactionPlus = findViewById(R.id.task_checkbox_transaction_plus)
        TaskTransactionMinus = findViewById(R.id.task_checkbox_transaction_minus)

        TaskButton = findViewById(R.id.task_button_action)
        TaskButton.setOnClickListener{

            val name = TaskName.text.toString().trim()
            val description = TaskDescription.text.toString().trim()
            val isTrasnactionPlus = TaskTransactionPlus.isChecked
            val isTrasnactionMinus = TaskTransactionMinus.isChecked

            if(name.isBlank()){
                TaskName.error = "Este campo é obrigatório"
                return@setOnClickListener
            }

            val taskDAO = DatabaseUtil
                .getInstance(applicationContext)
                .getTaskDAO()

            if(tId != -1){

                GlobalScope.launch {
                    val task = Task(tId, name, description, isTrasnactionPlus, isTrasnactionMinus, UserId)
                    taskDAO.update(task)
                    handler.post {
                        val dialog = AlertDialog.Builder(this@TaskActivity)
                            .setTitle("Bank Stars")
                            .setMessage("Transação $name modificada com sucesso!")
                            .setCancelable(false)
                            .setPositiveButton("OK") {dialog, _ ->
                                dialog.dismiss()
                                finish()
                            }
                        dialog.show()
                    }
                }

            } else {
                GlobalScope.launch {
                    val task = Task(
                        name = name,
                        description = description,
                        isTransactionPlus = isTrasnactionPlus,
                        isTransactionMinus = isTrasnactionMinus,
                        userId = UserId
                    )

                    taskDAO.insert(task)

                    handler.post {
                        val dialog = AlertDialog.Builder(this@TaskActivity)
                            .setTitle("Bank Stars")
                            .setMessage("Quantia de $name realizada com sucesso!")
                            .setCancelable(true)
                            .setPositiveButton("OK") {dialog, _ ->
                                dialog.dismiss()
                                finish()
                            }
                        dialog.show()
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        if(tId != -1){
            // TODO: Alterando item da lista
            GlobalScope.launch {
                val TaskDAO = DatabaseUtil
                    .getInstance(applicationContext)
                    .getTaskDAO()

                val Task = TaskDAO.findById(tId)

                handler.post{
                    TaskButton.text = "Alterar"
                    TaskName.text = Editable.Factory.getInstance().newEditable(Task.name)
                    TaskDescription.text = Editable.Factory.getInstance().newEditable(Task.description)
                }
            }
        } else {
            // TODO: Criando item da lista
            TaskButton.text = "Adicionar"
        }
    }
}