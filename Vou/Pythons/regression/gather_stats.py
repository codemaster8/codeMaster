#!/usr/bin/env python

import os, sys, re
import ConfigParser
from stats_objects import *
from bs4 import BeautifulSoup

workingDir="/root/regression/workingDir"
configfile="/root/regression/regression.config"
fileloc="/opt/dece/SoapUIReports/"
summ="overview-summary.html"
detd="package-summary.html"
cmod={}
banner="*****************************************************************"

def read_config():
	conf=Configs()
	config = ConfigParser.ConfigParser()
	config.read(configfile)
	conf.modules=config.get("modules", "modules").split(", ")
	conf.date=config.get("modules", "date")
	conf.build=config.get("modules", "build")
	conf.tag=config.get("modules", "tag")
	conf.ignore=config.get("modules", "ignore").split(", ")
	modules={}
	for module in conf.modules:
		try:
			mod=Module()
			mod.hostname=config.get(module, "hostname")
			mod.projects=config.get(module, "projects").split(", ")
			#print mod.projects
			modules[module]=mod
		except:
			pass
	return conf, modules

def dir_cleanup():
	filelist = [ f for f in os.listdir(workingDir) ]
	for f in filelist:
		os.chdir(workingDir)
		os.remove(f)

def get_module_files(conf, modules, module):
	cfileloc=fileloc+conf.date+"/"+conf.build
	for proj in modules[module].projects:
		nproj=re.split("_| |-|", proj)[0]
		sfile=cfileloc+"/"+proj+"/"+summ
		dfile=cfileloc+"/"+proj+"/"+nproj+"*/"+detd
		scmd="scp root@%s:%s %s/%s" % (modules[module].hostname, sfile, workingDir, proj+"_"+summ)
		dcmd="scp root@%s:%s %s/%s" % (modules[module].hostname, dfile, workingDir, proj+"_"+detd)
		#print scmd
		#print dcmd
		os.system(scmd)
		os.system(dcmd)

def read_module_files(conf, modules, module):
	global cmod
	tfailures=0
	ifailures=0
	files=os.listdir(workingDir)
	for file in files:
		if file.find('overview-summary.html')!=-1:
			os.chdir(workingDir)
			ifile=open(file)
			soup=BeautifulSoup(ifile)
			cfailures=int(soup.findAll(title="Display all failures")[0].string)
			tfailures+=cfailures
			#print file
			#print "Cfailures= %d" % (cfailures)
			#print "Tfailures= %d" % (tfailures)
		else:
			#print file
			os.chdir(workingDir)
			ifile=open(file)
			soup=BeautifulSoup(ifile)
			allfails=soup.findAll('tr', { 'class' : 'Failure' })
			index=0	
			for fail in allfails:
				tags=fail.findAll('a')
				tname=tags[0].string
				if any(word.lower() in tname.lower() for word in conf.ignore):
					fcount=int(fail.findAll(title="Display only failures")[0].string)
					ifailures+=fcount
					print "Current iFailures=%d" % (fcount)
			#print "IFailures= %d" % (ifailures)
	cmod[module]=tfailures-ifailures

if __name__=="__main__":
	conf,modules=read_config()
	dir_cleanup()
	for mod in modules:
		print mod
		dir_cleanup()
		get_module_files(conf, modules, mod)
		read_module_files(conf, modules, mod)
	print cmod
	print banner
	tstr="%25s			%s" % ("Module", "Actual Failures")
	print tstr
	print banner
	for mod in cmod.keys():
		print "  %25s\t\t\t%d  " % (mod, int(cmod[mod]))
	print banner
