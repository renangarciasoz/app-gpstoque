package br.com.gpssa.gpstoque

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.io.Serializable

@Entity(tableName = "uniform")
class Uniform : Serializable {

    @PrimaryKey
    var _id:String? = null
    var code:Int = 0
    var name = ""
    var description = ""
    var imgUrl = ""
    var amount = 0

    override fun toString(): String {
        return "Uniform(name='$name')"
    }

    fun toJson(): String {
        return GsonBuilder().create().toJson(this)
    }
}