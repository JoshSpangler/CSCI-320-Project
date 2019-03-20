"""
Filename: createAllTableData.py
Version: python 2.7
Author: Tim Johnson
Desc: Generates the table data for the schema in our database design in .csv format.
        The tables to generate are based on the schema in schemaHelper.py
"""
from collections import namedtuple

from generators import *
from schemaHelper import *

NUM_DEALERS = 12
NUM_MANUFACTURERS = 15
NUM_SUPPLIERS = 30
NUM_PEOPLE = 100
NUM_COMPANIES = 10
NUM_VEHICLES_PER_DEALER = 30
MIN_MAN_SUPPLIERS = 2
MAX_MAN_SUPPLIERS = 5
AVG_MAN_SUPPLIERS = 2.5
MIN_PHONE_NUMS = 1
MAX_PHONE_NUMS = 3
EXTRA_SALES = (NUM_PEOPLE + NUM_COMPANIES) / 4

STATIC_DATA_FILEPATH = INPUT_FILE_PATH + 'Cars.csv'

def setupDict():
    tables = {}
    for ttype in TABLE_TYPES.keys():
        tables[ttype] = []
    return tables

def parseEngineData(data):
    ENGINE = ' engine'
    LITER = '-liter '
    FOUR_CYL = ' 4-cylinder'
    SIX_CYL = ' 6-cylinder'
    EIGHT_CYL = 'V-8'
    TWELVE_CYL = 'V-12'

    uniqueEngines = {}

    engines = []
    pairs = {}

    idgen = IDGenerator()
    for engine, model in data:
        # If we already processed, assign that id to the model and move on
        if engine in uniqueEngines:
            pairs[model] = uniqueEngines[engine]
            continue

        # Otherwise start the process of unpacking the csv

        # Start with generating a new id and add that to appropriate dictionaries
        engineID = idgen.getNewID()
        uniqueEngines[engine] = engineID
        pairs[model] = engineID

        # Get the engine cylinders
        engine = engine.replace(ENGINE, '')
        if engine.count(FOUR_CYL) > 0:
            engine = engine.replace(FOUR_CYL, '')
            engineCylinders = 4
        elif engine.count(SIX_CYL) > 0:
            engine = engine.replace(SIX_CYL, '')
            engineCylinders = 6
        elif engine.count(EIGHT_CYL) > 0:
            engine = engine.replace(EIGHT_CYL, '')
            engineCylinders = 8
        elif engine.count(TWELVE_CYL) > 0:
            engine = engine.replace(TWELVE_CYL, '')
            engineCylinders = 12
        else:
            engineCylinders = 0

        # Get engine volume
        engineLiters = float(engine[0:3])
        engine = engine.replace(LITER, '')
        engine = engine[3:]

        # Finally the engine name is all that remains
        engineName = engine.strip()

        engines.append(EngineOpt(engineID, engineName, engineLiters, engineCylinders))

    return engines, pairs

def parseWheelData(data):
    uniqueWheels = {}
    textWheelDict = {}

    wheelOpts = []
    modelWheelOpts = []
    wheelDict = {model: [] for wheels, model in data}

    idgen = IDGenerator()
    for wheels, model in data:
        # Process the wheels data to separate into unique wheels
        wheels = wheels.strip().split('/')
        wheels = [wheels[i] + '/' + wheels[i+1] for i in range(0, len(wheels), 2)]

        for wheel in wheels:
            # If we already processed, assign that id to the model and move on
            if wheel in uniqueWheels:
                modelWheelOpts.append(ModelWheelOpt(model, uniqueWheels[wheel]))
                wheelDict[model].append(textWheelDict[wheel])
                continue

            # Otherwise start the process of unpacking the csv

            # Start with generating a new id and add that to appropriate dictionaries
            wheelsID = idgen.getNewID()
            modelWheelOpts.append(ModelWheelOpt(model, wheelsID))
            uniqueWheels[wheel] = wheelsID

            firstHalf, secondHalf = wheel.replace('\"', '').strip().split('/')

            diameter = int(firstHalf[0:2])
            wheelName = firstHalf[2:].replace('wheels', '').strip()

            stylePart, runFlatPart = secondHalf.split('with')
            tireStyle = stylePart.replace('style', '').strip()
            runFlat = runFlatPart.replace('tires', '').strip()

            wheelOpt = WheelOpt(wheelsID, diameter, wheelName, tireStyle, runFlat)
            wheelOpts.append(wheelOpt)
            textWheelDict[wheel] = wheelOpt
            wheelDict[model].append(wheelOpt)

    return wheelOpts, modelWheelOpts, wheelDict

def parseColorData(data):
    uniqueColors = set()
    textColorDict = {}

    colorOpts = []
    modelColorOpts = set()
    colorDict = {model: [] for colors, model in data}

    for colors, model in data:
        for color in colors.split('/'):
            color = color.strip()
            if color not in uniqueColors:
                uniqueColors.add(color)
                colorOpt = ColorOpt(color)
                colorOpts.append(colorOpt)
                textColorDict[color] = colorOpt

            modelColorOpts.add(ModelColorOpt(model, color))
            colorDict[model].append(textColorDict[color])

    return colorOpts, list(modelColorOpts), colorDict

def parseDesignData(data):
    uniqueDesigns = set()
    textDesignDict = {}

    designOpts = []
    modelDesignOpts = set()
    designDict = {model: [] for designs, model in data}

    for designs, model in data:
        for design in designs.split('/'):
            design = design.strip()
            if design not in uniqueDesigns:
                uniqueDesigns.add(design)
                designOpt = BodyDesignOpt(design)
                designOpts.append(designOpt)
                textDesignDict[design] = designOpt

            modelDesignOpts.add(ModelDesignOpt(model, design))
            designDict[model].append(textDesignDict[design])

    return designOpts, list(modelDesignOpts), designDict

def parseUpholsteryData(data):
    uniqueUpholsteries = set()
    textUpholsteryDict = {}

    upholsteryOpts = []
    modelUpholsteryOpts = set()
    upholsteryDict = {model: [] for colors, model in data}

    for upholsteries, model in data:
        for upholstery in upholsteries.split('/'):
            upholstery = upholstery.strip()
            if upholstery not in uniqueUpholsteries:
                uniqueUpholsteries.add(upholstery)
                upholsteryOpt = UpholsteryOpt(upholstery, 100 * r.randint(0, 5))
                upholsteryOpts.append(upholsteryOpt)
                textUpholsteryDict[upholstery] = upholsteryOpt

            modelUpholsteryOpts.add(ModelUpholsteryOpt(model, upholstery))
            upholsteryDict[model].append(textUpholsteryDict[upholstery])

    return upholsteryOpts, list(modelUpholsteryOpts), upholsteryDict

def parseUpgradeData(data):
    uniqueUpgrades = set()

    upgradeOpts = []

    for upgrades in data:
        for upgrade in upgrades.split('/'):
            upgrade, cost = upgrade.strip().split(':')
            if upgrade not in uniqueUpgrades:
                uniqueUpgrades.add(upgrade)
                upgradeOpts.append(OptUpgrade(upgrade, int(cost)))

    return upgradeOpts

def readStaticInfo(tables):
    # Get initial data from the csv
    CSVDataRow = namedtuple('CSVDataRow', ('brand', 'series', 'model', 'year', 'design', 'engine', 'drivetrain',
                                           'wheels', 'upholstery', 'color', 'optional_upgrades', 'cost'))
    with open(STATIC_DATA_FILEPATH, 'r') as f:
        staticDataRows = [CSVDataRow(*line.strip().split(',')) for line in f]

    # Create the engine data
    data = [(x.engine, x.model) for x in staticDataRows]
    engines, engineModelPairs = parseEngineData(data)
    tables[ENGINE_OPT] = engines

    # Combine with engine and create model data
    models = [Model(d.model, d.brand, d.series, int(d.year), d.drivetrain,
                    'Automatic', int(d.cost), engineModelPairs[d.model]) for d in staticDataRows]
    tables[MODEL] = models

    # Create the wheel data
    data = [(x.wheels, x.model) for x in staticDataRows]
    wheels, modelWheelOpts, wheelDict = parseWheelData(data)
    tables[WHEEL_OPT] = wheels
    tables[MODEL_WHEEL_OPT] = modelWheelOpts

    # Create the color data
    data = [(x.color, x.model) for x in staticDataRows]
    colors, modelColorOpts, colorDict = parseColorData(data)
    tables[COLOR_OPT] = colors
    tables[MODEL_COLOR_OPT] = modelColorOpts

    # Create the body design data
    data = [(x.design, x.model) for x in staticDataRows]
    designs, modelDesignOpts, designDict = parseDesignData(data)
    tables[BODY_DESIGN_OPT] = designs
    tables[MODEL_DESIGN_OPT] = modelDesignOpts

    # Create the upholstery data
    data = [(x.upholstery, x.model) for x in staticDataRows]
    upholsteries, modelUpholsteryOpts, upholsteryDict = parseUpholsteryData(data)
    tables[UPHOLSTERY_OPT] = upholsteries
    tables[MODEL_UPHOLSTERY_OPT] = modelUpholsteryOpts

    # Create optional upgrade data
    data = [x.optional_upgrades for x in staticDataRows]
    upgrades = parseUpgradeData(data)
    tables[OPT_UPGRADE] = upgrades

    return wheelDict, colorDict, designDict, upholsteryDict

def createGeneratedInfo(tables, wheelDict, colorDict, designDict, upholsteryDict):

    # First, create the suppliers, manufacturers, and dealers. Each has the same
    # general properties. Nothing generated should have the same business name or address
    idgen = IDGenerator()
    addrgen = AddressGenerator()
    namegen = BusinessNameGenerator()
    for _ in xrange(NUM_DEALERS):
        street, city, state, ZIP = addrgen.getNewAddress()
        dealer = Dealer(idgen.getNewID(), namegen.getNewName(), street, city, state, ZIP)
        tables[DEALER].append(dealer)

    idgen = IDGenerator()
    for _ in xrange(NUM_MANUFACTURERS):
        street, city, state, ZIP = addrgen.getNewAddress()
        manufacturer = Manufacturer(idgen.getNewID(), namegen.getNewName(), street, city, state, ZIP)
        tables[MANUFACTURER].append(manufacturer)

    idgen = IDGenerator()
    for _ in xrange(NUM_SUPPLIERS):
        street, city, state, ZIP = addrgen.getNewAddress()
        supplier = Supplier(idgen.getNewID(), namegen.getNewName(), street, city, state, ZIP)
        tables[SUPPLIER].append(supplier)

    # Connect suppliers and manufacturers so there is at least one connection for each supplier
    # and between 2 and 5 connections per manufacturer
    suppManDict = {x: r.choice(tables[MANUFACTURER]) for x in tables[SUPPLIER]}
    manSuppDict = {x: set() for x in tables[MANUFACTURER]}
    for supplier, manufacturers in suppManDict.iteritems():
        manSuppDict[manufacturers].add(supplier)

    for manufacturer, suppliers in manSuppDict.iteritems():
        while len(suppliers) < MIN_MAN_SUPPLIERS:
            manSuppDict[manufacturer].add(r.choice(tables[SUPPLIER]))

    while float(sum(map(lambda x: len(x), manSuppDict.values()))) / len(manSuppDict) < AVG_MAN_SUPPLIERS:
        for _ in xrange(5):
            manufacturer = r.choice(manSuppDict.keys())
            if len(manSuppDict[manufacturer]) >= 5:
                continue
            manSuppDict[manufacturer].add(r.choice(tables[SUPPLIER]))

    tables[SUPPLIES] = [Supplies(s.supplier_id, m.manufacturer_id) for m, sps in manSuppDict.iteritems() for s in sps]

    # Generate the customers, their phone numbers, and the sales they participate in
    idgen = IDGenerator()
    for _ in xrange(NUM_COMPANIES):
        ID = idgen.getNewID()
        street, city, state, ZIP = addrgen.getNewAddress()
        tables[CUSTOMER].append(Customer(ID, namegen.getNewName(), street, city, state, ZIP))
        tables[COMPANY].append(Company(ID, r.choice(('small', 'mid_size', 'large'))))

    persongen = NameGenerator()
    for _ in xrange(NUM_PEOPLE):
        ID = idgen.getNewID()
        street, city, state, ZIP = addrgen.getNewAddress()
        male = r.randint(0, 1) == 0
        tables[CUSTOMER].append(Customer(ID, persongen.getNewName(male), street, city, state, ZIP))
        tables[PERSON].append(Person(ID, 'male' if male else 'female', 1000 * r.randint(28, 300)))

    idgen = IDGenerator()
    phonegen = IDGenerator(10)
    for cust in tables[CUSTOMER]:
        for _ in xrange(r.randint(MIN_PHONE_NUMS, MAX_PHONE_NUMS)):
            tables[CUST_PHONE].append(CustPhone(cust.customer_id, str(phonegen.getNewID()).zfill(10)))
    nums = range(0, len(tables[CUSTOMER]))
    r.shuffle(nums)
    for i in nums:
        tables[SALE].append(Sale(idgen.getNewID(), tables[CUSTOMER][i].customer_id,
                                 r.randint(1, 30), r.randint(1, 12), 2019))
    for _ in xrange(EXTRA_SALES):
        tables[SALE].append(Sale(idgen.getNewID(), r.choice(tables[CUSTOMER]).customer_id,
                                 r.randint(1, 30), r.randint(1, 12), 2019))

    # Finally, let us generate the vehicles.
    idgen = IDGenerator(5)
    for dealer in tables[DEALER]:
        for _ in xrange(NUM_VEHICLES_PER_DEALER):
            model = r.choice(tables[MODEL])
            upholstery = r.choice(upholsteryDict[model.model_name])
            tables[VEHICLE].append(Vehicle(str(idgen.getNewID()).zfill(5),
                                           model.model_name,
                                           r.choice(wheelDict[model.model_name]).wheel_id,
                                           r.choice(colorDict[model.model_name]).color,
                                           r.choice(designDict[model.model_name]).design_name,
                                           upholstery.style,
                                           r.randint(1, 30),
                                           r.randint(1, 12),
                                           2018,
                                           model.base_cost + upholstery.material_cost,
                                           dealer.dealer_id,
                                           'null',
                                           r.choice(tables[MANUFACTURER]).manufacturer_id))

    for sale in tables[SALE]:
        numVehicles = 1
        if r.randint(0, 3) == 0:
            numVehicles = r.randint(2, 5)
        for _ in xrange(numVehicles):
            vehicle = r.choice(tables[VEHICLE])
            while vehicle.sale_id != 'null':
                vehicle = r.choice(tables[VEHICLE])
            idx = tables[VEHICLE].index(vehicle)
            tables[VEHICLE][idx] = vehicle._replace(sale_id=sale.sale_id)

    vehicleUpgrades = {vehicle.VIN: set() for vehicle in tables[VEHICLE]}
    for i in xrange(len(tables[VEHICLE])):
        cost = tables[VEHICLE][i].price
        if r.randint(0, 1) == 0:
            for _ in xrange(r.randint(1, 5)):
                upgrade = r.choice(tables[OPT_UPGRADE])
                if upgrade in vehicleUpgrades[tables[VEHICLE][i].VIN]:
                    continue
                vehicleUpgrades[tables[VEHICLE][i].VIN].add(upgrade)
                cost += upgrade.added_cost
        tables[VEHICLE][i] = tables[VEHICLE][i]._replace(price=cost + r.randint(0, 20) * 1000)

    tables[VEHICLE_UPGRADE] = [VehicleUpgrade(VIN, upgrade.upgrade_name)
                               for VIN, upgrades in vehicleUpgrades.iteritems() for upgrade in upgrades]

def main():
    tables = setupDict()

    wheelDict, colorDict, designDict, upholsteryDict = readStaticInfo(tables)
    createGeneratedInfo(tables, wheelDict, colorDict, designDict, upholsteryDict)

    outputTablesToFiles(tables)

if __name__ == '__main__':
    main()
