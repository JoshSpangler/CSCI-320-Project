import random as r

def getPhone(strn):
    phone_number=""
    for x in range(10):
        if(x==3 or x==6):
            phone_number+="-"
        phone_number+=str(r.randint(0,9))
    return (strn+"PhoneNumber="+phone_number)

def getFromFile(file):
    values=list()
    with open(file)as f:
        for line in f:
            values.append(line.strip())
    return values

def generateAddress(streetfile, countyfile, stateFile,strn,number):
    streets=getFromFile(streetfile)
    counties=getFromFile(countyfile)
    states=getFromFile(stateFile)
    addresses=list()
    for x in range((int)(number)):
        houseNumber = str(r.randint(0, 200))
        street=streets[r.randint(0, len(streets)-1)]
        county=counties[r.randint(0,len(counties)-1)]
        state=states[r.randint(0,len(states)-1)]
        zip=str(r.randint(0,99999))
        addresses.append(strn+"Street="+houseNumber+" "+street+","+strn+"County="+county+","+strn+"State="+state+","+strn+"Zip="+zip)
    return addresses