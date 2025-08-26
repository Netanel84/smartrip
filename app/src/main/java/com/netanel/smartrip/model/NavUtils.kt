package com.netanel.smartrip.model

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri

fun openCategory(context: Context, navApp: NavApp, categoryHeb: String, lat: Double, lng: Double) {
    when (navApp) {
        NavApp.MAPS -> {
            val uri = Uri.parse("geo:$lat,$lng?q=$categoryHeb")
            val i = Intent(Intent.ACTION_VIEW, uri).setPackage("com.google.android.apps.maps")
            context.startActivity(i)
        }
        NavApp.WAZE -> {
            val uri = Uri.parse("waze://?ll=$lat,$lng&navigate=yes")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            try {
                context.startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.waze")))
            }
        }
    }
}

fun openDestination(context: Context, navApp: NavApp, destination: String) {
    when (navApp) {
        NavApp.MAPS -> {
            val uri = Uri.parse("google.navigation:q=$destination")
            context.startActivity(Intent(Intent.ACTION_VIEW, uri))
        }
        NavApp.WAZE -> {
            val uri = Uri.parse("waze://?q=$destination")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            try {
                context.startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.waze")))
            }
        }
    }
}
