package app.permguide2.database

import android.provider.BaseColumns

/**
 * Created by burla on 19.03.2018.
 */
object DbContract {

    class ShowplaceEntry : BaseColumns {
        companion object {
            const val TABLE_NAME = "showplace"
            const val COLUMN_ID = "place_id"
            const val COLUMN_NAME = "name"
            const val COLUMN_DESCRIPTION = "description"
            const val COLUMN_IMAGE = "image"

            const val COLUMN_LOCATION_X = "location_x"
            const val COLUMN_LOCATION_Y = "location_y"

            const val COLUMN_SHOWPLACE_TYPE = "showplace_type"
            const val COLUMN_VISITED = "visited"

        }
    }


}