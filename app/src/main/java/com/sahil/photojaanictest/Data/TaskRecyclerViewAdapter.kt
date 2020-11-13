package com.sahil.photojaanictest.Data

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.sahil.photojaanictest.Model.TaskModel
import com.sahil.photojaanictest.R

class TaskRecyclerViewAdapter(_context : Context, _taskList:List<TaskModel>, private val listener: ItemClickListener) : RecyclerView.Adapter<TaskRecyclerViewAdapter.taskViewHolder>() {

    val context = _context
    val taskList = _taskList

    companion object {
        var mClickListener: ItemClickListener? = null
    }

    interface ItemClickListener {
        fun clickToDelete(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): taskViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_show_task_layout,parent,false)
        return taskViewHolder(view)
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    override fun onBindViewHolder(holder: taskViewHolder, position: Int) {
        val task = taskList[position]
        mClickListener = listener
        holder.Name.text = task.title
        holder.Description.text = task.description

        val requestOptions: RequestOptions = RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .circleCrop()

        Glide.with(context)
            .load(task.image)
            .placeholder(R.mipmap.ic_launcher)
            .apply(requestOptions)
            .into(holder.Image)

        holder.itemLayout.setOnClickListener {
            mClickListener?.clickToDelete(task.id)
        }

    }



    class taskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        val Name : TextView = itemView.findViewById(R.id.title)
        val Description : TextView = itemView.findViewById(R.id.body)
        val Image : ImageView = itemView.findViewById(R.id.image)
        val itemLayout : CardView = itemView.findViewById(R.id.cardview)
    }

}