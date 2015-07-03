class Configs:
	def __init__(self):
		self.modules=[]
		self.date=""
		self.build=""
		self.tag=""
		self.ignore=[]

	def get_modules(self):
		return self.modules

	def get_date(self):
		return self.date

	def get_build(self):
		return self.build

	def get_tag(self):
		return self.tag

	def get_ignore(self):
		return self.ignore

class Module:
	def __init__(self):
		self.hostname=""
		self.projects=[]
	
	def get_hostname(self):
		return self.hostname

	def get_projects(self):
		return self.projects
