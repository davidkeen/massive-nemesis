"""
A script to extract mongo data to csv.
"""

import csv
from pymongo import MongoClient

client = MongoClient('example.com', 27017)
db = client.my_db
users = db.users
with open('out.csv', 'wb') as f:
    writer = csv.writer(f, quoting=csv.QUOTE_NONNUMERIC)
    writer.writerow(('user_id', 'first_name', 'last_name', 'email', 'phone_number'))
    for user in users.find({'firstName': 'david'}):
        writer.writerow((user['_id'], user['firstName'].encode('utf-8'), user['lastName'].encode('utf-8'),
                         user['email'].encode('utf-8'),
                         user['phoneNumber'].encode('utf-8') if 'phoneNumber' in user else None))
