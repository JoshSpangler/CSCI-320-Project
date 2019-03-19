import random as r
import generateCustomerData as gcd
import sys

def getName(names):
    return names[r.randint(0, len(names)-1)]

def getFileData(fnf, mnf, lnf):
    boysnames = []
    girlsnames = []
    lastnames = []
    with open(fnf) as f:
        for name in f:
            boysnames.append(name.strip().lower())
            boysnames[len(boysnames) - 1] = \
                boysnames[len(boysnames)-1][0].upper()+boysnames[len(boysnames)-1][1:]
    with open(mnf) as f:
        for name in f:
            girlsnames.append(name.strip().lower())
            girlsnames[len(girlsnames) - 1] = \
                girlsnames[len(girlsnames) - 1][0].upper() + girlsnames[len(girlsnames) - 1][1:]
    with open(lnf) as f:
        for name in f:
            lastnames.append(name.strip().lower())
            lastnames[len(lastnames) - 1] = \
                lastnames[len(lastnames) - 1][0].upper() + lastnames[len(lastnames) - 1][1:]
    return boysnames, girlsnames, lastnames

def generate(person, maleNameFile, femaleNameFile, lastNameFile, streetFile, countyFile, stateFile, numEntries):
    maleNames, femaleNames, lastNames = getFileData(maleNameFile, femaleNameFile, lastNameFile)
    fullNames = []
    addresses = gcd.generateAddress(streetFile, countyFile, stateFile, person, numEntries)
    for x in range(int(numEntries)):

        if r.randint(0, 1) == 0:
            fullNames.append(person + 'Gender=male,' + person + 'Name=' + getName(maleNames) +
                             ' ' + getName(maleNames) + ' ' + getName(lastNames) + ',' + addresses[x] +
                             ',CustomerIncome=' + str(r.randint(70000, 400000)))
        else:
            fullNames.append(person + 'Gender=female,' + person + 'Name=' + getName(femaleNames) +
                             ' ' + getName(femaleNames) + ' ' + getName(lastNames) + ',' + addresses[x] +
                             ',CustomerIncome=' + str(r.randint(70000, 400000)))
    return fullNames

def main():
    if len(sys.argv) != 5:
        print('Usage: generatePeople.py -boysFirstnameFile -girlsFirstNames -lastnameFile -number')
    else:
        maleNames, femaleNames, lastName = getFileData(sys.argv[1], sys.argv[2], sys.argv[3])
        fullnames = []
        for x in range(int(sys.argv[4])):
            if r.randint(0, 1) == 0:
                fullnames.append('Gender=male,Name=' + getName(maleNames) + ' ' +
                                 getName(maleNames) + ' ' + getName(lastName))
            else:
                fullnames.append('Gender=female,Name='+getName(femaleNames) + ' ' +
                                 getName(femaleNames) + ' ' + getName(lastName))
        for name in fullnames:
            print(name)

if __name__ == '__main__':
    main()
