package ru.skillbranch.devintensive.extensions


fun String.truncate(length: Int = 16): String = with(trim()) {
    if (this.length <= length) {
        this
    } else {
        "${this.substring(0, length).trim()}..."
    }
}

fun String.stripHtml(): String {
    val regex = Regex("\\<.*?\\>|&[a-z|#\\d]+;")
    return this.replace(regex, "").replace("((?!\\n+)\\s+)".toRegex(), " ")
}