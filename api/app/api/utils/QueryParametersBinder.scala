package api.utils

import play.api.mvc.QueryStringBindable

object QueryParametersBinder {
  def binder[A, T1, T2](key1: String, key2: String)(constructor: (T1, T2) => A, destructor: A => Option[(T1, T2)])
                       (implicit elemOneBinder: QueryStringBindable[T1], elemTwoBinder: QueryStringBindable[T2], manifest: Manifest[A]) =
    new QueryStringBindable[A] {
      override def bind(key: String, params: Map[String, Seq[String]]) =
        for {
          elemOne <- elemOneBinder.bind(key1, params)
          elemTwo <- elemTwoBinder.bind(key2, params)
        } yield {
          (elemOne, elemTwo) match {
            case (Right(e1), Right(e2)) => Right(constructor(e1, e2))
            case _ => Left(s"Unable to bind a $manifest")
          }
        }

      override def unbind(key: String, m: A) = {
        val Some((e1, e2)) = destructor(m)
        Seq(elemOneBinder.unbind(key1, e1), elemTwoBinder.unbind(key2, e2)).filterNot(_.isEmpty).mkString("&")
      }
    }
}

case class Dimensions(maxWidth: Int, maxHeight: Int) {
  override def toString = s"2D-${maxWidth}X$maxHeight"
}

object Dimensions {
  implicit val binder = QueryParametersBinder.binder("maxWidth", "maxHeight")(Dimensions.apply, Dimensions.unapply)
}
