package br.com.gpssa.gpstoque

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.*
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast

class ApplicationIndex : DebugActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val context: Context get() = this
    private var uniforms = listOf<Uniform>()
    var recyclerUniforms: RecyclerView? = null
    private var REQUEST_CADASTRO = 1
    private var REQUEST_REMOVE= 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_application_index)

        // acessar parametros da intnet
        // intent é um atributo herdado de Activity
        val args:Bundle? = intent.extras

        // recuperar o parâmetro do tipo String
        val nome = args?.getString("nome")

        // recuperar parâmetro simplificado
        val numero = intent.getIntExtra("nome",0)

        Toast.makeText(context, "Parâmetro: $nome", Toast.LENGTH_LONG).show()
        Toast.makeText(context, "Numero: $numero", Toast.LENGTH_LONG).show()

        // colocar toolbar
        var toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        // alterar título da ActionBar
        supportActionBar?.title = "Uniformes"

        // up navigation
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        initMenu()

        // configurar cardview
        recyclerUniforms = findViewById<RecyclerView>(R.id.recyclerUniforms)
        recyclerUniforms?.layoutManager = LinearLayoutManager(context)
        recyclerUniforms?.itemAnimator = DefaultItemAnimator()
        recyclerUniforms?.setHasFixedSize(true)

    }

    override fun onResume() {
        super.onResume()

        taskUniforms()
    }

    fun taskUniforms() {

        // Criar a Thread
        Thread {
            // Código para procurar as uniforms
            // que será executado em segundo plano / Thread separada
            this.uniforms = UniformService.getUniforms(context)
            runOnUiThread {
                // Código para atualizar a UI com a lista de uniforms
                recyclerUniforms?.adapter = UniformAdapter(this.uniforms) { onClickUniform(it) }
                // enviar notificação
                enviaNotificacao(this.uniforms.get(0))

            }
        }.start()

    }

    fun enviaNotificacao(uniform: Uniform) {
        // Intent para abrir tela quando clicar na notificação
        val intent = Intent(this, UniformActivity::class.java)
        // parâmetros extras
        intent.putExtra("disciplina", uniform)
        // Disparar notificação
        NotificationUtil.create(this, 1, intent, "GPSApp", "Você tem nova atividade na ${uniform.nome}")
    }

    // tratamento do evento de clicar em uma uniform
    fun onClickUniform(uniform: Uniform) {
        Toast.makeText(context, "Clicou uniform ${uniform.nome}", Toast.LENGTH_SHORT).show()
        val intent = Intent(context, UniformActivity::class.java)
        intent.putExtra("uniform", uniform)
        startActivityForResult(intent, REQUEST_REMOVE)
    }

    // configuraçao do navigation Drawer com a toolbar
    private fun initMenu() {
        var toolbar = findViewById<Toolbar>(R.id.toolbar)
        var menuLateral = findViewById<DrawerLayout>(R.id.layoutMenuLateral)

        // ícone de menu (hamburger) para mostrar o menu
        var toogle = ActionBarDrawerToggle(this, menuLateral, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)

        menuLateral.addDrawerListener(toogle)
        toogle.syncState()

        val navigationView = findViewById<NavigationView>(R.id.menu_lateral)
        navigationView.setNavigationItemSelectedListener(this)
    }

    // método que deve ser implementado quando a activity implementa a interface NavigationView.OnNavigationItemSelectedListener
    // para tratar os eventos de clique no menu lateral
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
//            R.id.nav_dashboard -> {
//                supportActionBar?.title = item.title
//            }
//            R.id.nav_requests -> {
//                supportActionBar?.title = item.title
//            }
//            R.id.nav_devolutions -> {
//                supportActionBar?.title = item.title
//            }
            R.id.nav_uniforms -> {
                supportActionBar?.title = item.title
            }
//            R.id.nav_orders -> {
//                supportActionBar?.title = item.title
//            }
//            R.id.nav_customers -> {
//                supportActionBar?.title = item.title
//            }
//            R.id.nav_providers -> {
//                supportActionBar?.title = item.title
//            }
            R.id.nav_exit -> {
                val returnIntent = Intent()
                returnIntent.putExtra("result","")
                setResult(Activity.RESULT_OK,returnIntent)
                finish()
            }
        }

        // fecha menu depois de tratar o evento
        val drawer = findViewById<DrawerLayout>(R.id.layoutMenuLateral)
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    // método sobrescrito para inflar o menu na Actionbar
    @RequiresApi(Build.VERSION_CODES.HONEYCOMB)
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        // infla o menu com os botões da ActionBar
        menuInflater.inflate(R.menu.menu_main, menu)
        // vincular evento de buscar
        (menu?.findItem(R.id.action_buscar)?.actionView as SearchView).setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                // ação enquanto está digitando
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                // ação  quando terminou de buscar e enviou
                return false
            }

        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        // id do item clicado
        val id = item?.itemId
        // verificar qual item foi clicado e mostrar a mensagem Toast na tela
        // a comparação é feita com o recurso de id definido no xml
        if  (id == R.id.action_buscar) {
            Toast.makeText(context, "Botão de buscar", Toast.LENGTH_LONG).show()
        } else if (id == R.id.action_atualizar) {
            Toast.makeText(context, "Botão de atualizar", Toast.LENGTH_LONG).show()
        } else if (id == R.id.action_config) {
            Toast.makeText(context, "Botão de configuracoes", Toast.LENGTH_LONG).show()
        } else if (id == R.id.action_adicionar) {
            // iniciar activity de cadastro
            val intent = Intent(context, UniformCadastroActivity::class.java)
            startActivityForResult(intent, REQUEST_CADASTRO)
        }
        // botão up navigation
        else if (id == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
    // esperar o retorno do cadastro da uniform
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CADASTRO || requestCode == REQUEST_REMOVE ) {
            // atualizar lista de uniforms
            taskUniforms()
        }
    }
}
