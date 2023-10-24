package com.example.goclass.classUi.qna

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.goclass.databinding.FragmentQnaBinding

class QnaFragment : Fragment() {
    private var _binding: FragmentQnaBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val qnaViewModel =
            ViewModelProvider(this).get(QnaViewModel::class.java)

        _binding = FragmentQnaBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.hintText
        qnaViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
