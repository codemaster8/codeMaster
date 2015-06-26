#!/usr/bin/env python
import re
import web
from web import form
import eras
from getCOORDLog import *

render = web.template.render('templates/')
htmlrender = web.template.render('templates1/')

banner="========================================================================================\n"

urls = (
	'/primes', 'list_10000_primes',
	'/primes/(.*)', 'list_x_primes',
	'/pform',	'pform',
	'/aws/(.*)', 	'aws',
	'/vcoord',	'vcoord',
	'/url',		'url'
)

app = web.application(urls, globals())

myform = form.Form( 
    form.Textbox("Transaction ID", 
        form.notnull,),
    form.Dropdown('Environment', ['QALP1', 'QALP2', 'QALP3_ST', 'QALP3_CH'])) 

class list_10000_primes:
	def GET(self):
		output="The prime numbers from 1 to 10000 are: "
		output+=str(eras.list_primes(10001))
		return output

class list_x_primes:
	def GET(self, number):
		ubound=int(number)+1
		output="The prime numbers from 1 to %d are: " % (ubound-1)
		output+=str(eras.list_primes(ubound))
		return output

class vcoord:
	def GET(self):
		cmd="python /opt/RND/getCOORDVersion.py"
		os.system(cmd)
		return "Success"
class aws:
	def GET(self,machineName):
		rdict = getMachineName(machineName)
		ip = ''.join(rdict)
		rstr = ""
		rstr+=banner
		rstr+= ip
		rstr+="\n"
		rstr+=banner			
		return rstr


def pprint(list):
	strr=""
	for line in list:
		strr+=line
	return strr

class pform:
	def GET(self):
		form=myform()
		return render.formtest(form)

	def POST(self):
		form = myform() 
		if not form.validates(): 
			return render.formtest(form)
		else:
			rdict=getlog(form['Transaction ID'].value,form['Environment'].value)
			rstr=""
			for key in rdict.keys():
				rstr+=banner
				rstr+=key
				rstr+="\n"
				rstr+=banner
				#rstr+=rdict[key]
				rstr+=pprint(rdict[key])
				rstr+="\n\n\n"
			return rstr

class url:
	def GET(self):
		rstr = getCSVTOHTMLConverter()
                
                return htmlrender.test(' Neustar URL ',rstr)
	
		
if __name__ == "__main__":
	app.run()
