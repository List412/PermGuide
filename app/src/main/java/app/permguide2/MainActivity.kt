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
import android.app.AlertDialog
import android.view.View
import android.widget.ImageView
import android.content.Intent
import android.graphics.Bitmap
import app.permguide2.Context.model.ShowPlace
import app.permguide2.Context.DBContext
import app.permguide2.database.DbHelper
import android.graphics.BitmapFactory



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

        var db = DbHelper(this)
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.picture)
        //db.insertData(ShowPlace(0, "blalbla", "123123123123", 12, -45, bitmap, "сам ты церковь"))

        db.allData.forEach { value -> addCard(value) }

        nav_view.setNavigationItemSelectedListener(this)
    }


    fun addCard(showPlace: ShowPlace) {

        val linLayout = findViewById<LinearLayout>(R.id.contentPanel)

        val ltInflater = layoutInflater

        val item = ltInflater.inflate(R.layout.sample_content_shower, linLayout, false)

        var txtName = item.findViewById<TextView>(R.id.name)
        var txtDescription = item.findViewById<TextView>(R.id.description)
        var imageView = item.findViewById<ImageView>(R.id.image)
        var txtType = item.findViewById<TextView>(R.id.type)


        val clickListener = View.OnClickListener {view ->
            if (view is ImageView) onImagePressed(showPlace.image) else onCardPressed(showPlace) }
        imageView.setOnClickListener(clickListener)
        item.setOnClickListener(clickListener)
        imageView.setImageBitmap(showPlace.image)
        txtName.text = showPlace.name
        txtDescription.text = showPlace.description
        txtType.text = showPlace.type

        linLayout.addView(item)
    }

    fun onCardPressed(showPlace : ShowPlace)
    {
        val card = Intent(applicationContext, CardShowActivity::class.java)
        card.putExtra("cardID", showPlace.id)
        startActivity(card)
    }

    fun onImagePressed(image: Bitmap)
    {
        var builder = AlertDialog.Builder(this)
        var alert = builder.create()

        val ltInflater = layoutInflater

        val item = ltInflater.inflate(R.layout.alert_dialog_photo_viewer, null)
        var imageView = item.findViewById<ImageView>(R.id.image)
        imageView.setImageBitmap(image)
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
