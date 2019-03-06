#!/usr/bin/env python3
import os
from Crypto.Cipher import AES
from Crypto.Hash import SHA256
from Crypto.Util import Counter
from Crypto.Util.number import *
from Crypto.PublicKey import RSA
import gmpy2

pubkey = RSA.importKey(open("pubkey.txt").read())
e = pubkey.e
n = pubkey.n
rsa_enc = int.from_bytes(open("rsa.enc","rb").read(), 'big')

assert GCD(2, n) == 1
# 680 * 3 = 2040
inv = pow(inverse(2, n), 2040, n)
aes_enc = open("aes.enc","rb").read()

# From here we get the size of the actual flag
assert len(aes_enc) == 43
print(int.from_bytes(b"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa".ljust(128,b'\0'), 'big') == int.from_bytes(b"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", 'big') << 680)

rsa_enc = rsa_enc*inv % n
print(rsa_enc)
for i in range(1000):
    ans = gmpy2.iroot(rsa_enc + i*n, 3)[1]
    if ans == True:
        print("Gotit", i)
        pt = int(gmpy2.iroot(rsa_enc + i*n, 3)[0])
        print(pt.to_bytes(43, 'big'))
        break