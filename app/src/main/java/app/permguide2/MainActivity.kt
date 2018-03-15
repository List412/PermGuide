package app.permguide2

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import android.widget.TextView
import android.widget.LinearLayout
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.view.LayoutInflater
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.sample_content_shower.view.*
import android.R.attr.name
import android.app.AlertDialog
import android.view.View
import android.widget.ImageView
import kotlinx.android.synthetic.main.nav_header_main.*
import app.permguide2.R.id.imageView




class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()


        var showPlaces = Showplaces.Showplaces;
        Showplaces.Add("23", Location(1.0f, 2.0f))
        for (x in 0..10) {
            showPlaces.forEach { value -> draww(value) }
        }

        nav_view.setNavigationItemSelectedListener(this)
    }


    fun draww(showPlace: ShowPlace) {
        val linLayout = findViewById<LinearLayout>(R.id.contentPanel)

        val ltInflater = layoutInflater

        val item = ltInflater.inflate(R.layout.sample_content_shower, linLayout, false)

        var txtName = item.findViewById<TextView>(R.id.name)
        var txtDescription = item.findViewById<TextView>(R.id.description)
        var imageView = item.findViewById<ImageView>(R.id.image)

        var imagePath = R.drawable.picture

        val clickListener = View.OnClickListener { onImageTap(imagePath) }
        imageView.setOnClickListener(clickListener)
        imageView.setImageResource(imagePath)
        txtName.text = showPlace.name
        txtDescription.text = showPlace.name+"HAHA"

        linLayout.addView(item)
    }


    fun onImageTap(imagePath: Int)
    {
        var builder = AlertDialog.Builder(this)
        var alert = builder.create()

        val ltInflater = layoutInflater

        val item = ltInflater.inflate(R.layout.alert_dialog_photo_viewer, null)
        var imageView = item.findViewById<ImageView>(R.id.image)
        imageView.setImageResource(imagePath)
        alert.setView(item)
        alert.show()
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_camera -> {
                // Handle the camera action
            }
            R.id.nav_gallery -> {

            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_manage -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
