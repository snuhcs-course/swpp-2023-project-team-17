package com.example.goclass.mainUi

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.goclass.ClassActivity
import com.example.goclass.R
import com.example.goclass.databinding.FragmentProfessorMainBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfessorMainFragment : Fragment() {
    private lateinit var binding: FragmentProfessorMainBinding
    private val viewModel: ProfessorMainViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentProfessorMainBinding.inflate(inflater, container, false)

        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
        val userId = sharedPref!!.getInt("userId", -1)

        binding.createButton.setOnClickListener {
            val dialog = Dialog(requireContext())
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.dialog_create)

            val editCode = dialog.findViewById<EditText>(R.id.codeEdittext)
            val editClassName = dialog.findViewById<EditText>(R.id.classNameEdittext)
            val editClassTime = dialog.findViewById<EditText>(R.id.classTimeEdittext)
            val createButtonDialog = dialog.findViewById<Button>(R.id.createButton)

            createButtonDialog.setOnClickListener {
                val enteredCode = editCode.text.toString()
                val enteredClassName = editClassName.text.toString()
                val enteredClassTime = editClassTime.text.toString()

                viewModel.createClass(enteredClassName, enteredCode, userId, enteredClassTime, "301", "311")

                dialog.dismiss()
            }
            dialog.show()
        }

        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        // Profile Button
        binding.profileButton.setOnClickListener {
            findNavController().navigate(R.id.action_professorMainFragment_to_profileFragment)
        }

        // Class Button
        binding.classButton.setOnClickListener {
            val intent = Intent(view.context, ClassActivity::class.java)

            startActivity(intent)
        }
    }
}
