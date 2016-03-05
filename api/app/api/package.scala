import play.api.libs.json._
import reactivemongo.bson._

package object api {
  implicit class RichBSONValue(val bsonValue: BSONValue) extends AnyVal {
    def toJson: JsValue = bsonValue match {
      case value: BSONNull.type       => JsNull
      case value: BSONUndefined.type  => JsNull
      case value: BSONBoolean         => JsBoolean(value.value)
      case value: BSONLong            => JsNumber(value.value)
      case value: BSONInteger         => JsNumber(value.value)
      case value: BSONDouble          => JsNumber(value.value)
      case value: BSONString          => JsString(value.value)
      case value: BSONSymbol          => JsString(value.value)
      case value: BSONRegex           => JsObject(Seq(("value", JsString(value.value)), ("flags", JsString(value.flags))))
      case value: BSONObjectID        => JsString(value.stringify)
      case value: BSONBinary          => JsString(value.value.readString)
      case value: BSONDateTime        => JsString(value.value.toString)
      case value: BSONTimestamp       => JsString(value.value.toString)
      case value: BSONArray           => JsArray(value.values.map(_.toJson))
      case value: BSONDocument        => JsObject(value.stream.filter(_.isSuccess).map(_.get).map{case (k, v) => (k, v.toJson)})
    }
  }
}
