package com.sahil.photojaanictest.Data

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.sahil.photojaanictest.Model.TaskModel

class MainActivityViewModel : ViewModel() {

    var mainActivityRepository = TaskRepository()

    fun getAlltaskList(): LiveData<List<TaskModel>>
    {
        return MainActivityRepository().getTasks()
    }

    fun getCountriesFromAPIAndStore()
    {
        mainActivityRepository.getPosts()
    }


}