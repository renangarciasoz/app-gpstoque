package br.com.gpssa.gpstoque

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.squareup.picasso.Picasso

// define o construtor que recebe a lista de uniforms e o evento de clique
class UniformAdapter (
    val uniforms: List<Uniform>,
    val onClick: (Uniform) -> Unit): RecyclerView.Adapter<UniformAdapter.UniformsViewHolder>() {

    // ViewHolder com os elemetos da tela
    class UniformsViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val cardNome: TextView
        val cardImg : ImageView
        var cardProgress: ProgressBar
        var cardView: CardView

        init {
            cardNome = view.findViewById<TextView>(R.id.cardNome)
            cardImg = view.findViewById<ImageView>(R.id.cardImg)
            cardProgress = view.findViewById<ProgressBar>(R.id.cardProgress)
            cardView = view.findViewById<CardView>(R.id.card_uniforms)

        }

    }

    // Quantidade de uniforms na lista

    override fun getItemCount() = this.uniforms.size

    // inflar layout do adapter

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UniformsViewHolder {
        // infla view no adapter
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_uniform, parent, false)

        // retornar ViewHolder
        val holder = UniformsViewHolder(view)
        return holder
    }

    // bind para atualizar Views com os dados

    override fun onBindViewHolder(holder: UniformsViewHolder, position: Int) {
        val context = holder.itemView.context

        // recuperar objeto uniform
        val uniform = uniforms[position]

        // atualizar dados de uniform

        holder.cardNome.text = uniform.nome
        holder.cardProgress.visibility = View.VISIBLE

        // download da imagem
        Picasso.with(context).load(uniform.foto).fit().into(holder.cardImg,
                object: com.squareup.picasso.Callback{
                    override fun onSuccess() {
                        holder.cardProgress.visibility = View.GONE
                    }

                    override fun onError() {
                        holder.cardProgress.visibility = View.GONE
                    }
                })

        // adiciona evento de clique
        holder.itemView.setOnClickListener {onClick(uniform)}
    }
}