package com.github.heartdisease.srtedit

import java.util.*

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

    private fun leftpad(num: Int, digits: Int = 2): String {
      val numAsString = num.toString()

      return if (digits > numAsString.length) {
        numAsString.padStart(digits, '0')
      } else {
        numAsString
      }
    }

    private fun asTimestamp(milliseconds: Long): Timestamp {
      var remainder = milliseconds

      val hours = remainder / HOUR_IN_MS
      if (hours > 0L) {
        remainder %= HOUR_IN_MS
      }

      val minutes = remainder / MINUTE_IN_MS
      if (minutes > 0L) {
        remainder %= MINUTE_IN_MS
      }

      val seconds = remainder / SECOND_IN_MS
      if (seconds > 0L) {
        remainder %= SECOND_IN_MS
      }
      val milliseconds = remainder

      return Timestamp(hours.toInt(), minutes.toInt(), seconds.toInt(), milliseconds.toInt())
    }
  }

  operator fun plus(timestamp: Timestamp): Timestamp {
    return asTimestamp(toMilliseconds() + timestamp.toMilliseconds())
  }

  operator fun minus(timestamp: Timestamp): Timestamp {
    return asTimestamp(toMilliseconds() - timestamp.toMilliseconds())
  }

  fun compareTo(timestamp: Timestamp): Int {
    val diff = toMilliseconds() - timestamp.toMilliseconds()

    return when {
      diff > 0 -> 1
      diff < 0 -> -1
      else -> 0
    }
  }

  override fun equals(other: Any?): Boolean {
    return this === other || other is Timestamp && compareTo(other) == 0
  }

  override fun hashCode(): Int {
    return Objects.hash(hours, minutes, seconds, milliseconds)
  }

  override fun toString(): String {
    return toSrtString()
  }

  private fun toMilliseconds(): Long {
    return hours * HOUR_IN_MS + minutes * MINUTE_IN_MS + seconds * SECOND_IN_MS + milliseconds
  }

  private fun toSrtString(): String {
    return leftpad(hours) + ':' + leftpad(minutes) + ':' + leftpad(seconds) + ',' + leftpad(milliseconds, 3)
  }
}