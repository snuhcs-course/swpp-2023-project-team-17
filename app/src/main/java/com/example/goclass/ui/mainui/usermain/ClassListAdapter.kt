package com.example.goclass.ui.mainui.usermain

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.goclass.ui.classui.ClassActivity
import com.example.goclass.databinding.ItemClassBinding
import com.example.goclass.network.dataclass.ClassesResponse

class ClassListAdapter(
    private val viewModel: ProfessorMainViewModel,
    private val userType: Int,
) : RecyclerView.Adapter<ClassListAdapter.ClassViewHolder>() {
    private var classList = listOf<ClassesResponse>()

    fun setClassList(list: List<ClassesResponse>) {
        classList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ClassViewHolder {
        val binding =
            ItemClassBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
        return ClassViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ClassViewHolder,
        position: Int,
    ) {
        val classItem = classList[position]
        holder.bind(classItem, viewModel, userType)
    }

    override fun getItemCount() = classList.size

    class ClassViewHolder(private val binding: ItemClassBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            classItem: ClassesResponse,
            viewModel: ProfessorMainViewModel,
            userType: Int,
        ) {
            binding.classNameTextView.text = classItem.className
            binding.classNameTextView.setOnClickListener {
                val intent = Intent(itemView.context, ClassActivity::class.java)
                intent.putExtra("classId", classItem.classId)
                intent.putExtra("className", classItem.className)
                ContextCompat.startActivity(itemView.context, intent, null)
            }
            binding.deleteButton.visibility = if (userType == 1) View.VISIBLE else View.GONE
            binding.dummyButton.visibility = if (userType == 1) View.VISIBLE else View.GONE

            binding.deleteButton.setOnClickListener {
                viewModel.deleteClass(classItem.classId, classItem.professorId)
            }
        }
    }
}
