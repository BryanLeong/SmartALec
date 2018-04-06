import firebase_admin
from firebase_admin import db
from firebase_admin import credentials
from firebase_admin import auth
from csv import DictReader
from time import sleep

firebase_admin.initialize_app(credentials.Certificate('key.json'), {'databaseURL': 'https://sicc-6a316.firebaseio.com/'})

try:
    csvReader = DictReader(open('users.csv'))
    for row in csvReader:
        try:
            user = auth.get_user(row['uid'])
        except auth.AuthError:
            print('User ' + row['uid'] + ' not found. Attempting to create new user...')
            try:
                user = auth.create_user(uid = row['uid'], email = row['email'], password = 'Password')
                sleep(1)
            except auth.AuthError as e:
                print('Could not create new user ' + row['uid'])
                print(e.detail)
        userProfile = {'id': str(row['uid']), 'name': row['name'], 'type': row['type'], 'courses': row['courses'].split('/')}
        db.reference(path = '/users/').child(row['uid']).set(userProfile)
except FileNotFoundError:
    print('Error: users.csv not found')
            