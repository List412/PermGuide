package app.permguide2

/**
 * Created by burla on 15.03.2018.
 */
object Showplaces {

    var first = ShowPlace("first", Location(10f,20f))

    var second = ShowPlace("second", Location(30f, 23f))

    var third = ShowPlace("third", Location(60f, 23f))

    var fouth = ShowPlace("fouth", Location(38f, 43f))



    var Showplaces = arrayListOf(first, second, third, fouth)

    fun Add(name: String, location: Location){
        Showplaces.add(ShowPlace(name, location))
    }

}