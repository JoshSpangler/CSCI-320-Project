import random as r
import collections as c
import sys
import generateData as d
import options as o

Car = c.namedtuple('Car', ('VIN', 'Brand', 'Series', 'Model', 'ModelYear', 'Design',
                           'Engine', 'DriveTrain', 'Wheel', 'Upholstry', 'Color', 'BaseCost'))

def generateVIN():
    VIN = ''
    charArray = getCharArray()
    for x in range(17):
        VIN += charArray[r.randint(0, len(charArray)-1)]
    return VIN

def getUpgrades(Upgrades):
    probability = r.randint(0, len(Upgrades))/len(Upgrades)
    strn = ''
    for Upgrade in Upgrades:
        if r.random() < probability:
            ind_upgrade = Upgrade.split(':')
            strn += ',OptionalUpgrade=' + ind_upgrade[0] + ',OptionalUpgradeCost=' + ind_upgrade[1]
    return strn

def getCar(cars):
    carString = cars[r.randint(0, len(cars)-1)]
    carComponents = carString.split(',')
    design = carComponents[4].split('/')
    wheel = carComponents[7].split('/')
    upholstry = carComponents[8].split('/')
    color = carComponents[9].split('/')
    upgrades = carComponents[10].strip().split('/')
    upgradeString = getUpgrades(upgrades)
    cost = carComponents[11].strip()
    return Car(generateVIN(), carComponents[0], carComponents[1], carComponents[2], carComponents[3],
               design[r.randint(0, len(design)-1)], carComponents[5], carComponents[6],
               wheel[r.randint(0, len(wheel)-1)], upholstry[r.randint(0, len(upholstry)-1)]+upgradeString,
               color[r.randint(0, len(color) - 1)], cost)

def getCharArray():
    charArray = []
    for x in range(10):
        charArray.append(str(x))
    for x in range(65, 91):
        charArray.append(chr(x))
    return charArray

def readCSV(fn):
    cars = []
    with open(fn) as f:
        for line in f:
            cars.append(line)
    return cars

def generate(carfile, number):
    cars = []
    for x in range(int(number)):
        cars.append(getCar(readCSV(carfile)))
    Designs = []
    DriveTrains = []
    Upholstry = []
    Color = []
    for car in cars:
        Designs.append(car.Design)
        DriveTrains.append(car.DriveTrain)
        Upholstry.append(car.Upholstry)
        Color.append(car.Color)
    # Designs=o.getExtraCost(Designs, 'Design')
    # DriveTrains=o.getExtraCost(DriveTrains, 'DriveTrain')
    # Upholstry=o.getExtraCost(Upholstry, 'Upholstry')
    # Color=o.getExtraCost(Color, 'Color')
    cars2 = []
    for car in cars:
        chance = r.randint(0, 1)
        Transmission = 'Automatic'
        if chance == 0:
            Transmission = 'Manual'
        cars2.append(Car(car.VIN, car.Brand, car.Series, car.Model, car.ModelYear,
                     Designs[d.findIndex(Designs, car.Design)],
                     car.Engine+',Transmission=' + Transmission,
                     DriveTrains[d.findIndex(DriveTrains, car.DriveTrain)],
                     car.Wheel,
                     Upholstry[d.findIndex(Upholstry, car.Upholstry)],
                     Color[d.findIndex(Color, car.Color)],
                     car.BaseCost))
    return cars2

def main():
    if len(sys.argv) != 3:
        print('Usage: GenerateCar -file -number')
    else:
        cars = []
        for x in range(int(sys.argv[2])):
            cars.append(getCar(readCSV(sys.argv[1])))
        for car in cars:
            print(car)

if __name__ == '__main__':
    main()
