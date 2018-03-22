package app.permguide2

import android.os.Bundle
import android.app.Activity
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import app.permguide2.Context.model.ShowPlace
import app.permguide2.database.DbHelper

import kotlinx.android.synthetic.main.activity_card_show.view.*

class CardShowActivity : Activity() {

    var showPlace : ShowPlace? = null
    lateinit var dbHelper: DbHelper
    lateinit var buton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_show)

        dbHelper = DbHelper(this)

        var idCard = this.intent.extras["cardID"] as Int
        showPlace = dbHelper.allData.find { x->x.id == idCard }

        var name = findViewById<TextView>(R.id.name)
        var image = findViewById<ImageView>(R.id.image)
        var description = findViewById<TextView>(R.id.description)

        buton = findViewById(R.id.mapButton)
//        val clickListener = View.OnClickListener {  }
//        buton.setOnClickListener(clickListener)

        name.text = showPlace?.name
        image.image.setImageBitmap(showPlace?.image)
        description.text = showPlace?.description

    }

}
