package com.example.goclass.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.goclass.ClassActivity
import com.example.goclass.dataClass.Classes
import com.example.goclass.databinding.ItemClassBinding

class ClassListAdapter : RecyclerView.Adapter<ClassListAdapter.ClassViewHolder>() {
    private var classList = listOf<Classes>()

    fun setClassList(list: List<Classes>) {
        classList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ClassListAdapter.ClassViewHolder {
        val binding =
            ItemClassBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
            false,
            )
        return ClassViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ClassListAdapter.ClassViewHolder,
        position: Int,
    ) {
        val classItem = classList[position]
        holder.bind(classItem)
    }

    override fun getItemCount() = classList.size

    class ClassViewHolder(private val binding: ItemClassBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(classItem: Classes) {
            binding.classNameTextView.text = classItem.className
            binding.classNameTextView.setOnClickListener {
                Log.d("ClassViewHolder", "Item clicked!")

                val intent = Intent(itemView.context, ClassActivity::class.java)
                intent.putExtra("classId", classItem.classId)
                intent.putExtra("className", classItem.className)
                ContextCompat.startActivity(itemView.context, intent, null)
            }
        }
    }
}
