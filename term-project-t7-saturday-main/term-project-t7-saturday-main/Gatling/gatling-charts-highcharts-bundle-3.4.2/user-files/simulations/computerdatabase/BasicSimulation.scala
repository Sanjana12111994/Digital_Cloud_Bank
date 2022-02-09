/*
 * Copyright 2011-2020 GatlingCorp (https://gatling.io)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import scala.concurrent.duration.DurationInt
import scala.concurrent.duration._
import scala.util.Random
import io.gatling.core.Predef._
import io.gatling.http.Predef._

class BasicSimulation extends Simulation {

  val y = Iterator.continually(
    Map(
      "randfname" -> (Random.nextInt(99)),
      "randlname" -> (Random.nextInt(99)),
      "randemail" -> (Random.nextInt(99)),
      "IGW" -> ("127.0.0.1")
      // "afc033ba1409146a1bf7531ecd5842ad-2083102065.us-west-1.elb.amazonaws.com"
    )
  )

  /* ********************************************************************************************************
  ******************************************** Customer *****************************************************
  ******************************************************************************************************** */

  object CreateCustomer {
    val createcustomer = exec(http("Request Name = Create Customer POST")
      .post("http://${IGW}:80/api/v1/customer/")
      .header("content-type" , "application/json")
      .body(StringBody(string = """{  "fname": "${randfname}" ,"email": "${randemail}","lname": "${randlname}" }"""))
      .check(status.not(404), status.not(500))
      .check(jsonPath("$..customer_id").ofType[String].saveAs("customer_id"))
      )
  }
  
  object LoginCustomer {
    val logincustomer = exec(http("Request Name = Login Customer PUT")
      .put("http://${IGW}:80/api/v1/customer/login")
      .header("content-type" , "application/json")
      .body(StringBody(string = """{   "cid": "${customer_id}" }"""))
      .check(bodyString.saveAs("ResponseTokenLogin"))
      )
  }

  object LogoffCustomer {
    val logoffcustomer = exec(http("Request Name = Logoff Customer PUT")
      .put("http://${IGW}:80/api/v1/customer/logoff")
      .header("content-type" , "application/json")
      .body(StringBody(string = """{   "jwt": "${ResponseTokenLogin}" }"""))
      .check(bodyString.saveAs("ResponseTokenLogoff"))
      )
  }

  object UpdateCustomer {
    val updatecustomer = exec(http("Request Name = Update Customer PUT")
      .put("http://${IGW}:80/api/v1/customer/${customer_id}")
      .header("content-type" , "application/json")
      .header("authorization", "ResponseTokenLogin")
      .body(StringBody(string = """{  "fname": "updated" ,"email": "updated","lname": "updated" }"""))
      //.check(bodyString.saveAs("ResponseTokenUpdateCustomer"))
      )
  }

  object DeleteCustomer {
    val deletecustomer = exec(http("Request Name = Delete Customer DELETE")
      .delete("http://${IGW}:80/api/v1/customer/${customer_id}")
      .header("authorization", "ResponseTokenLogin")
      //.check(bodyString.saveAs("ResponseTokenDeleteCustomer"))
      )
  }

  object ReadCustomer {
    val readcustomer = exec(http("Request Name = Read Customer GET")
      .get("http://${IGW}:80/api/v1/customer/4ab31555-ad0b-495d-bab6-531eca7b4643")
      .header("authorization", "ResponseTokenLogin")
      //.check(bodyString.saveAs("ResponseTokenReadCustomer"))
      )
  }

  /* ********************************************************************************************************
  ******************************************** Account  *****************************************************
  ******************************************************************************************************** */

  object CreateAccount {
    val createaccount = exec(http("Request Name = Create Account POST")
      .post("http://${IGW}:80/api/v1/account/")
      .header("content-type" , "application/json")
      .header("authorization", "ResponseTokenLogin")
      .body(StringBody(string = """{  "CustomerId": "${customer_id}" ,"AccountType": "Savings", "Balance": 20000 }"""))
      .check(status.not(404), status.not(500))
      .check(jsonPath("$..account_id").ofType[String].saveAs("account_id"))
      )
  }

  object UpdateAccount {
    val updateaccount = exec(http("Request Name = Update Account PUT")
      .put("http://${IGW}:80/api/v1/account/${account_id}")
      .header("content-type" , "application/json")
      .header("authorization", "ResponseTokenLogin")
      .body(StringBody(string = """{  "Balance": 10000 }"""))
      //.check(bodyString.saveAs("ResponseTokenUpdateAccount"))
      )
  }

  object DeleteAccount {
    val deleteaccount = exec(http("Request Name = Delete Account DELETE")
      .delete("http://${IGW}:80/api/v1/account/${account_id}")
      .header("authorization", "ResponseTokenLogin")
      //.check(bodyString.saveAs("ResponseTokenDeleteAccount"))
      )
  }

  object ReadAccount {
    val readaccount = exec(http("Request Name = Read Account GET")
      .get("http://${IGW}:80/api/v1/account/${account_id}")
      .header("authorization", "ResponseTokenLogin")
      .check(bodyString.saveAs("ResponseTokenReadAccount"))
      )
  }

  /* ********************************************************************************************************
  ******************************************* Transaction ***************************************************
  ******************************************************************************************************** */

  object CreateTransaction {
    val createtransaction = exec(http("Request Name = Create Transaction POST")
      .post("http://${IGW}:80/api/v1/transaction/")
      .header("content-type" , "application/json")
      .header("authorization", "ResponseTokenLogin")
      .body(StringBody(string = """{  "AccountId": "${account_id}" ,"TransactionType": "credit", "Amount": 500 }"""))
      .check(status.not(404), status.not(500))
      .check(bodyString.saveAs("RESPONSE BODY TRANSACTION"))
      .check(jsonPath("$..transaction_id").ofType[String].saveAs("transaction_id"))
      )
  }

  object DeleteTransaction {
    val deletetransaction = exec(http("Request Name = Delete Transaction DELETE")
      .delete("http://${IGW}:80/api/v1/transaction/${transaction_id}")
      .header("authorization", "ResponseTokenLogin")
      .check(bodyString.saveAs("ResponseTokenDeleteTransaction"))
      )
  }

  object ReadTransaction {
    val readtransaction = exec(http("Request Name = Read Transaction GET")
      .get("http://${IGW}:80/api/v1/account/${account_id}")
      .header("authorization", "ResponseTokenLogin")
      .check(bodyString.saveAs("ResponseTokenReadTransaction"))
      )
  }

  /* ********************************************************************************************************
  ******************************************** Scenarios ****************************************************
  ******************************************************************************************************** */

  // Coverage Test
  // This Scenario will 1. create a customer, 2. use its saved session customer_id to login, 
  // and 3. use the saved bearer token for authenticating the update request. Then,
  // 4. create account, 5. read account, 6. update account, 7. create transaction, 8. read transaction,
  // 9. delete transaction, 10. delete account, 11. logoff customer.

  val FullCoverageScenario = scenario("Your Scenario Name is => FullCoverage")
              .feed(y)
              .exec(CreateCustomer.createcustomer)
              .exec { session =>
                      println("1. After CREATE!!!!!!\n\n"+session+"\n\n"+session("customer_id").as[String]+"\n\nDone\n\n")                  
                      session
                    }
              .exec(LoginCustomer.logincustomer)
              .exec { session =>
                      println("2. After LOGIN!!!!!!\n\n"+session+"\n\n"+session("ResponseTokenLogin").as[String]+"\n\nDone\n\n")                  
                      session
                    }
              .exec(UpdateCustomer.updatecustomer)
              .exec { session =>
                      println("3. After UPDATE!!!!!!\n\n"+session+"\n\nDone\n\n")                  
                      session
                    }
              .exec(CreateAccount.createaccount)
              .exec { session =>
                      println("4. After CREATE ACCOUNT!!!!!!\n\n"+session+"\n\nDone\n\n")                  
                      session
                    }
              .exec(ReadAccount.readaccount)
              .exec { session =>
                      println("5. After READ ACCOUNT!!!!!!\n\n"+session+"\n\nDone\n\n")                  
                      session
                    }     
              .exec(UpdateAccount.updateaccount)
              .exec { session =>
                      println("6. After UPDATE ACCOUNT!!!!!!\n\n"+session+"\n\nDone\n\n")                  
                      session
                    }
              .exec(CreateTransaction.createtransaction)
              .exec { session =>
                      println("7. After CREATE TRANSACTION!!!!!!\n\n"+session+"\n\nDone\n\n")                  
                      session
                    }
              .exec(DeleteTransaction.deletetransaction)
              .exec { session =>
                      println("9. After DELETE TRANSACTION!!!!!!\n\n"+session+"\n\nDone\n\n")                  
                      session
                    }
              .exec(DeleteAccount.deleteaccount)
              .exec { session =>
                      println("10. After DELETE ACCOUNT!!!!!!\n\n"+session+"\n\nDone\n\n")                  
                      session
                    }
              .exec(LogoffCustomer.logoffcustomer)
              .exec { session =>
                      println("11. After LOGOFF CUSTOMER!!!!!!\n\n"+session+"\n\nDone\n\n")                  
                      session
                    }

  // Uncomment the next line to run a Coverage Test
  //setUp(FullCoverageScenario.inject(atOnceUsers(1)))


  // Load Test
  val LoadTestScenario = scenario("Your Scenario Name is => LoadTest")
              .forever(){
              feed(y)
              .exec(CreateCustomer.createcustomer)
              .exec(LoginCustomer.logincustomer)
              .exec(UpdateCustomer.updatecustomer)
              .exec(CreateAccount.createaccount)
              .exec(ReadAccount.readaccount) 
              .exec(UpdateAccount.updateaccount)
              .exec(CreateTransaction.createtransaction)
              .exec(DeleteTransaction.deletetransaction)
              .exec(DeleteAccount.deleteaccount)
              .exec(DeleteCustomer.deletecustomer)
              }
  // Uncomment the next line to run a Load Test for 30 minutes
  //setUp(LoadTestScenario.inject(atOnceUsers(50))).maxDuration(30 minutes)
  
}
