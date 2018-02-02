package com.davidkeen.test

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}
import scala.util.{Failure, Success, Try}
import scala.concurrent.duration._

object FutureTransform extends App {

  val f = Future(throw new Exception("oops"))
  val result: Future[String] = f.transform {
    case Success(_) => Try("OK")
    case Failure(e) => Try("Failure: " + e.getMessage)
  }

  println(Await.result(result, 5000.millis))
}
