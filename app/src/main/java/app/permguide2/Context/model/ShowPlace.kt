package app.permguide2.Context.model

import android.graphics.Bitmap

/**
 * Created by burla on 15.03.2018.
 */
class ShowPlace(var id: Int = 0,
                var name: String, var description: String,
                var locationX: Int, var locationY:Int,
                var image: Bitmap, var type: String) { }