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
  val timestamp = parseTimestamp(args[2])
  val parser = SrtParser().parse(File(args[0]))
  val subtitles = parser.subtitles.map {
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