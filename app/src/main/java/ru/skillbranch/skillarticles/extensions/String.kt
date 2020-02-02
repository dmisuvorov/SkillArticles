package ru.skillbranch.skillarticles.extensions

fun String.indexesOf(substr: String, ignoreCase: Boolean = true): List<Int> {
    val result = mutableListOf<Int>()
    (if (ignoreCase) substr.toRegex(RegexOption.IGNORE_CASE) else substr.toRegex())
        .findAll(this).forEach { matchResult ->
            result.add(matchResult.range.first)
        }
    return result
}