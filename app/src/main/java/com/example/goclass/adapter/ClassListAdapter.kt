package com.example.goclass.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.goclass.ClassActivity
import com.example.goclass.dataClass.Classes
import com.example.goclass.databinding.ActivityClassBinding
import com.example.goclass.databinding.ItemClassBinding
import okhttp3.internal.notify

class ClassListAdapter : RecyclerView.Adapter<ClassListAdapter.ClassViewHolder>() {

    private var classList = listOf<Classes>()

    fun setClassList(list: List<Classes>){
        classList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ClassListAdapter.ClassViewHolder {
        val binding = ItemClassBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ClassViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ClassListAdapter.ClassViewHolder, position: Int) {
        val classItem = classList[position]
        holder.bind(classItem)
    }

    override fun getItemCount() = classList.size

    class ClassViewHolder(private val binding: ItemClassBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(classItem: Classes) {
            binding.classNameTextView.text = classItem.className
            itemView.setOnClickListener{
                val intent = Intent(itemView.context, ClassActivity::class.java)
                intent.putExtra("userRole", "professor")
                ContextCompat.startActivity(itemView.context, intent, null)
            }
        }
    }
}