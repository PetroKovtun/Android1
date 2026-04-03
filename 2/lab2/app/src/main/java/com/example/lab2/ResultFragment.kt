package com.example.lab2

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment

class ResultFragment : Fragment(R.layout.fragment_result) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvResult = view.findViewById<TextView>(R.id.tvResult)
        val btnCancel = view.findViewById<Button>(R.id.btnCancel)

        tvResult.text = arguments?.getString(ARG_TEXT)

        btnCancel.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    companion object {
        private const val ARG_TEXT = "text"

        fun newInstance(text: String): ResultFragment {
            val fragment = ResultFragment()
            fragment.arguments = Bundle().apply {
                putString(ARG_TEXT, text)
            }
            return fragment
        }
    }
}
