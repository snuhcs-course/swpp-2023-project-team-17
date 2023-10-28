package com.example.goclass.mainUi

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.goclass.ClassActivity
import com.example.goclass.R
import com.example.goclass.adapter.ClassListAdapter
import com.example.goclass.dataClass.Users
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

        viewModel.toastMessage.observe(viewLifecycleOwner) { message ->
            Toast.makeText(requireActivity().applicationContext, message, Toast.LENGTH_SHORT).show()
        }

        binding.createButton.setOnClickListener {
            val dialog = Dialog(requireContext())
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.dialog_create)

            val editCode = dialog.findViewById<EditText>(R.id.codeEdittext)
            val editClassName = dialog.findViewById<EditText>(R.id.classNameEdittext)
            val editClassTime = dialog.findViewById<EditText>(R.id.classTimeEdittext)
            val editBuildingNumber = dialog.findViewById<EditText>(R.id.buildingNumberEdittext)
            val editRoomNumber = dialog.findViewById<EditText>(R.id.roomNumberEditText)
            val createButtonDialog = dialog.findViewById<Button>(R.id.createButton)

            createButtonDialog.setOnClickListener {
                val enteredCode = editCode.text.toString()
                val enteredClassName = editClassName.text.toString()
                val enteredClassTime = editClassTime.text.toString()
                val enteredBuildingNumber = editBuildingNumber.text.toString()
                val enteredRoomNumber = editRoomNumber.text.toString()

                viewModel.createClass(enteredClassName, enteredCode, userId, enteredClassTime, enteredBuildingNumber, enteredRoomNumber)

                dialog.dismiss()
            }
            dialog.show()
        }

        // show classList with dummy data
        val userMap = mapOf("userId" to "2", "userType" to "1")
        val classListLiveData = viewModel.getClassList(userMap)
        val classListAdapter = ClassListAdapter()
        binding.professorClassRecyclerView.adapter = classListAdapter
        binding.professorClassRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        classListLiveData.observe(viewLifecycleOwner) { classList ->
            classListAdapter.setClassList(classList)
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

        // recyclerView
        binding.professorClassRecyclerView.setOnClickListener {
            val intent = Intent(view.context, ClassActivity::class.java)
            intent.putExtra("userRole", "professor")
            startActivity(intent)
        }
    }
}
