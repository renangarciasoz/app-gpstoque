package br.com.gpssa.gpstoque

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_cadastro_uniform.*
import kotlinx.android.synthetic.main.login.*

class UniformCadastroActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_uniform)
        setTitle("Novo Uniforme")

        val numero = intent.getIntExtra("numero",0)

        salvarUniform.setOnClickListener {
            val uniform = Uniform()
            uniform.code = numero + 1
            uniform.name = nameUniform.text.toString()
            uniform.description = descriptionUniform.text.toString()
            uniform.amount = amountUniform.text.toString().toInt()
            uniform.imgUrl = imgUrl.text.toString()

            taskAtualizar(uniform)
        }
    }

    private fun taskAtualizar(uniform: Uniform) {
        // Thread para salvar a discilpina
        Thread {
            if (AndroidUtils.isInternetDisponivel(GPSApplication.getInstance().applicationContext)) {
                UniformService.save(uniform)
            } else {
                UniformService.saveOffline(uniform)
            }
            runOnUiThread {
                // após cadastrar, voltar para activity anterior
                finish()
            }
        }.start()
    }
}
