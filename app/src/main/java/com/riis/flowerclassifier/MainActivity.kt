package com.riis.flowerclassifier

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.navigation.NavigationView
import com.riis.flowerclassifier.fragments.FragmentList
import com.riis.flowerclassifier.fragments.FragmentSample
import com.riis.flowerclassifier.fragments.FragmentUpload
import com.riis.flowerclassifier.fragments.FragmentAbout
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Checks to see what the current fragment is
        // If the app has just started, then the sample images fragment will be shown
        if(getCurrentFragment() == null){
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, FragmentSample(), FragmentTags.Sample.tag).commit()
        }

        // Creates the backStackChangedListener
        // Used to update the highlighted items in the navigation drawer
        loadBackStackChangedListener()

        //Gets the topAppBar and creates a toggle to open and close the drawer
        val topAppBar = findViewById<MaterialToolbar>(R.id.topAppBar)
        val toggle = ActionBarDrawerToggle(this, drawer_layout, topAppBar, R.string.open_nav_drawer, R.string.close_nav_drawer)

        //adds the toggle listener
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            // If the tab selected is the sample images
            // Each tab also checks if the tab is already selected before loading the fragment
            // This makes sure the same fragment doesn't get loaded multiple times
            R.id.nav_sample -> {
                if(!nav_view.menu.getItem(0).isChecked) {
                    loadFragment(FragmentTags.Sample)
                }
            }
            // If the tab selected is the upload an image
            R.id.nav_upload -> {
                if(!nav_view.menu.getItem(1).isChecked) {
                    loadFragment(FragmentTags.Upload)
                }
            }
            // If the tab selected is the dog name list view
            R.id.nav_list -> {
                if(!nav_view.menu.getItem(2).isChecked) {
                    loadFragment(FragmentTags.List)
                }
            }
            // If the tab selected is the about tab
            R.id.nav_about -> {
                if(!nav_view.menu.getItem(3).isChecked) {
                    loadFragment(FragmentTags.About)
                }
            }
        }
        //Closes the drawer after a tab is clicked
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun loadBackStackChangedListener(){
        // Used when the back button is clicked to change the highlighted
        // item in the navigation drawer
        supportFragmentManager.addOnBackStackChangedListener {
            // Whem the back button is pressed, then the current fragment is obtained
            val currentFrag = getCurrentFragment()
            //checks if the fragment is null
            if (currentFrag != null) {
                //checks which fragment is loaded and highlights that item in the menu
                when(currentFrag.tag) {
                    FragmentTags.Sample.tag -> {
                        nav_view.menu.getItem(0).isChecked = true
                    }
                    FragmentTags.Upload.tag -> {
                        nav_view.menu.getItem(1).isChecked = true
                    }
                    FragmentTags.List.tag -> {
                        nav_view.menu.getItem(2).isChecked = true
                    }
                    FragmentTags.About.tag-> {
                        nav_view.menu.getItem(3).isChecked = true
                    }
                    else -> {
                        nav_view.menu.getItem(0).isChecked = true
                    }
                }
            }else{
                nav_view.menu.getItem(0).isChecked = true
            }
        }
    }

    private fun getCurrentFragment(): Fragment?{
        //checks if the backstack is empty
        return if(supportFragmentManager.backStackEntryCount == 0){
            null
        }else {
            //gets the current fragment by subtracting 1 from the back-stack entry count and getting its tag
            val tag = supportFragmentManager.getBackStackEntryAt(supportFragmentManager.backStackEntryCount -1).name
            supportFragmentManager.findFragmentByTag(tag)
        }
    }

    private fun loadFragment(fragment: FragmentTags){
        // Creates a transaction for replacing the fragment loaded
        val transaction = supportFragmentManager.beginTransaction()
        // Gets the fragment selected
        val frag = when(fragment){
            FragmentTags.Sample -> {
                FragmentSample()
            }
            FragmentTags.Upload -> {
                FragmentUpload()
            }
            FragmentTags.List -> {
                FragmentList()
            }
            FragmentTags.About -> {
                FragmentAbout()
            }
        }

        // Replaces the fragment that is loaded in the fragment container
        transaction.replace(R.id.fragment_container, frag, fragment.tag)
        transaction.addToBackStack(fragment.tag)
        transaction.commit()
    }

    override fun onBackPressed() {
        // Closes the navigation drawer if it is open
        // when the back button is pressed
        // prevents loading to previous fragment if it is open
        if(drawer_layout.isDrawerOpen(GravityCompat.START)){
            drawer_layout.closeDrawer(GravityCompat.START)
        }else{
            super.onBackPressed()
        }
    }

    // Enumeration for all fragment tags
    enum class FragmentTags(val tag: String) {
        Sample("sample_tag"),
        Upload("upload_tag"),
        List("list_tag"),
        About("about_tag")
    }
}