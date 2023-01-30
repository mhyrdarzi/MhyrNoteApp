package dz.mhyrdarzi.mhyrnoteapp.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import dz.mhyrdarzi.mhyrnoteapp.R
import dz.mhyrdarzi.mhyrnoteapp.data.model.NoteModel
import dz.mhyrdarzi.mhyrnoteapp.databinding.ActivityMainBinding
import dz.mhyrdarzi.mhyrnoteapp.ui.add.NoteFragment
import dz.mhyrdarzi.mhyrnoteapp.utils.Constants.ALL
import dz.mhyrdarzi.mhyrnoteapp.utils.Constants.BUNDLE_ID
import dz.mhyrdarzi.mhyrnoteapp.utils.Constants.DELETE_NOTE
import dz.mhyrdarzi.mhyrnoteapp.utils.Constants.EDIT_NOTE
import dz.mhyrdarzi.mhyrnoteapp.utils.Constants.HIGH
import dz.mhyrdarzi.mhyrnoteapp.utils.Constants.LOW
import dz.mhyrdarzi.mhyrnoteapp.utils.Constants.NORMAL
import dz.mhyrdarzi.mhyrnoteapp.viewModel.MainViewModel
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    //Binding
    private var _bind: ActivityMainBinding? = null
    private val bind get() = _bind

    //Adapter
    @Inject
    lateinit var noteAdapter: NoteAdapter

    //Entity
    @Inject
    lateinit var model: NoteModel

    //ViewModel
    private val viewModel: MainViewModel by viewModels()

    private var selectedItem = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _bind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind?.root)
        //Init views
        bind?.apply {
            setSupportActionBar(noteToolsBar)
            btnAddNote.setOnClickListener {
                NoteFragment().show(supportFragmentManager, NoteFragment().tag)
            }
            //Get notes data
            viewModel.loadNotesList()
            viewModel.notesList.observe(this@MainActivity) { notes ->
                //Set empty list
                showEmptyList(notes.isEmpty)
                noteAdapter.setData(notes.data!!)
                recyclerNotes.apply {
                    layoutManager =
                        StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                    adapter = noteAdapter
                }
            }

            //Filter menu set
            noteToolsBar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.actionFilter -> {
                        filterPriority()
                        return@setOnMenuItemClickListener true
                    }
                    else -> {
                        return@setOnMenuItemClickListener false
                    }
                }
            }
            //Clicks
            noteAdapter.setOnItemClickListener { noteModel, state ->
                when(state) {
                    EDIT_NOTE -> {
                        val noteFragment = NoteFragment()
                        val bundle = Bundle()
                        bundle.putInt(BUNDLE_ID, noteModel.id)
                        noteFragment.arguments = bundle
                        noteFragment.show(supportFragmentManager, NoteFragment().tag)
                    }
                    DELETE_NOTE -> {
                        model.id = noteModel.id
                        model.title = noteModel.title
                        model.desc = noteModel.desc
                        model.category = noteModel.category
                        model.priority = noteModel.priority
                        viewModel.deleteNote(model)
                        Toast.makeText(this@MainActivity, "${model.title} حذف شد", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    //Search setup
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        val search = menu!!.findItem(R.id.actionSearch)
        val searchView = search.actionView as SearchView
        searchView.queryHint = getString(R.string.doSearch)
        searchView.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                viewModel.searchNotes(newText)
                return true
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    //Filter setup
    private fun filterPriority() {
        val alertBuilder = AlertDialog.Builder(this)

        val items = arrayOf(ALL, LOW, NORMAL, HIGH)
        alertBuilder.setSingleChoiceItems(items, selectedItem) { builder, item ->
            when (item) {
                0 -> {
                    viewModel.loadNotesList()
                }

                in 1..3 -> {
                    viewModel.filterNotes(items[item])
                }
            }
            selectedItem = item
            builder.dismiss()
        }

        val dialog: AlertDialog = alertBuilder.create()
        dialog.show()
    }

    //Show empty list
    private fun showEmptyList(isShown: Boolean) {
        bind?.apply {
            when (isShown) {
                true -> {
                    recyclerNotes.visibility = View.GONE; emptyLay.visibility = View.VISIBLE
                }
                false -> {
                    emptyLay.visibility = View.GONE; recyclerNotes.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _bind = null
    }
}