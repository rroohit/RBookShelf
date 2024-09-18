package roh.book.shelf.util

import android.os.Build
import android.util.Patterns
import androidx.annotation.RequiresApi
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

fun isValidEmail(email: String): Boolean {
    return email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

fun isValidPassword(password: String): Boolean {
    val passwordPattern = Regex("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#\$%^&*()]).{8,}\$")
    return passwordPattern.matches(password) && password.length >= 8
}


@RequiresApi(Build.VERSION_CODES.O)
fun getYearFromTimestamp(timestamp: Long): Int {
    val instant = Instant.ofEpochSecond(timestamp)
    val zonedDateTime: ZonedDateTime = instant.atZone(ZoneId.systemDefault())
    return zonedDateTime.year
}