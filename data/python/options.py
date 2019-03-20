import random as r

def getWheelOptions(wheels):
    total_wheel = []
    for wheel in wheels:
        if wheel not in total_wheel:
            parsed_wheel = wheel.split()
            wheelStyle = ' '
            wheelStyle = wheelStyle.join(parsed_wheel[1:])
            wheelDiameter = parsed_wheel[0]
            if wheelDiameter == 'style':
                wheelDiameter = str(r.randint(18, 20))
            total_wheel.append('WheelName=' + wheel + ',WheelID=' + str(r.randint(0, 1000000)) + 
                               ',WheelDiameter=' + wheelDiameter + ',WheelStyle=' + wheelStyle + 
                               ',WheelRunFlat=' + str(bool(r.randint(0, 1))))
    return total_wheel

def getEngineOptions(engines):
    total_engine = []
    for engine in engines:
        if engine not in total_engine:
            total_engine.append('EngineName=' + engine + ',EngineID=' + str(r.randint(0, 1000000)) + 
                                ',EngineLiters=' + str(r.random()*6+2)[0:3] + ',EngineNumCylinders=' + 
                                str(r.randint(2, 4)*2) + ',EngineProductionCost=' + str(r.randint(2, 8)*1000))
    return total_engine

def getExtraCost(attributes, strn):
    attribCost = []
    for attribute in attributes:
        if attribute not in attribCost:
            attribCost.append(attribute+',' + strn + 'ExtraCost=' + str(r.randint(5, 20)*100))
    return attribCost
