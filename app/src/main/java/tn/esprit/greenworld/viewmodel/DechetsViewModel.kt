package tn.esprit.greenworld.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tn.esprit.greenworld.models.Dechets
import java.io.File

class DechetsViewModel : ViewModel() {
    lateinit var recyclerListData: MutableLiveData<List<Dechets>?>
    lateinit var createLiveData: MutableLiveData<Dechets?>
    lateinit var deleteLiveData: MutableLiveData<Boolean>

    private val READ_EXTERNAL_STORAGE_PERMISSION_CODE = 123

    init {
        recyclerListData = MutableLiveData()
        createLiveData = MutableLiveData()
        deleteLiveData = MutableLiveData()
    }
    fun getDechetsOfConvObservable(): MutableLiveData<List<Dechets>?> {
        return recyclerListData
    }

    fun getCreateNewDechetsObservable(): MutableLiveData<Dechets?> {
        return createLiveData
    }

    fun deleteDechetsObservable(): MutableLiveData<Boolean> {
        return deleteLiveData
    }


}