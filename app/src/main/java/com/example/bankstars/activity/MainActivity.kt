package com.example.bankstars.activity

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bankstars.R
import com.example.bankstars.TaskAdapter
import com.example.bankstars.TaskItemListenner
import com.example.bankstars.repository.DatabaseUtil
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), TaskItemListenner {
    private lateinit var MainTaskList: RecyclerView
    private lateinit var MainTaskAdd: FloatingActionButton

    private lateinit var TaskAdapter: TaskAdapter

    private val handler = Handler(Looper.getMainLooper())

    private var UserId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        UserId = intent.getIntExtra("userId", -1)

        MainTaskList = findViewById(R.id.main_recyclerView_taskList)
        MainTaskList.layoutManager = LinearLayoutManager(this)

        MainTaskAdd = findViewById(R.id.main_FAB_add)
        MainTaskAdd.setOnClickListener {
            val it = Intent(this, TaskActivity::class.java)
            startActivity(it)
        }
    }

    override fun onStart() {
        super.onStart()

        GlobalScope.launch {
            val taskDAO = DatabaseUtil
                .getInstance(applicationContext)
                .getTaskDAO()

            val tasks = taskDAO.findAll()

            handler.post {
                TaskAdapter = TaskAdapter(tasks)
                TaskAdapter.setOnTaskItemListenner(this@MainActivity)
                MainTaskList.adapter = TaskAdapter
            }
        }
    }

    override fun setOnItemClickListenner(view: View, position: Int) {

        val it = Intent(this, TaskActivity::class.java)
        it.putExtra("userId", UserId)
        it.putExtra("TaskId", TaskAdapter.tasks[position].id)
        startActivity(it)
    }

    override fun setOnItemLongClickListenner(view: View, position: Int) {

        val task = TaskAdapter.tasks[position]

        val dialog = AlertDialog.Builder(this)
            .setTitle("Bank Stars")
            .setMessage("Deseja excluir a transação  ${task.name} ?")
            .setCancelable(false)
            .setPositiveButton("Confirmar") {dialog, _ ->

                GlobalScope.launch {
                    val taskDAO = DatabaseUtil
                        .getInstance(applicationContext)
                        .getTaskDAO()

                    taskDAO.delete(task)
                    val tasks = taskDAO.findAll()

                    handler.post{
                        TaskAdapter.tasks = tasks
                        TaskAdapter.notifyItemRemoved(position)
                    }
                }
                dialog.dismiss()
            }
            .setNegativeButton("Não") {dialog, _ ->
                dialog.dismiss()
            }
        dialog.show()
    }
}