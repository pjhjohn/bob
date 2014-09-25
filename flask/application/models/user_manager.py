from application import db
from schema import User, Request, Device

# # when the user signs up, add a row containing the user_id and the password
def add(user_id, password, phone_number, user_name):
	user = User(
		user_id 		= user_id,
		password 		= db.func.md5(password),
		phone_number 	= phone_number,
		user_name 		= user_name,
		hunger_status 	= 'H'
		)
	db.session.add(user)
	db.session.commit()


# # when the user verifies his or her phone number, add the phone number to the existing row
def add_phone_number(user_id, phone_number):
	user = User.query.filter(User.user_id == user_id).scalar()
	if bool(user):
		user.phone_number = phone_number


# # when an existing user logs in again
def login(user_id, password):
	return bool(User.query.filter(User.user_id == user_id, User.password == db.func.md5(password)).count())


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

def get_primary_key(user_id):
	return User.query.filter(User.user_id == user_id).scalar().id

# takes in a list of contacts(phone numbers) and returns a list of dictionaries containing the phone_number and the id(null if doesn't exist)
def analyze_contacts(contacts):
	analyzed_contacts = {}
	user_ids = []
	existing_users = User.query.filter(User.phone_number.in_(contacts)).all()
	for user in existing_users:
		analyzed_contacts[user.phone_number] = user.user_id
	for number in contacts:
		number = str(number)
		user_ids.append(analyzed_contacts.get(number))
	return user_ids

def set_hungry(user_id):
	user = User.query.filter(User.user_id == user_id).scalar()
	if bool(user):
		user.hunger_status = 'H'

def set_full(user_id):
	user = User.query.filter(User.user_id == user_id).scalar()
	if bool(user):
		user.hunger_status = 'F'

def add_device(user_id, device_id, registration_id):
	device = Device(
	device_id		= device_id,
	owner_id		= get_primary_key(user_id),
	registration_id	= registration_id
	)
	db.session.add(device)
	db.session.commit()

def get_registration_ids(user_id):
	regids = []
	for device in User.query.filter(User.user_id == user_id).scalar().devices.all():
		regids.append(device.registration_id)
	return regids