package br.com.gpssa.gpstoque

import android.arch.persistence.room.Room

object DatabaseManager {

    // singleton
    private var dbInstance: GPSDatabase
    init {
        val appContext = GPSApplication.getInstance().applicationContext
        dbInstance = Room.databaseBuilder(
                appContext, // contexto global
                GPSDatabase::class.java, // Referência da classe do banco
                "gps.sqlite" // nome do arquivo do banco
        ).build()
    }

    fun getUniformDAO(): UniformDAO {
        return dbInstance.uniformDAO()
    }
}