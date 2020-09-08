from bluetooth import *
from socket import *
import threading
import time

def receiverArduinoData(sock):
    try:
        while True:
            recvArduinoData = sock.recv(2048)
            arduinoData = recvArduinoData[:-1].decode('UTF-8')
            if(arduinoData == ""):
                pass
            else:
                print("recived Data : {}\n".format(arduinoData))
                passingArduinoData = arduinoData.split(",")    
    except KeyboardInterrupt:
        print("\nFinished\n")
        exit()
    finally:
        bluetoothArduino.close()
        

def receiverAndroidRequest(sock):
    try:
        while True:
            recvAndroidData = sock.recv(1024)
            print('받은 데이터 : ', recvAndroidData.decode('utf-8'))
            sock, addr = androidSock.accept()
    except KeyboardInterrupt:
        print("\nFinished\n")
        exit()
    finally:
        androidSock.close()
        connectionSock.close()

bluetoothArduino = BluetoothSocket(RFCOMM)
bluetoothArduino.connect(("98:D3:37:90:AF:07",1))

print('블루투스 접속이 확인되었습니다.')

arduinoThread = threading.Thread(target=receiverArduinoData, args=(bluetoothArduino,))
arduinoThread.start()

androidSock = socket(AF_INET, SOCK_STREAM)
androidSock.bind(('', 9999))
androidSock.listen(1)
connectionSock, addr = androidSock.accept()

print(str(addr),'에서 접속이 확인되었습니다.')

androidThread = threading.Thread(target=receiverAndroidRequest, args=(connectionSock,))
androidThread.start()

while True:
    time.sleep(1)
    pass