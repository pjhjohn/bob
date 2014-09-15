from application import db
from schema import User

# # when the user signs up, add a row containing the user_id and the password
def add(data):
	user = User(
		user_id 		= data['user-id'],
		password 		= db.func.md5(data['password']),
		phone_number 	= data['phone-number'],
		user_name 		= data['user-name'],
		hunger_status 	= data['hunger-status']
		)
	db.session.add(user)
	db.session.commit()


# # when the user verifies his or her phone number, add the phone number to the existing row
# def add_phone_number(data):


# # when an existing user logs in again
# def login(data):


# when signing up, check the user_id and return taken or not
def get_by_user_id(user_id):
	return User.query.filter(User.user_id == user_id).scalar()


# checks if a phone_number registered in user_list, returns the user_id if it is, and null if not
def check_phone_number(phone_number):
	user = User.query.filter(User.phone_number == phone_number)
	if user.count() > 0:
		return user.all()[0].user_id
	else:
		return None


# takes in a list of contacts(phone numbers) and returns a list of dictionaries containing the phone_number and the id(null if doesn't exist)
def analyze_contacts(contacts):
	analyzed_contacts = []
	for contact in contacts:
		number = contact['phone-number']
		analyzed_contacts.append({'phone-number':number, 'user-id':check_phone_number(number)})
	return analyzed_contacts