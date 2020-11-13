package com.sahil.photojaanictest.Data

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.sahil.photojaanictest.Model.TaskModel

class TaskRepository {

    private val sourceFactory = TaskDataSourceFactory()

    fun getPosts(): LiveData<PagedList<List<TaskModel>>> {
        return sourceFactory.toLiveData(
            pageSize = 10
        )
    }
}