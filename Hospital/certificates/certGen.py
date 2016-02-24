import subprocess
import time
import re

def password_check(password):
    """
    Verify the strength of 'password'
    Returns a dict indicating the wrong criteria
    A password is considered strong if:
        8 characters length or more
        1 digit or more
        1 symbol or more
        1 uppercase letter or more
        1 lowercase letter or more
    """

    # calculating the length
    length_error = len(password) < 8

    # searching for digits
    digit_error = re.search(r"\d", password) is None

    # searching for uppercase
    uppercase_error = re.search(r"[A-Z]", password) is None

    # searching for lowercase
    lowercase_error = re.search(r"[a-z]", password) is None

    # searching for symbols
    symbol_error = re.search(r"[ !#$%&'()*+,-./[\\\]^_`{|}~\";:<>=?@"+r'"]', password) is None

    # overall result
    password_ok = not ( length_error or digit_error or uppercase_error or lowercase_error or symbol_error )

    s = {'Password too short' : length_error,
    'No digits' : digit_error,
    'No uppercase symbols' : uppercase_error,
    'No lowercase symbols' : lowercase_error,
    'No special symbols' : symbol_error,}

    print(s)
    return password_ok

alias = raw_input('Choose an ID: ')
name = raw_input('What is your name? ')
title = raw_input('What is your title? ')
division = raw_input('What is you division? ')
subprocess.Popen("stty -echo", shell=True)
passw = raw_input('Enter a password: ')
passw2 = raw_input('\nRe-enter the password: ')
passcheck = password_check(passw)
while passw != passw2 or passcheck != True :
    if passw != passw2 :
        print("\nPasswords don't match!")
    passw = raw_input('\nEnter a password: ')
    passw2 = raw_input('\nRe-enter the password: ')
    passcheck = password_check(passw)

CApass = raw_input('\nEnter CA password: ')

print("\n")
subprocess.Popen("stty echo", shell=True)
subprocess.Popen("keytool -genkeypair -alias " + alias + " -keyalg rsa -dname \"CN=" + alias + ", OU=" + title + ", O=" + division + ", L=" + name + "\" -keystore Client/" + alias + " -storepass " + passw + " -keypass " + passw, shell=True)
time.sleep(2)
subprocess.Popen("keytool -keystore Client/" + alias + " -storepass " + passw + " -certreq -alias " + alias + " -keyalg rsa -file .csr/"  + alias + ".csr", shell=True)
time.sleep(1)
subprocess.Popen("openssl x509 -req -CA CA/CA.cert -CAkey CA/privatekey.pem -in .csr/" + alias + ".csr -out Client/" + alias + ".cert -days 365 -CAcreateserial -passin pass:" + CApass, shell=True)
time.sleep(1)
subprocess.Popen("keytool -import -noprompt -file CA/CA.cert -keystore Client/" + alias + " -storepass " + passw + " -alias CA", shell=True)
time.sleep(0.5)
subprocess.Popen("keytool -import -noprompt -file Client/" + alias + ".cert -keystore Client/" + alias + " -storepass " + passw + " -alias " + alias, shell=True)
time.sleep(1)
subprocess.Popen("rm .csr/*", shell=True)
