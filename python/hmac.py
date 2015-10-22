import hmac
import hashlib
import datetime
import pytz
import base64

uri = '/api/example'
access_key = 'MY_ACCESS_KEY'

# Base64 encoded
secret_key = 'TVlfU0VDUkVUX0tFWQ=='

local = pytz.timezone('Europe/London')
local_dt = local.localize(datetime.datetime.today(), is_dst=None)
gmt_dt = local_dt.astimezone(pytz.timezone('GMT'))
date = gmt_dt.strftime('%a, %d %b %Y %H:%M:%S %Z')

message = ':'.join([date, uri])

digest = hmac.new(secret_key.decode("base64"), message, digestmod=hashlib.sha1).digest()
sig = base64.encodestring(digest).rstrip()

print 'Authorization: ' + access_key + ':' + sig
