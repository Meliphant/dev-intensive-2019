package ru.skillbranch.devintensive.extensions

import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

fun Date.format(pattern: String = "HH:mm:ss dd.MM.yy"): String =
        SimpleDateFormat(pattern, Locale("ru")).format(this)

fun Date.add(value: Int, timeUnit: TimeUnits): Date =
        Date(time + (value * timeUnit.value))

fun Date.humanizeDiff(date: Date = Date()): String =
        when (val difference = date.time - this.time) {
            in -TimeUnits.SECOND.value..TimeUnits.SECOND.value -> "только что"
            in TimeUnits.SECOND.value..TimeUnits.SECOND.value * 45 -> "несколько секунд назад"
            in -TimeUnits.SECOND.value * 45..-TimeUnits.SECOND.value -> "через несколько секунд"
            in TimeUnits.SECOND.value * 45..TimeUnits.SECOND.value * 75 -> "минуту назад"
            in -TimeUnits.SECOND.value * 75..-TimeUnits.SECOND.value * 45 -> "через минуту"
            in TimeUnits.SECOND.value * 75..TimeUnits.MINUTE.value * 45 -> {
                "${TimeUnits.MINUTE.plural((difference / TimeUnits.MINUTE.value).toFloat().roundToInt())} назад"
            }
            in -TimeUnits.MINUTE.value * 45..-TimeUnits.SECOND.value * 75 -> {
                "через ${TimeUnits.MINUTE.plural((difference.absoluteValue / TimeUnits.MINUTE.value).toFloat().roundToInt())}"
            }
            in TimeUnits.MINUTE.value * 45..TimeUnits.MINUTE.value * 75 -> "час назад"
            in -TimeUnits.MINUTE.value * 75..-TimeUnits.MINUTE.value * 45 -> "через час"
            in TimeUnits.MINUTE.value * 75..TimeUnits.HOUR.value * 22 -> {
                "${TimeUnits.HOUR.plural((difference / TimeUnits.HOUR.value).toFloat().roundToInt())} назад"
            }
            in -TimeUnits.HOUR.value * 22..-TimeUnits.MINUTE.value * 75 -> {
                "через ${TimeUnits.HOUR.plural((difference.absoluteValue / TimeUnits.HOUR.value).toFloat().roundToInt())}"
            }
            in TimeUnits.HOUR.value * 22..TimeUnits.HOUR.value * 26 -> "день назад"
            in -TimeUnits.HOUR.value * 26..-TimeUnits.HOUR.value * 22 -> "через день"
            in TimeUnits.HOUR.value * 26..TimeUnits.DAY.value * 360 -> {
                "${TimeUnits.DAY.plural((difference / TimeUnits.DAY.value).toFloat().roundToInt())} назад"
            }
            in -TimeUnits.DAY.value * 360..-TimeUnits.HOUR.value * 26 -> {
                "через ${TimeUnits.DAY.plural((difference.absoluteValue / TimeUnits.DAY.value).toFloat().roundToInt())}"
            }
            in TimeUnits.DAY.value * 360..Long.MAX_VALUE -> "более года назад"
            else -> "более чем через год"
        }

enum class TimeUnits(val value: Long) {
    SECOND(1000) {
        override fun plural(value: Int): String {
            return "$value ${toPlural(value, "секунду", "секунды", "секунд")}"
        }
    },
    MINUTE(SECOND.value * 60) {
        override fun plural(value: Int): String {
            return "$value ${toPlural(value, "минуту", "минуты", "минут")}"
        }
    },
    HOUR(MINUTE.value * 60) {
        override fun plural(value: Int): String {
            return "$value ${toPlural(value, "час", "часа", "часов")}"
        }
    },
    DAY(HOUR.value * 24) {
        override fun plural(value: Int): String {
            return "$value ${toPlural(value, "день", "дня", "дней")}"
        }
    };

    abstract fun plural(value: Int): String

}

fun toPlural(value: Int, single: String, few: String, many: String): String {
    return when {
        value % 10 == 0 -> many
        value % 100 in 11..19 -> many
        value % 10 == 1 -> single
        value % 10 in 1..4 -> few
        else -> many
    }
}