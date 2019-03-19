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

def createEngineData(engineData):
    WITHEM = ' with integrated electric motor'
    ENGINE = ' engine'
    LITER = '-liter '
    FOUR_CYL = ' 4-cylinder'
    SIX_CYL = ' 6-cylinder'
    EIGHT_CYL = 'V-8'
    TWELVE_CYL = 'V-12'

    engines = []

    idgen = IDGenerator(3)
    for engine in engineData:
        engineID = idgen.getNewID()
        engineName = ''
        if engine.endswith(WITHEM):
            engineName += WITHEM
            engine.replace(WITHEM, '')

        engine.replace(ENGINE, '')
        if engine.endswith(FOUR_CYL):
            engine.replace(FOUR_CYL, '')
            engineCylinders = 4
        elif engine.endswith(SIX_CYL):
            engine.replace(SIX_CYL, '')
            engineCylinders = 6
        elif engine.endswith(EIGHT_CYL):
            engine.replace(EIGHT_CYL, '')
            engineCylinders = 8
        elif engine.endswith(TWELVE_CYL):
            engine.replace(TWELVE_CYL, '')
            engineCylinders = 12
        else:
            engineCylinders = 0

        engineLiters = float(engine[0:3])
        engine.replace(LITER, '')
        engine = engine[3:]

        engineName = engine + engineName

        engines.append(EngineOpt(engineID, engineName, engineLiters, engineCylinders))

    return engines

def readStaticInfo(tables):
    CSVDataRow = namedtuple('CSVDataRow', ('series', 'model', 'year', 'design', 'engine', 'drivetrain',
                                           'wheels', 'upholstery', 'color', 'optional_upgrades', 'cost'))
    with open(STATIC_DATA_FILEPATH, 'r') as f:
        staticDataRows = [CSVDataRow(line.strip().split(',')) for line in f]

    # Create the engine data
    engineData = set(map(lambda x: x.engine, staticDataRows))
    tables[ENGINE_OPT] += createEngineData(engineData)


def main():
    tables = setupDict()
    readStaticInfo(tables)

    print(tables[ENGINE_OPT])

if __name__ == '__main__':
    main()
