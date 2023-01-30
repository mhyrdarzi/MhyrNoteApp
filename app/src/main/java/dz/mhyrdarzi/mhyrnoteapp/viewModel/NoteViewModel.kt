package dz.mhyrdarzi.mhyrnoteapp.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dz.mhyrdarzi.mhyrnoteapp.data.model.NoteModel
import dz.mhyrdarzi.mhyrnoteapp.data.repository.NoteRepository
import dz.mhyrdarzi.mhyrnoteapp.utils.Constants.EDUCTION
import dz.mhyrdarzi.mhyrnoteapp.utils.Constants.HEALTH
import dz.mhyrdarzi.mhyrnoteapp.utils.Constants.HIGH
import dz.mhyrdarzi.mhyrnoteapp.utils.Constants.HOME
import dz.mhyrdarzi.mhyrnoteapp.utils.Constants.LOW
import dz.mhyrdarzi.mhyrnoteapp.utils.Constants.NORMAL
import dz.mhyrdarzi.mhyrnoteapp.utils.Constants.WORK
import dz.mhyrdarzi.mhyrnoteapp.utils.DataStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(private val repository: NoteRepository) : ViewModel() {
    val categoryList = MutableLiveData<MutableList<String>>()
    val priorityList = MutableLiveData<MutableList<String>>()
    val noteData = MutableLiveData<DataStatus<NoteModel>>()

    fun loadCategoryList() = viewModelScope.launch(Dispatchers.IO) {
        val data = mutableListOf(HOME, WORK, EDUCTION, HEALTH)
        categoryList.postValue(data)
    }

    fun loadPriorityList() = viewModelScope.launch(Dispatchers.IO) {
        val data = mutableListOf(LOW, NORMAL, HIGH)
        priorityList.postValue(data)
    }


    fun saveOrEditNote(isEdit: Boolean, model: NoteModel) = viewModelScope.launch(Dispatchers.IO) {
        if (isEdit) {
            repository.updateNote(model)
        } else {
            repository.saveNote(model)
        }
    }

    fun getNoteData(id: Int) = viewModelScope.launch {
        repository.getNote(id).collect { note->
            noteData.postValue(DataStatus.success(note, false))
        }
    }

}