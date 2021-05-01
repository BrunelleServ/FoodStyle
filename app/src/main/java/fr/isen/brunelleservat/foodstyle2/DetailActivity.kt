package fr.isen.brunelleservat.foodstyle2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle


import android.widget.ImageView
import com.squareup.picasso.Picasso
import fr.isen.brunelleservat.foodstyle2.databinding.ActivityDetailBinding


class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        val dataItem = intent.getSerializableExtra("items") as? Dish
        setContentView(binding.root)

        if (dataItem != null) {
            binding.titlePlats.text = dataItem.title
            binding.titrePrix.text = dataItem.getAffichagePrice()
            binding.detailsDesPlats.text = dataItem.getIngredients()
            binding.totalPrix.text = dataItem.getAffichagePrice()
        }

        var quantity = 0
        if (dataItem != null) {
            calculTotal(quantity, dataItem)
        }

        // Bouton + plus
        binding.boutonPlus.setOnClickListener {
            quantity++
            binding.quantite.text = quantity.toString()
            if (dataItem != null) {
                calculTotal(quantity, dataItem)
            }
        }

        // Bouton - moins
        binding.boutonMoins.setOnClickListener {
            if (quantity > 0)
                quantity--
            binding.quantite.text = quantity.toString()
            if (dataItem != null) {
                calculTotal(quantity, dataItem)
            }
        }

    }
    private fun calculTotal(quantity: Int, itemPricedata: Dish) {
        val total = quantity * itemPricedata.getPrice()
        "Total : $total €".also {
            binding.totalPrix.text = it
        }
    }

}
