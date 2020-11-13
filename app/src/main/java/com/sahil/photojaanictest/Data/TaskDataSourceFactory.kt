package com.sahil.photojaanictest.Data

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.sahil.photojaanictest.Model.TaskModel

class TaskDataSourceFactory: DataSource.Factory<String, List<TaskModel>>() {

    private val sourceLiveData = MutableLiveData<MainActivityRepository>()

    override fun create(): DataSource<String, List<TaskModel>> {
        val source = MainActivityRepository()
        sourceLiveData.postValue(source)
        return source
    }
}