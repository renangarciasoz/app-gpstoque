package br.com.gpssa.gpstoque

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
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

        // criar intent
        val intent = Intent(context, ApplicationIndex::class.java)

        // colocar parâmetros para a mensagem(opcional)
        val params = Bundle()
        params.putString("nome", "impacta")
        intent.putExtras(params)

        Thread {
            // Código para procurar iniciar o login
            // que será executado em segundo plano / Thread separada

            val dataPost= "{\n" +
                    "    \"identifier\": \"$identifierValue\",\n" +
                    "    \"password\": \"$passwordValue\"\n" +
                    "}"

            val login = HttpHelper.post("https://gpstoque-api.herokuapp.com/auth/local", dataPost)

            runOnUiThread {
                // enviar parâmetros simplificado
                intent.putExtra("numero", 10)

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
}
