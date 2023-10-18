package com.example.goclass.main_ui

import android.app.Dialog
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

class ProfessorMainFragment : Fragment(R.layout.fragment_professor_main) {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_professor_main, container, false)

        val createButton = view.findViewById<Button>(R.id.createButton)

        createButton.setOnClickListener {
            val dialog = Dialog(requireContext())
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.dialog_create)

            val editCode = dialog.findViewById<EditText>(R.id.codeEdittext)
            val editClassName = dialog.findViewById<EditText>(R.id.classNameEdittext)
            val createButtonDialog = dialog.findViewById<Button>(R.id.createButton)

            createButtonDialog.setOnClickListener {
                val enteredCode = editCode.text.toString()
                val enteredClassName = editClassName.text.toString()
                dialog.dismiss()
            }
            dialog.show()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Profile Button
        val profileButton = view.findViewById<Button>(R.id.profileButton)
        profileButton.setOnClickListener {
            findNavController().navigate(R.id.action_professorMainFragment_to_profileFragment)
        }

        // Class Button
        val classButton = view.findViewById<Button>(R.id.classButton)
        classButton.setOnClickListener {
            val intent = Intent(view.context, ClassActivity::class.java)
            intent.putExtra("userRole", "professor")
            startActivity(intent)
        }
    }
}
