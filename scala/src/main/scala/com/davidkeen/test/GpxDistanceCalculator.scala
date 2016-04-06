package com.davidkeen.test

object GpxDistanceCalculator {

  def haversineDistance(pointA: (Double, Double), pointB: (Double, Double)): Double = {
    val deltaLat = math.toRadians(pointB._1 - pointA._1)
    val deltaLong = math.toRadians(pointB._2 - pointA._2)
    val a = math.pow(math.sin(deltaLat / 2), 2) + math.cos(math.toRadians(pointA._1)) * math.cos(math.toRadians(pointB._1)) * math.pow(math.sin(deltaLong / 2), 2)
    val greatCircleDistance = 2 * math.atan2(math.sqrt(a), math.sqrt(1 - a))
    3958.761 * greatCircleDistance
  }

  def main(args: Array[String]) {
    val points = List((51.168437004089355, -0.648922920227051), (51.16805076599121, -0.64918041229248), (51.16757869720459, -0.64995288848877))

    val distance = points match {
      case head :: tail => tail.foldLeft(head, 0.0)((accum, elem) => (elem, accum._2 + haversineDistance(accum._1, elem)))._2
      case Nil => 0.0
    }
    println(s"Total distance: $distance miles")
  }
}
