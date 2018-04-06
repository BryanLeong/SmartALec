import firebase_admin
from firebase_admin import db
from firebase_admin import credentials
from firebase_admin import auth
from csv import DictReader

firebase_admin.initialize_app(credentials.Certificate('key.json'), {'databaseURL': 'https://sicc-6a316.firebaseio.com/'})

try:
    csvReader = DictReader(open('courses.csv'))
    for row in csvReader:
        courseDetails = {'id': str(row['id']), 'name': row['name'], 'instructors': row['instructors']}
        db.reference(path = '/courses').child(row['id']).set(courseDetails)
except FileNotFoundError:
    print('Error: courses.csv not found')