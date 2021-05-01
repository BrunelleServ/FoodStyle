package fr.isen.brunelleservat.foodstyle2



import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Dish(

    @SerializedName("name_fr") val title: String,
    @SerializedName("images")  val images: List<String>,
    @SerializedName("ingredients") val ingredients: List<Ingredient>,
    @SerializedName("prices") val prices: List<Price>
): Serializable {

    fun getAffichagePrice() = prices[0].price
    fun getPrice() = prices[0].price.toDouble()

    fun getFirstPicture() = if (images.isNotEmpty() && images[0].isNotEmpty()){
        images[0]
    }else {
        null
    }
    fun getIngredients(): String = ingredients.map(Ingredient::name).joinToString(", ")


}

