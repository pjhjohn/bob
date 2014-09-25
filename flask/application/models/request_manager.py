from application import db
from schema import User, Request
import user_manager

def add(sender_user_id, receiver_user_id):
	request = Request(
		serder_id = user_manager.get_primary_key(sender_user_id),
		receiver_id = user_manager.get_primary_key(receiver_user_id)
	)