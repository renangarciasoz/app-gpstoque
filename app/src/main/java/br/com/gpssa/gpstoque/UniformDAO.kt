package br.com.gpssa.gpstoque

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query

@Dao
interface UniformDAO {
    @Query("SELECT * FROM uniform where code = :code")
    fun getById(code: Int) : Uniform?

    @Query("SELECT * FROM uniform")
    fun findAll(): List<Uniform>

    @Insert
    fun insert(uniform: Uniform)

    @Delete
    fun delete(uniform: Uniform)

}