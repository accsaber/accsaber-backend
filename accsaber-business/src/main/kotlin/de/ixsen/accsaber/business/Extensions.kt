package de.ixsen.accsaber.business

import java.util.*

fun String.capitalise() = this.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

fun cheese () = " "