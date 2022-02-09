# This file covers off driving the API independent of where the cluster is
# running.
# Be sure to set your context appropriately for the log monitor.
#
# The intended approach to working with this makefile is to update select
# elements (body, id, IP, port, etc) as you progress through your workflow.
# Where possible, stodout outputs are tee into .out files for later review.
#


KC=kubectl
CURL=curl

PODS1=pod/cmpt756s1-8557865b4b-jnwrj
PODCONT=service1

logs:
	$(KC) logs $(PODS1) -c $(PODCONT)

#IGW=a6fd5e2247f1d4819a48bbc17ce5c517-1000820680.us-west-1.elb.amazonaws.com:80
IGW=127.0.0.1:80

# stock body & fragment for API requests
BODY_CUSTOMER= { \
"fname": "virat", \
"email": "virat@sfu.ca", \
"lname": "kohli"\
}

BODY_TRANS= { \
  "TransactionType": "debit", \
  "AccountId": "2820d384-257d-452f-84d4-720e950c3726", \
  "Amount": 350\
} 

BODY_ACCOUNT_CREATE= { \
  "CustomerId": "7fde64d7-3e9b-4b92-b4b0-809611402219", \
  "AccountType": "Checking", \
  "Balance": 95478 \
}

BODY_ACCOUNT= { \
  "Balance": 600 \
}

BODY_CID= { \
    "cid": "c63cb402-1fa8-4436-81db-a1f39184559f" \
}

BODY_ACCOUNT_UPDATE= { \
  "Balance": 5000 \
}

ACCOUNT_ID=07ec7c68-b5a6-47d4-a6fc-2a44d5f1f8c7
TRANS_ID=0f236159-4bbf-449d-89a1-31784936b16d
CUSTOMER_ID=c63cb402-1fa8-4436-81db-a1f39184559f

# this is a token for ???
#TOKEN=Authorization: Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjoiMTA0YTMyZDktNTYwMS00NThlLTk2MGUtOTAxOWM4ZTk2MTIxIiwidGltZSI6MTYwNjI2MDIzOS4xMTI1Mn0.iR-fRs1E8JCNf0lN5FdjzksW6a4kxfrKb5rDYGTQxw8
#BODY_TOKEN={ \
#    "jwt": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjoiMTA0YTMyZDktNTYwMS00NThlLTk2MGUtOTAxOWM4ZTk2MTIxIiwidGltZSI6MTYwNjI2MDIzOS4xMTI1Mn0.iR-fRs1E8JCNf0lN5FdjzksW6a4kxfrKb5rDYGTQxw8" \

TOKEN=Authorization: Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjoiYzYzY2I0MDItMWZhOC00NDM2LTgxZGItYTFmMzkxODQ1NTlmIiwidGltZSI6MTYwNjk1NzIxNC4zMTYzNDd9.OPuHgB6aK51oLPk4z-_w-TZ_zCVQ0nPAsShZWNdtpMc
BODY_TOKEN={ \
    "jwt": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjoiYzYzY2I0MDItMWZhOC00NDM2LTgxZGItYTFmMzkxODQ1NTlmIiwidGltZSI6MTYwNjk1NzIxNC4zMTYzNDd9.OPuHgB6aK51oLPk4z-_w-TZ_zCVQ0nPAsShZWNdtpMc" \



# POST is used for user (apipost) to create a new record
ccustomer:
	echo curl --location --request POST 'http://$(IGW)/api/v1/customer/' --header 'Content-Type: application/json' --data-raw '$(BODY_CUSTOMER)' > ccustomer.out
	$(CURL) --location --request POST 'http://$(IGW)/api/v1/customer/' --header 'Content-Type: application/json' --data-raw '$(BODY_CUSTOMER)' | tee -a customer.out

ctrans:
	echo curl --location --request POST 'http://$(IGW)/api/v1/transaction/' --header 'Content-Type: application/json' --data-raw '$(BODY_TRANS)' > ctrans.out
	$(CURL) --location --request POST 'http://$(IGW)/api/v1/transaction/' --header 'Content-Type: application/json' --data-raw '$(BODY_TRANS)' | tee -a ctrans.out
	
caccount:
	echo curl --location --request POST 'http://$(IGW)/api/v1/account/' --header '$(TOKEN)' --header 'Content-Type: application/json' --data-raw '$(BODY_ACCOUNT_CREATE)' > caccount.out
	$(CURL) --location --request POST 'http://$(IGW)/api/v1/account/' --header '$(TOKEN)' --header 'Content-Type: application/json' --data-raw '$(BODY_ACCOUNT_CREATE)' | tee -a caccount.out 
 
 
# PUT is used for user (update) to update a record
##Used for interaction purpose.
uaccount:
	echo curl --location --request PUT 'http://$(IGW)/api/v1/transaction/$(ACCOUNT_ID)' --header '$(TOKEN)' --header 'Content-Type: application/json' --data-raw '$(BODY_ACCOUNT)' > uaccount.out
	$(CURL) --location --request PUT 'http://$(IGW)/api/v1/transaction/$(ACCOUNT_ID)' --header '$(TOKEN)' --header 'Content-Type: application/json' --data-raw '$(BODY_ACCOUNT)' | tee -a uaccount.out

uaccount1:
	echo curl --location --request PUT 'http://$(IGW)/api/v1/account/$(ACCOUNT_ID)' --header '$(TOKEN)' --header 'Content-Type: application/json' --data-raw '$(BODY_ACCOUNT_UPDATE)' > uaccount1.out
	$(CURL) --location --request PUT 'http://$(IGW)/api/v1/account/$(ACCOUNT_ID)' --header '$(TOKEN)' --header 'Content-Type: application/json' --data-raw '$(BODY_ACCOUNT_UPDATE)' | tee -a uaccount1.out

ucustomer:
	echo curl --location --request PUT 'http://$(IGW)/api/v1/user/$(CUSTOMER_ID)' --header '$(TOKEN)' --header 'Content-Type: application/json' --data-raw '$(BODY_CUSTOMER)' > ucustomer.out
	$(CURL) --location --request PUT 'http://$(IGW)/api/v1/user/$(CUSTOMER_ID)' --header '$(TOKEN)' --header 'Content-Type: application/json' --data-raw '$(BODY_CUSTOMER)' | tee -a ucustomer.out

# GET is used to read a record
##Used for interaction purpose.
raccount:
	echo curl --location --request GET 'http://$(IGW)/api/v1/transaction/$(ACCOUNT_ID)' --header '$(TOKEN)' > raccount.out
	$(CURL) --location --request GET 'http://$(IGW)/api/v1/transaction/$(ACCOUNT_ID)' --header '$(TOKEN)' | tee -a raccount.out

raccount1:
	echo curl --location --request GET 'http://$(IGW)/api/v1/account/$(ACCOUNT_ID)' --header '$(TOKEN)' > raccount1.out
	$(CURL) --location --request GET 'http://$(IGW)/api/v1/account/$(ACCOUNT_ID)' --header '$(TOKEN)' | tee -a raccount1.out

##Used for interaction purpose.
rtrans:
	echo curl --location --request GET 'http://$(IGW)/api/v1/transaction/$(TRANS_ID)' --header '$(TOKEN)' > rtrans.out
	$(CURL) --location --request GET 'http://$(IGW)/api/v1/transaction/$(TRANS_ID)' --header '$(TOKEN)' | tee -a rtrans.out


# DELETE is used to delete a record
dtrans:
	echo curl --location --request DELETE 'http://$(IGW)/api/v1/transaction/$(TRANS_ID)' --header '$(TOKEN)' > dtrans.out
	$(CURL) --location --request DELETE 'http://$(IGW)/api/v1/transaction/$(TRANS_ID)' --header '$(TOKEN)' | tee -a dtrans.out

daccount:
	echo curl --location --request DELETE 'http://$(IGW)/api/v1/account/$(ACCOUNT_ID)' --header '$(TOKEN)' > daccount.out
	$(CURL) --location --request DELETE 'http://$(IGW)/api/v1/account/$(ACCOUNT_ID)' --header '$(TOKEN)' | tee -a daccount.out

dcustomer:
	echo curl --location --request DELETE 'http://$(IGW)/api/v1/customer/$(CUSTOMER_ID)' --header '$(TOKEN)' > dcustomer.out
	$(CURL) --location --request DELETE 'http://$(IGW)/api/v1/customer/$(CUSTOMER_ID)' --header '$(TOKEN)' | tee -a dcustomer.out

# login purpose.
apilogin:
	echo curl --location --request PUT 'http://$(IGW)/api/v1/customer/login' --header 'Content-Type: application/json' --data-raw '$(BODY_CID)' > apilogin.out
	$(CURL) --location --request PUT 'http://$(IGW)/api/v1/customer/login' --header 'Content-Type: application/json' --data-raw '$(BODY_CID)' | tee -a apilogin.out

apilogoff:
	echo curl --location --request PUT 'http://$(IGW)/api/v1/customer/logoff' --header 'Content-Type: application/json' --data-raw '$(BODY_TOKEN)' > apilogoff.out
	$(CURL) --location --request PUT 'http://$(IGW)/api/v1/customer/logoff' --header 'Content-Type: application/json' --data-raw '$(BODY_TOKEN)' | tee -a apilogoff.out



