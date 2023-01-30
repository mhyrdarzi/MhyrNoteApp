package dz.mhyrdarzi.mhyrnoteapp.ui.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import dz.mhyrdarzi.mhyrnoteapp.data.model.NoteModel
import dz.mhyrdarzi.mhyrnoteapp.databinding.FragmentNoteBinding
import dz.mhyrdarzi.mhyrnoteapp.utils.Constants.BUNDLE_ID
import dz.mhyrdarzi.mhyrnoteapp.utils.Constants.EDIT
import dz.mhyrdarzi.mhyrnoteapp.utils.Constants.NEW
import dz.mhyrdarzi.mhyrnoteapp.utils.getIndexFromList
import dz.mhyrdarzi.mhyrnoteapp.utils.setupSpinner
import dz.mhyrdarzi.mhyrnoteapp.viewModel.NoteViewModel
import javax.inject.Inject

@AndroidEntryPoint
class NoteFragment : BottomSheetDialogFragment() {

    //Binding
    private var _bind: FragmentNoteBinding? = null
    private val bind get() = _bind

    //ViewModel
    private val viewModel: NoteViewModel by viewModels()

    //Category
    private var category = ""

    //priority
    private var priority = ""

    //Id
    private var noteID = 0

    //type
    private var type = ""

    //EDit
    private var isEdit = false

    //Entity
    @Inject
    lateinit var model: NoteModel

    private val categoryList: MutableList<String> = mutableListOf()
    private val priorityList: MutableList<String> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState:
        Bundle?
    ): View? {
        _bind = FragmentNoteBinding.inflate(layoutInflater)
        return bind?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Bundle
        noteID = arguments?.getInt(BUNDLE_ID) ?: 0
        //Type
        if (noteID > 0) {
            isEdit = true
            type = EDIT
        } else {
            isEdit = false
            type = NEW
        }
        //Init views
        bind?.apply {
            //Close
            imgClose.setOnClickListener { this@NoteFragment.dismiss() }
            //Setup Category Spinner
            viewModel.loadCategoryList()
            viewModel.categoryList.observe(viewLifecycleOwner) {
                categoryList.addAll(it)
                categorySpinner.setupSpinner(it) { catContent -> category = catContent }
            }
            //Setup Priority Spinner
            viewModel.loadPriorityList()
            viewModel.priorityList.observe(viewLifecycleOwner) {
                priorityList.addAll(it)
                prioritySpinner.setupSpinner(it) { priContent -> priority = priContent }
            }
            //Save or Edit note
            btnSave.setOnClickListener {
                val title = inpNoteName.text.toString()
                val desc = inpNoteDesc.text.toString()
                //Fill entity
                model.id = noteID
                model.title = title
                model.desc = desc
                model.priority = priority
                model.category = category
                //Value validation for add note
                if (title.isNotEmpty() && desc.isNotEmpty()) {
                        viewModel.saveOrEditNote(isEdit, model)
                }
                dismiss()
            }
            //Type checking
            if (type == EDIT) {
                viewModel.getNoteData(noteID)
                viewModel.noteData.observe(viewLifecycleOwner) { receivedData->
                    receivedData.data?.let { data->
                        inpNoteName.setText(data.title)
                        inpNoteDesc.setText(data.desc)
                        categorySpinner.setSelection(categoryList.getIndexFromList(data.category))
                        prioritySpinner.setSelection(priorityList.getIndexFromList(data.priority))
                    }
                }
            }
        }
    }
}