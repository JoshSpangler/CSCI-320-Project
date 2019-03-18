import random as r
import sys

def getBusinesses(file):
    businesses=list()
    with open(file) as f:
        for line in f:
            businesses.append(line.strip())
    return businesses

def getBusiness(businesses):
    return(businesses[r.randint(0, len(businesses)-1)])

def getBusinessSize():
    size=r.randint(0,2)
    if(size==0):
        return("BusinessSize=small")
    elif(size==1):
        return("BusinessSize=mid-size")
    else:
        return("BusinessSize=large")

def generate(Filename, Number):
    businesses = list()
    for x in range((int)(Number)):
        businesses.append(getBusiness(getBusinesses(Filename))+","+getBusinessSize()+",")
    return businesses

def main():
    if(len(sys.argv)!=3):
        print("Usage: GenerateBusiness.py -filename -number")
    else:
        businesses=list()
        for x in range((int)(sys.argv[2])):
            businesses.append(getBusiness(getBusinesses(sys.argv[1])))
        for business in businesses:
            print(business)
if(__name__=="__main__"):
    main()