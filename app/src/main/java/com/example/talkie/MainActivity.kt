package com.example.talkie

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.talkie.adapters.ViewpagerAdapter
import com.example.talkie.databinding.ActivityMainBinding
import com.example.talkie.fragments.ChatFragment
import com.example.talkie.fragments.GroupsFragment
import com.example.talkie.fragments.StatusFragment
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val viewpagerAdapter = ViewpagerAdapter(supportFragmentManager, lifecycle)
        viewpagerAdapter.addFragment(ChatFragment())
        viewpagerAdapter.addFragment(StatusFragment())
        viewpagerAdapter.addFragment(GroupsFragment())

        binding.viewPager.adapter = viewpagerAdapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "Chats"
                }
                1 -> {
                    tab.text = "Status"
                }
                2 -> {
                    tab.text = "Groups"
                }
            }
        }.attach()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_search -> {
                Toast.makeText(this, "Search", Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.menu_invite -> {
                Toast.makeText(this, "Invite", Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.menu_new_group -> {
                return true
            }
            R.id.menu_settings -> {
                return true
            }
            R.id.menu_starred_messages -> {
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}