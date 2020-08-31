package request

object Method {
  val GET    = "GET"
  val POST   = "POST"
  val PUT    = "PUT"
  val DELETE = "DELETE"
}

object Endpoints {
  /** BASE URL */
  val urlBase     = "http://aacctt.ddns.net:19901"
  /**
    * MICROSERVICE-BASED URLS.
    * 30.08.2020 - 4 pcs
    */
  val urlAuth     = s"$urlBase/auth-service"
  val urlOptimize = s"$urlBase/optimisation-service"
  val urlProdComp = s"$urlBase/product-composite-service"
  val urlShopList = s"$urlBase/shopping-list-service"

  /**
    * concrete urls by microservice
    */
  val urlAuthReg      = s"$urlAuth/ss/account/register"                              // POST
  val urlLogin        = s"$urlAuth/ss/account/login"                                 // POST
  val urlLogout       = s"$urlAuth/ss/account/logout"                                // DELETE
  val urlForgotPwd    = s"$urlAuth/ss/account/forgot/password"                       // POST
  val urlRefreshToken = s"$urlAuth/ss/account/token/refresh"                         // POST
  val urlUpdatePasswd = s"$urlAuth/ss/account/password"                              // PUT
  val urlAccUpdate    = s"$urlAuth/ss/account/update"                                // PUT
  val urlAccDetails   = s"$urlAuth/ss/account"                                       // GET
  
  val urlOptimizeList = s"$urlOptimize/cp/optimize/shoppinglist"                     // POST
  
  val urlProdCompByLoc= s"$urlProdComp/es/get/categories/products/shops/by/location" // POST
  
  val urlShopListAdd  = s"$urlShopList/ss/add/shoppingList"                          // POST
  val urlShopListDel  = s"$urlShopList/ss/delete/order/{id}"                         // DELETE
  val urlShopListUpd  = s"$urlShopList/ss/update/shoppingList"                       // PUT
  val urlShopListGet  = s"$urlShopList/ss/all/shoppingLists"                         // GET /{id}
  
}
