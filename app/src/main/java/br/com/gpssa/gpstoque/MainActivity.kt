package br.com.gpssa.gpstoque

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import br.com.gpssa.gpstoque.UniformService.parserJson
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.login.*

class MainActivity : DebugActivity() {

    private val context: Context get() = this
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        val btnLogin = findViewById<Button>(R.id.btnLogin)

        btnLogin.setOnClickListener(View.OnClickListener { onClickLogin() })

        progressBar.visibility = View.INVISIBLE

        // procurar pelas preferências, se pediu para lembrar os dados de acesso.
        var remind = Prefs.getBoolean("remind")
        if (remind) {
            var remindIdentifier  = Prefs.getString("remindIdentifier")
            var remindPassword  = Prefs.getString("remindPassword")
            identifierLogin.setText(remindIdentifier)
            passwordLogin.setText(remindPassword)
            remindLogin.isChecked = remind

        }
    }

    private fun onClickLogin(){
        val identifierValue = identifierLogin.text.toString()
        val passwordValue = passwordLogin.text.toString()

        if (identifierValue == "" || passwordValue == "") {
            Toast.makeText(context, "Verifique os campos de acesso em branco", Toast.LENGTH_LONG).show()
            return
        }

        // armazenar valor do checkbox
        Prefs.setBoolean("remind", remindLogin.isChecked)

        // verificar se é para pra lembrar os dados de acesso
        if (remindLogin.isChecked) {
            Prefs.setString("remindIdentifier", identifierValue)
            Prefs.setString("remindPassword", passwordValue)
        } else {
            Prefs.setString("remindIdentifier", "")
            Prefs.setString("remindPassword", "")
        }

        progressBar.visibility = View.VISIBLE

        Thread {
            // Código para procurar iniciar o login
            // que será executado em segundo plano / Thread separada

            val dataPost= "{\n" +
                    "    \"identifier\": \"$identifierValue\",\n" +
                    "    \"password\": \"$passwordValue\"\n" +
                    "}"

            val json = HttpHelper.post("https://gpstoque-api.herokuapp.com/auth/local", dataPost)

            // criar intent
            val intent = Intent(context, ApplicationIndex::class.java)

            runOnUiThread {
                progressBar.visibility = View.INVISIBLE

                // enviar parâmetros simplificado
                intent.putExtra("nome", "Fulano(a)")

                // Fazer chamada se o login e senha for igual ao esperado.
                startActivityForResult(intent, 1)
            }
        }.start()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1) {
            val result = data?.getStringExtra("result")
            Toast.makeText(context, "$result", Toast.LENGTH_LONG).show()
        }
    }

    override fun onResume() {
        super.onResume()
        // abrir a disciplina caso clique na notificação com o aplicativo fechado
        abrirUniform()
        // mostrar no log o tokem do firebase
        Log.d("firebase", "Firebase Token: ${Prefs.getString("FB_TOKEN")}")
    }

    fun abrirUniform() {
        // verificar se existe id do uniform na intent
        if (intent.hasExtra("uniform_Id")) {
            Thread {
                var uniformCode = intent.getStringExtra("uniformCode")?.toInt()!!
                var uniformId = intent.getStringExtra("uniform_Id")?.toString()!!
                val uniform = UniformService.getUniform(this, uniformId, uniformCode)
                runOnUiThread {
                    val intentUniform = Intent(this, UniformActivity::class.java)
                    intentUniform.putExtra("uniform", uniform)
                    startActivity(intentUniform)
                }
            }.start()
        }
    }
}
