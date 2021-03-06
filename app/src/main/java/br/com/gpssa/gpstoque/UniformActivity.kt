package br.com.gpssa.gpstoque

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.squareup.picasso.Picasso

class UniformActivity : DebugActivity() {

    private val context: Context get() = this
    var uniform: Uniform? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_uniform)

        // recuperar onjeto de Uniform da Intent
        if (intent.getSerializableExtra("uniform") is Uniform)
            uniform = intent.getSerializableExtra("uniform") as Uniform

        // configurar título com nome da Uniform e botão de voltar da Toobar
        // colocar toolbar
        var toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        // alterar título da ActionBar
        supportActionBar?.title = uniform?.name

        // up navigation
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // atualizar dados do uniforme
        var name = findViewById<TextView>(R.id.nameUniformInfo)
        name.text = uniform?.name

        var description = findViewById<TextView>(R.id.descriptionUniformInfo)
        description.text = uniform?.description

        var amount = findViewById<TextView>(R.id.amountUniformInfo)
        amount.text = uniform?.amount.toString()

        var imagem = findViewById<ImageView>(R.id.imagemUniform)
        Picasso.with(this).load(uniform?.imgUrl).fit().into(imagem,
                object: com.squareup.picasso.Callback{
                    override fun onSuccess() {}

                    override fun onError() { }
                })
    }

    // método sobrescrito para inflar o menu na Actionbar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // infla o menu com os botões da ActionBar
        menuInflater.inflate(R.menu.menu_main_uniform, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        // id do item clicado
        val id = item?.itemId
        // verificar qual item foi clicado
        // remover a uniform no WS
        if  (id == R.id.action_remover) {
            // alerta para confirmar a remeção
            // só remove se houver confirmação positiva
            AlertDialog.Builder(this)
                    .setTitle(R.string.app_name)
                    .setMessage("Deseja excluir a uniform")
                    .setPositiveButton("Sim") {
                        dialog, which ->
                            dialog.dismiss()
//                            taskExcluir()
                    }.setNegativeButton("Não") {
                        dialog, which -> dialog.dismiss()
                    }.create().show()
        }
        // botão up navigation
        else if (id == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

//    private fun taskExcluir() {
//        if (this.uniform != null && this.uniform is Uniform) {
            // Thread para remover a uniform
//            Thread {
                //                UniformService.delete(this.uniform as Uniform)
//                runOnUiThread {
                    // após remover, voltar para activity anterior
//                    finish()
//                }
//            }.start()
//        }
//    }

}
