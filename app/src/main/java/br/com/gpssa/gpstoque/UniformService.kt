package br.com.gpssa.gpstoque

import android.content.Context
import android.provider.CalendarContract
import android.util.Log
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONArray
import java.net.URL

object UniformService {

    //TROQUE PELO IP DE ONDE ESTÁ O WS
    val host = "https://gpstoque-api.herokuapp.com"
    val TAG = "WS_GPSApp"

    fun getUniforms (context: Context): List<Uniform> {
        var uniforms = ArrayList<Uniform>()
        if (AndroidUtils.isInternetDisponivel(context)) {
            val url = "$host/uniform"
            val json = HttpHelper.get(url)
            uniforms = parserJson(json)
            // salvar offline
            for (d in uniforms) {
                saveOffline(d)
            }
            return uniforms
        } else {
            val dao = DatabaseManager.getUniformDAO()
            val uniforms = dao.findAll()
            return uniforms
        }

    }

    fun getUniform (context: Context, id: String): Uniform? {

        if (AndroidUtils.isInternetDisponivel(context)) {
            val url = "$host/uniform/${id}"
            val json = HttpHelper.get(url)
            val uniform = parserJson<Uniform>(json)

            return uniform
        } else {
            val dao = DatabaseManager.getUniformDAO()
            val uniform = dao.getById(id)
            return uniform
        }

    }

    fun save(uniform: Uniform): Response {
        val json = HttpHelper.post("$host/uniform", uniform.toJson())
        return parserJson(json)
    }

    fun saveOffline(uniform: Uniform) : Boolean {
        val dao = DatabaseManager.getUniformDAO()

        if (! existeUniform(uniform)) {
            dao.insert(uniform)
        }

        return true

    }

    fun existeUniform(uniform: Uniform): Boolean {
        val dao = DatabaseManager.getUniformDAO()
        return dao.getById(uniform.id) != null
    }

    fun delete(uniform: Uniform): Response {
        if (AndroidUtils.isInternetDisponivel(GPSApplication.getInstance().applicationContext)) {
            val url = "$host/uniform/${uniform.id}"
            val json = HttpHelper.delete(url)

            return parserJson(json)
        } else {
            val dao = DatabaseManager.getUniformDAO()
            dao.delete(uniform)
            return Response(status = "OK", msg = "Dados salvos localmente")
        }

    }

    inline fun <reified T> parserJson(json: String): T {
        val type = object : TypeToken<T>(){}.type
        return Gson().fromJson<T>(json, type)
    }
}