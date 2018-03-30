package app.permguide2.Context.model

import android.graphics.Bitmap
import android.media.AudioFormat

/**
 * Created by burla on 15.03.2018.
 */
class ShowPlace(var id: String,
                var name: String,
                var description: String, var locationLat: Float,
                var locationLong:Float,
                var image: Bitmap?, var type: String,
                var visited: Int) {
}