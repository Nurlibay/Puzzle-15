package uz.nurlibaydev.numberpuzzle15.util

import android.app.Activity
import android.widget.Toast
import androidx.fragment.app.Fragment

fun Fragment.showMessage(message: String, duration: Int = Toast.LENGTH_SHORT) {
    if (context != null) {
        Toast.makeText(context, message, duration).show()
    }
}

fun Activity.showMessage(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}