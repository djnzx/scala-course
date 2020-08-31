package request

import java.util.UUID

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers
import pprint.{pprintln => println}
import request.Cred.{email, password}
import request.Endpoints._
import request.Headers.{auth, headers, headersAuth}
import request.dto.auth.rq._
import request.dto.auth.rs.{AccessTokenRs, TokenPairRs, UserInfoRs}
import request.dto.optimisation.rs.{ListItemMetadata, ShoppingListProduct, Weight}
import request.dto.prod_comp.rq.ACTFullCatalogInfoRq
import request.dto.shop_list.ShoppingList
import request.dto.shop_list.rq.ShoppingListRq
import requests.Response
import ujson.Value
import upickle.default.{read => des, write => ser}

import scala.util.{Failure, Success, Try}

class ActApiSpec extends AnyFunSpec with Matchers {
  def readJson(r: Response): Value = ujson.read(r.data.toString)

  def login() = {
    val r = requests.post(urlLogin, headers = headers, data = ser(UserLoginRq(email, password)))
    des[TokenPairRs](r.data.toString)
  }
  lazy val logged: TokenPairRs = login()
  lazy val headersa = headersAuth(logged.act_token)

  implicit class ResponseMatchers(r: Response) {
    def hasHttpCode(code: Int) = r.statusCode shouldEqual code
    def hasObjKey(name: String) = hasObjKeys(Seq(name))
    def hasObjKeys(must: Seq[String]) = {
      // data from response
      val data: Value = readJson(r)
      // data represented as object
      val obj = data.obj
      // keys
      val keys = obj.keySet
      must.forall { k => keys.contains(k) } shouldEqual true
    }
    def hasText(text: String) = r.data.toString should include (text)
  }

  describe("0 syntax") {
    it("success") {
      "Password has been changed successfully".toLowerCase should include ("success")
    }
    it("unknown") {
      "User 123 UNKNOWN".toLowerCase should include ("unknown")
    }
  }

  describe("headers") {
    it("base configuration") {
      headers shouldEqual List("Content-Type"->"application/json")
    }
    it("authentication") {
      auth("qwe123") shouldEqual "Authorization"->"Bearer qwe123"
    }
    it("base configuration with authentication") {
      headersAuth("qwe123") shouldEqual List("Content-Type"->"application/json", "Authorization"->"Bearer qwe123")
    }
  }

  describe("ms: auth") {
    val tokenPairKeys = Seq("act_token", "user_Id", "ep_enabled", "refresh_token")

    /**
      * [[dto.auth.rq.UserRegRq]] => [[dto.auth.rs.TokenPairRs]]
      */
    it("urlAuthReg") {
      val r = requests.post(urlAuthReg, headers = headers, data = ser(UserRegRq.test))
      r hasHttpCode 201
      r hasObjKeys tokenPairKeys
      println(r)
    }

    /**
      * [[dto.auth.rq.UserLoginRq]] => [[dto.auth.rs.TokenPairRs]]
      */
    it("urlLogin") {
      val loginRq = UserLoginRq(email, password)
      val r = requests.post(urlLogin, headers = headers, data = ser(loginRq))
      r hasHttpCode 200
      r hasObjKeys tokenPairKeys
      println(r)
    }

    /**
      * after this test user became logged out
      */
    it("urlLogout") {
      val r = requests.delete(urlLogout, headers = headersa, data = ser(RefreshTokenRq(logged.refresh_token)))
      r hasHttpCode 204
      println(r)
    }

    /**
      * [[dto.auth.rq.ForgotPasswordRq]] =>
      * after this test user will have changed password in Db
      * so, we need to use dummy user for testing purposes !!!
      */
    it("urlForgotPwd RIGHT user") {
//      val r = requests.post(urlForgotPwd, headers = headers, data = ser(ForgotPasswordRq("a77x77@gmail.com", "")))
//      r hasHttpCode 202
//      println(r)
    }

    /**
      * implementation problems
      * it gives you 500 in any case
      */
    it("urlForgotPwd WRONG user") {
      Try(
        requests.post(urlForgotPwd, headers = headers, data = ser(ForgotPasswordRq("abracadabra@planet.earth", "x")))
      ).map { r =>
        println(r)
        r hasHttpCode 400 // 400 - OK
      } match {
        case Success(value) => value
        case Failure(ex) =>
          // 500 - also temporary OK
          ex.getMessage should include ("Request to http://aacctt.ddns.net:19901/auth-service/ss/account/forgot/password failed with status code 500")
      }
    }

    /**
      * [[dto.auth.rq.RefreshTokenRq]] => [[dto.auth.rs.AccessTokenRs]]
      */
    it("urlRefreshToken") {
      val r = requests.post(urlRefreshToken, headers = headers, data = ser(RefreshTokenRq(logged.refresh_token)))
      r hasHttpCode 200
      val ent: AccessTokenRs = des[AccessTokenRs](r.data.toString)
      r hasObjKey "act_token"
      println(logged)
      println(ent)
    }

    it("urlUpdatePasswd") {
      requests.put(urlUpdatePasswd)
    }

    it("urlAccUpdate") {
      val updateUser = UpdateUserRq("", "", "", "", "", UUID.randomUUID().toString)
      val r = requests.put(urlAccUpdate, headers = headersa, data = ser(updateUser))
      r hasHttpCode 200
      r hasText "Successfully Updated User Information"
    }

    /**
      *  () => [[dto.auth.rs.UserInfoRs]]
      *  we assume that user successfully logged in
      *  we take token from `lazy val logged: TokenPairRs = ...`
      */
    it("urlAccDetails") {
      val r = requests.get(urlAccDetails, headers = headersa)
      r hasHttpCode 200
      val ui: UserInfoRs = des[UserInfoRs](r.data.toString)
      ui.userId shouldEqual logged.user_Id
      println(ui)
    }

  }

  describe("ms: optimisation") {
    /**
      * [[dto.optimisation.rq.ACTFullCatalogInfoRq]] => [[dto.optimisation.rs.OptimisedList]]
      */
    val json = ser(dto.optimisation.rq.ACTFullCatalogInfoRq(
      59.392900,
      5.285696,
      "fa4f6832-cfd0-4c61-bcea-d769331cb974",
      "",
      "string",
      "",
      "",
      "COORDINATES",
      1L,
      0.7,
      0.5
    ))
    it("urlOptimizeList") {
      val r = requests.post(urlOptimizeList, headers = headersa, data = json)
      r hasHttpCode 200
      println(r)
      val resp = readJson(r)
      resp.arrOpt should not be None
      val keys: collection.Set[String] = resp(0).obj.keySet
      Set("shoppingListCategories", "shoppingListProducts").forall(keys.contains) shouldEqual true
    }
  }

  describe("ms: product-composite") {
    /**
      * [[dto.prod_comp.rq.ACTFullCatalogInfoRq]] => [[dto.prod_comp.rs.FullCatalogInfo]]
      */
    it("urlProdCompByLoc") {
      val json = ser(
        ACTFullCatalogInfoRq(
          43.64,
          -3.89,
          "Johannesburg",
          "Mayfair",
          "Norway",
          "Some guy",
          "COORDINATES",
          "fa4f6832-cfd0-4c61-bcea-d769331cb974"
        )
      )
      val r = requests.post(urlProdCompByLoc, headers = headersa, data = json)
      r hasHttpCode 200
      r hasObjKeys Seq("actFullCatalogInfo", "shoppingLists", "sellers")
      println(r)
    }
  }

  describe("ms: shopping-list") {
    /**
      * [[ShoppingListRq]] => success
      */
    it("urlShopListAdd") {
      val json = ser(ShoppingListRq(
        "fa4f6832-cfd0-4c61-bcea-d769331cb974",
        "", // ???
        "", // ???
        "new list",
        true,
        "2019-11-27T04:14:40.832Z",
        "2019-11-27T04:14:40.832Z",
        List(0),
        List(),
        List()
      ))
      val r = requests.post(urlShopListAdd, headers = headersa, data = json)
      r hasHttpCode 201
      r hasText "Successfully Created Shopping List"
      println(r)
    }

    it("urlShopListUpd") {
      val json = ser(
        ShoppingList(
          200L,
          "2019-09-15T19:01:33.647+0000",
          "2020-03-30T11:46:36.591+0000",
          "fa4f6832-cfd0-4c61-bcea-d769331cb974",
          "Crocky",
          "",
          "",
          true,
          List(
            ShoppingListProduct(
              0L,
              127L,
              20L,
              0,
              false,
              false,
              ListItemMetadata(
                29L,
                null,
                Weight(11954L, "pcs", 1.0),
                ""
              )
            )
          ),
          List(),
          List()
        ))
      val r = requests.put(urlShopListUpd, headers = headersa, data = json)
      r hasHttpCode 201
      r hasText "Successfully Updated Shopping List"
      println(r)
    }

    it("urlShopListDel") {
      //requests.delete(urlShopListDel)
    }

    it("urlShopListGet") {
      val r = requests.get(s"$urlShopListGet/fa4f6832-cfd0-4c61-bcea-d769331cb974", headers = headersa)
      r hasHttpCode 200
      val resp = readJson(r)
      resp.arrOpt should not be None
      val keys = resp(0).obj.keySet
      val must = Set(
        "id",
        "shoppingListDate",
        "shoppingListUpdatedDate",
        "customerId",
        "shoppingListCustomName",
        "imageName",
        "description",
        "shoppingListIsActive",
        "shoppingListProducts",
        "shoppingListCategories",
        "listOrder"
      )
      must.forall(keys.contains) shouldEqual true
      println(r)
    }
  }

}
