from bluetooth import *

client_socket=BluetoothSocket(RFCOMM)
client_socket.connect(("98:D3:37:90:AF:07",1))
while True:
    msg = client_socket.recv(2048)
    NLLFU = msg[:-2].decode('UTF-8')
    if(NLLFU == ""):
        print("\n")
    else:
        print("recived message : {}".format(NLLFU))


print ("Finished")

client_socket.close()