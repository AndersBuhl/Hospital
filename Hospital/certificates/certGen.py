import subprocess
import time

alias = raw_input('Choose an ID: ')
name = raw_input('What is your name? ')
title = raw_input('What is your title? ')
division = raw_input('What is you division? ')
subprocess.Popen("stty -echo", shell=True)
passw = raw_input('Enter a password: ')
passw2 = raw_input('\nRe-enter the password: ')
while passw != passw2 :
    print("\nPasswords don't match!")
    passw = raw_input('\nEnter a password: ')
    passw2 = raw_input('\nRe-enter the password: ')

#Ange password till CA

print("\n")
subprocess.Popen("stty echo", shell=True)
subprocess.Popen("keytool -genkeypair -alias " + alias + " -keyalg rsa -dname \"CN=" + alias + ", OU=" + title + ", O=" + division + ", L=" + name + "\" -keystore Client/" + alias + " -storepass " + passw + " -keypass " + passw, shell=True)
time.sleep(1)
subprocess.Popen("keytool -keystore Client/" + alias + " -storepass " + passw + " -certreq -alias " + alias + " -keyalg rsa -file .csr/"  + alias + ".csr", shell=True)
time.sleep(1)
subprocess.Popen("openssl x509 -req -CA CA/CA.cert -CAkey CA/privatekey.pem -in .csr/" + alias + ".csr -out Client/" + alias + ".cert -days 365 -CAcreateserial -passin pass:password", shell=True)
time.sleep(1)
subprocess.Popen("keytool -import -noprompt -file CA/CA.cert -keystore Client/" + alias + " -storepass " + passw + " -alias CA", shell=True)
time.sleep(0.5)
subprocess.Popen("keytool -import -noprompt -file Client/" + alias + ".cert -keystore Client/" + alias + " -storepass " + passw + " -alias " + alias, shell=True)
time.sleep(1)
subprocess.Popen("rm .csr/*", shell=True)
subprocess.Popen("rm Client/" + alias + ".cert", shell=True)
