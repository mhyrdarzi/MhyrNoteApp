package dz.mhyrdarzi.mhyrnoteapp.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import dz.mhyrdarzi.mhyrnoteapp.R
import dz.mhyrdarzi.mhyrnoteapp.data.model.NoteModel
import dz.mhyrdarzi.mhyrnoteapp.databinding.NoteListItemBinding
import dz.mhyrdarzi.mhyrnoteapp.utils.Constants.DELETE_NOTE
import dz.mhyrdarzi.mhyrnoteapp.utils.Constants.EDIT_NOTE
import dz.mhyrdarzi.mhyrnoteapp.utils.Constants.EDUCTION
import dz.mhyrdarzi.mhyrnoteapp.utils.Constants.HEALTH
import dz.mhyrdarzi.mhyrnoteapp.utils.Constants.HIGH
import dz.mhyrdarzi.mhyrnoteapp.utils.Constants.HOME
import dz.mhyrdarzi.mhyrnoteapp.utils.Constants.LOW
import dz.mhyrdarzi.mhyrnoteapp.utils.Constants.NORMAL
import dz.mhyrdarzi.mhyrnoteapp.utils.Constants.WORK
import javax.inject.Inject

class NoteAdapter @Inject constructor() : RecyclerView.Adapter<NoteAdapter.ViewHolder>() {

    private lateinit var bind: NoteListItemBinding
    private var noteList = emptyList<NoteModel>()
    private lateinit var context: Context

    inner class ViewHolder() : RecyclerView.ViewHolder(bind.root) {
        fun bindData(item: NoteModel) {
            bind.apply {
                txtNoteName.text = item.title
                txtNoteDesc.text = item.desc
                //Priority
                when (item.priority) {
                    HIGH -> priorityView.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.red
                        )
                    )
                    NORMAL -> priorityView.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.green
                        )
                    )
                    LOW -> priorityView.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.blue
                        )
                    )
                }
                //Category
                when (item.category) {
                    HOME -> imgCategory.setImageResource(R.drawable.ic_baseline_home_24)
                    WORK -> imgCategory.setImageResource(R.drawable.ic_baseline_work_24)
                    EDUCTION -> imgCategory.setImageResource(R.drawable.ic_baseline_library_books_24)
                    HEALTH -> imgCategory.setImageResource(R.drawable.ic_baseline_health_and_safety_24)
                }
                //Popup menu
                imgMenu.setOnClickListener {
                    val popupMenu = PopupMenu(context, it)
                    popupMenu.menuInflater.inflate(R.menu.item_menu, popupMenu.menu)
                    popupMenu.show()
                    //Click
                    popupMenu.setOnMenuItemClickListener { menuItem ->
                        when (menuItem.itemId) {
                            R.id.editNote -> {
                                onClickListener?.let {
                                    it(item, EDIT_NOTE)
                                }
                            }

                            R.id.deleteNote -> {
                                onClickListener?.let {
                                    it(item, DELETE_NOTE)
                                }
                            }
                        }
                        return@setOnMenuItemClickListener true
                    }
                }
            }
        }
    }

    private var onClickListener: ((NoteModel, String) -> Unit)? = null

    fun setOnItemClickListener(listener: (NoteModel, String) -> Unit) {
        onClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        bind = NoteListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        context = parent.context
        return ViewHolder()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(noteList[position])
        holder.setIsRecyclable(false)
    }

    override fun getItemCount(): Int {
        return noteList.size
    }

    fun setData(data: List<NoteModel>) {
        val noteDiffUtils = NoteDiffUtils(noteList, data)
        val diffUtils = DiffUtil.calculateDiff(noteDiffUtils)
        noteList = data
        diffUtils.dispatchUpdatesTo(this)
    }

    class NoteDiffUtils(
        private val oldItem: List<NoteModel>,
        private val newItem: List<NoteModel>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int {
            return oldItem.size
        }

        override fun getNewListSize(): Int {
            return newItem.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldItem[oldItemPosition] === newItem[newItemPosition]
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldItem[oldItemPosition] === newItem[newItemPosition]
        }

    }


}
