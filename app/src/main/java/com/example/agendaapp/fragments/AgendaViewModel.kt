package com.example.agendaapp.fragments

import android.database.Cursor
import android.os.Bundle
import androidx.lifecycle.ViewModel

import android.provider.ContactsContract
import androidx.loader.app.LoaderManager
import androidx.loader.content.Loader

class AgendaViewModel : ViewModel(), LoaderManager.LoaderCallbacks<Cursor> {
    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        TODO("Not yet implemented")
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        TODO("Not yet implemented")
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        TODO("Not yet implemented")
    }


}