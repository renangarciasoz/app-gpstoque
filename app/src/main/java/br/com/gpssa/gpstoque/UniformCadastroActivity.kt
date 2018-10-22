package br.com.gpssa.gpstoque

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_cadastro_uniform.*
import kotlinx.android.synthetic.main.login.*

class UniformCadastroActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_uniform)
        setTitle("Nova Uniform")

        salvarUniform.setOnClickListener {
            val uniform = Uniform()
            uniform.nome = nomeUniform.text.toString()
            uniform.ementa = ementaUniform.text.toString()
            uniform.professor = professorUniform.text.toString()
            uniform.foto = urlFoto.text.toString()

            taskAtualizar(uniform)
        }
    }

    private fun taskAtualizar(uniform: Uniform) {
        // Thread para salvar a discilpina
        Thread {
            UniformService.save(uniform)
            runOnUiThread {
                // após cadastrar, voltar para activity anterior
                finish()
            }
        }.start()
    }
}
