package com.example.luminosity.ui.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.example.luminosity.models.CollectionPhoto


class CollectionsViewModel(application: Application) : AndroidViewModel(application) {

    lateinit var listForFragment: List<com.example.luminosity.models.CollectionPhoto>

    private val contactsLiveData = MutableLiveData<List<CollectionPhoto>>()

    val contactsList: LiveData<List<CollectionPhoto>>
        get() = contactsLiveData


    fun loadList(list: List<CollectionPhoto>) {
        try {
                contactsLiveData.postValue(list)
            listForFragment = list
            } catch (t: Throwable) {
                contactsLiveData.postValue(emptyList())
            listForFragment = emptyList()
            }
        }

    }