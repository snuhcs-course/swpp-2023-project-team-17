package com.example.goclass.mainUi

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
import com.example.goclass.databinding.FragmentStudentMainBinding

class StudentMainFragment : Fragment() {
    private lateinit var binding: FragmentStudentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentStudentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        // Join Button
        binding.joinButton.setOnClickListener {
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

        // Profile Button
        binding.profileButton.setOnClickListener {
            findNavController().navigate(R.id.action_studentMainFragment_to_profileFragment)
        }

        // Class Button
        binding.classButton.setOnClickListener {
            val intent = Intent(view.context, ClassActivity::class.java)
            intent.putExtra("userRole", "student")
            startActivity(intent)
        }
    }
}
