import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Connection;
import java.util.*;
import java.util.List;

public class GUI{

    //
    // CONSTANTS
    //

    private static final String CAR_FILE_PATH = "./data/input/Cars.csv";
    private static final String COLOR = "#00b6ff";
    private static final String SELECT_ALL = "*";

    private static final String WELCOME_PAGE_HEADER =
            getHeader("Welcome to WMB.\nAre you a dealer, customer, or administrator?");
    private static final String DEALER_LOGIN_HEADER =
            getHeader("Hello Dealer! Please login with your dealer ID to access the database.");

    //
    // ATTRIBUTES
    //

    // SQL database connection
    private Connection connection;

    // GUI elements
    private JLabel body;
    private AdminPage adminPage;

    // For sorting based on any conjunction of these
    private String dboSeries;
    private String dboModel;
    private String dboColor;
    private String dboWheelDiameter;
    private String dboWheelName;
    private String dboWheelStyle;
    private String dboWheelRF;
    private String dboUpholstery;
    private String dboDealerID;
    private String selectedCar;
    private ArrayList<String> optUpgradeList;

    /**
     * Constructor for initializing the JFrame and setting the connection
     * @param connection the connection
     */
    public GUI(Connection connection){
        this.connection = connection;

        // Initialize the window
        JFrame window = new JFrame("WMB Database Applications");
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setExtendedState(JFrame.MAXIMIZED_BOTH);
        window.setVisible(true);

        // Create the background image
        ImageIcon bgi = (new ImageIcon("./images/logo.png"));
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int bgSize = (dim.width < dim.height) ? dim.width : dim.height;
        bgi = new ImageIcon(bgi.getImage().getScaledInstance(bgSize, bgSize, Image.SCALE_SMOOTH));

        // Create the body
        body = new JLabel(bgi);
        body.setLayout(new GridBagLayout());
        window.add(body);

        adminPage = new AdminPage(connection, e -> welcomePane());
        welcomePane();
    }

    /**
     * Creates the initial menu pane for distinguishing customers vs. dealers
     * Sets all sorting options to *
     */
    private void welcomePane(){
        // Get ready to add things to the body
        body.removeAll();
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = 0;
        gc.gridy = 0;

        // Initialize all sorting options to SELECT_ALL
        dboSeries = SELECT_ALL;
        dboModel = SELECT_ALL;
        dboColor = SELECT_ALL;
        dboWheelDiameter = SELECT_ALL;
        dboWheelName = SELECT_ALL;
        dboWheelStyle = SELECT_ALL;
        dboWheelRF = SELECT_ALL;
        dboUpholstery = SELECT_ALL;
        dboDealerID = SELECT_ALL;
        optUpgradeList = new ArrayList<>();

        // Welcome page header
        JLabel welcomeLabel = new JLabel(WELCOME_PAGE_HEADER);
        welcomeLabel.setOpaque(true);
        welcomeLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 30));
        body.add(welcomeLabel, gc);
        gc.gridy++;

        // Create the buttons
        JPanel buttons = new JPanel();
        buttons.setLayout(new GridLayout(3,1));
        buttons.setPreferredSize(new Dimension(750,75));
        buttons.setOpaque(false);

        JButton dealer = new JButton("Dealer");
        dealer.addActionListener(e -> getDealerName());
        buttons.add(dealer);

        JButton customer = new JButton("Customer");
        customer.addActionListener(e -> customerPaneInit());
        buttons.add(customer);

        JButton admin = new JButton("Administrator");
        admin.addActionListener(e -> setupAdmin());
        buttons.add(admin);

        body.add(buttons, gc);

        // Refresh the window
        body.revalidate();
        body.repaint();
    }

    /**
     * pane to get the dealerID and add it to the sort-by data
     */
    private void getDealerName(){
        // Get ready to add things to the body
        body.removeAll();
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = 0;
        gc.gridy = 0;

        // Login page header
        JLabel loginLabel = new JLabel(DEALER_LOGIN_HEADER);
        loginLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 30));
        loginLabel.setOpaque(true);
        body.add(loginLabel, gc);
        gc.gridy++;

        // Login form
        JPanel loginForm = new JPanel();
        loginForm.setLayout(new GridLayout(1,2));

        JTextField dealerField = new JTextField(5);
        loginForm.add(dealerField);

        JButton continueButton = new JButton("Continue");
        continueButton.addActionListener(e -> {
            if(dealerField.getText().length() == 0 || !dealerField.getText().matches("\\d+")){
                dealerField.setText("");
            }
            else if (!AccessDatabase.dealerInData(connection, dealerField.getText())){
                dealerField.setText("");
            }
            else {
                dboDealerID = dealerField.getText();
                dealerMenuPane();
            }
        });
        loginForm.add(continueButton);
        body.add(loginForm, gc);

        // Refresh the window
        body.revalidate();
        body.repaint();
    }

    /**
     * The initial dealer pane displaying possible actions that the dealer can take
     */
    public void dealerMenuPane(){
        body.removeAll();
        //if the dealer wants to order a new car
        JButton order = new JButton("Order From Manufacturer");
        JLabel questionLabel = new JLabel("<html><font COLOR="+ COLOR +">Do you want to order a new car, look at your inventory, or check your sale history?</font></html>");
        GridBagConstraints gc = new GridBagConstraints();
        order.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dealerOrderingPane();
            }
        });
        order.setPreferredSize(new Dimension(500, 25));
        //if the dealer wants to look at their inventory
        JButton inventory = new JButton("Vehicle Locator Services");
        inventory.addActionListener(e -> inventoryPane());
        inventory.setPreferredSize(new Dimension(500, 25));
        //if the dealer wants to look at the sale history
        JButton history = new JButton("Sales History");
        history.addActionListener(e -> getDealerHistory(new JPanel()));
        history.setPreferredSize(new Dimension(500, 25));
        gc.gridx = 0;
        gc.gridy = 0;
        questionLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 30));
        body.add(questionLabel, gc);
        gc.gridy++;
        body.add(order, gc);
        gc.gridy++;
        body.add(inventory, gc);
        gc.gridy++;
        body.add(history, gc);
        body.revalidate();
        body.repaint();
    }

    /**
     * Creates the dealer gui for ordering
     */
    public void dealerOrderingPane(){
        body.removeAll();
        //left pane containing some options
        JPanel dealerLeft = new JPanel();
        //right pane containing the cars
        JPanel dealerRight = new JPanel();
        //the panel to put buttons on in the JScrollPane
        String[] fileContents = GetData.readFile(CAR_FILE_PATH);
        GridBagConstraints gc = new GridBagConstraints();
        //The dealers option
        JComboBox<String> Brand = getDealerBrand(fileContents);
        //The models option
        JComboBox<String> Series = getDealerSeries(fileContents);
        //The design option
        JComboBox<String> Model = getDealerModel(fileContents);
        //The design option
        JComboBox<String> Design = getDealerDesign(fileContents);
        //The COLOR option
        JComboBox<String> ColorChoice = getDealerColor(fileContents);
        //The wheel option
        JComboBox<String> WheelChoice = getDealerWheels(fileContents);
        //The upholstry option
        JComboBox<String> Upholstry = getDealerUpholstery(fileContents);
        //The optional upgrades option
        JScrollPane optUpgrades = getDealerUpgrades(fileContents);
        //Creates the buttons for the scrollPane
        JButton cont = new JButton("Continue");
        cont.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String car = "";
                int baseprice = 0;
                AccessDatabase.buyCar(connection, dboDealerID, Brand.getItemAt(Brand.getSelectedIndex()),
                        Series.getItemAt(Series.getSelectedIndex()),
                        Model.getItemAt(Model.getSelectedIndex()),
                        ColorChoice.getItemAt(ColorChoice.getSelectedIndex()),
                        WheelChoice.getItemAt(WheelChoice.getSelectedIndex()),
                        Upholstry.getItemAt(Upholstry.getSelectedIndex()),
                        Design.getItemAt(Design.getSelectedIndex()),
                        baseprice, optUpgradeList);
                System.out.println(car);
                welcomePane();
            }
        });
        //set layouts
        dealerLeft.setLayout(new GridLayout(6,1));
        dealerRight.setLayout(new GridLayout(1,1));
        dealerRight.setOpaque(false);
        //Add all the choices
        dealerRight.add(cont);
        dealerLeft.add(Brand);
        dealerLeft.add(Series);
        dealerLeft.add(Design);
        dealerLeft.add(ColorChoice);
        dealerLeft.add(WheelChoice);
        dealerLeft.add(Upholstry);
        //adding the panels
        gc.gridx = 0;
        gc.gridy = 0;
        body.add(dealerLeft, gc);
        gc.gridx++;
        body.add(optUpgrades, gc);
        gc.gridy++;
        body.add(dealerRight, gc);
        body.revalidate();
        body.repaint();
    }

    public JComboBox<String> getDealerBrand(String[] filecontents){
        JComboBox<String> series = new JComboBox<String>(GetData.getAttribute(filecontents, 0));
        return series;
    }

    public JComboBox<String> getDealerSeries(String[] filecontents){
        return new JComboBox<String>(GetData.getAttribute(filecontents, 1));
    }

    public JComboBox<String> getDealerModel(String[] filecontents){
        return new JComboBox<String>(GetData.getAttribute(filecontents, 2));
    }

    public JComboBox<String> getDealerDesign(String[] filecontents){
        return new JComboBox<String>(GetData.getAttribute(filecontents, 4));
    }

    public JComboBox<String> getDealerColor(String[] filecontents){
        return new JComboBox<String>(GetData.getAttribute(filecontents, 9));
    }

    public JComboBox<String> getDealerUpholstery(String[] filecontents){
        return new JComboBox<String>(GetData.getAttribute(filecontents,8));
    }

    public JComboBox<String> getDealerWheels(String[] filecontents){
        return new JComboBox<String>(GetData.getWheels(GetData.getAttribute(filecontents, 7)));
    }

    public JScrollPane getDealerUpgrades(String[] filecontents){
        String[] attributes = GetData.getAttribute(filecontents, 10);
        JPanel scroll = new JPanel();
        scroll.setLayout(new GridLayout(attributes.length, 1));
        JCheckBox[] checkBoxes = new JCheckBox[attributes.length];
        for(int i = 0; i < attributes.length; i++){
            checkBoxes[i] = new JCheckBox(attributes[i]);
            checkBoxes[i].addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    optUpgradeList.removeAll(optUpgradeList);
                    for(JCheckBox cb: checkBoxes){
                        if(cb.isSelected()){
                            optUpgradeList.add(cb.getText());
                        }
                    }
                }
            });
            scroll.add(checkBoxes[i]);
        }
        JScrollPane scrollPane = new JScrollPane(scroll,ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(750,325));
        return scrollPane;
    }

    public JComboBox<String> getSeries(String accessor){
        //gets the output from the database
        String[][] carData = AccessDatabase.getUnsoldCars(connection,dboSeries,dboModel,dboColor, dboWheelDiameter,dboWheelName,dboWheelStyle
                ,dboWheelRF, dboUpholstery,dboDealerID);
        //creates and adds the strings to the combo box
        String[] series = new String[carData.length+1];
        series[0] = "Series";
        for(int i = 0; i < carData.length; i++){
            series[i+1] = carData[i][0];
        }
        List<String> hashSet = new ArrayList<>( new HashSet<>(Arrays.asList(series)) );
        Collections.sort(hashSet);
        String[] seriesWithoutDuplicates = hashSet.toArray(new String[] {});
        JComboBox<String> seriesCBox = new JComboBox<String>(seriesWithoutDuplicates);
        //what to initially select the combo box as
        if(dboSeries.equals("*")){
            seriesCBox.setSelectedItem("Series");
        }
        else{
            seriesCBox.setSelectedItem(dboSeries);
        }
        //what to do when one is chosen
        seriesCBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String series2 = e.toString();
                series2 = series2.substring(series2.indexOf("selectedItemReminder=")+21,series2.length()-1);
                if(series2.equals("Series")){
                    series2 = "*";
                }
                System.out.println(series2);
                dboSeries = series2;
                //refreshes the pane
                if(accessor.toLowerCase().equals("dealer")) {
                    dealerOrderingPane();
                }
                else{
                    customerPaneInit();
                }
            }
        });
        seriesCBox.setPreferredSize(new Dimension(500, 25));
        return seriesCBox;
    }

    /**
     * Gets the model based on the sort by data
     * @param accessor the person accessing the info
     * @return a JComboBox of models
     */
    public JComboBox<String> getModel(String accessor){
        //gets output from the database
        String[][] carData = AccessDatabase.getUnsoldCars(connection,dboSeries, dboModel,dboColor, dboWheelDiameter,dboWheelName,dboWheelStyle
                ,dboWheelRF, dboUpholstery,dboDealerID);
        String[] models = new String[carData.length+1];
        models[0] = "Model";
        for(int i = 0; i < carData.length; i++){
            models[i+1] = carData[i][1];
        }
        List<String> hashSet = new ArrayList<>( new HashSet<>(Arrays.asList(models)) );
        Collections.sort(hashSet);
        String[] modelsWithoutDuplicates = hashSet.toArray(new String[] {});
        JComboBox<String> modelCBox = new JComboBox<String>(modelsWithoutDuplicates);
        //sets the selected item based on the sort-by data
        if(dboModel.equals("*")){
            modelCBox.setSelectedItem("Model");
        }
        else{
            modelCBox.setSelectedItem(dboModel);
        }
        //if a certain model is chosen
        modelCBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String model = e.toString();
                model = model.substring(model.indexOf("selectedItemReminder=")+21,model.length()-1);
                if(model.equals("Model")){
                    model = "*";
                }
                System.out.println(model);
                dboModel = model;
                //refreshes the pane
                if(accessor.toLowerCase().equals("dealer")) {
                    dealerOrderingPane();
                }
                else{
                    customerPaneInit();
                }
            }
        });
        modelCBox.setPreferredSize(new Dimension(500, 25));
        return modelCBox;
    }

    /**
     * Uses the model, series, wheels, and upholstry to sort out the COLOR combo box
     * @return a JComboBox based on model, series, wheels, and upholstry
     */

    public JComboBox<String> getColor(String accessor){
        //gets output from the database
        String[][] carData = AccessDatabase.getUnsoldCars(connection, dboSeries,dboModel,dboColor, dboWheelDiameter,
                dboWheelName,dboWheelStyle,dboWheelRF, dboUpholstery,dboDealerID);
        String[] colors = new String[carData.length+1];
        colors[0] = "Colors";
        for(int i = 0; i < carData.length; i++){
            colors[i+1] = carData[i][2];
        }
        List<String> hashSet = new ArrayList<>( new HashSet<>(Arrays.asList(colors)) );
        Collections.sort(hashSet);
        String[] colorsWithoutDuplicates = hashSet.toArray(new String[] {});
        JComboBox<String> colorsCBox = new JComboBox<String>(colorsWithoutDuplicates);
        //sets the selected item based on the sort-by data
        if(dboColor.equals("*")){
            colorsCBox.setSelectedItem("Colors");
        }
        else{
            colorsCBox.setSelectedItem(dboColor);
        }
        //if one of these is chosen
        colorsCBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String color = e.toString();
                color = color.substring(color.indexOf("selectedItemReminder=")+21,color.length()-1);
                if(color.equals("Colors")){
                    color = "*";
                }
                System.out.println(color);
                dboColor = color;
                //refreshes the pane
                if(accessor.toLowerCase().equals("dealer")) {
                    dealerOrderingPane();
                }
                else{
                    customerPaneInit();
                }
            }
        });
        colorsCBox.setPreferredSize(new Dimension(500, 25));
        return colorsCBox;
    }

    /**
     * Uses the model, series, COLOR, and upholstry to sort out the wheels combo box
     * @return a JComboBox based on model, series, COLOR, and upholstry
     */
    public JComboBox<String> getWheels(String accessor){
        //gets the data to fill in the wheel combo box from the database
        String[][] carData = AccessDatabase.getUnsoldCars(connection,dboSeries,dboModel,dboColor, dboWheelDiameter,
                dboWheelName,dboWheelStyle,dboWheelRF, dboUpholstery, dboDealerID);
        //Creates the combo box
        String[] wheels = new String[carData.length+1];
        wheels[0] = "Wheels";
        for(int i = 0; i < carData.length; i++){
            wheels[i+1] = carData[i][6]+", "+carData[i][7]+", "+carData[i][8]+", "+carData[i][9];
        }
        List<String> hashSet = new ArrayList<>( new HashSet<>(Arrays.asList(wheels)) );
        Collections.sort(hashSet);
        String[] wheelsWithoutDuplicates = hashSet.toArray(new String[] {});
        JComboBox<String> wheelsCBox = new JComboBox<String>(wheelsWithoutDuplicates);
        //sets the default value for the combobox
        if(dboWheelName.equals("*")){
            wheelsCBox.setSelectedItem("Wheels");
        }
        else{
            wheelsCBox.setSelectedItem(dboWheelDiameter+", "+dboWheelName+", "+dboWheelStyle+", "+dboWheelRF);
        }
        wheelsCBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //gets the selected string
                String wheels = e.toString();
                wheels = wheels.substring(wheels.indexOf("selectedItemReminder=")+21,wheels.length()-1);
                if(wheels.equals("Wheels")){
                    wheels = "*";
                }
                //Changes the values for the database to be queried on
                String[] wheelAttribs = wheels.split(", ");
                dboWheelDiameter = wheelAttribs[0];
                dboWheelName = wheelAttribs[1];
                dboWheelStyle = wheelAttribs[2];
                dboWheelRF = wheelAttribs[3];
                //reloads the pane
                if(accessor.toLowerCase().equals("dealer")) {
                    dealerOrderingPane();
                }
                else{
                    customerPaneInit();
                }
            }
        });
        wheelsCBox.setPreferredSize(new Dimension(500, 25));
        return wheelsCBox;
    }

    /**
     * Uses the model, series, COLOR, and wheels to sort out the wheels combo box
     * @param accessor the person accessing the database
     * @return a JComboBox based on model, series, COLOR, and wheels
     */
    public JComboBox<String> getUpholstry(String accessor){
        //gets output from the database
        String[][] carData = AccessDatabase.getUnsoldCars(connection, dboSeries, dboModel, dboColor, dboWheelDiameter, dboWheelName,
                dboWheelStyle, dboWheelRF, dboUpholstery, dboDealerID);
        String[] upholstries = new String[carData.length+1];
        upholstries[0] = "Upholstry";
        for(int i = 0; i < carData.length; i++){
            upholstries[i+1] = carData[i][4];
        }
        List<String> hashSet = new ArrayList<>( new HashSet<>(Arrays.asList(upholstries)) );
        Collections.sort(hashSet);
        String[] upholstriesWithoutDuplicates = hashSet.toArray(new String[] {});
        JComboBox<String> upholstriesCBox = new JComboBox<String>(upholstriesWithoutDuplicates);
        //what to set the selected item to based on the sort-by data
        if(dboUpholstery.equals("*")){
            upholstriesCBox.setSelectedItem("Upholstry");
        }
        else{
            upholstriesCBox.setSelectedItem(dboUpholstery);
        }
        upholstriesCBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String upholstry = e.toString();
                upholstry = upholstry.substring(upholstry.indexOf("selectedItemReminder=")+21,upholstry.length()-1);
                if(upholstry.equals("Upholstry")){
                    upholstry = "*";
                }
                dboUpholstery = upholstry;
                //refreshes the pane
                if(accessor.toLowerCase().equals("dealer")) {
                    dealerOrderingPane();
                }
                else{
                    customerPaneInit();
                }
            }
        });
        upholstriesCBox.setPreferredSize(new Dimension(500, 25));
        return upholstriesCBox;
    }

    /**
     * Returns a list of JButtons for a scrollPane that matches the attributes that the user put in
     * @return a list of JButtons for cars
     */
    public JButton[] getDealerCars(){
        JButton[] cars = new JButton[]{new JButton("Car")};
        for(JButton b:cars){
            b.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    selectedCar = b.getText();
                    dealerReciept();
                }
            });
        }
        return cars;
    }

    /**
     * Shows the dealer a reciept of their purchase and returns the dealer to the menu
     */
    public void dealerReciept(){
        body.removeAll();
        body.add(new JLabel("You purchased: "+selectedCar));
        JButton cont = new JButton("Continue");
        cont.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                welcomePane();
            }
        });
        body.add(cont);
        body.revalidate();
        body.repaint();
    }

    /**
     * Shows the dealer's inventory
     */
    public void inventoryPane(){
        body.removeAll();
        //creates the scroll pane
        JButton cont = new JButton("Continue");
        JTable table = getCarsByDealerID();
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(750,325));
        //when the user tries to continue
        cont.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dboDealerID = "*";
                welcomePane();
            }
        });
        cont.setPreferredSize(new Dimension(100,50));
        body.add(scrollPane);
        body.add(cont);
        body.revalidate();
        body.repaint();
    }

    /**
     * Gets a JTable with each car that fits the sort-by data
     * @return a JTable with each car that fits the sort-by data
     */
    public JTable getCarsByDealerID(){
        //gets output from the database
        String[][] carData = AccessDatabase.getUnsoldCars(connection, dboSeries,dboModel,dboColor, dboWheelDiameter,
                dboWheelName,dboWheelStyle,dboWheelRF, dboUpholstery,dboDealerID);
        JTable cars = new JTable(carData.length, (carData.length>0)?(carData[0].length):(0));
        //string and JTable builder
        for(int i = 0; i < carData.length; i++){
            for(int j = 0; j < carData[i].length; j++){
                cars.setValueAt(carData[i][j],i,j);
            }
        }
        //Creates the header for the columns
        String[] header = new String[]{"Series","Model","Color","Design","Upholstery", "Price", "Wheel Diameter","Wheel Name",
                "Wheel Style", "Wheel Runflat"};
        for(int i = 0; i<cars.getColumnCount(); i++) {
            cars.getColumnModel().getColumn(i).setMinWidth(200);
            cars.getTableHeader().getColumnModel().getColumn(i).setHeaderValue(header[i]);
        }
        cars.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        return cars;
    }

    /**
     * Shows the dealer's history with regard to sales
     * @param scrollPanel the scrollPanel to be shown (initially blank)
     */

    public void getDealerHistory(JPanel scrollPanel){
        body.removeAll();
        JPanel checkBoxes = new JPanel();
        JButton show = new JButton("Show data");
        JButton cont = new JButton("Continue");
        GridBagConstraints gc = new GridBagConstraints();
        JCheckBox[] cba = new JCheckBox[]{
                new JCheckBox("VIN"), new JCheckBox("Model"), new JCheckBox("Series"),
                new JCheckBox("Color"),new JCheckBox("Design"),new JCheckBox("Upholstery"),
                new JCheckBox("Manufactured Day"), new JCheckBox("Manufacture Month"),
                new JCheckBox("Manufacture Year"), new JCheckBox("Base Cost"),
                new JCheckBox("Total Cost"), new JCheckBox("Optional upgrades"),
                new JCheckBox("Additional Cost"), new JCheckBox("Wheel Diameter"),
                new JCheckBox("Wheel Name"),new JCheckBox("Wheel Style"),new JCheckBox("Wheel Runflat"),
                new JCheckBox("Drivetrain"),new JCheckBox("Transmission"),new JCheckBox("Sale Day"),
                new JCheckBox("Sale Month"), new JCheckBox("Sale Year"), new JCheckBox("Customer Name"),
                new JCheckBox("Customer Street"), new JCheckBox("Customer County"),
                new JCheckBox("Customer State"), new JCheckBox("Customer ZIP")};
        checkBoxes.setLayout(new GridLayout(cba.length,1));
        for(JCheckBox c:cba){
            checkBoxes.add(c);
        }
        show.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean[] isSelected = new boolean[cba.length];
                for(int i = 0; i < cba.length; i++){
                    isSelected[i] = cba[i].isSelected();
                }
                String[][] carData = AccessDatabase.getCarHistoryByDealerID(connection,dboDealerID, isSelected);
                JTable table = new JTable(carData.length, (carData.length>0)?(carData[0].length):(0));
                JScrollPane scrollPane = new JScrollPane(table,ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                        ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
                for(int i = 0; i < carData.length; i++){
                    for(int j = 0; j < carData[i].length; j++){
                        table.setValueAt(carData[i][j],i,j);
                    }
                }
                for(int i = 0; i<table.getColumnCount(); i++){
                    table.getColumnModel().getColumn(i).setMinWidth(200);
                    table.getTableHeader().getColumnModel().getColumn(i).setHeaderValue(cba[i].getText());
                }
                table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                scrollPanel.add(scrollPane);
                getDealerHistory(scrollPanel);
            }
        });
        cont.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                welcomePane();
            }
        });
        gc.gridx = 0;
        gc.gridy = 0;
        body.add(checkBoxes,gc);
        gc.gridx++;
        scrollPanel.setOpaque(false);
        body.add(scrollPanel, gc);
        gc.gridx = 0;
        gc.gridy++;
        body.add(show,gc);
        gc.gridy++;
        body.add(cont, gc);
        body.revalidate();
        body.repaint();
    }

    /**
     * The first customer pane where the specifics are specified
     */
    public void customerPaneInit(){
        body.removeAll();
        GridBagConstraints gc = new GridBagConstraints();
        JButton cont = new JButton("Continue");
        //when continue is pressed
        cont.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chooseCar();
            }
        });
        JLabel seriesPane = new JLabel("<html><font COLOR="+ COLOR +">Series</font></html>");
        JLabel modelPane = new JLabel("<html><font COLOR="+ COLOR +">Model</font></html>");
        JLabel colorPane = new JLabel("<html><font COLOR="+ COLOR +">Color</font></html>");
        JLabel upholstryPane = new JLabel("<html><font COLOR="+ COLOR +">Upholstry</font><html>");
        JLabel wheelPane = new JLabel("<html><font COLOR="+ COLOR +">Wheels</font></html>");
        //gets all JComboBoxes based on the sort-by data
        gc.gridx = 0;
        gc.gridy = 0;
        body.add(seriesPane, gc);
        gc.gridx++;
        body.add(getSeries("Customer"), gc);
        gc.gridx = 0;
        gc.gridy++;
        body.add(modelPane, gc);
        gc.gridx++;
        body.add(getModel("Customer"), gc);
        gc.gridx = 0;
        gc.gridy++;
        body.add(colorPane, gc);
        gc.gridx++;
        body.add(getColor("Customer"), gc);
        gc.gridx = 0;
        gc.gridy++;
        body.add(upholstryPane, gc);
        gc.gridx++;
        body.add(getWheels("Customer"), gc);
        gc.gridx = 0;
        gc.gridy++;
        body.add(wheelPane, gc);
        gc.gridx++;
        body.add(getUpholstry("Customer"), gc);
        gc.gridx = 0;
        gc.gridy++;
        body.add(cont, gc);
        body.revalidate();
        body.repaint();
    }

    /**
     * UI to actually choose the car based on their preferences
     */
    public void chooseCar(){
        body.removeAll();
        JPanel scroll = new JPanel();
        //gets the car information from the database and puts it into the scrollpane buttons
        JButton[] buttons = getUnsoldCars();
        JLabel questionLabel = new JLabel("<html><font COLOR="+ COLOR +">Choose the car you want</font></html>");
        GridBagConstraints gc = new GridBagConstraints();
        scroll.setLayout(new GridLayout(buttons.length, 1));
        for(JButton b:buttons){
            scroll.add(b);
        }
        questionLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 30));
        //creates the scrollpane
        JScrollPane scrollPane = new JScrollPane(scroll,ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(750,325));
        gc.gridx = 0;
        gc.gridy = 0;
        body.add(questionLabel, gc);
        gc.gridy++;
        body.add(scrollPane, gc);
        body.revalidate();
        body.repaint();
    }

    /**
     * returns a list of JButtons with all of the possible cars matching user input
     * @return all the possible cars based on the user input
     */

    public JButton[] getUnsoldCars(){
        //Gets output from the database
        String[][] carData = AccessDatabase.getUnsoldCars(connection, dboSeries,  dboModel,dboColor, dboWheelDiameter,
                dboWheelName,dboWheelStyle,dboWheelRF, dboUpholstery,dboDealerID);
        JButton[] buttons = new JButton[carData.length];
        //builds the car buttons
        for(int i = 0; i<carData.length; i++){
            String car = carData[i][0]+", "+carData[i][1]+", "+carData[i][2]+", "+carData[i][3]+", "+carData[i][4]+", "+
                    carData[i][5]+", "+carData[i][6]+", "+carData[i][7]+", "+carData[i][8]+", "+carData[i][9];
            buttons[i] = new JButton(car);
        }
        //what to do when the car is chosen
        for(JButton b:buttons){
            b.setPreferredSize(new Dimension(750, 50));
            b.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    selectedCar = b.getText();
                    customerReceipt();
                }
            });
        }
        return buttons;
    }

    /**
     * A reciept of the transaction for the customer
     */
    public void customerReceipt(){
        body.removeAll();
        JButton cont = new JButton("Continue");
        JLabel reciept = new JLabel("<html><font COLOR="+ COLOR +">"+selectedCar+"</font></html>");
        JPanel fields = new JPanel();
        GridBagConstraints gc = new GridBagConstraints();
        JTextField firstname = new JTextField(30);
        firstname.setText("First Name");
        JTextField lastname = new JTextField(30);
        lastname.setText("Last Name");
        JTextField gender = new JTextField(30);
        gender.setText("Gender");
        JTextField annualIncome = new JTextField(30);
        annualIncome.setText("Annual Income");
        JTextField street = new JTextField(30);
        street.setText("Street");
        JTextField county = new JTextField(30);
        county.setText("County");
        JTextField state = new JTextField(30);
        state.setText("State");
        JTextField zip = new JTextField(30);
        zip.setText("Zip");
        fields.setLayout(new GridLayout(4,2));
        //what to do when continue is chosen
        cont.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AccessDatabase.sellCar(connection, firstname.getText(), lastname.getText(), gender.getText(),
                        annualIncome.getText(), street.getText(), county.getText(), state.getText(), zip.getText(),
                        AccessDatabase.getVIN(connection, selectedCar));
                AccessDatabase.getVIN(connection, selectedCar);
                welcomePane();
            }
        });
        reciept.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));
        fields.setOpaque(false);
        fields.add(firstname);
        fields.add(lastname);
        fields.add(gender);
        fields.add(annualIncome);
        fields.add(street);
        fields.add(county);
        fields.add(state);
        fields.add(zip);

        gc.gridx = 0;
        gc.gridy = 0;
        body.add(reciept, gc);
        gc.gridy++;
        body.add(fields,gc);
        gc.gridy++;
        body.add(cont, gc);
        body.revalidate();
        body.repaint();
    }

    private void setupAdmin() {
        body.removeAll();

        adminPage.refresh();
        body.add(adminPage);

        body.revalidate();
        body.repaint();
    }

    public static String getHeader(String text) {
        return String.format("<html><font COLOR=%s>%s</font></html>", COLOR, text);
    }

    /**
     * Main method
     * @param args command line args
     */
    public static void main(String[] args){
        Connection c = AccessDatabase.connect("./database/database","me","password");
        if(c != null) {
            GUI g = new GUI(c);
        }
    }
}