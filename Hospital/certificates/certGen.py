import os

alias = raw_input('What is your name? ')
passw = raw_input('Enter a password: ')
passw2 = raw_input('Re-enter the password: ')
os.system("echo hej " + alias)
os.system('keytool -genkeypair -alias client -keystore Client/clientKeyStore')
os.system('password')
