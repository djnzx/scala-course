package dynamox

/** val sessionKeysF: F[List[SessionKey]] =
  *        dynamo.pure { dy =>
  *          import scala.jdk.CollectionConverters._
  *          dy.getTable("Session").query(TableSession.hashName, user.id).asScala.toList.map { itm: Item =>
  *            itm.getString("session_key")
  *          }
  *        }
  * //        dynamo.pureRawClient { dy =>
  * //          val expressionAttributesNames = Map(
  * //            "#user_id" -> "user_id"
  * //          ).asJava
  * //
  * //          val expressionAttributesValues = Map(
  * //            ":user_id" -> new AttributeValue().withS(user.id)
  * //          ).asJava
  * //
  * //          val queryRequest = new QueryRequest()
  * //            .withTableName("Session")
  * //            .withKeyConditionExpression("#user_id = :user_id")
  * //            .withExpressionAttributeNames(expressionAttributesNames)
  * //            .withExpressionAttributeValues(expressionAttributesValues)
  * //
  * //          dy.query(queryRequest).getItems.asScala.toList.map(_.asScala.toList.mkString(""))
  * //        }
  */
class Snippets {}
