package com.cheonajjang.uas0707.ui.home

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.cheonajjang.uas0707.R
import com.cheonajjang.uas0707.adapter.MainAdapter
import com.cheonajjang.uas0707.data.model.User
import com.cheonajjang.uas0707.databinding.ActivityMainBinding
import com.cheonajjang.uas0707.ui.detail.DetailActivity


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: MainAdapter
    private lateinit var viewModel: UserViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = MainAdapter()
        adapter.notifyDataSetChanged()

        adapter.setOnItemClickCallback(object : MainAdapter.OnItemClickCallback{
            override fun onItemClicked(data: User) {
                Intent(this@MainActivity, DetailActivity::class.java).also {
                    it.putExtra(DetailActivity.EXTRA_USERNAME, data.login)
                    startActivity(it)
                }
            }
        })


        viewModel = ViewModelProvider(this,ViewModelProvider.NewInstanceFactory()).get(UserViewModel::class.java)

        binding.apply {

            recyclerMain.adapter = adapter
            recyclerMain.layoutManager = LinearLayoutManager(this@MainActivity)
            recyclerMain.addItemDecoration(DividerItemDecoration(this@MainActivity,LinearLayout.VERTICAL))
        }


        viewModel.getSearchUser().observe(this){
            if(it!=null){
                if (it.isEmpty()) {
                    Toast.makeText(this, getString(R.string.not_found), Toast.LENGTH_SHORT).show()
                }
                adapter.setList(it)
                showLoading(false)
            }
        }

    }

    private fun searchUser(query: String){
        binding.apply {
         if(query.isEmpty()) return
            showLoading(true)
            viewModel.setSearchUser(query)
        }
    }

    private fun showLoading(state: Boolean){
        if(state){
            binding.progressBar.visibility = View.VISIBLE
        }else{
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_bar, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView =  menu?.findItem(R.id.search)?.actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                searchUser(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }


        })
        return super.onCreateOptionsMenu(menu)
    }


}