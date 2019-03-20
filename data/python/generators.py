"""
Filename: generators.py
Version: python 2.7
Author: Tim Johnson
Desc: Holds all of the generators for random table data that cannot be repeated, but also
        includes a name generator because then all the input filenames are in one file
"""

import random as r
import collections as c

INPUT_FILE_PATH         = '../input/'
MALE_NAME_FILEPATH      = INPUT_FILE_PATH + 'BoysFirstNames.txt'
FEMALE_NAME_FILEPATH    = INPUT_FILE_PATH + 'GirlsFirstNames.txt'
LAST_NAME_FILEPATH      = INPUT_FILE_PATH + 'LastNames.txt'
BUSINESS_NAME_FILEPATH  = INPUT_FILE_PATH + 'Businesses.txt'
STREET_LIST_FILEPATH    = INPUT_FILE_PATH + 'Streets.txt'
CITY_LIST_FILEPATH      = INPUT_FILE_PATH + 'Cities.txt'
STATE_LIST_FILEPATH     = INPUT_FILE_PATH + 'States.txt'

class IDGenerator:
    generatedIDs = set()
    digits = -1
    counter = 0

    def __init__(self, numDigits=-1):
        self.digits = numDigits

    def getNewID(self):
        if self.digits == -1:
            self.counter += 1
            return self.counter - 1

        if len(self.generatedIDs) >= 10**self.digits:
            raise Exception('No more IDs available to generate')

        ID = r.randint(0, 10**self.digits - 1)
        while ID in self.generatedIDs:
            ID = r.randint(0, 10**self.digits - 1)
        self.generatedIDs.add(ID)
        return ID

Address = c.namedtuple('Address', ('street', 'city', 'state', 'zip'))

class AddressGenerator:
    generatedAddresses = set()
    streets = []
    cities = []
    states = []

    def __init__(self):
        with open(STREET_LIST_FILEPATH, 'r') as f:
            for line in f:
                self.streets.append(line.strip())
        with open(CITY_LIST_FILEPATH, 'r') as f:
            for line in f:
                self.cities.append(line.strip())
        with open(STATE_LIST_FILEPATH, 'r') as f:
            for line in f:
                self.states.append(line.strip())

    def getNewAddress(self):
        address = Address(r.choice(self.streets), r.choice(self.cities),
                          r.choice(self.states), '{:0>5}'.format(r.randint(0, 99999)))
        while address in self.generatedAddresses:
            address = Address(r.choice(self.streets), r.choice(self.cities),
                              r.choice(self.states), '{:0>5}'.format(r.randint(0, 99999)))
        self.generatedAddresses.add(address)
        return address

Name = c.namedtuple('Name', ('firstName', 'lastName'))

class NameGenerator:
    maleNames = []
    femaleNames = []
    lastNames = []

    def __init__(self):
        with open(MALE_NAME_FILEPATH, 'r') as f:
            for line in f:
                self.maleNames.append(line.strip())
        with open(FEMALE_NAME_FILEPATH, 'r') as f:
            for line in f:
                self.femaleNames.append(line.strip())
        with open(LAST_NAME_FILEPATH, 'r') as f:
            for line in f:
                self.lastNames.append(line.strip())

    def getNewName(self, male):
        return Name(r.choice(self.maleNames if male else self.femaleNames), r.choice(self.lastNames))

class BusinessNameGenerator:
    businessNames = []

    def __init__(self):
        with open(BUSINESS_NAME_FILEPATH, 'r') as f:
            for line in f:
                self.businessNames.append(line.strip())

    def getNewName(self):
        if len(self.businessNames) <= 0:
            raise Exception('No more business names')

        name = r.choice(self.businessNames)
        self.businessNames.remove(name)
        return name
