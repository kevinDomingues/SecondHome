package ipvc.estg.secondhome.utils

fun parseDate(date: String): String {
    val parsedDate = ""+date.substring(8, 10)+"/"+date.substring(5, 7)+"/"+date.substring(0, 4)

    return parsedDate
}