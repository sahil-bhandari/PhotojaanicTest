package com.sahil.photojaanictest.Activity

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.sahil.photojaanictest.Data.MainActivityViewModel
import com.sahil.photojaanictest.Data.TaskRecyclerViewAdapter
import com.sahil.photojaanictest.Model.TaskModel
import com.sahil.photojaanictest.R
import io.realm.Realm
import java.io.IOException

class MainActivity : AppCompatActivity() {

    lateinit var taskRecyclerView: RecyclerView
    lateinit var mainActivityViewModel: MainActivityViewModel
    private lateinit var realm: Realm
    lateinit var taskRecyclerViewAdapter: TaskRecyclerViewAdapter
    lateinit var fabAddBtn:FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        realm = Realm.getDefaultInstance()
        taskRecyclerView = findViewById(R.id.taskRecyclerView)
        fabAddBtn = findViewById(R.id.addBtnFab)

        mainActivityViewModel = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)

        if(isNetworkConnected())
        {
            mainActivityViewModel.getCountriesFromAPIAndStore()
        }
        else
        {
            Toast.makeText(this,"No internet found. Showing cached list in the view",Toast.LENGTH_LONG).show()
        }

        viewData()

        fabAddBtn.setOnClickListener {
            openDialog(this)
        }
    }

    private fun viewData() {
        mainActivityViewModel.getAlltaskList().observe(this, { taskList ->
            Log.e(MainActivity::class.java.simpleName, taskList.toString())
            setUptaskRecyclerView(taskList!!)
        })
    }

    private fun openDialog(context: Context) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.item_add_task_layout)
        val body = dialog.findViewById(R.id.textTitle) as TextView
        val description = dialog.findViewById(R.id.textDescription) as TextView
        val yesBtn = dialog.findViewById(R.id.addBtn) as Button
        val noBtn = dialog.findViewById(R.id.cancelBtn) as Button
        yesBtn.setOnClickListener {
            if (body.text.toString().trim().isEmpty()||body.text.toString().trim().isBlank()){
                Toast.makeText(context,"Please add a Title.",Toast.LENGTH_SHORT).show()
            } else if (description.text.toString().trim().isEmpty()||description.text.toString().trim().isBlank()){
                Toast.makeText(context,"Please add a Description.",Toast.LENGTH_SHORT).show()
            }
            else{
                AddDetails(body.text.toString(),description.text.toString())
                dialog.dismiss()
            }

        }
        noBtn.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    fun isNetworkConnected(): Boolean {
        val runtime = Runtime.getRuntime()
        try {
            val ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8")
            val exitValue = ipProcess.waitFor()
            return exitValue == 0
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        return false
    }

    fun setUptaskRecyclerView(tasks: List<TaskModel>) {
        taskRecyclerViewAdapter = TaskRecyclerViewAdapter(this, tasks,object : TaskRecyclerViewAdapter.ItemClickListener {
            override fun clickToDelete(position: Int) {
                realmDelete(position)
            }
        })
        taskRecyclerView.adapter = taskRecyclerViewAdapter
        taskRecyclerView.layoutManager = LinearLayoutManager(this)
        taskRecyclerView.setHasFixedSize(true)
        taskRecyclerViewAdapter.notifyDataSetChanged()
    }

    private fun realmDelete(id: Int) {

        val alertDialog= AlertDialog.Builder(this)
        alertDialog.setTitle("Delete!")
            .setMessage("Are you Sure?")
            .setPositiveButton(android.R.string.ok) { dialogInterface, i ->
                try {
                    realm.beginTransaction()
                    realm.where(TaskModel::class.java).equalTo("id", id).findFirst()!!.deleteFromRealm()
                    realm.commitTransaction()
                    Toast.makeText(this,"Entry deleted.",Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Toast.makeText(this,"Unable to delete entry.",Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton(android.R.string.cancel){dialogInterface, i ->null}
            .show()
        viewData()
    }

    fun AddDetails(titleText: String, textDescription: String) {
        try {
            realm.beginTransaction()
        } catch (e: Exception) {
            Log.d("TAG","transaction failed")
        }
        val maxId = realm.where(TaskModel::class.java).max("id")
        val nextId = if (maxId == null) 1 else maxId.toInt() + 1
        val user = TaskModel(id = nextId,index = 0,title = titleText,description = textDescription,image="")
        realm.insertOrUpdate(user)
        realm.commitTransaction()
        viewData()
        Toast.makeText(this,"Data Added!",Toast.LENGTH_SHORT).show()

    }

}
