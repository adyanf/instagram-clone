package com.adyanf.clone.instagram.utils.display

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.adyanf.clone.instagram.databinding.FragmentLoadingDialogBinding

class LoadingDialog : DialogFragment() {

    companion object {

        private const val TAG = "LoadingDialog"
        private const val KEY_LOADING_TEXT = "LOADING_TEXT"

        private var instance: LoadingDialog? = null

        fun show(text: String, supportFragmentManager: FragmentManager) {
            val args = Bundle().apply {
                putString(KEY_LOADING_TEXT, text)
            }
            val fragment = LoadingDialog().apply {
                arguments = args
            }
            instance = fragment
            instance?.show(supportFragmentManager, TAG)
        }

        fun dismiss() {
            instance?.dismiss()
            instance = null
        }
    }

    private var binding: FragmentLoadingDialogBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoadingDialogBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.let(::setupView)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    private fun setupView(binding: FragmentLoadingDialogBinding) {
        binding.loadingText.text = arguments?.getString(KEY_LOADING_TEXT)
    }
}