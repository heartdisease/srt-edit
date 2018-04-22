package com.github.heartdisease.srtedit

import java.io.File

fun main(args: Array<String>) {
  if (args.size != 1) {
    println("Incorrect number of arguments!")
    return
  }
  parseSrtFile(args[0])
}

fun parseSrtFile(path: String): MutableList<Subtitle> {
  val reader = File(path).bufferedReader()
  val subtitleList = mutableListOf<Subtitle>()

  reader.useLines { lines ->
    var counter = 0

    for (line in lines) {
      println(line)
      if (counter++ > 10) break
    }
  }

  return subtitleList
}