import generateCustomerData as cd
import generateBusiness as b

def generate(streetfile, countyfile, stateFile, businessFile, str, number):
    names = b.generate(businessFile, number)
    addresses = cd.generateAddress(streetfile, countyfile, stateFile, str, number)
    info = []
    for name in names:
        name = name[:name.index(',')]
    for x in range(int(number)):
        info.append(str+'Name='+names[x]+addresses[x])
    return info
