# tprj/code
This directory holds the assets for building and deploying the banking application micro services.

We have the following services:

### Customer Service
- Create customer: insert a new customer record in the customer table
- Update customer: modify an existing customer record in the customer table
- Delete customer: delete the record from the customer table
- Login customer: Authenticate a customer to interact with the system
- Logoff customer: Log off a customer for complete authentication
### Account Service
- Open account: Insert a new record in the account table
- Update account: Modify the existing account record in the account table
- Close account: Delete the record in the account table
### Transaction Service
- Make payment (credit/debit) : Update the balance in the account table
        	    Insert the record in the transaction table
- Make deposit:   Update the balance in the account table
                Insert the record in the transaction table
- Check balance: Fetch the record from the account table

All the above service rely on the following db proxy service.
# db proxy service
- Create
- Read
- Update
- Delete