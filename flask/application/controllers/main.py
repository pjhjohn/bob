from application import app
from flask import request
from application.models import user_manager
import json

@app.route('/check-user-id', methods = ['POST'])
def check_user_id():
	return bool(user_manager.get_by_user_id(json.loads(request.form['user-id'])))

@app.route('/signup', methods = ['POST'])
def signup():
	if not bool(user_manager.get_by_user_id(json.loads(request.form['user-id']))):
		user_manager.add(json.loads(request.form))
		return 'Success'
	else:
		return 'Fail'

@app.route('/contacts', methods = ['POST'])
def contacts():
	return json.dumps({'user-id-list':user_manager.analyze_contacts(json.loads(request.form['phone-number-list']))})

@app.route('/test-listview', methods=['POST'])
def test_listview():
	return json.dumps([{'name':'first', 'content':'c-first'},{'name':'second', 'content':'c-second'},{'name':'third', 'content':'c-third'}])

@app.route('/bob', methods = ['POST'])
def bob():
	return None

@app.route('/test', methods = ['GET', 'POST'])
def test():
	# new_user = {'user-id':'null', 'phone-number':'01068799209', 'user-name':'loser', 'hunger-status':'H', 'password':'ehfhfhfh'}
	# user_manager.add(new_user)
	return str(user_manager.get_by_user_id('null').phone_number)
