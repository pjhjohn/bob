from application import app
from flask import request
from application.models import user_manager
import json
import urllib2
from application.models.schema import User


@app.route('/check-user-id', methods = ['POST'])
def check_user_id():
	return bool(user_manager.get_by_user_id(json.loads(request.form['user-id'])))

@app.route('/signup', methods = ['POST'])
def signup():
	data = json.loads(request.form)
	if not bool(user_manager.get_by_user_id(data['user-id'])):
		user_manager.add(data['sign-up-id'], data['sign-up-pw'], data['phone-number'], data['user-name'])
		return 'Success'
	else:
		return 'Fail'

@app.route('/login', methods = ['POST'])
def login():
	data = json.loads(request.form)
	if user_manager.login(data['user-id'], data['password']):
		return 'Success'
	else:
		return 'Fail'

@app.route('/contacts', methods = ['POST'])
def contacts():
	return json.dumps({'user-id-list':user_manager.analyze_contacts(json.loads(request.form['phone-number-list']))})

@app.route('/bob', methods = ['GET', 'POST'])
def bob():
	data = json.loads(request.form)
	url = 'http://android.googleapis.com/gcm/send'
	headers = {'Authorization':'key=AIzaSyDjqsvPD4dJCV4HnEtFY0ay6n4EmGGekQ0', 'Content-Type':'application/json'}
	registration_id = user_manager.get_registration_ids(data['user_id'])
	return None

@app.route('/register', methods = ['POST'])
def register():
	data = json.loads(request.form)
	user_manager.add_device(data['user-id'], data['device-id'], data['registration-id'])

@app.route('/test', methods = ['GET', 'POST'])
def test():
	return str(user_manager.get_registration_ids('azulpanda'))