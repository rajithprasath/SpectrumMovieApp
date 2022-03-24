package com.rajith.spectrummovieapp.data.util

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

object DateTimeFormatter {
    private const val API_DATE_FORMAT = "yyyy-MM-dd"
    private const val TARGET_DATE_FORMAT = "dd/MMM/yyyy"

    fun format(format: String?): String? {
        try {
            format?.let {
                var originalFormat: DateFormat = SimpleDateFormat(API_DATE_FORMAT, Locale.ENGLISH)
                var targetFormat: DateFormat = SimpleDateFormat(TARGET_DATE_FORMAT)
                var date: Date = originalFormat.parse(format)
                return targetFormat.format(date)
            }
            return ""
        }catch (e: Exception){

        }
        return ""
    }
}