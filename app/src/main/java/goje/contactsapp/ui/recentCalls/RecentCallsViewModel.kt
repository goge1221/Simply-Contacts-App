package goje.contactsapp.ui.recentCalls

import android.app.Application
import android.database.Cursor
import android.provider.CallLog
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import goje.contactsapp.entity.RecentCall
import java.util.*

class RecentCallsViewModel(application: Application) : AndroidViewModel(application) {

    private var _recentCallsList = MutableLiveData<List<RecentCall>>()
    val recentCallsList: LiveData<List<RecentCall>> = _recentCallsList

    private val projection = arrayOf(
        CallLog.Calls.NUMBER,
        CallLog.Calls.CACHED_NAME,
        CallLog.Calls.DATE,
        CallLog.Calls.DURATION,
        CallLog.Calls.TYPE
    )

    init {
        getList()
    }

    private fun getList() {

        val cursor = getCursor()

        val retrievedList: ArrayList<RecentCall> = ArrayList()

        var numberColumnIndex = 0
        var dateColumnIndex = 0
        var durationColumnIndex = 0
        var typeColumnIndex = 0
        var nameColumnIndex = 0

        cursor?.apply {
            numberColumnIndex = cursor.getColumnIndex(CallLog.Calls.NUMBER)
            dateColumnIndex = cursor.getColumnIndex(CallLog.Calls.DATE)
            durationColumnIndex = cursor.getColumnIndex(CallLog.Calls.DURATION)
            typeColumnIndex = cursor.getColumnIndex(CallLog.Calls.TYPE)
            nameColumnIndex = cursor.getColumnIndex(CallLog.Calls.CACHED_NAME)
        }

        while (cursor!!.moveToNext()) {
            val number: String = cursor.getString(numberColumnIndex)
            val date: Long = cursor.getLong(dateColumnIndex)
            val durationInSeconds: Int = cursor.getInt(durationColumnIndex)
            val type: Int = cursor.getInt(typeColumnIndex)
            var name = ""
            if (cursor.getString(nameColumnIndex) != null)
                name = cursor.getString(nameColumnIndex)

            if (name.isEmpty())
                name = number

            retrievedList.add(
                RecentCall(
                    name, number, Date(date), durationInSeconds, type
                )
            )
        }

        Log.i("RECENT_CALLS", retrievedList.toString())
        cursor.close()
        _recentCallsList.value = retrievedList
    }

    private fun getCursor(): Cursor? {
        return getApplication<Application>().contentResolver.query(
            CallLog.Calls.CONTENT_URI,
            projection,
            null,
            null,
            CallLog.Calls.DATE + " DESC"
        )
    }

    fun retrieveReentCalls(){
        getList()
    }
}