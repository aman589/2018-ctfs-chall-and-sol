#!/usr/bin/python
from Crypto.Util.number import *
from Crypto.PublicKey import RSA
import random
flag = "CTF{you_got_it_nowasjbdadbajbdjabd}"

m = bytes_to_long(flag)

key = RSA.generate(1024)

c = pow(m, key.e, key.n)
print("Welcome to BACKDOORCTF17\n")
print("PublicKey:\n")
print("N = " + str(key.n) + "\n")
print("e = " + str(key.e) + "\n")
print("c = " + str(c) + "\n")

while True:
    try:
        temp_c = int(raw_input("temp_c = "))
        temp_m = pow(temp_c, key.d, key.n)
    except:
        break
    print("temp_m : " + str(temp_m))
    l = str(((temp_m&5) * random.randint(1,10000))%(2*(random.randint(1,10000))))
    print "l = "+l