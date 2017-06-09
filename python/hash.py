import hashlib

CONTENT = b'foo'

print('Algorithms available: ', hashlib.algorithms_available)
print('Algorithms guaranteed: ', hashlib.algorithms_guaranteed)

print('{0: >8}'.format('CONTENT:'), CONTENT.decode())

md5 = hashlib.md5(CONTENT)
md5_hex = md5.hexdigest()
print('{0: >8}'.format('MD5:'), md5_hex)

sha1 = hashlib.sha1(CONTENT)
sha1_hex = sha1.hexdigest()
print('{0: >8}'.format('SHA1:'), sha1_hex)

sha256 = hashlib.sha256(CONTENT)
sha256_hex = sha256.hexdigest()
print('{0: >8}'.format('SHA256:'), sha256_hex)
