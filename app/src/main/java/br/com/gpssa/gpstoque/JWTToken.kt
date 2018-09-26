package br.com.gpssa.gpstoque

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class JWTToken: Serializable{

    @SerializedName("token")
    @Expose

    private var token: String? = null

    fun JWTToken() {

    }

    fun getToken(): String? {
        return token
    }

    fun setToken(token: String) {
        this.token = token
    }
}


