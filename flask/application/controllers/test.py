from application import app
from flask import request

@app.route('/', methods = ['GET', 'POST'])
def test() :
	if request.method == 'POST' :
		return '\n'.join(['POST REQUEST', 'TEST SUCCEED', request.form['test-name']])
	return 'GET REQUEST'