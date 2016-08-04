package utils

/**
  * Created by amit on 31/7/16.
  */
import models._
import play.api.libs.json.Json

object JsonFormat {
  implicit val todoFormat = Json.format[Todo]
}