/*
 * LoginFragment is a Fragment responsible for handling user authentication via Google Sign-In.
 *
 * @property _binding: FragmentLoginBinding?, nullable binding for the Fragment's layout.
 * @property binding: FragmentLoginBinding, non-nullable binding for the Fragment's layout.
 * @property viewModel: LoginViewModel, the associated ViewModel for handling the authentication logic.
 * @property googleSignInClient: GoogleSignInClient, client for Google Sign-In.
 * @property signINLauncher: ActivityResultLauncher for handling the result of Google Sign-In.
 *
 * onCreateView: Inflates the layout for this fragment.
 * onViewCreated: Called immediately after onCreateView. Initializes UI elements and sets up the Google Sign-In client.
 * signInWithGoogle: Initiates the Google Sign-In process.
 * firebaseAuthWithGoogle: Authenticates the Google Sign-In result with Firebase authentication.
 * showSnackbar: Displays a Snackbar with a given message and background color.
 * onDestroyView: Called when the view is about to be destroyed. Cleans up the binding.
 */

package com.example.goclass.ui.mainui.login

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.goclass.R
import com.example.goclass.databinding.FragmentLoginBinding
import com.example.goclass.utility.StatusCheckUtils
import com.example.goclass.utility.SnackbarBuilder
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LoginViewModel by viewModel()

    private lateinit var googleSignInClient: GoogleSignInClient
    private val signINLauncher =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    val account = task.getResult(ApiException::class.java)
                    firebaseAuthWithGoogle(account!!)
                } catch (e: ApiException) {
                    Log.d("signinlauncher", e.toString())
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        viewModel.userId.observe(viewLifecycleOwner) { userId ->
            if (userId != null) {
                val sharedPref = activity?.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
                with(sharedPref!!.edit()) {
                    putBoolean("isLoggedIn", true)
                    putInt("userId", userId)
                    apply()
                }
                findNavController().navigate(R.id.action_loginFragment_to_profileFragment)
            }
            else {
                showSnackbar("Login Failed: Server error", R.color.red)
            }
        }

        // Login Button
        binding.loginButton.setOnClickListener {
            if (!StatusCheckUtils.isNetworkConnected(requireContext())) {
                showSnackbar("No Network Connection", R.color.red)
            } else {
                showSnackbar("Logging In...", R.color.black)
                signInWithGoogle()
            }
        }
    }

    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        signINLauncher.launch(signInIntent)
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                Log.d("loginaa", task.isSuccessful.toString())
                if (task.isSuccessful) {
                    viewModel.userLogin(account.email!!)
                    showSnackbar("Login Successful!", R.color.green)
                } else {
                    showSnackbar("Login Failed: Server error", R.color.red)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
