package dz.mhyrdarzi.mhyrnoteapp.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dz.mhyrdarzi.mhyrnoteapp.data.model.NoteModel
import dz.mhyrdarzi.mhyrnoteapp.data.repository.MainRepository
import dz.mhyrdarzi.mhyrnoteapp.utils.DataStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: MainRepository) : ViewModel() {

    val notesList = MutableLiveData<DataStatus<List<NoteModel>>>()

    fun loadNotesList() = viewModelScope.launch(Dispatchers.IO) {
        repository.getAllNotes().collect {
            notesList.postValue(DataStatus.success(it, it.isEmpty()))
        }
    }

    fun searchNotes(search: String) = viewModelScope.launch {
        repository.searchNote(search).collect {
            notesList.postValue(DataStatus.success(it, it.isEmpty()))
        }
    }

    fun filterNotes(filter: String) = viewModelScope.launch(Dispatchers.IO) {
        repository.filterNote(filter).collect {
            notesList.postValue(DataStatus.success(it, it.isEmpty()))
        }
    }

    fun deleteNote(model: NoteModel) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteNote(model)
    }
}