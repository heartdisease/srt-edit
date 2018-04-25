package com.github.heartdisease.srtedit

import java.util.*

class Timestamp(
  private val hours: Int,
  private val minutes: Int,
  private val seconds: Int,
  private val milliseconds: Int
) {
  class IllegalTimestampFormatException(override val message: String?) : RuntimeException()

  companion object {
    private const val HOUR_IN_MS = 60 * 60 * 1000L
    private const val MINUTE_IN_MS = 60 * 1000L
    private const val SECOND_IN_MS = 1000L

    fun parseTimestamp(timestamp: String): Timestamp {
      try {
        val parts = timestamp.split(":", ",")
        return Timestamp(parts[0].toInt(), parts[1].toInt(), parts[2].toInt(), parts[3].toInt())
      } catch (e: Exception) {
        throw IllegalTimestampFormatException("Invalid timestamp: $timestamp")
      }
    }

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

      return Timestamp(hours.toInt(), minutes.toInt(), seconds.toInt(), remainder.toInt())
    }
  }

  operator fun plus(timestamp: Timestamp) = asTimestamp(toMilliseconds() + timestamp.toMilliseconds())

  operator fun minus(timestamp: Timestamp) = asTimestamp(toMilliseconds() - timestamp.toMilliseconds())

  fun compareTo(timestamp: Timestamp): Int {
    val diff = toMilliseconds() - timestamp.toMilliseconds()

    return when {
      diff > 0 -> 1
      diff < 0 -> -1
      else -> 0
    }
  }

  override fun equals(other: Any?): Boolean = this === other || other is Timestamp && compareTo(other) == 0

  override fun hashCode(): Int = Objects.hash(hours, minutes, seconds, milliseconds)

  override fun toString(): String = serialize()

  fun serialize(): String = leftpad(hours) + ':' + leftpad(minutes) + ':' + leftpad(seconds) + ',' + leftpad(milliseconds, 3)

  private fun toMilliseconds(): Long = hours * HOUR_IN_MS + minutes * MINUTE_IN_MS + seconds * SECOND_IN_MS + milliseconds
}