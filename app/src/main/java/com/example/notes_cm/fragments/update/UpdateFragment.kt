package com.example.notes_cm.fragments.update

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.notes_cm.R
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.notes_cm.data.entities.Note
import com.example.notes_cm.data.vm.NoteViewModel

class UpdateFragment : Fragment() {
    private  val args by navArgs<UpdateFragmentArgs>()
    private lateinit var mNoteViewModel: NoteViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_update, container, false)

        mNoteViewModel = ViewModelProvider(this)[NoteViewModel::class.java]

        view.findViewById<TextView>(R.id.updateNote).text = args.currentNote.note
        view.findViewById<TextView>(R.id.updateDate).text = args.currentNote.date
        view.findViewById<TextView>(R.id.updateDescription).text = args.currentNote.description


        val updateButton = view.findViewById<Button>(R.id.update)
        updateButton.setOnClickListener {
            updateNote()
        }

        val deleteButton = view.findViewById<Button>(R.id.delete)
        deleteButton.setOnClickListener {
            deleteNote()
        }

        val backButton = view.findViewById<Button>(R.id.backToListFromUpdate)
        backButton.setOnClickListener {
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }

        return  view
    }

    private  fun updateNote(){
        val noteText = view?.findViewById<EditText>(R.id.updateNote)?.text.toString()
        val noteDescription = view?.findViewById<EditText>(R.id.updateDescription)?.text.toString()
        val noteDate = view?.findViewById<EditText>(R.id.updateDate)?.text.toString()

        if(noteText.isEmpty()) {
            makeText(view?.context, R.string.EmptyNoteMessage, Toast.LENGTH_LONG).show()
        }
        else if (noteDate.isEmpty()) {
            makeText(view?.context, R.string.EmptyDateMessage, Toast.LENGTH_LONG).show()
        }
        else if (noteDescription.isEmpty()) {
            makeText(view?.context, R.string.EmptyDescriptionMessage, Toast.LENGTH_LONG).show()
        }
        else if (noteDescription.length < 5) {
            makeText(view?.context, R.string.DescriptionLengthMessage, Toast.LENGTH_LONG).show()
        }
        else {
            val note = Note(args.currentNote.id, noteText, noteDescription, noteDate)

            mNoteViewModel.updateNote(note)

            makeText(requireContext(), R.string.NoteUpdatedMessage, Toast.LENGTH_LONG).show()
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }
    }

    private fun deleteNote() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton(R.string.Yes) { _, _ ->
            mNoteViewModel.deleteNote(args.currentNote)
            makeText(
                requireContext(),
                R.string.NoteDeletedMessage,
                Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }
        builder.setNegativeButton(R.string.No) { _, _ -> }
        builder.setTitle(R.string.DeleteNoteTitle)
        builder.setMessage(R.string.DeleteNoteMessage)
        builder.create().show()
    }
}