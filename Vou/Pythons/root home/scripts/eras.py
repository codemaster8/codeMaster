#!/usr/bin/env python

a=range(1,10001)
max=10000

def list_primes(ubound):
	a=range(1,ubound)
	max=ubound-1
	for i in range(2,max):
		#print i
		count=2
		num=i*count
		while (num<=max):
			if num in a:
				a.remove(num)
				count+=1
				num=i*count
			else:
				count+=1
				num=i*count
	return a
