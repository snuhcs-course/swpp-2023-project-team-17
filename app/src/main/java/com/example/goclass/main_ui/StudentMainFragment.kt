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
import com.example.goclass.databinding.ActivityClassBinding
import com.example.goclass.databinding.FragmentStudentMainBinding

class StudentMainFragment : Fragment(R.layout.fragment_student_main) {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_student_main, container, false)

        val joinButton = view.findViewById<Button>(R.id.joinButton)

        joinButton.setOnClickListener {
            val dialog = Dialog(requireContext())
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.dialog_join)

            val editCode = dialog.findViewById<EditText>(R.id.codeEdittext)
            val joinButtonDialog = dialog.findViewById<Button>(R.id.joinButton)

            joinButtonDialog.setOnClickListener {
                val enteredCode = editCode.text.toString()
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
            findNavController().navigate(R.id.action_studentMainFragment_to_profileFragment)
        }

        // Class Button
        val classButton = view.findViewById<Button>(R.id.classButton)
        classButton.setOnClickListener {
            val intent = Intent(view.context, ClassActivity::class.java)
            intent.putExtra("userRole", "student")
            startActivity(intent)
        }
    }
}
