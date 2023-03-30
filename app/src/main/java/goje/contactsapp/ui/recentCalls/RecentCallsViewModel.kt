package goje.contactsapp.ui.recentCalls

import android.annotation.SuppressLint
import android.app.Application
import android.database.Cursor
import android.provider.CallLog
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import goje.contactsapp.entity.Contact
import goje.contactsapp.entity.RecentCall
import java.util.*

class RecentCallsViewModel(application: Application) : AndroidViewModel(application) {

    private var _recentCallsList = MutableLiveData<List<RecentCall>>()
    val recentCallsList: LiveData<List<RecentCall>> = _recentCallsList

    private var hashMap : HashMap<String, String> = hashMapOf()

    private val projection = arrayOf(
        CallLog.Calls.NUMBER,
        CallLog.Calls.CACHED_NAME,
        CallLog.Calls.DATE,
        CallLog.Calls.DURATION,
        CallLog.Calls.TYPE
    )

    private fun getList(value: List<Contact>) {

        val cursor = getCursor()

        val retrievedList: ArrayList<RecentCall> = ArrayList()

        var numberColumnIndex = 0
        var dateColumnIndex = 0
        var durationColumnIndex = 0
        var typeColumnIndex = 0

        cursor?.apply {
            numberColumnIndex = cursor.getColumnIndex(CallLog.Calls.NUMBER)
            dateColumnIndex = cursor.getColumnIndex(CallLog.Calls.DATE)
            durationColumnIndex = cursor.getColumnIndex(CallLog.Calls.DURATION)
            typeColumnIndex = cursor.getColumnIndex(CallLog.Calls.TYPE)
        }

        while (cursor!!.moveToNext()) {
            val number: String = cursor.getString(numberColumnIndex)
            val date: Long = cursor.getLong(dateColumnIndex)
            val durationInSeconds: Int = cursor.getInt(durationColumnIndex)
            val type: Int = cursor.getInt(typeColumnIndex)
            val name = getNameByPhoneNumber(number, value)

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

    private fun getNameByPhoneNumber(phoneNumber: String, value: List<Contact>): String {
        for (contact in value) {
            val stringWithoutSpaces = contact.phoneNumber.replace("\\s".toRegex(), "")
            var phoneNumberWithPlus = ""
            var phoneNumberWithZero = ""
            if (phoneNumber.length > 2){
                phoneNumberWithPlus = "+" + phoneNumber.takeLast(phoneNumber.length-2)
            }
            if (phoneNumber.length > 1){
                phoneNumberWithZero = "00" + phoneNumber.takeLast(phoneNumber.length-1)
            }

            if (stringWithoutSpaces.contains(phoneNumber) || stringWithoutSpaces.contains(phoneNumberWithZero) ||stringWithoutSpaces.contains(phoneNumberWithPlus)) {
                return contact.name
            }
        }
        return phoneNumber
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

    fun retrieveReentCalls(value: List<Contact>) {
        getList(value)
    }
}