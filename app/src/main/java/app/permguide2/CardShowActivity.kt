package app.permguide2

import android.os.Bundle
import android.app.Activity
import android.view.View
import android.widget.*
import app.permguide2.Context.model.ShowPlace
import app.permguide2.database.DbHelper

import kotlinx.android.synthetic.main.activity_card_show.view.*

class CardShowActivity : Activity() {

    var showPlace: ShowPlace? = null
    lateinit var dbHelper: DbHelper
    lateinit var buton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_show)

        dbHelper = DbHelper(this)

        var idCard = this.intent.extras["cardID"] as String
        showPlace = dbHelper.allData.find { x -> x.id == idCard }
        dbHelper.close()
        var name = findViewById<TextView>(R.id.name)
        var image = findViewById<ImageView>(R.id.image)
        var description = findViewById<TextView>(R.id.description)
        var type = findViewById<TextView>(R.id.type)
        var visited = findViewById<Switch>(R.id.visited)



        visited.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                showPlace?.visited = 1
                dbHelper.visited(showPlace!!, 1)
                dbHelper.close()
                Toast.makeText(this, "Вы посетили ${showPlace?.name}", Toast.LENGTH_LONG).show()
            } else {
                showPlace?.visited = 0
                dbHelper.visited(showPlace!!, 0 )
                dbHelper.close()
                Toast.makeText(this, "Вы больше не были в ${showPlace?.name}", Toast.LENGTH_LONG).show()
            }
        }

        buton = findViewById(R.id.mapButton)
//        val clickListener = View.OnClickListener {  }
//        buton.setOnClickListener(clickListener)

        visited.isChecked = showPlace?.visited != 0
        type.text = showPlace?.type
        name.text = showPlace?.name
        if (showPlace?.image != null)
            image.image.setImageBitmap(showPlace?.image)
        description.text = showPlace?.description

    }

}
