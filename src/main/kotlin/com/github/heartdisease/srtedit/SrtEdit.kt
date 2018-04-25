package com.github.heartdisease.srtedit

import java.io.File

fun main(args: Array<String>) {
  if (args.size != 3) {
    println("Incorrect number of arguments!")
    println("java SrtEdit [srt file] [action] [timestamp]")
    println("action: add | remove")
    println("timestamp: hh:mm:ss,ms")
    return
  }

  val action = args[1]
  val timestamp = Timestamp.parseTimestamp(args[2])
  val subtitles = parseSrtFile(args[0]).subList(0, 5).map {
    val begin: Timestamp
    val end: Timestamp

    when (action) {
      "add" -> {
        begin = it.begin + timestamp
        end = it.end + timestamp
      }
      "remove" -> {
        begin = it.begin - timestamp
        end = it.end - timestamp
      }
      else -> throw IllegalArgumentException("Unsupported action '$action'")
    }
    Subtitle(it.index, begin, end, it.text)
  }

  for (subtitle in subtitles) {
    println(subtitle.serialize())
    println()
  }
}

private fun parseSrtFile(path: String): List<Subtitle> {
  val reader = File(path).bufferedReader(Charsets.UTF_8)
  val subtitleList = mutableListOf<Subtitle>()

  reader.useLines {
    var index: Long? = null
    var begin: Timestamp? = null
    var end: Timestamp? = null
    var text: String? = null

    for (line in it.map { line -> line.trim() }) {
      if (line.isNotEmpty()) {
        when {
          (index == null) -> index = line.toLong()
          (begin == null) -> {
            val timestamps = line.split(" --> ")

            begin = Timestamp.parseTimestamp(timestamps[0].trim())
            end = Timestamp.parseTimestamp(timestamps[1].trim())
          }
          (text == null) -> text = line
          else -> text += "\n$line"
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

  subtitleList.sortBy { it -> it.index } // ensure subtitles are sorted correctly according to index
  return subtitleList.toList()
}