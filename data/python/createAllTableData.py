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

    idgen = IDGenerator(3)
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

    wheelOpts = []
    modelWheelOpts = []

    idgen = IDGenerator(3)
    for wheels, model in data:
        # Process the wheels data to separate into unique wheels
        wheels = wheels.strip().split('/')
        wheels = [wheels[i] + '/' + wheels[i+1] for i in range(0, len(wheels), 2)]

        for wheel in wheels:
            # If we already processed, assign that id to the model and move on
            if wheel in uniqueWheels:
                modelWheelOpts.append(ModelWheelOpt(model, uniqueWheels[wheel]))
                continue

            # Otherwise start the process of unpacking the csv

            # Start with generating a new id and add that to appropriate dictionaries
            wheelsID = idgen.getNewID()
            uniqueWheels[wheel] = wheelsID
            modelWheelOpts.append(ModelWheelOpt(model, wheelsID))

            wheel = wheel.replace('\"', '')
            firstHalf, secondHalf = wheel.strip().split('/')

            diameter = int(firstHalf[0:2])
            wheelName = firstHalf[2:].replace('wheels', '').strip()

            stylePart, runFlatPart = secondHalf.split('with')
            tireStyle = stylePart.replace('style', '').strip()
            runFlat = runFlatPart.replace('tires', '').strip()

            wheelOpts.append(WheelOpt(wheelsID, diameter, wheelName, tireStyle, runFlat))

    return wheelOpts, modelWheelOpts

def parseColorData(data):
    uniqueColors = set()

    colorOpts = []
    modelColorOpts = []

    for colors, model in data:
        for color in colors.split('/'):
            color = color.strip()
            if color not in uniqueColors:
                uniqueColors.add(color)
                colorOpts.append(ColorOpt(color))

            modelColorOpts.append(ModelColorOpt(model, color))

    return colorOpts, modelColorOpts

def parseDesignData(data):
    uniqueDesigns = set()

    designOpts = []
    modelDesignOpts = []

    for designs, model in data:
        for design in designs.split('/'):
            design = design.strip()
            if design not in uniqueDesigns:
                uniqueDesigns.add(design)
                designOpts.append(BodyDesignOpt(design))

            modelDesignOpts.append(ModelDesignOpt(model, design))

    return designOpts, modelDesignOpts

def parseUpholsteryData(data):
    uniqueUpholsteries = set()

    upholsteryOpts = []
    modelUpholsteryOpts = []

    for upholsteries, model in data:
        for upholstery in upholsteries.split('/'):
            upholstery = upholstery.strip()
            if upholstery not in uniqueUpholsteries:
                uniqueUpholsteries.add(upholstery)
                upholsteryOpts.append(UpholsteryOpt(upholstery, 100 * r.randint(0, 5)))

            modelUpholsteryOpts.append(ModelUpholsteryOpt(model, upholstery))

    return upholsteryOpts, modelUpholsteryOpts

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
    models = [Model(d.model, d.brand, d.series, d.year, d.drivetrain,
                    'Automatic', d.cost, engineModelPairs[d.model]) for d in staticDataRows]
    tables[MODEL] = models

    # Create the wheel data
    data = [(x.wheels, x.model) for x in staticDataRows]
    wheels, modelWheelOpts = parseWheelData(data)
    tables[WHEEL_OPT] = wheels
    tables[MODEL_WHEEL_OPT] = modelWheelOpts

    # Create the color data
    data = [(x.color, x.model) for x in staticDataRows]
    colors, modelColorOpts = parseColorData(data)
    tables[COLOR_OPT] = colors
    tables[MODEL_COLOR_OPT] = modelColorOpts

    # Create the body design data
    data = [(x.design, x.model) for x in staticDataRows]
    designs, modelDesignOpts = parseDesignData(data)
    tables[BODY_DESIGN_OPT] = designs
    tables[MODEL_DESIGN_OPT] = modelDesignOpts

    # Create the upholstery data
    data = [(x.upholstery, x.model) for x in staticDataRows]
    upholsteries, modelUpholsteryOpts = parseUpholsteryData(data)
    tables[UPHOLSTERY_OPT] = upholsteries
    tables[MODEL_UPHOLSTERY_OPT] = modelUpholsteryOpts

    # Create optional upgrade data
    data = [x.optional_upgrades for x in staticDataRows]
    upgrades = parseUpgradeData(data)
    tables[OPT_UPGRADE] = upgrades

def createGeneratedInfo(tables):
    pass

def outputTablesToFiles(tables):
    for table in tables.values():
        if len(table) > 0:
            outputToFile(table)

def main():
    tables = setupDict()

    readStaticInfo(tables)
    createGeneratedInfo(tables)

    outputTablesToFiles(tables)

if __name__ == '__main__':
    main()
