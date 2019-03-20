import sys
import generateCar as c
import generatePeople as p
import generateBusiness as b
import generateManufacturingData as m
import generateCustomerData as cd
import random as r
import generateRelations as gr
import options as o

def substituteOptions(dataset):
    wheels = []
    engines = []
    for data in dataset:
        parsed_data = data.split(',')
        for data_element in parsed_data:
            if 'Wheel=' in data_element:
                wheels.append(data_element[6:])
            elif 'Engine=' in data_element:
                engines.append(data_element[7:])
    wheels = o.getWheelOptions(wheels)
    engines = o.getEngineOptions(engines)
    for x in range(len(dataset)):
        parsed_data = dataset[x].split(',')
        parsed_data[6] = engines[findIndex(engines, parsed_data[6][7:])]
        parsed_data[9] = wheels[findIndex(wheels, parsed_data[9][6:])]
        s = ','
        dataset[x] = s.join(parsed_data)
    return dataset

def findIndex(attributes, part):
    for x in range(len(attributes)):
        if part in attributes[x]:
            return x

def writeToOutput(dataset):
    fullFile = open('../Output/FullFile.csv', 'w')
    carFile = open('../Output/CarFile.csv', 'w')
    customerFile = open('../Output/CustomerFile.csv', 'w')
    dealerFile = open('../Output/DealerFile.csv', 'w')
    for data in dataset:
        fullFile.write(data+'\n')
        parsed_data = data.split(',')
        for data_element in parsed_data:
            if 'Customer' in data_element:
                customerFile.write(data_element)
            elif 'Dealer' in data_element:
                dealerFile.write(data_element)
        customerFile.write('\n')
        dealerFile.write('\n')
        print(data)
    fullFile.close()
    carFile.close()
    customerFile.close()
    dealerFile.close()
    gr.getData(dataset)

def main():
    if len(sys.argv) != 10:
        print('Usage: GenerateData -CarFile -BoysFirstNamesFile -GirlsFirstNamesFile -LastNamesFile '
              '-StreetFile -CountyTable -StateFile -BusinessNamesFile -number')
    else:
        cars = c.generate(sys.argv[1], sys.argv[9])
        customer = p.generate('Customer', sys.argv[2], sys.argv[3], sys.argv[4],
                              sys.argv[5], sys.argv[6], sys.argv[7], sys.argv[9])
        dealer = p.generate('Dealer', sys.argv[2], sys.argv[3], sys.argv[4],
                            sys.argv[5], sys.argv[6], sys.argv[7], sys.argv[9])
        businesses = b.generate(sys.argv[8], sys.argv[9])
        manufacturer = m.generate(sys.argv[5], sys.argv[6], sys.argv[7], sys.argv[8], 'Manufacturer', sys.argv[9])
        supplier = m.generate(sys.argv[5], sys.argv[6], sys.argv[7], sys.argv[8], 'Supplier', sys.argv[9])
        fullData = []
        for x in range(int(sys.argv[9])):
            carString = str(cars[x])[4:len(str(cars[x]))-1]
            carString = carString.replace('\'', '')
            carString = carString.replace('\'', '')
            carString = carString.replace(', ', ',')
            # parsed_string=carString.split(',')
            # total_cost=0
            # for str_element in parsed_string:
            #    if('Cost' in str_element):
            #        total_cost+=(int)(str_element[str_element.index('=')+1:])
            # carString+=',TotalCost='+str(total_cost)
            inventoryNumber = 'InventoryID=NULL'
            dateOfSale = 'SaleMonth=NULL,SaleDay=NULL,SaleYear=NULL'
            addresses = cd.generateAddress(sys.argv[5], sys.argv[6], sys.argv[7], 'Customer', sys.argv[9])
            if r.randint(0, 1) == 0:
                inventoryNumber = 'InventoryID=' + str(r.randint(100000, 999999))
            else:
                dateOfSale = 'SaleMonth=' + str(r.randint(0, 12)) + ',SaleDay=' + str(r.randint(0, 28)) + \
                           ',SaleYear=' + str(r.randint(2019, 2019))
            if r.randint(0, 10) == 0:
                fullData.append(carString + ',' + inventoryNumber + ',SaleID=' + str(r.randint(0, 1000000)) +
                                ',' + dateOfSale + ',CustomerID=' + str(r.randint(0, 1000000)) +
                                ',CustomerName=' + businesses[x] + ',' + addresses[x] +
                                ',' + cd.getPhone('Customer') + ',DealerID=' + str(r.randint(0, 1000000)) +
                                ',' + dealer[x] + ',SupplierID=' + str(x) + ',' + supplier[x] +
                                ',ManufacturerID=' + str(r.randint(0, 1000000)) + ',' + manufacturer[x])
            else:
                fullData.append(carString + ',' + inventoryNumber + ',SaleID=' + str(r.randint(0, 1000000)) +
                                ',' + dateOfSale + ',CustomerID=' + str(r.randint(0, 1000000)) +
                                ',' + customer[x] + ',' + cd.getPhone('Customer') +
                                ',DealerID=' + str(r.randint(0, 1000000)) + ',' + dealer[x] +
                                ',SupplierID=' + str(r.randint(0, 1000000)) + ',' + supplier[x] +
                                ',ManufacturerID=' + str(r.randint(0, 1000000)) + ',' + manufacturer[x])
        fullData = substituteOptions(fullData)
        for x in range(len(fullData)):
            total_cost = 0
            car_attributes = fullData[x].split(',')
            for y in range(len(car_attributes)):
                if 'Cost=' in car_attributes[y]:
                    total_cost += int(car_attributes[y][car_attributes[y].index('=')+1:])
                if 'BaseCost' in car_attributes[y]:
                    car_attributes[y] += ',TotalCost='+str(total_cost)
            fullData[x] = ','.join(car_attributes)
        writeToOutput(fullData)
        # writeToOutput(substituteOptions(fullData))

if __name__ == '__main__':
    main()
