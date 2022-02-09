'''  
*****ACCOUNT API******
1. Create ACCOUNT
2. Update ACCOUNT
3. Get ACCOUNT
4. Delete ACCOUNT
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

@bp.route('/<account_id>', methods=['GET'])
def get_account(account_id):
    headers = request.headers
    # check header here
    if 'Authorization' not in headers:
        return Response(json.dumps({"error": "missing auth"}), status=401, mimetype='application/json')
    payload = {"objtype": "account", "objkey": account_id}
    url = db['name'] + '/' + db['endpoint'][0]
    response = requests.get(url, params = payload, headers = {'Authorization': headers['Authorization']})
    return (response.json())

@bp.route('/', methods=['POST'])
def create_account():
    headers = request.headers
    # check header here
    if 'Authorization' not in headers:
        return Response(json.dumps({"error": "missing auth"}), status=401, mimetype='application/json')
    try:
        content = request.get_json()
        CustomerId = content['CustomerId']
        AccountType = content['AccountType']
        Balance = content['Balance']
    except: 
        return json.dumps({"message": "error reading arguments"})
    url = db['name'] + '/' + db['endpoint'][1]
    response = requests.post(url, json = {"objtype": "account", "CustomerId":CustomerId,"AccountType":AccountType, "Balance": Balance}, headers = {'Authorization': headers['Authorization']})
    return (response.json())

#Update account when a transaction is done 
@bp.route('/<account_id>', methods=['PUT'])    
def update_account(account_id):
    headers = request.headers
    if 'Authorization' not in headers:
        return Response(json.dumps({"error": "missing auth"}), status=401, mimetype='application/json')
    
    try:
        content = request.get_json()
        #AccountNumber = content['AccountNumber']
        Balance = content['Balance']
    except:
        return json.dumps({"message": "error reading arguments"})
    url = db['name'] + '/' + db['endpoint'][3]
    response = requests.put(url, params = {"objtype": "account", "objkey": account_id}, json = {"Balance": Balance})
    return (response.json())
#Update function end

@bp.route('/<account_id>', methods=['DELETE'])
def delete_account(account_id):
    headers = request.headers
    # check header here
    if 'Authorization' not in headers:
        return Response(json.dumps({"error": "missing auth"}), status=401, mimetype='application/json')
    url = db['name'] + '/' + db['endpoint'][2]
    response = requests.delete(url, params = { "objtype": "account", "objkey": account_id}, headers = {'Authorization': headers['Authorization']})
    return (response.json())

app.register_blueprint(bp, url_prefix='/api/v1/account/')

if __name__ == '__main__':
    if len(sys.argv) < 2:
        logging.error("missing port arg 1")
        sys.exit(-1)

    p = int(sys.argv[1])
    app.run(host='0.0.0.0', port=p, threaded=True)
    