package fr.isen.brunelleservat.foodstyle2


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import fr.isen.brunelleservat.foodstyle2.databinding.CellCategoryBinding


class CategoryAdapter(private val categories: List<Dish>, private val clickListener: onItemClickListener) :
        RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>(){


    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ): CategoryViewHolder {

        val binding = CellCategoryBinding
                .inflate(LayoutInflater.from(parent.context),parent, false)

        return CategoryViewHolder(binding.root)
    }

    override fun getItemCount(): Int = categories.size


    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val dish = categories[position]
        holder.layout.setOnClickListener{
            clickListener.onItemClicked(dish)
        }
        if (dish.getFirstPicture().isNullOrEmpty()){
            Picasso.get()
                .load("https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.commentcamarche.net%2Fapplis-sites%2Fservices-en-ligne%2F729-faire-une-recherche-a-partir-d-une-image-sur-google%2F&psig=AOvVaw0lQTqL8i4mwkPL5ie4sDRL&ust=1617218775377000&source=images&cd=vfe&ved=2ahUKEwjFjqjG39jvAhURxuAKHaDPCUUQjRx6BAgAEAc")
                .into (holder.images)
        }
        else {
            Picasso.get().load(dish.getFirstPicture())
                .into(holder.images)
        }
        holder.bind(dish)
    }

    inner class CategoryViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val title: TextView = view.findViewById(R.id.cellCategoryTitle)
        val layout = view.findViewById<View>(R.id.celLayout)
        val images: ImageView = view.findViewById(R.id.imageView)
        val prices: TextView = view.findViewById(R.id.prix)

        fun bind(dish: Dish){
            title.text = categories[position].title
            prices.text = "${dish.prices.first().price}"
            Picasso.get()
                    .load(dish.getFirstPicture())
                    .into(images)
        }
    }
    interface onItemClickListener {
        fun onItemClicked(item: Dish)
    }

}