package br.com.gpssa.gpstoque

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

// anotação define a lista de entidades e a versão do banco
@Database(entities = arrayOf(Uniform::class), version = 7)
abstract class GPSDatabase: RoomDatabase() {
    abstract fun uniformDAO(): UniformDAO
}