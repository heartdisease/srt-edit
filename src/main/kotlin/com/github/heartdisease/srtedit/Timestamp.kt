package com.github.heartdisease.srtedit

class Timestamp(
  private val hours: Int,
  private val minutes: Int,
  private val seconds: Int,
  private val milliseconds: Int
) {
  companion object {
    private const val HOUR_IN_MS = 60 * 60 * 1000L
    private const val MINUTE_IN_MS = 60 * 1000L
    private const val SECOND_IN_MS = 1000L

    fun leftpad(num: Int, digits: Int = 2): String {
      val numAsString = num.toString()

      return if (digits > numAsString.length) {
        numAsString.padStart(digits - numAsString.length, '0')
      } else {
        numAsString
      }
    }
  }

  operator fun plus(timestamp: Timestamp) {
    TODO()
  }

  operator fun plusAssign(timestamp: Timestamp) {
    TODO()
  }

  operator fun minus(timestamp: Timestamp) {
    TODO()
  }

  operator fun minusAssign(timestamp: Timestamp) {
    TODO()
  }

  fun compareTo(timestamp: Timestamp): Int {
    val diff = toMilliseconds() - timestamp.toMilliseconds()

    return when {
      diff > 0 -> 1
      diff < 0 -> -1
      else -> 0
    }
  }

  fun toMilliseconds(): Long {
    return hours * HOUR_IN_MS + minutes * MINUTE_IN_MS + seconds * SECOND_IN_MS + milliseconds
  }

  fun toSrtString(): String {
    return leftpad(hours) + ':' + leftpad(minutes) + ':' + leftpad(seconds) + ',' + leftpad(milliseconds, 3)
  }
}