import GenerateCustomerData as cd
import GenerateBusiness as b

def generate(streetfile, countyfile, stateFile, businessFile, str,number):
    names=b.generate(businessFile, number)
    addresses=cd.generateAddress(streetfile, countyfile, stateFile,str,number)
    info=list()
    for name in names:
        name=name[:name.index(",")]
    for x in range((int)(number)):
        info.append(str+"Name="+names[x]+addresses[x])
    return info
