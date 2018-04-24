package com.github.heartdisease.srtedit

import java.io.File

fun main(args: Array<String>) {
  if (args.size != 1) {
    println("Incorrect number of arguments!")
    return
  }
  for (subtitle in parseSrtFile(args[0]).subList(0, 5)) {
    println(subtitle)
  }
}

private fun parseSrtFile(path: String): List<Subtitle> {
  val reader = File(path).bufferedReader()
  val subtitleList = mutableListOf<Subtitle>()

  reader.useLines {
    var index: Long? = null
    var begin: Timestamp? = null
    var end: Timestamp? = null
    var text: String? = null

    for (line in it.map { line -> line.trim() }) {
      if (line.isNotEmpty()) {
        when {
          (index == null) -> index = line.toLongOrNull() ?: -1
          (begin == null) -> {
            val timestamps = line.split(" --> ")

            begin = parseTimestamp(timestamps[0].trim())
            end = parseTimestamp(timestamps[1].trim())
          }
          (text == null) -> text = line
          else -> text += " $line"
        }
      } else if (text != null) {
        subtitleList.add(Subtitle(index!!, begin!!, end!!, text))
        index = null
        begin = null
        end = null
        text = null
      }
    }
  }

  return subtitleList.toList()
}

private fun parseTimestamp(timestamp: String): Timestamp {
  val parts = timestamp.split(":", ",")
  return Timestamp(parts[0].toInt(), parts[1].toInt(), parts[2].toInt(), parts[3].toInt())
}