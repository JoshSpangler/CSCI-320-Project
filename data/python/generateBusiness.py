import random as r
import sys

def getBusinesses(fn):
    businesses = []
    with open(fn) as f:
        for line in f:
            businesses.append(line.strip())
    return businesses

def getBusiness(businesses):
    return businesses[r.randint(0, len(businesses)-1)]

def getBusinessSize():
    size = r.randint(0, 2)
    if size == 0:
        return 'BusinessSize=small'
    elif size == 1:
        return 'BusinessSize=mid-size'
    else:
        return 'BusinessSize=large'

def generate(fn, number):
    businesses = []
    for x in range(int(number)):
        businesses.append(getBusiness(getBusinesses(fn)) + ',' + getBusinessSize() + ',')
    return businesses

def main():
    if len(sys.argv) != 3:
        print('Usage: generateBusiness.py -filename -number')
    else:
        businesses=[]
        for x in range(int(sys.argv[2])):
            businesses.append(getBusiness(getBusinesses(sys.argv[1])))
        for business in businesses:
            print(business)
            
if __name__ == '__main__':
    main()
