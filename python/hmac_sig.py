import hmac
import hashlib
import datetime
import pytz
import base64

uri = '/api/example'

# Base64 encoded string
access_key = 'QUNDRVNTS0VZ'

# Base64 encoded string
secret_key = 'UEFTU1dPUkQx'

local = pytz.timezone('Europe/London')
local_dt = local.localize(datetime.datetime.today(), is_dst=None)
gmt_dt = local_dt.astimezone(pytz.timezone('GMT'))
date = gmt_dt.isoformat()

message = ':'.join([date, uri])

digest = hmac.new(base64.b64decode(secret_key), message.encode(), digestmod=hashlib.sha1).digest()
sig = base64.b64encode(digest).rstrip()

print('date: ' + date)
print('uri: ' + uri)
print('Signature: ' + access_key + ':' + sig.decode())
