import random as r
import GenerateCustomerData as gcd
import sys

def getName(names):
    return(names[r.randint(0, len(names)-1)])

def getFileData(fnf,mnf,lnf):
    boysnames=list()
    girlsnames=list() 
    lastnames=list()
    with open(fnf) as f:
        for name in f:
            boysnames.append(name.strip().lower())
            boysnames[len(boysnames) - 1]=boysnames[len(boysnames)-1][0].upper()+boysnames[len(boysnames)-1][1:]
    with open(mnf) as f:
        for name in f:
            girlsnames.append(name.strip().lower())
            girlsnames[len(girlsnames) - 1]=girlsnames[len(girlsnames) - 1][0].upper()+girlsnames[len(girlsnames) - 1][1:]
    with open(lnf) as f:
        for name in f:
            lastnames.append(name.strip().lower())
            lastnames[len(lastnames) - 1]=lastnames[len(lastnames) - 1][0].upper()+lastnames[len(lastnames) - 1][1:]
    return(boysnames,girlsnames,lastnames)

def generate(Person,BoysNamesFile, GirlsNamesFile, LastNamesFile, StreetFile, CountyFile, StateFile,Number):
    boysfirstnames, girlsfirstnames, lastname = getFileData(BoysNamesFile, GirlsNamesFile, LastNamesFile)
    fullnames = list()
    addresses=gcd.generateAddress(StreetFile,CountyFile,StateFile,Person,Number)
    for x in range((int)(Number)):

        if (r.randint(0, 1) == 0):
            fullnames.append(Person+'Gender=male,'+Person+'Name='+ getName(boysfirstnames) + " " + getName(boysfirstnames) + " " + getName(lastname)+","+addresses[x]+",CustomerIncome="+str(r.randint(70000,400000)))
        else:
            fullnames.append(Person+'Gender=female,'+Person+'Name=' +getName(girlsfirstnames) + " " + getName(girlsfirstnames) + " " + getName(lastname)+","+addresses[x]+",CustomerIncome="+str(r.randint(70000,400000)))
    return fullnames

def main():
    if(len(sys.argv)!=5):
        print("Usage: GeneratePeople.py -boysFirstnameFile -girlsFirstNames -lastnameFile -number")
    else:
        boysfirstnames, girlsfirstnames, lastname=getFileData(sys.argv[1],sys.argv[2],sys.argv[3])
        fullnames=list()
        for x in range((int)(sys.argv[4])):
            if(r.randint(0,1)==0):
                fullnames.append('Gender=male,Name='+getName(boysfirstnames)+" "+getName(boysfirstnames)+" "+getName(lastname))
            else:
                fullnames.append('Gender=female,Name='+getName(girlsfirstnames) + " " + getName(girlsfirstnames) + " " + getName(lastname))
        for name in fullnames:
            print(name)

if(__name__=="__main__"):
    main()