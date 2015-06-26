#!/usr/bin/env python

# Test program to connect to UV DB

import os
import sys
import cx_Oracle
import ConfigParser
import binascii

config_file="./lp1.ini"
account_oids=[]
user_oids=[]
days=360

banner="=====================================================================================================\n"

def create_conn():
	conn = None
	config = ConfigParser.ConfigParser()
	config.read(config_file)
	dbhost = config.get("db", "hostname")
	dbport = int(config.get("db", "port"))
	dbservice = config.get("db", "service")
	dbuser = config.get("db", "username")
	dbpass = config.get("db", "password")
	connstr="%s/%s@%s:%d/%s" % (dbuser, dbpass, dbhost, dbport, dbservice)
	try:
		conn=cx_Oracle.connect(connstr)
	except:
		print "Failed to connect with db  "
		sys.exit(1)
	return conn

def sample_sql(conn):
	db_curs=conn.cursor()
	sql="select count(1) from account"
	db_curs.execute(sql)
	recs=db_curs.fetchall()
	print recs[0][0]

def extract_accounts(conn):
	#global account_oids
	ofilename="./account_oids.txt"
	ofile=open(ofilename, 'w')
	db_curs=conn.cursor()	
	sql="select account_oid from account where status in ('deleted' ,'forcedeleted', 'mergedeleted') and created_date<sysdate-%d" % (days)
	db_curs.execute(sql)
	recs=db_curs.fetchall()
	#print recs
	for rec in recs:
		#account_oids.append(binascii.hexlify(rec[0].strip()).upper())
		ofile.write(binascii.hexlify(rec[0].strip()).upper())
		ofile.write("\n")
	print "%d accounts found matching the following sql: %s" % (len(recs), sql)
	#print account_oids
	ofile.write("")
	ofile.close()
	print banner

def extract_users(conn):
	global user_oids
	db_curs=conn.cursor()
	sql="select user_oid from account_user where status in ('deleted' ,'forcedeleted', 'mergedeleted') and created_date<sysdate-%d" % (days)
	db_curs.execute(sql)
	recs=db_curs.fetchall()
	for rec in recs:
		user_oids.append(binascii.hexlify(rec[0].strip()).upper())
	print "%d users found matching the following sql: %s" % (len(user_oids), sql)
	print banner

def verify_accounts(conn):
	global account_oids
	db_curs=conn.cursor()
	acc_list=str(account_oids).replace('[', '(').replace(']', ')')
	sql="select account_oid from account where account_oid in %s and DISPLAY_NAME<>'@@@deleted@@@' and status<>'deidentified'" % (acc_list)
	db_curs.execute(sql)
	recs=db_curs.fetchall()
	if len(recs)>0:
		print "Following accounts have NOT been properly deidentified"
		for rec in recs:
			print binascii.hexlify(rec[0].strip()).upper()
	print banner

def verify_users(conn):
	print banner
	print "VERIFYING USER STATUS"
	global user_oids
	db_curs=conn.cursor()
	user_list=str(user_oids).replace('[', '(').replace(']', ')')
	sql="select user_oid, username, status from account_user where user_oid in " + user_list + " and username not like '@@@deleted@@@%'  and status<>'deidentified'"
	db_curs.execute(sql)
	recs=db_curs.fetchall()
	if len(recs)>0:
		print "Following Users have NOT been properly deidentified"
		print "UserOID, Username, Status"
		for rec in recs:
			print binascii.hexlify(rec[0].strip()).upper()+"| "+rec[1]+"| "+rec[2]
	print banner

def generic_verify(conn, attrib, message, sql, pmess):
	print banner
	print "VERIFYING %s STATUS" % (attrib)
	db_curs=conn.cursor()
	db_curs.execute(sql)
	recs=db_curs.fetchall()
	if len(recs)>0:
		print message
		print pmess
		for rec in recs:
			print binascii.hexlify(rec[0].strip()).upper()+"| "+rec[1]
	print banner

def verify_password(conn):
	print banner
	print "VERIFYING PASSWORD STATUS"
	global user_oids
	user_list=str(user_oids).replace('[', '(').replace(']', ')')
	db_curs=conn.cursor()
	sql="select user_oid, password from account_user where user_oid in %s and password<>'@@@deleted@@@'" % (user_list)
	db_curs.execute(sql)
	recs=db_curs.fetchall()
	if len(recs)>0:
		print "Following User's Passwords have NOT been properly deidentified"
		print "UserOID, Password"
		for rec in recs:
			print binascii.hexlify(rec[0].strip()).upper()+"| "+rec[1]
	print banner

def verify_givenname(conn):
	attrib="GIVEN_NAME"
	global user_oids
	user_list=str(user_oids).replace('[', '(').replace(']', ')')
	sql="select user_oid, given_name from account_user where user_oid in %s and given_name <>'@@@deleted@@@'" % (user_list)
	message="Following User's Given Names have NOT been properly deidentified"
	pmess="UserOID, GivenName"
	generic_verify(conn, attrib, message, sql, pmess)

def verify_surname(conn):
	attrib="SURNAME"
	global user_oids
	user_list=str(user_oids).replace('[', '(').replace(']', ')')
	sql="select user_oid, surname from account_user where user_oid in %s and surname <>'@@@deleted@@@'" % (user_list)
	message="Following User's Surnames have NOT been properly deidentified"
	pmess="UserOID, Surname"
	generic_verify(conn, attrib, message, sql, pmess)

def verify_displayimage(conn):
	attrib="DISPLAY_IMAGE"
	global user_oids
	user_list=str(user_oids).replace('[', '(').replace(']', ')')
	sql="select user_oid, 'NOT NULL' from account_user where user_oid in %s and DISPLAY_IMAGE_URI is not null" % (user_list)
	message="Following User's Display Images have NOT been properly deidentified"
	pmess="UserOID, DisplayImage"
	generic_verify(conn, attrib, message, sql, pmess)

def verify_addressline1(conn):
	attrib="ADDRESS LINE 1"
	global user_oids
	user_list=str(user_oids).replace('[', '(').replace(']', ')')
	sql="select user_oid, address_line1 from user_address where user_oid in %s and ADDRESS_LINE1 is not null and ADDRESS_LINE1<>'@@@deleted@@@'" % (user_list)
	message="Following User's Address Line 1 have NOT been properly deidentified"
	pmess="UserOID, AddressLine1"	
	generic_verify(conn, attrib, message, sql, pmess)

def verify_addressline2(conn):
	attrib="ADDRESS LINE 2"
	global user_oids
	user_list=str(user_oids).replace('[', '(').replace(']', ')')
	sql="select user_oid, address_line2 from user_address where user_oid in %s and ADDRESS_LINE2 is not null and ADDRESS_LINE2<>'@@@deleted@@@'" % (user_list)
	message="Following User's Address Line 2 have NOT been properly deidentified"
	pmess="UserOID, AddressLine2"
	generic_verify(conn, attrib, message, sql, pmess)

def verify_addressline3(conn):
	attrib="ADDRESS LINE 3"
	global user_oids
	user_list=str(user_oids).replace('[', '(').replace(']', ')')
	sql="select user_oid, address_line3 from user_address where user_oid in %s and ADDRESS_LINE3 is not null and ADDRESS_LINE3<>'@@@deleted@@@'" % (user_list)
	message="Following User's Address Line 3 have NOT been properly deidentified"
	pmess="UserOID, AddressLine3"
	generic_verify(conn, attrib, message, sql, pmess)

def verify_city(conn):
	attrib="CITY"
	global user_oids
	user_list=str(user_oids).replace('[', '(').replace(']', ')')
	sql="select user_oid, city from user_address where user_oid in %s and CITY is not null and CITY <>'@@@deleted@@@'" % (user_list)
	message="Following User's City have NOT been properly deidentified"
	pmess="UserOID, City"
	generic_verify(conn, attrib, message, sql, pmess)

def verify_state(conn):
	attrib="STATE"
	global user_oids
	user_list=str(user_oids).replace('[', '(').replace(']', ')')
	sql="select user_oid, state from user_address where user_oid in %s and STATE is not null and STATE <>'@@@deleted@@@'" % (user_list)
	message="Following User's State have NOT been properly deidentified"
	pmess="UserOID, State"
	generic_verify(conn, attrib, message, sql, pmess)

def verify_postalcode(conn):
	attrib="POSTALCODE"
	global user_oids
	user_list=str(user_oids).replace('[', '(').replace(']', ')')
	sql="select user_oid, postal_code from user_address where user_oid in %s and postal_code is not null and  postal_code <>'@@@deleted@@@'" % (user_list)
	message="Following User's PostalCode have NOT been properly deidentified"
	pmess="UserOID, PostalCode"
	generic_verify(conn, attrib, message, sql, pmess)

def verify_country(conn):
	attrib="COUNTRY"
	global user_oids
	user_list=str(user_oids).replace('[', '(').replace(']', ')')
	sql="select user_oid, country from user_address where user_oid in %s and country is null or country='@@@deleted@@@'" % (user_list)
	message="Following User's Country have NOT been properly deidentified"
	pmess="UserOID, Country"
	generic_verify(conn, attrib, message, sql, pmess)

def verify_birthdate(conn):
	attrib="BIRTHDATE"
	global user_oids
	user_list=str(user_oids).replace('[', '(').replace(']', ')')
	sql="select user_oid, birth_date from account_user where user_oid in %s and BIRTH_DATE is null" % (user_list)
	message="Following User's BirthDate have NOT been properly deidentified"
	pmess="UserOID, BirthDate"
	generic_verify(conn, attrib, message, sql, pmess)

def verify_avatar(conn):
	print banner
	print "VERIFYING AVATAR"
	global user_oids
	user_list=str(user_oids).replace('[', '(').replace(']', ')')
	db_curs=conn.cursor()
	sql="select user_OID from ACCOUNT_USER_AVATAR where user_oid in %s" % (user_list)
	db_curs.execute(sql)
	recs=db_curs.fetchall()
	if len(recs)>0:
		print "Following User's Avatars have NOT been properly deidentified"
		print "UserOID"
		for rec in recs:
			print binascii.hexlify(rec[0].strip()).upper()
	print banner

def verify_altemail(conn):
	attrib="ALTEMAIL"
	global user_oids
	user_list=str(user_oids).replace('[', '(').replace(']', ')')
	sql="select user_oid, email from USER_ALT_EMAIL where user_oid in %s and email<>'@@@deleted@@@'" % (user_list)
	message="Following User's Alt Emails have NOT been properly deidentified"
	pmess="UserOID, AltEmail"
	generic_verify(conn, attrib, message, sql, pmess)

def verify_legalguardian(conn):
	print banner
	print "VERIFYING LEGAL GUARDIAN"
	global user_oids
	user_list=str(user_oids).replace('[', '(').replace(']', ')')
	db_curs=conn.cursor()
	sql="select user_OID from USER_LEGAL_GUARDIAN where user_oid in %s" % (user_list)
	db_curs.execute(sql)
	recs=db_curs.fetchall()
	if len(recs)>0:
		print "Following User's Legal Guardian have NOT been properly deidentified"
		print "UserOID"
		for rec in recs:
			print binascii.hexlify(rec[0].strip()).upper()
	print banner

def verify_secquestions(conn):
	print banner
	print "VERIFYING SECURITY QUESTIONS"
	global user_oids
	user_list=str(user_oids).replace('[', '(').replace(']', ')')
	db_curs=conn.cursor()
	sql="select user_OID from USER_SECURITY_QUESTIONS where user_oid in %s" % (user_list)
	db_curs.execute(sql)
	recs=db_curs.fetchall()
	if len(recs)>0:
		print "Following User's Security Questions have NOT been properly deidentified"
		print "UserOID"
		for rec in recs:
			print binascii.hexlify(rec[0].strip()).upper()
	print banner

def write_users(conn):
	ofilename="./user_oids.txt"
	ofile=open(ofilename, 'a')
	sql="select user_oid from account_user where status in ('deleted' ,'forcedeleted', 'mergedeleted') and created_date<sysdate-%d" % (days)
	db_curs=conn.cursor()
	db_curs.execute(sql)
	recs=db_curs.fetchall()
	if len(recs)>0:
		for rec in recs:
			ofile.write(binascii.hexlify(rec[0].strip()).upper())
			ofile.write("\n")
	ofile.write("")
	ofile.close()

def read_users():
	ifilename="./user_oids.txt"
	ifile=open(ifilename, 'r')
	global user_oids
	lines=ifile.readlines()
	for line in lines:
		user_oids.append(line.strip())
	ifile.close()

def read_accounts():
	ifilename="./account_oids.txt"
	ifile=open(ifilename, 'r')
	global account_oids
	lines=ifile.readlines()
	for line in lines:
		account_oids.append(line.strip())
	ifile.close()

def write_account_users(conn):
	ofilename="./user_oids.txt"
	ofile=open(ofilename, 'w')
	global account_oids
	acc_list=str(account_oids).replace('[', '(').replace(']', ')')
	sql="select distinct user_oid from account_user where account_oid in %s" % (acc_list)
	db_curs=conn.cursor()
	db_curs.execute(sql)
	recs=db_curs.fetchall()
	if len(recs)>0:
		for rec in recs:
			ofile.write(binascii.hexlify(rec[0].strip()).upper())
			ofile.write("\n")
	ofile.write("")	ofile.close()
	

##### Beginning of Main Program #####
if __name__=="__main__":
	conn=create_conn()
	#sample_sql(conn)
	print "####### WORKING ON ACCOUNTS ########"
	#extract_accounts(conn)
	write_accounts(conn)
	write_account_users(conn)
	read_accounts(conn)
	verify_accounts(conn)
	print "####### WORKING ON USERS ##########"
	#extract_users(conn)
	write_users(conn)
	read_users()
	verify_users(conn)
	verify_password(conn)
	verify_givenname(conn)
	verify_surname(conn)
	verify_displayimage(conn)
	verify_addressline1(conn)
	verify_addressline2(conn)
	verify_addressline3(conn)
	verify_city(conn)
	verify_state(conn)
	verify_postalcode(conn)
	verify_country(conn)
	verify_birthdate(conn)
	verify_avatar(conn)
	verify_altemail(conn)
	verify_legalguardian(conn)
	verify_secquestions(conn)
	print "Done !"
	
