package ru.skillbranch.devintensive.utils

object Utils {

    fun parseFullName(fullName: String?): Pair<String?, String?> {
        fullName?.trim()?.takeIf { it.isNotEmpty() }?.split(" ").let{
            val firstName = it?.getOrNull(0)
            val secondName = it?.getOrNull(1)
            return firstName to secondName
        }
    }

    fun toInitials(
            firstName: String?,
            lastName: String?
    ): String? {
        val firstInitial = firstName?.trim().takeIf { !it.isNullOrEmpty() }?.run{
            first().toUpperCase().toString()
        } ?: ""
        val lastInitial = lastName?.trim().takeIf { !it.isNullOrEmpty() }?.run{
            first().toUpperCase().toString()
        } ?: ""

        return if (firstInitial.isEmpty() && lastInitial.isEmpty()) {
            null
        } else {
            firstInitial + lastInitial
        }
    }

    fun transliteration(payload: String, divider: String = " "): String = buildString {
        payload.trim().replace(" ", divider).split(divider).asSequence().iterator().apply {
            while (hasNext()) {
                next().asSequence().forEach { char ->
                    val res = if (char.isUpperCase()) {
                        translit[char.toLowerCase()]?.capitalize()
                    } else {
                        translit[char.toLowerCase()]
                    }

                    this@buildString.append(res ?: char.toString())
                }

                if (hasNext()) {
                    this@buildString.append(divider)
                }
            }
        }
    }.trim()

    private val translit = mapOf<Char, String>(
            'а' to "a",
            'б' to "b",
            'в' to "v",
            'г' to "g",
            'д' to "d",
            'е' to "e",
            'ё' to "e",
            'ж' to "zh",
            'з' to "z",
            'и' to "i",
            'й' to "i",
            'к' to "k",
            'л' to "l",
            'м' to "m",
            'н' to "n",
            'о' to "o",
            'п' to "p",
            'р' to "r",
            'с' to "s",
            'т' to "t",
            'у' to "u",
            'ф' to "f",
            'х' to "h",
            'ц' to "c",
            'ч' to "ch",
            'ш' to "sh",
            'щ' to "sh'",
            'ъ' to "",
            'ы' to "i",
            'ь' to "",
            'э' to "e",
            'ю' to "yu",
            'я' to "ya"
    )

}