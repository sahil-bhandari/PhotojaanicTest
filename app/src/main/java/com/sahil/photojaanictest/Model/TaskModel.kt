package com.sahil.photojaanictest.Model

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class TaskModel (
        @PrimaryKey
        var id : Int=0,
        var index : Int=0,
        var title : String="",
        var description : String="",
        var image : String=""
) : RealmObject()
