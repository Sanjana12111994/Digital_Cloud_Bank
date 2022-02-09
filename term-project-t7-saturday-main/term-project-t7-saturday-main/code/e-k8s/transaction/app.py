'''  
*****TRANSACTION API******
1. Create Transaction(Interaction with account api)
2. Delete Transaction
3. Get Transaction
***********************
'''


from flask import Flask
import requests
import sys
import logging
import simplejson as json
import urllib
from flask import request
from flask import Response
from flask import Blueprint
app = Flask(__name__)

db = {
    "name": "http://cmpt756db:30002/api/v1/datastore",
    "endpoint": [
        "read",
        "write",
        "delete",
        "update"
    ]
}
bp = Blueprint('app', __name__)
@bp.route('/health')
def health():
    return Response("", status=200, mimetype="application/json")

@bp.route('/readiness')
def readiness():
    return Response("", status=200, mimetype="application/json")

@bp.route('/', methods=['GET'])
def list_all():
    headers = request.headers
    # check header here
    if 'Authorization' not in headers:
        return Response(json.dumps({"error": "missing auth"}), status=401, mimetype='application/json')
    # list all songs here
    return {}

@bp.route('/<transaction_id>', methods=['GET'])
def get_transaction(transaction_id):
    headers = request.headers
    # check header here
    if 'Authorization' not in headers:
        return Response(json.dumps({"error": "missing auth"}), status=401, mimetype='application/json')
    payload = {"objtype": "transaction", "objkey": transaction_id}
    url = db['name'] + '/' + db['endpoint'][0]
    response = requests.get(url, params = payload, headers = {'Authorization': headers['Authorization']})
    return (response.json())

@bp.route('/', methods=['POST'])
@bp.route('/', methods=['GET'])
@bp.route('/', methods=['PUT'])
def create_transaction():
    headers = request.headers
    # check header here
    if request.method == 'POST':
        try:
            content = request.get_json()
            TransactionType = content['TransactionType']
            AccountId = content['AccountId']
            Amount = content['Amount'] 
        except: 
            return json.dumps({"message": "error reading arguments"})
        url = db['name'] + '/' + db['endpoint'][1]
        response0 = requests.post(url, json = {"objtype": "transaction", "TransactionType":TransactionType, "AccountId": AccountId, "Amount": Amount})
        amount = content['Amount']
        transactionType = content['TransactionType']
        payload = {"objtype": "account", "objkey": content['AccountId']}
        url = db['name'] + '/' + db['endpoint'][0]
        response1 = requests.get(url, params = payload)
        details = response1.json()
        details = details['Items'][0]
        account_id = details['account_id']
        balance = details['Balance']
        if transactionType == 'credit':
            new_balance = balance + amount
        else:
            new_balance = balance - amount
        url = db['name'] + '/' + db['endpoint'][3]
        response = requests.put(url, params = {"objtype": "account", "objkey": account_id}, json = {"Balance": new_balance})
        return (response0.json())

@bp.route('/<transaction_id>', methods=['DELETE'])
def delete_transaction(transaction_id):
    headers = request.headers
    # check header here
    if 'Authorization' not in headers:
        return Response(json.dumps({"error": "missing auth"}), status=401, mimetype='application/json')
    url = db['name'] + '/' + db['endpoint'][2]
    response = requests.delete(url, params = { "objtype": "transaction", "objkey": transaction_id}, headers = {'Authorization': headers['Authorization']})
    return (response.json())
        
app.register_blueprint(bp, url_prefix='/api/v1/transaction/')

if __name__ == '__main__':
    if len(sys.argv) < 2:
        logging.error("missing port arg 1")
        sys.exit(-1)

    p = int(sys.argv[1])
    app.debug = True
    app.run(host='0.0.0.0', port=p, threaded=True)
