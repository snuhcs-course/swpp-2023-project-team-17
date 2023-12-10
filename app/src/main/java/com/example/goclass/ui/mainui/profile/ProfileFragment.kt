/*
 * ProfileFragment is a Fragment class responsible for managing the user profile settings.
 *
 * @property binding: FragmentProfileBinding, the ViewBinding instance for the profile fragment.
 * @property viewModel: ProfileViewModel, the ViewModel handling the logic for the profile fragment.
 * @property googleSignInClient: GoogleSignInClient, the Google Sign-In client for user authentication.
 * @property permissionUtils: PermissionUtils, utility class for handling runtime permissions.
 *
 * onCreateView: Initializes the ViewBinding and checks for necessary runtime permissions.
 * onViewCreated: Sets up UI elements and observes ViewModel changes for updating the UI.
 * observeViewModel: Observes ViewModel changes and displays appropriate snackbar messages.
 * showSnackbar: Displays a snackbar with the given message and background color.
 */

package com.example.goclass.ui.mainui.profile

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.goclass.R
import com.example.goclass.databinding.FragmentProfileBinding
import com.example.goclass.ui.mainui.profile.utils.RadioButtonsUtils
import com.example.goclass.ui.mainui.profile.utils.SharedPrefsUtils
import com.example.goclass.ui.mainui.profile.utils.SnackBarUtils
import com.example.goclass.utility.PermissionUtils
import com.example.goclass.utility.SnackbarBuilder
import com.example.goclass.utility.StatusCheckUtils
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.Duration


class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private val viewModel: ProfileViewModel by viewModel()
    private lateinit var googleSignInClient: GoogleSignInClient

    private lateinit var permissionUtils: PermissionUtils

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding =
            FragmentProfileBinding.inflate(
                inflater,
                container,
                false,
            )

        permissionUtils = PermissionUtils(requireContext())
        if (!permissionUtils.checkPermissions()) {
            permissionUtils.showPermissionDeniedDialog()
        }


        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(
            view,
            savedInstanceState,
        )

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        val storedUserName = SharedPrefsUtils.get(requireContext(), "userName", "") as String
        val storedUserRole = SharedPrefsUtils.get(requireContext(), "userRole", "") as String

        if (storedUserName.isNotEmpty()) {
            binding.nameEditText.setText(storedUserName)
        } else {
            val userId = SharedPrefsUtils.get(requireContext(), "userId", -1) as Int
            viewModel.userGet(userId)
            viewModel.userName.observe(
                viewLifecycleOwner,
                Observer { receivedUserName ->
                    binding.nameEditText.setText(receivedUserName)
                    SharedPrefsUtils.save(requireContext(),"userName", receivedUserName)
                },
            )
        }

        RadioButtonsUtils.restoreRadioButtonState(binding, storedUserRole)

        viewModel.editSuccess.observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess) {
                val selectedRole = RadioButtonsUtils.getSelectedRole(binding)
                selectedRole?.let {
                    SharedPrefsUtils.save(requireContext(),"userRole", it)
                    when (it) {
                        "student" -> {
                            findNavController().navigate(R.id.action_profileFragment_to_studentMainFragment)
                        }
                        "professor" -> {
                            findNavController().navigate(R.id.action_profileFragment_to_professorMainFragment)
                        }
                    }
                }
            }
        }

        binding.professorRadioButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.studentRadioButton.isChecked = false
            }
        }

        binding.studentRadioButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.professorRadioButton.isChecked = false
            }
        }

        // Logout Button
        binding.logoutButton.setOnClickListener {
            val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_logout, null)
            val alertDialog = AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .create()

            alertDialog.window?.setBackgroundDrawableResource(R.drawable.dialog_bg)

            dialogView.findViewById<AppCompatButton>(R.id.yesButton).setOnClickListener {
                SharedPrefsUtils.clear(requireContext())
                googleSignInClient.signOut().addOnCompleteListener {
                    findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
                }
                alertDialog.dismiss()
            }

            dialogView.findViewById<AppCompatButton>(R.id.noButton).setOnClickListener {
                alertDialog.dismiss()
            }

            alertDialog.show()
        }

        // Confirm Button
        binding.confirmButton.setOnClickListener {
            val selectedRole = RadioButtonsUtils.getSelectedRole(binding)
            val userId = SharedPrefsUtils.get(requireContext(), "userId", -1) as Int
            val userType =
                when (selectedRole) {
                    "student" -> 0
                    "professor" -> 1
                    else -> null
                }
            val userName = binding.nameEditText.text.toString()

            if (userType == null) {
                showSnackbar("Please select your role.", R.color.red)
            } else if (userName == "") {
                showSnackbar("Please enter your name.", R.color.red)
            } else {
                SharedPrefsUtils.save(requireContext(), "userName", userName)
                viewModel.userEdit(userId, userType, userName)
            }
        }


        // Keyboard down when you touch other space in screen
        binding.root.setOnTouchListener { _, _ ->
            val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
            binding.nameEditText.clearFocus()
            false
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.editSuccess.observe(viewLifecycleOwner) { isSuccess ->
            if (!isSuccess) {
                showSnackbar("Server Error: Check your network connection.", R.color.red)
            }
        }
    }
    private fun showSnackbar(message: String, colorResId: Int) {
        SnackbarBuilder(binding.root)
            .setMessage(message)
            .setBackgroundColor(colorResId)
            .build()
            .show()
    }
}
