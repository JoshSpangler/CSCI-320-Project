def getData(allData):
    while raw_input('Do you want to make a relation table? ') == 'y':
        query = raw_input('What do you want to generate (separate only by spaces)? ')
        filename = raw_input('What is the filename? ')
        parsed_query = query.split(' ')
        f = open('../Output/'+filename+'.csv', 'w')
        f.write(','.join(parsed_query)+'\n')
        alreadyDone = []
        for data in allData:
            if 'Individual_Buyer' in filename and 'CustomerGender' not in data:
                continue
            elif 'Company' in filename and 'BusinessSize' not in data:
                continue
            split_data = data.split(',')
            string=''
            for query in parsed_query:
                for data_element in range(len(split_data)):
                    if query + '=' in split_data[data_element]:
                        if query == 'OptionalUpgrade':
                            f.write(string+split_data[data_element][len(query)+1:] + ',' + 
                                    split_data[data_element+1][len('OptionalUpgradeCost')+1:] + '\n')
                        elif query == 'OptionalUpgradeCost':
                            continue
                        else:
                            string += (split_data[data_element][len(query)+1:] + ',')
            if 'OptionalUpgrade' not in parsed_query and string[:-1] not in alreadyDone:
                if 'InventoryID' not in parsed_query or 'NULL' not in string:
                    if 'SaleDay' not in parsed_query or 'NULL' not in string:
                        f.write(string[:-1]+'\n')
                        alreadyDone.append(string[:-1])
        f.close()
