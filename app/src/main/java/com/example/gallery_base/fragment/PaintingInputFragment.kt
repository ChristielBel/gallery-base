package com.example.gallery_base.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.example.gallery_base.R

class PaintingInputFragment : Fragment() {
    interface PaintingListener {
        fun onPaintingSaved(title: String, description: String, date: Long)
    }

    private var listener: PaintingListener? = null

    fun setPaintingListener(listener: PaintingListener) {
        this.listener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_painting_input, container, false)

        val titleEdit = view.findViewById<EditText>(R.id.etTitle)
        val descEdit = view.findViewById<EditText>(R.id.etDescription)
        val calendar = view.findViewById<CalendarView>(R.id.cwDateOfWriting)
        val btnSave = view.findViewById<Button>(R.id.btnSave)
        val btnCancel = view.findViewById<Button>(R.id.btnCancel)

        // Устанавливаем начальные значения
        arguments?.getString(ARG_TITLE)?.let { titleEdit.setText(it) }
        arguments?.getString(ARG_DESCRIPTION)?.let { descEdit.setText(it) }

        btnSave.setOnClickListener {
            val title = titleEdit.text.toString()
            val description = descEdit.text.toString()
            val date = calendar.date

            if (title.isNotBlank()) {
                listener?.onPaintingSaved(title, description, date)
                // Используем childFragmentManager для согласованности
                parentFragmentManager.popBackStack()
            } else {
                titleEdit.error = "Введите название"
            }
        }

        btnCancel.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        return view
    }

    companion object {
        private const val ARG_TITLE = "title"
        private const val ARG_DESCRIPTION = "description"

        fun newInstance(title: String, description: String) = PaintingInputFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_TITLE, title)
                putString(ARG_DESCRIPTION, description)
            }
        }
    }
}