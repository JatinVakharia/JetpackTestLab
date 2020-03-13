package com.practice.jetpack.testlab.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class Story : Serializable{
    @SerializedName("title")
    @Expose
    var title : String = ""
    @SerializedName("type")
    @Expose
    var type : String = ""
    @SerializedName("url")
    @Expose
    var url : String = ""
    @SerializedName("id")
    @Expose
    var id : Long = 0

}