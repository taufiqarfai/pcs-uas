package com.cheonajjang.uas0707.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cheonajjang.uas0707.data.model.User
import com.cheonajjang.uas0707.data.model.UserList
import com.cheonajjang.uas0707.data.remote.RetrofitApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserViewModel: ViewModel() {

    val listUsers = MutableLiveData<ArrayList<User>>()

    fun setSearchUser(query: String){
        RetrofitApi.userService()
            .searchUser(query)
            .enqueue(object : Callback<UserList>{
                override fun onResponse(call: Call<UserList>, response: Response<UserList>) {
                    if(response.isSuccessful){
                        listUsers.postValue(response.body()?.items)
                    }
                }

                override fun onFailure(call: Call<UserList>, t: Throwable) {
                    t.message?.let { Log.d("Failure" , it)
                    }
                }
            })
    }

    fun getSearchUser():LiveData<ArrayList<User>>{
        return listUsers
    }
}