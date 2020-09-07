from bluetooth import *
import sys
import pymysql
import time

client_socket=BluetoothSocket(RFCOMM)
client_socket.connect(("98:D3:37:90:AF:07",1))
#conn = pymysql.connect(host = "db-kdh-test.cr9trjzf2btf.us-east-2.rds.amazonaws.com", user = "donghyun", passwd = "1a2s3d1a2s3d", db = "raspi_db")
conn = pymysql.connect(host = "dbinstance3.cjytw5i33eqd.us-west-2.rds.amazonaws.com", user = "luck0707", passwd = "disorder2848", db = "raspi_db_KDH")
try:
    with conn.cursor() as cur:
        sql = "insert into Humi_Temp values(%s,%s)"

        while True:
            msg = client_socket.recv(2048)
            NLLFU = msg[:-2].decode('UTF-8')
            if(NLLFU == ""):
                print("\n")
            else:
                print("recived message : {}".format(NLLFU))
                pasNLLFU = NLLFU.split(",")
                cur.execute(sql, (pasNLLFU[0],pasNLLFU[1]))
            conn.commit()
            time.sleep(1)

except KeyboardInterrupt:
    exit()

finally:
    conn.close()
print ("Finished")

client_socket.close()

