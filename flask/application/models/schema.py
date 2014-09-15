from application import db

class User(db.Model) :
	id				= db.Column(db.Integer, primary_key = True)
	user_id			= db.Column(db.String(45))
	password		= db.Column(db.String(100))
	phone_number	= db.Column(db.String(15))
	user_name		= db.Column(db.String(60))
	hunger_status	= db.Column(db.Enum('H', 'F'))
	hunger_time		= db.Column(db.DateTime, default = db.func.now(), onupdate = db.func.now())

class Device(db.Model)	:
	id				= db.Column(db.Integer, primary_key = True)
	device_id		= db.String(db.String(60))
	owner_id		= db.Column(db.Integer, db.ForeignKey('user.id'))
	owner			= db.relationship('User', backref = db.backref('devices', cascade = 'all, delete-orphan', lazy = 'dynamic'))

class Request(db.Model) :
	id				= db.Column(db.Integer, primary_key = True)
	sender_id		= db.Column(db.Integer, db.ForeignKey('user.id'))
	sender			= db.relationship('User', foreign_keys=[sender_id], backref = db.backref('sent_requests', cascade = 'all, delete-orphan', lazy = 'dynamic'))
	receiver_id		= db.Column(db.Integer, db.ForeignKey('user.id'))
	receiver		= db.relationship('User', foreign_keys=[receiver_id], backref = db.backref('received_requests', cascade = 'all, delete-orphan', lazy = 'dynamic'))
	request_time	= db.Column(db.DateTime, default = db.func.now(), onupdate = db.func.now())
