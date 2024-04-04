package com.example.bankstars

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter(var tasks: List<Task>): RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    private var listenner: TaskItemListenner? = null

    class TaskViewHolder(view: View, listenner: TaskItemListenner?): RecyclerView.ViewHolder(view){
        val name: TextView = view.findViewById(R.id.task_item_textView_name)
        val transaction: View = view.findViewById(R.id.task_item_view)

        init {
            view.setOnClickListener {
                listenner?.setOnItemClickListenner(it, adapterPosition)
            }
            view.setOnLongClickListener {
                listenner?.setOnItemLongClickListenner(it, adapterPosition)
                true
            }
        }
    }

    fun setOnTaskItemListenner(listenner: TaskItemListenner){
        this.listenner = listenner
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.activity_task_item, parent, false)
        return TaskViewHolder(view, listenner)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.name.text = tasks[position].name
        if(tasks[position].isTransactionPlus){
            holder.transaction.setBackgroundColor(Color.GREEN)
        }else if(tasks[position].isTransactionMinus){
            holder.transaction.setBackgroundColor(Color.RED)
        }
    }

    override fun getItemCount(): Int {
        return tasks.size
    }
}