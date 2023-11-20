package com.example.goclass.ui.mainui.usermain

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.goclass.R
import com.example.goclass.ui.classui.ClassActivity
import com.example.goclass.databinding.ItemClassBinding
import com.example.goclass.network.dataclass.ClassesResponse

class ClassListAdapter(
    private val viewModel: ProfessorMainViewModel,
    private val userType: Int,
) : RecyclerView.Adapter<ClassListAdapter.ClassViewHolder>() {
    private var classList = listOf<ClassesResponse>()
    private var expandedPosition = -1

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
        return ClassViewHolder(binding, viewModel, userType)
    }

    override fun onBindViewHolder(
        holder: ClassViewHolder,
        position: Int,
    ) {
        val classItem = classList[position]
        holder.bind(classItem, position == expandedPosition) {
            expandedPosition = if (expandedPosition == it) -1 else it
            notifyDataSetChanged()
        }
    }

    override fun getItemCount() = classList.size

    @SuppressLint("NotifyDataSetChanged")
    fun hideAllDeleteButtons() {
        expandedPosition = -1
        Log.d("nnn", expandedPosition.toString())
        notifyDataSetChanged()
    }

    class ClassViewHolder(
        private val binding: ItemClassBinding,
        private val viewModel: ProfessorMainViewModel,
        private val userType: Int,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            classItem: ClassesResponse,
            isExpanded: Boolean,
            onExpandChange: (Int) -> Unit,
        ) {
            binding.classNameTextView.text = classItem.className

            binding.deleteButton.visibility = if (isExpanded && userType == 1) View.VISIBLE else View.GONE

            if (isExpanded && userType == 1) {
                val shake = ObjectAnimator.ofPropertyValuesHolder(
                    itemView,
                    PropertyValuesHolder.ofFloat("translationX",0f, 5f, -5f, 5f, -5f, 5f, -5f, 0f)
                )
                shake.duration = 500
                shake.start()
            }

            binding.classNameTextView.setOnClickListener {
                val intent = Intent(itemView.context, ClassActivity::class.java)
                intent.putExtra("classId", classItem.classId)
                intent.putExtra("className", classItem.className)
                ContextCompat.startActivity(itemView.context, intent, null)
            }

            binding.classNameTextView.setOnLongClickListener {
                if (userType == 1) {
                    onExpandChange(adapterPosition)
                }
                true
            }

            binding.deleteButton.setOnClickListener {
                val dialogView = LayoutInflater.from(itemView.context).inflate(R.layout.dialog_delete_class, null)
                val deleteDialog = AlertDialog.Builder(itemView.context)
                    .setView(dialogView)
                    .create()

                deleteDialog.window?.setBackgroundDrawableResource(R.drawable.dialog_bg)

                val yesButton = dialogView.findViewById<Button>(R.id.yesButton)
                val noButton = dialogView.findViewById<Button>(R.id.noButton)
                val checkText = dialogView.findViewById<TextView>(R.id.checkText)

                checkText.text = "Are you sure you want to delete class \"${classItem.className}\"?"

                yesButton.setOnClickListener {
                    viewModel.deleteClass(classItem.classId, classItem.professorId)
                    deleteDialog.dismiss()
                }

                noButton.setOnClickListener {
                    deleteDialog.dismiss()
                }

                deleteDialog.show()
            }
        }
    }
}
