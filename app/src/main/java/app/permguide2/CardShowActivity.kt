package app.permguide2

import android.os.Bundle
import android.app.Activity
import android.widget.ImageView
import android.widget.TextView
import app.permguide2.Context.model.ShowPlace
import app.permguide2.Context.DBContext

import kotlinx.android.synthetic.main.activity_card_show.view.*

class CardShowActivity : Activity() {

    var showPlace : ShowPlace? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_show)

        var idCard = this.intent.extras["cardID"] as Int
        showPlace = DBContext.ShowPlaceSet.find { x-> x.id == idCard }

        var name = findViewById<TextView>(R.id.name)
        var image = findViewById<ImageView>(R.id.image)
        var description = findViewById<TextView>(R.id.description)

        name.text = showPlace?.name
        image.image.setImageBitmap(showPlace?.image)
        description.text = showPlace?.description

    }

}
