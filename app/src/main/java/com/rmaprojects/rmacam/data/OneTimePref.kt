package com.rmaprojects.rmacam.data

import com.chibatching.kotpref.KotprefModel

object OneTimePref: KotprefModel() {
    var isFirstTime by booleanPref(true)
}