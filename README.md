# CSCI-320-Project

### After Downloading the ZIP (or cloning)
1. Make sure you are using **java 11** to prevent any initial compilation and execution issues
2. Make sure the **h2-1.4.197** is added as a **module** in the **project settings**

### To Connect to the h2 Browser Database:
1. Make sure a .db file is present in the **database/** folder (if not, run the **Populator.java** class)
2. Run the **h2-1.4.197.jar** file and make sure the following settings are set properly:
    * The **JDBC URL** is **jdbc:h2:./database/database**
    * The **username** is **me**
    * The **password** is **password**

### To Start the Database Application:
1. Locate and run the **gui.java** class in the **java** folder
