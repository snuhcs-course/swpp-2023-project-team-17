package com.example.goclass.view

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.goclass.R

class ProfessorMainFragment : Fragment(R.layout.fragment_professor_main) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Profile Button
        val profileButton = view.findViewById<Button>(R.id.profileButton)
        profileButton.setOnClickListener {
            findNavController().navigate(R.id.action_professorMainFragment_to_profileFragment)
        }
    }
}
