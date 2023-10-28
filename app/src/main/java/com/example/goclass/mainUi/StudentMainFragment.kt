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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.goclass.ClassActivity
import com.example.goclass.R
import com.example.goclass.adapter.ClassListAdapter
import com.example.goclass.databinding.FragmentStudentMainBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class StudentMainFragment : Fragment() {
    private lateinit var binding: FragmentStudentMainBinding
    private val viewModel: StudentMainViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentStudentMainBinding.inflate(inflater, container, false)
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
        val userId = sharedPref!!.getInt("userId", -1)

        viewModel.toastMessage.observe(viewLifecycleOwner) { message ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }

        // Join Button
        binding.joinButton.setOnClickListener {
            val dialog = Dialog(requireContext())
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.dialog_join)

            val editCode = dialog.findViewById<EditText>(R.id.codeEditText)
            val editName = dialog.findViewById<EditText>(R.id.nameEditText)
            val joinButtonDialog = dialog.findViewById<Button>(R.id.joinButton)

            joinButtonDialog.setOnClickListener {
                val enteredCode = editCode.text.toString()
                val enteredName = editName.text.toString()

                viewModel.classJoin(userId, enteredName, enteredCode)

                dialog.dismiss()
            }

            dialog.show()
        }

        // show classList with dummy data
        val userMap = mapOf("userId" to "1", "userType" to "0")
        val classListLiveData = viewModel.getClassList(userMap)
        val classListAdapter = ClassListAdapter()
        binding.studentClassRecyclerView.adapter = classListAdapter
        binding.studentClassRecyclerView.layoutManager = LinearLayoutManager(requireContext())

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
            findNavController().navigate(R.id.action_studentMainFragment_to_profileFragment)
        }

        // recyclerView
        binding.studentClassRecyclerView.setOnClickListener {
            val intent = Intent(view.context, ClassActivity::class.java)
            intent.putExtra("userRole", "student")
            startActivity(intent)
        }
    }
}
