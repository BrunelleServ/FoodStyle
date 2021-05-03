package fr.isen.brunelleservat.foodstyle2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.squareup.picasso.Picasso
import com.synnapps.carouselview.ImageListener
import fr.isen.brunelleservat.foodstyle2.databinding.ActivityDetailBinding


class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater) // test
        val dataItems = intent.getSerializableExtra("items") as? Dish
        setContentView(binding.root)

        if (dataItems != null) {
            binding.titlePlats.text = dataItems.title
            binding.titrePrix.text = dataItems.getAffichagePrice()
            binding.ingredients.text = dataItems.getIngredients()
            binding.totalPrix.text = dataItems.getAffichagePrice()

        }

        var quantity = 0
        if (dataItems != null) {
            calculTotal(quantity, dataItems)
        }

        // Bouton + plus
        binding.boutonPlus.setOnClickListener {
            quantity++
            binding.quantite.text = quantity.toString()
            if (dataItems != null) {
                calculTotal(quantity, dataItems)
            }
        }

        // Bouton - moins
        binding.boutonMoins.setOnClickListener {
            if (quantity > 0)
                quantity--
            binding.quantite.text = quantity.toString()
            if (dataItems != null) {
                calculTotal(quantity, dataItems)
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
