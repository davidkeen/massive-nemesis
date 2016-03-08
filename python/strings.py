"""
Generates random alphanumeric strings.

First argument is the length of the strings to generate.
Second argument is the number of strings to generate.
"""

import random
import string
import sys

length = int(sys.argv[1])
count = int(sys.argv[2])
chars = str(string.ascii_uppercase + string.digits).translate(None, 'ACDEFIOSUYZ0125')

strings = set()
while len(strings) < count:
    strings.add(''.join(random.choice(chars) for x in range(length)))

print('\n'.join(strings))
