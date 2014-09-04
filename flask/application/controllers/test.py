from application import app
from flask import request
import json

@app.route('/', methods = ['GET', 'POST'])
def test() :
	if request.method == 'POST' :
		return '\n'.join(['POST REQUEST', 'TEST SUCCEED', request.form['test-name']])
	return 'GET REQUEST'

@app.route('/test-listview', methods=['POST'])
def test_listview() :
	return json.dumps([{'name':'first', 'content':'c-first'},{'name':'second', 'content':'c-second'},{'name':'third', 'content':'c-third'}])