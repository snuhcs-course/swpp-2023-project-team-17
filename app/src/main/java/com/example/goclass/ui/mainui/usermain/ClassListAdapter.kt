/*
 * ClassListAdapter is a RecyclerView adapter responsible for displaying a list of classes.
 */

package com.example.goclass.ui.mainui.usermain

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.goclass.R
import com.example.goclass.databinding.ItemClassBinding
import com.example.goclass.network.dataclass.ClassesResponse
import com.example.goclass.ui.classui.ClassActivity

class ClassListAdapter(
    private val viewModel: ProfessorMainViewModel,
    private val userType: Int,
) : RecyclerView.Adapter<ClassListAdapter.ClassViewHolder>() {
    private var classList = listOf<ClassesResponse>()
    private var expandedPosition = -1

    /*
     * setClassList sets the list of classes to be displayed in the RecyclerView.
     *
     * @param list: List<ClassesResponse>, the list of classes.
     */
    fun setClassList(list: List<ClassesResponse>) {
        classList = list
        notifyDataSetChanged()
    }

    /*
     * onCreateViewHolder is called when RecyclerView needs a new ViewHolder of the given type to represent an item.
     *
     * @param parent: ViewGroup, the parent view.
     * @param viewType: Int, the type of the view.
     * @return ClassViewHolder, the created ViewHolder.
     */
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

    /*
     * onBindViewHolder is called to bind the data to the views inside the ViewHolder.
     *
     * @param holder: ClassViewHolder, the ViewHolder to bind the data.
     * @param position: Int, the position of the item in the data set.
     */
    @SuppressLint("NotifyDataSetChanged")
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

    /*
     * getItemCount returns the total number of items in the data set held by the adapter.
     *
     * @return Int, the total number of items.
     */
    override fun getItemCount() = classList.size

    /*
     * hideAllDeleteButtons hides all delete buttons in the RecyclerView items.
     */
    @SuppressLint("NotifyDataSetChanged")
    fun hideAllDeleteButtons() {
        expandedPosition = -1
        Log.d("nnn", expandedPosition.toString())
        notifyDataSetChanged()
    }

    /*
     * ClassViewHolder is a ViewHolder for the RecyclerView items.
     */
    class ClassViewHolder(
        private val binding: ItemClassBinding,
        private val viewModel: ProfessorMainViewModel,
        private val userType: Int,
    ) : RecyclerView.ViewHolder(binding.root) {

        /*
         * bind binds the data to the views inside the ViewHolder.
         *
         * @param classItem: ClassesResponse, the class item data.
         * @param isExpanded: Boolean, flag indicating whether the item is expanded.
         * @param onExpandChange: (Int) -> Unit, callback to handle item expansion changes.
         */
        @SuppressLint("SetTextI18n")
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
                    PropertyValuesHolder.ofFloat("translationX", 0f, 5f, -5f, 5f, -5f, 5f, -5f, 0f)
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
                val dialogView =
                    LayoutInflater.from(itemView.context).inflate(R.layout.dialog_delete_class, null)
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
