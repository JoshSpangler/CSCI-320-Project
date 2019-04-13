import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.*;

/**
 * Handles the entire GUI...
 *
 * @author Tim Johnson and Samuel Adams
 */
public class GUI{

    //
    // CONSTANTS
    //

    private static final String CAR_FILE_PATH = "./data/input/Cars.csv";
    private static final String FONT_COLOR = "#00b6ff";
    public static final Border BORDER = BorderFactory.createLineBorder(new Color(71, 67, 64), 2);
    private static final String SELECT_ALL = "*";

    private static final String WELCOME_PAGE_HEADER =
            getHeader("&nbsp;Welcome to WMB. Are you a dealer, customer, or administrator? ");
    private static final String DEALER_LOGIN_HEADER =
            getHeader("&nbsp;Hello Dealer! Please login with your dealer ID to access the database. ");
    private static final String DEALER_ACTION_HEADER =
            getHeader("&nbsp;Do you want to order a new car, look at your inventory, or check your sale history? ");
    private static final String DEALER_ORDER_HEADER =
            getHeader("&nbsp;What car do you want to order? ");
    private static final String DEALER_LOCATOR_HEADER =
            getHeader("&nbsp;Locator Services ");
    private static final String DEALER_HISTORY_HEADER =
            getHeader("&nbsp;Your History ");
    private static final String CUSTOMER_ORDER_HEADER =
            getHeader("&nbsp;Order a new car! ");
    private static final String CUSTOMER_CHOOSE_HEADER =
            getHeader("&nbsp;Choose the car you want: ");
    private static final String CUSTOMER_RECEIPT_HEADER =
            getHeader("&nbsp;Your receipt: ");
    private static final String VEHICLE_LOCATOR_HEADER =
            getHeader("&nbsp;Vehicle Locator ");


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
    private String dboBrand;
    private String dboModel;
    private String dboDesign;
    private String dboColor;
    private String dboWheel;
    private String dboWheelDiameter;
    private String dboWheelName;
    private String dboWheelStyle;
    private String dboWheelRF;
    private String dboUpholstery;
    private String dboDealerID;
    private ArrayList<String> optUpgradeList;

    ///////////////////////////////////////////////////////
    ////////////// GENERAL PURPOSE FUNCTIONS //////////////
    ///////////////////////////////////////////////////////

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
     * Creates a header string from the given text
     *
     * @param text text to make a header out of
     * @return new header string
     */
    public static String getHeader(String text) {
        return String.format("<html><font color=%s>%s</font></html>", FONT_COLOR, text);
    }

    /**
     * Sets up a new pane by removing the current things in the body and adding a new header
     *
     * @param header string to make a header label out of
     * @return Fully initialized gridbagconstraints with x=0 and y=1
     */
    private GridBagConstraints setupNewPage(String header) {
        // Get ready to add things to the body
        body.removeAll();
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;

        // Add header
        JLabel headerLabel = new JLabel(header);
        headerLabel.setOpaque(true);
        headerLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 30));
        body.add(headerLabel, constraints);
        constraints.gridy++;

        return constraints;
    }

    /**
     * Creates the initial menu pane for distinguishing customers vs. dealers
     * Sets all sorting options to *
     */
    private void welcomePane(){
        // Initialize all sorting options to SELECT_ALL
        dboSeries = SELECT_ALL;
        dboBrand = SELECT_ALL;
        dboModel = SELECT_ALL;
        dboDesign = SELECT_ALL;
        dboColor = SELECT_ALL;
        dboWheel = SELECT_ALL;
        dboWheelDiameter = SELECT_ALL;
        dboWheelName = SELECT_ALL;
        dboWheelStyle = SELECT_ALL;
        dboWheelRF = SELECT_ALL;
        dboUpholstery = SELECT_ALL;
        dboDealerID = SELECT_ALL;
        optUpgradeList = new ArrayList<>();

        // Initialize the page
        GridBagConstraints constraints = setupNewPage(WELCOME_PAGE_HEADER);

        // Create the buttons
        JPanel buttons = new JPanel();
        buttons.setLayout(new GridLayout(3,1));
        buttons.setPreferredSize(new Dimension(750,75));

        JButton dealer = new JButton("Dealer");
        dealer.addActionListener(e -> dealerLoginPane());
        buttons.add(dealer);

        JButton customer = new JButton("Customer");
        customer.addActionListener(e -> customerOrderPane());
        buttons.add(customer);

        JButton admin = new JButton("Administrator");
        admin.addActionListener(e -> setupAdmin());
        buttons.add(admin);

        body.add(buttons, constraints);

        // Refresh the window
        body.revalidate();
        body.repaint();
    }

    /**
     * Shows the dealer's inventory
     */
    private void inventoryPane(String header, String accessor, ActionListener listener){
        // Initialize the page
        GridBagConstraints constraints = setupNewPage(header);
        constraints.gridy = 0;

        // Add all of the selectors
        JPanel inventoryPanel = new JPanel(new GridBagLayout());
        inventoryPanel.setBorder(BORDER);

        JLabel seriesLabel = new JLabel("Series");
        seriesLabel.setOpaque(true);
        inventoryPanel.add(seriesLabel);
        constraints.gridx++;
        inventoryPanel.add(getSeries(accessor), constraints);
        constraints.gridx = 0;
        constraints.gridy++;

        JLabel modelLabel = new JLabel("Model");
        modelLabel.setOpaque(true);
        inventoryPanel.add(modelLabel, constraints);
        constraints.gridx++;
        inventoryPanel.add(getModel(accessor), constraints);
        constraints.gridx = 0;
        constraints.gridy++;

        JLabel colorLabel = new JLabel("Color");
        colorLabel.setOpaque(true);
        inventoryPanel.add(colorLabel, constraints);
        constraints.gridx++;
        inventoryPanel.add(getColor(accessor), constraints);
        constraints.gridx = 0;
        constraints.gridy++;

        JLabel upholsteryLabel = new JLabel("Upholstery");
        upholsteryLabel.setOpaque(true);
        inventoryPanel.add(upholsteryLabel, constraints);
        constraints.gridx++;
        inventoryPanel.add(getUpholstry(accessor), constraints);
        constraints.gridx = 0;
        constraints.gridy++;

        JLabel wheelsLabel = new JLabel("Wheels");
        wheelsLabel.setOpaque(true);
        inventoryPanel.add(wheelsLabel, constraints);
        constraints.gridx++;
        inventoryPanel.add(getWheels(accessor), constraints);
        constraints.gridx = 0;
        constraints.gridy = 1;

        body.add(inventoryPanel, constraints);
        constraints.gridy++;

        //

        JButton nextButton = new JButton("Continue");
        nextButton.addActionListener(listener);
        body.add(nextButton, constraints);

        // Refresh the window
        body.revalidate();
        body.repaint();
    }

    /**
     * Gets the series based on the sort by data
     *
     * @param accessor the person accessing the info
     * @return a JComboBox of series
     */
    private JComboBox<String> getSeries(String accessor){
        // Get output from the database
        JComboBox<String> seriesCBox;

        if(accessor.toLowerCase().equals("alldealers")) {
            String[] fileContents = GetData.readFile(CAR_FILE_PATH);
            // The dealers option
            seriesCBox = new JComboBox<>(GetData.getAttribute(fileContents, dboBrand, dboModel,
                    dboSeries, dboDesign,"Series", 1, true));
        }
        else {
            String[][] carData = AccessDatabase.getUnsoldCars(connection, dboSeries, dboModel, dboColor,
                    dboWheelDiameter, dboWheelName, dboWheelStyle, dboWheelRF, dboUpholstery, dboDealerID);
            // Creates and adds the strings to the combo box
            String[] series = new String[carData.length + 1];
            series[0] = "Series";
            for (int i = 0; i < carData.length; i++) {
                series[i + 1] = carData[i][0];
            }

            seriesCBox = new JComboBox<>(Arrays.stream(series).distinct().toArray(String[]::new));
        }

        //what to initially select the combo box as
        seriesCBox.setSelectedItem(dboSeries.equals(SELECT_ALL) ? "Series" : dboSeries);

        //what to do when one is chosen
        seriesCBox.addActionListener(e -> {
            String series2 = e.toString();
            series2 = series2.substring(series2.indexOf("selectedItemReminder=")+21, series2.length()-1);
            if(series2.equals("Series")){
                series2 = SELECT_ALL;
            }
            System.out.println(series2);
            dboSeries = series2;
            //refreshes the pane
            if(accessor.toLowerCase().equals("dealer")) {
                dealerOrderingPane();
            }
            else if (accessor.toLowerCase().equals("alldealers")) {
                dealerInventoryPane();
            }
            else{
                customerOrderPane();
            }
        });
        seriesCBox.setPreferredSize(new Dimension(500, 25));
        return seriesCBox;
    }

    /**
     * Gets the model based on the sort by data
     *
     * @param accessor the person accessing the info
     * @return a JComboBox of models
     */
    private JComboBox<String> getModel(String accessor){
        //gets output from the database
        JComboBox<String> modelCBox;
        String[] models;
        if(accessor.toLowerCase().equals("alldealers") && !dboSeries.equals(SELECT_ALL)) {
            String[][] modelData = AccessDatabase.getModels(connection, dboSeries);
            models = new String[modelData.length + 1];
            models[0] = "Model";
            for (int i = 0; i < modelData.length; i++) {
                models[i + 1] = modelData[i][0];
            }
        }
        else {
            String[][] carData = AccessDatabase.getUnsoldCars(connection, dboSeries, dboModel, dboColor, dboWheelDiameter, dboWheelName, dboWheelStyle
                    , dboWheelRF, dboUpholstery, dboDealerID);
            models = new String[carData.length + 1];
            models[0] = "Model";
            for (int i = 0; i < carData.length; i++) {
                models[i + 1] = carData[i][1];
            }
        }
        modelCBox = new JComboBox<>(Arrays.stream(models).distinct().toArray(String[]::new));

        //sets the selected item based on the sort-by data
        modelCBox.setSelectedItem(dboModel.equals(SELECT_ALL) ? "Model" : dboModel);

        //if a certain model is chosen
        modelCBox.addActionListener(e -> {
            String model = e.toString();
            model = model.substring(model.indexOf("selectedItemReminder=")+21, model.length()-1);
            if(model.equals("Model")){
                model = SELECT_ALL;
            }
            dboModel = model;

            //refreshes the pane
            refreshInventory(accessor);
        });
        modelCBox.setPreferredSize(new Dimension(500, 25));
        return modelCBox;
    }

    /**
     * Uses the model, series, wheels, and upholstry to sort out the FONT_COLOR combo box
     * @return a JComboBox based on model, series, wheels, and upholstry
     */
    private JComboBox<String> getColor(String accessor){
        //gets output from the database
        JComboBox<String> colorsCBox;
        String[] colors;
        if(accessor.toLowerCase().equals("alldealers") && !dboModel.equals(SELECT_ALL)) {
            String[][] colorData = AccessDatabase.getColors(connection, dboModel);
            colors = new String[colorData.length + 1];
            colors[0] = "Colors";
            for (int i = 0; i < colorData.length; i++) {
                colors[i + 1] = colorData[i][0];
            }
        }
        else {
            String[][] carData = AccessDatabase.getUnsoldCars(connection, dboSeries, dboModel, dboColor,
                    dboWheelDiameter, dboWheelName, dboWheelStyle, dboWheelRF, dboUpholstery, dboDealerID);
            colors = new String[carData.length + 1];
            colors[0] = "Colors";
            for (int i = 0; i < carData.length; i++) {
                colors[i + 1] = carData[i][2];
            }
        }
        colorsCBox = new JComboBox<>(Arrays.stream(colors).distinct().toArray(String[]::new));

        //sets the selected item based on the sort-by data
        if(dboColor.equals(SELECT_ALL)){
            colorsCBox.setSelectedItem("Colors");
        }
        else{
            colorsCBox.setSelectedItem(dboColor);
        }
        //if one of these is chosen
        colorsCBox.addActionListener(e -> {
            String color = e.toString();
            color = color.substring(color.indexOf("selectedItemReminder=")+21,color.length()-1);
            if(color.equals("Colors")){
                color = SELECT_ALL;
            }
            System.out.println(color);
            dboColor = color;
            //refreshes the pane
            refreshInventory(accessor);

        });
        colorsCBox.setPreferredSize(new Dimension(500, 25));
        return colorsCBox;
    }

    /**
     * Uses the model, series, color, and upholstry to sort out the wheels combo box
     *
     * @return a JComboBox based on model, series, FONT_COLOR, and upholstry
     */
    private JComboBox<String> getWheels(String accessor){
        //gets the data to fill in the wheel combo box from the database
        JComboBox<String> wheelsCBox;
        String[] wheels;
        if(accessor.toLowerCase().equals("alldealers") && !dboModel.equals(SELECT_ALL)) {
            String[][] wheelsData = AccessDatabase.getWheels(connection, dboModel);
            wheels = new String[wheelsData.length + 1];
            wheels[0] = "Wheels";
            for (int i = 0; i < wheelsData.length; i++) {
                wheels[i + 1] = wheelsData[i][0] + ", " + wheelsData[i][1] + ", " + wheelsData[i][2] + ", " + wheelsData[i][3];
            }
        }
        else {
            String[][] carData = AccessDatabase.getUnsoldCars(connection, dboSeries, dboModel, dboColor,
                    dboWheelDiameter, dboWheelName, dboWheelStyle, dboWheelRF, dboUpholstery, dboDealerID);
            //Creates the combo box
            wheels = new String[carData.length + 1];
            wheels[0] = "Wheels";
            for (int i = 0; i < carData.length; i++) {
                wheels[i + 1] = carData[i][6] + ", " + carData[i][7] + ", " + carData[i][8] + ", " + carData[i][9];
            }
        }
        wheelsCBox = new JComboBox<>(Arrays.stream(wheels).distinct().toArray(String[]::new));

        //sets the default value for the combobox
        wheelsCBox.setSelectedItem(dboWheelName.equals(SELECT_ALL) ? "Wheels" :
                dboWheelDiameter + ", " + dboWheelName + ", " + dboWheelStyle + ", " + dboWheelRF);
        wheelsCBox.setPreferredSize(new Dimension(500, 25));

        wheelsCBox.addActionListener(e -> {
            // Get the selected string
            String wheel = e.toString();
            wheel = wheel.substring(wheel.indexOf("selectedItemReminder=")+21, wheel.length()-1);
            if(wheel.equals("Wheels")){
                dboWheelDiameter = SELECT_ALL;
                dboWheelName = SELECT_ALL;
                dboWheelStyle = SELECT_ALL;
                dboWheelRF = SELECT_ALL;
            }
            else {
                String[] wheelAttributes = wheel.split(", ");
                dboWheelDiameter = wheelAttributes[0];
                dboWheelName = wheelAttributes[1];
                dboWheelStyle = wheelAttributes[2];
                dboWheelRF = wheelAttributes[3];
            }

            //reloads the pane
            refreshInventory(accessor);
        });

        return wheelsCBox;
    }

    /**
     * Uses the model, series, FONT_COLOR, and wheels to sort out the wheels combo box
     *
     * @param accessor the person accessing the database
     * @return a JComboBox based on model, series, FONT_COLOR, and wheels
     */
    public JComboBox<String> getUpholstry(String accessor){
        //gets output from the database
        JComboBox<String> upholstriesCBox;
        String[] upholsteries;
        if(accessor.toLowerCase().equals("alldealers") && !dboModel.equals(SELECT_ALL)) {
            String[][] upholsteryData = AccessDatabase.getUpholstry(connection, dboModel);
            upholsteries = new String[upholsteryData.length + 1];
            upholsteries[0] = "Upholstery";
            for (int i = 0; i < upholsteryData.length; i++) {
                upholsteries[i + 1] = upholsteryData[i][0];
            }
        }
        else {
            String[][] carData = AccessDatabase.getUnsoldCars(connection, dboSeries, dboModel, dboColor, dboWheelDiameter, dboWheelName,
                    dboWheelStyle, dboWheelRF, dboUpholstery, dboDealerID);
            upholsteries = new String[carData.length + 1];
            upholsteries[0] = "Upholstry";
            for (int i = 0; i < carData.length; i++) {
                upholsteries[i + 1] = carData[i][4];
            }
        }
        upholstriesCBox = new JComboBox<>(Arrays.stream(upholsteries).distinct().toArray(String[]::new));

        //what to set the selected item to based on the sort-by data
        upholstriesCBox.setSelectedItem(dboUpholstery.equals(SELECT_ALL) ? "Upholstry" : dboUpholstery);

        upholstriesCBox.addActionListener(e -> {
            String upholstery = e.toString();
            upholstery = upholstery.substring(upholstery.indexOf("selectedItemReminder=")+21, upholstery.length()-1);
            if(upholstery.equals("Upholstry")){
                upholstery = SELECT_ALL;
            }
            dboUpholstery = upholstery;

            //refreshes the pane
            refreshInventory(accessor);
        });
        upholstriesCBox.setPreferredSize(new Dimension(500, 25));
        return upholstriesCBox;
    }

    /**
     * Refreshes the dealer application based on the accessor
     *
     * @param accessor where is this function accessed from
     */
    private void refreshInventory(String accessor) {
        if(accessor.toLowerCase().equals("dealer")) {
            dealerOrderingPane();
        }
        else if (accessor.toLowerCase().equals("alldealers")) {
            dealerInventoryPane();
        }
        else{
            customerOrderPane();
        }
    }

    /////////////////////////////////////////////////
    ////////////// DEALER APPLICATIONS //////////////
    /////////////////////////////////////////////////

    /**
     * pane to get the dealerID and add it to the sort-by data
     */
    private void dealerLoginPane(){
        // Initialize the page
        GridBagConstraints constraints = setupNewPage(DEALER_LOGIN_HEADER);

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
        body.add(loginForm, constraints);

        // Refresh the window
        body.revalidate();
        body.repaint();
    }

    /**
     * The initial dealer pane displaying possible actions that the dealer can take
     */
    private void dealerMenuPane(){
        // Initialize the page
        GridBagConstraints constraints = setupNewPage(DEALER_ACTION_HEADER);

        // Button for if the dealer wants to order a new car
        JButton order = new JButton("Order From Manufacturer");
        order.addActionListener(e -> dealerOrderingPane());
        order.setPreferredSize(new Dimension(500, 25));
        body.add(order, constraints);
        constraints.gridy++;

        // Button for if the dealer wants to look at their inventory
        JButton inventory = new JButton("Vehicle Locator Services");
        inventory.addActionListener(e -> dealerInventoryPane());
        inventory.setPreferredSize(new Dimension(500, 25));
        body.add(inventory, constraints);
        constraints.gridy++;

        // Button for if the dealer wants to look at the sale history
        JButton history = new JButton("Sales History");
        history.addActionListener(e -> getDealerHistory());
        history.setPreferredSize(new Dimension(500, 25));
        body.add(history, constraints);

        // Refresh the window
        body.revalidate();
        body.repaint();
    }

    /**
     * Creates the dealer gui for ordering
     */
    private void dealerOrderingPane(){
        // Initialize the page
        GridBagConstraints constraints = setupNewPage(DEALER_ORDER_HEADER);

        // Get contents of the file for populating the combo boxes
        String[] fileContents = GetData.readFile(CAR_FILE_PATH);
        if (fileContents == null) {
            welcomePane();
            return;
        }

        // The middle has all of the options
        JPanel carSelection = new JPanel(new GridBagLayout());
        carSelection.setBorder(BORDER);

        // Left pane containing some options
        JPanel requiredOptions = new JPanel(new GridLayout(7,1));

        JComboBox<String> brandOption = new JComboBox<>(GetData.getAttribute(fileContents, dboBrand, dboModel,
                dboSeries, dboDesign, "Brand", 0, true));
        requiredOptions.add(brandOption);

        JComboBox<String> modelOption = new JComboBox<>(GetData.getAttribute(fileContents, dboBrand, dboModel,
                dboSeries, dboDesign,"Model", 2, true));
        requiredOptions.add(modelOption);

        JComboBox<String> seriesOption = new JComboBox<>(GetData.getAttribute(fileContents, dboBrand, dboModel,
                dboSeries, dboDesign,"Series",1, true));
        requiredOptions.add(seriesOption);

        JComboBox<String> designOption = new JComboBox<>(GetData.getAttribute(fileContents, dboBrand, dboModel,
                dboSeries, dboDesign,"Design", 4, true));
        requiredOptions.add(designOption);

        JComboBox<String> colorOption = new JComboBox<>(GetData.getAttribute(fileContents, SELECT_ALL, SELECT_ALL,
                SELECT_ALL, SELECT_ALL, "Color", 9, true));
        requiredOptions.add(colorOption);

        JComboBox<String> wheelOption = new JComboBox<>(GetData.getWheels(fileContents, SELECT_ALL, SELECT_ALL,
                SELECT_ALL, SELECT_ALL));
        requiredOptions.add(wheelOption);

        JComboBox<String> upholsteryOption = new JComboBox<>(GetData.getAttribute(fileContents, SELECT_ALL, SELECT_ALL,
                SELECT_ALL, SELECT_ALL, "Upholstery",8, true));
        requiredOptions.add(upholsteryOption);

        //Sets the selected item

        if(!dboBrand.equals(SELECT_ALL)){
            brandOption.setSelectedItem(dboBrand);
        }
        if(!dboModel.equals(SELECT_ALL)){
            modelOption.setSelectedItem(dboModel);
        }
        if(!dboSeries.equals(SELECT_ALL)){
            seriesOption.setSelectedItem(dboSeries);
        }
        if(!dboDesign.equals(SELECT_ALL)){
            designOption.setSelectedItem(dboDesign);
        }
        if(!dboColor.equals(SELECT_ALL)){
            colorOption.setSelectedItem(dboColor);
        }
        if(!dboWheel.equals(SELECT_ALL)){
            wheelOption.setSelectedItem(dboWheel);
        }
        if(!dboUpholstery.equals(SELECT_ALL)){
            upholsteryOption.setSelectedItem(dboUpholstery);
        }

        //Action listeners for sorting
        brandOption.addActionListener(e->{
            if(!brandOption.getSelectedItem().equals("Brand")){
                dboBrand=(String)(brandOption.getSelectedItem());
            }
            else{
                dboBrand=SELECT_ALL;
            }
            dealerOrderingPane();
        });

        modelOption.addActionListener(e->{
            if(!modelOption.getSelectedItem().equals("Model")){
                dboModel=(String)(modelOption.getSelectedItem());
            }
            else{
                dboModel=SELECT_ALL;
            }
            dealerOrderingPane();
        });

        seriesOption.addActionListener(e->{
            if(!seriesOption.getSelectedItem().equals("Series")){
                dboSeries=(String)(seriesOption.getSelectedItem());
            }
            else{
                dboSeries=SELECT_ALL;
            }
            dealerOrderingPane();
        });

        designOption.addActionListener(e->{
            if(!designOption.getSelectedItem().equals("Design")){
                dboDesign=(String)(designOption.getSelectedItem());
            }
            else{
                dboDesign=SELECT_ALL;
            }
            dealerOrderingPane();
        });

        colorOption.addActionListener(e->{
            if(!colorOption.getSelectedItem().equals("Color")) {
                dboColor = (String) (colorOption.getSelectedItem());
            }
            else{
                dboColor = SELECT_ALL;
            }
        });

        wheelOption.addActionListener(e->{
            if(wheelOption.getSelectedItem().equals("Wheels")){
                dboWheel=(String)(wheelOption.getSelectedItem());
            }
            else{
                dboWheel=SELECT_ALL;
            }
        });

        upholsteryOption.addActionListener(e->{
            if(!upholsteryOption.equals("Upholstery")){
                dboUpholstery = (String)(upholsteryOption.getSelectedItem());
            }
            else{
                dboUpholstery = SELECT_ALL;
            }
        });

        constraints.gridy = 0;
        carSelection.add(requiredOptions, constraints);
        constraints.gridx++;

        // The right has the optional upgrades
        JScrollPane optUpgrades = getDealerUpgrades(fileContents);
        carSelection.add(optUpgrades, constraints);
        constraints.gridx = 0;
        constraints.gridy = 1;

        body.add(carSelection, constraints);
        constraints.gridy++;

        // Continue button
        JButton cont = new JButton("Continue");
        cont.addActionListener(e -> {
            int baseprice = 0;
            //If one of them is not chosen
            if(brandOption.getItemAt(brandOption.getSelectedIndex()).equals("Brand") ||
                    seriesOption.getItemAt(seriesOption.getSelectedIndex()).equals("Series") ||
                    modelOption.getItemAt(modelOption.getSelectedIndex()).equals("Model") ||
                    colorOption.getItemAt(colorOption.getSelectedIndex()).equals("Color") ||
                    wheelOption.getItemAt(wheelOption.getSelectedIndex()).equals("Wheels") ||
                    upholsteryOption.getItemAt(upholsteryOption.getSelectedIndex()).equals("Upholstery") ||
                    designOption.getItemAt(designOption.getSelectedIndex()).equals("Design")
            ){
                dealerOrderingPane();
            }
            //Otherwise add the car to the database
            else {
                AccessDatabase.buyCar(connection, dboDealerID, brandOption.getItemAt(brandOption.getSelectedIndex()),
                        seriesOption.getItemAt(seriesOption.getSelectedIndex()),
                        modelOption.getItemAt(modelOption.getSelectedIndex()),
                        colorOption.getItemAt(colorOption.getSelectedIndex()),
                        wheelOption.getItemAt(wheelOption.getSelectedIndex()),
                        upholsteryOption.getItemAt(upholsteryOption.getSelectedIndex()),
                        designOption.getItemAt(designOption.getSelectedIndex()),
                        baseprice, optUpgradeList);
                welcomePane();
            }
        });
        body.add(cont, constraints);

        // Refresh the window
        body.revalidate();
        body.repaint();
    }

    /**
     * Creates a list of checkboxes for each upgrade that you might want
     *
     * @param filecontents contents of a file
     * @return scrollpane containing the checkboxes
     */
    private JScrollPane getDealerUpgrades(String[] filecontents){
        String[] attributes = GetData.getAttribute(filecontents, SELECT_ALL, SELECT_ALL, SELECT_ALL, SELECT_ALL,
                "Upgrade", 10, true);
        JPanel scroll = new JPanel();
        scroll.setLayout(new GridLayout(attributes.length, 1));

        JCheckBox[] checkBoxes = new JCheckBox[attributes.length];
        for(int i = 0; i < attributes.length; i++){
            checkBoxes[i] = new JCheckBox(attributes[i]);
            checkBoxes[i].addItemListener(e -> {
                optUpgradeList.clear();
                for(JCheckBox cb: checkBoxes){
                    if(cb.isSelected()){
                        optUpgradeList.add(cb.getText());
                    }
                }
            });
            scroll.add(checkBoxes[i]);
        }

        JScrollPane scrollPane = new JScrollPane(scroll,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(750,325));
        return scrollPane;
    }

    /**
     * Returns a list of JButtons for a scrollPane that matches the attributes that the user put in
     *
     * @return a list of JButtons for cars
     */
    private JButton[] getDealerCars(){
        JButton[] carButtons = new JButton[] {new JButton("Car")};
        for(JButton button : carButtons)
            button.addActionListener(e -> dealerReciept(button.getText()));

        return carButtons;
    }

    /**
     * Shows the dealer a reciept of their purchase and returns the dealer to the menu
     */
    private void dealerReciept(String selectedCar){
        body.removeAll();

        body.add(new JLabel("You purchased: " + selectedCar));
        JButton cont = new JButton("Continue");
        cont.addActionListener(e -> welcomePane());
        body.add(cont);

        body.revalidate();
        body.repaint();
    }

    private void dealerInventoryPane() {
        inventoryPane(DEALER_LOCATOR_HEADER, "AllDealers", e -> vehicleLocatorPane());
    }

    private void vehicleLocatorPane() {
        body.removeAll();

        GridBagConstraints constraints = setupNewPage(VEHICLE_LOCATOR_HEADER);
        JPanel scroll = new JPanel(new GridBagLayout());

        // Gets the car information from the database and puts it into the buttons
        String[][] carData = AccessDatabase.getUnsoldCars(connection, dboSeries,  dboModel,dboColor, dboWheelDiameter,
                dboWheelName,dboWheelStyle,dboWheelRF, dboUpholstery,dboDealerID);

        if(carData.length != 0){
            JLabel dealerUnsold = new JLabel("Your dealership has the requested car!");
            scroll.add(dealerUnsold);
            constraints.gridy ++;

            // Build the car buttons
            for (String[] carRow : carData){
                String car = carRow[0]+", "+carRow[1]+", "+carRow[2]+", "+carRow[3]+", "+carRow[4]+", "+
                        carRow[5]+", "+carRow[6]+", "+carRow[7]+", "+carRow[8]+", "+carRow[9];
                JButton carButton = new JButton(car);
                carButton.setPreferredSize(new Dimension(750, 50));
                scroll.add(carButton, constraints);
                constraints.gridy ++;
            }

            // Make the buttons scrollable
            JScrollPane scrollPane = new JScrollPane(scroll,
                    ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
            scrollPane.setPreferredSize(new Dimension(1000,600));

            body.add(scrollPane, constraints);
            constraints.gridy ++;

            JButton homePage = new JButton("Home Page");
            homePage.addActionListener(e -> welcomePane());

            body.add(homePage, constraints);

            // Refresh the window
            body.revalidate();
            body.repaint();
        }
        else {
            carData = AccessDatabase.getUnsoldCarsAllDealer(connection, dboModel, dboColor, dboWheelDiameter,
                    dboWheelName,dboWheelStyle,dboWheelRF, dboUpholstery);

            if (carData.length != 0) {

                JLabel dealerUnsold = new JLabel("Your dealership does not has the requested car! Here are dealerships that do:");
                scroll.add(dealerUnsold);
                constraints.gridy ++;

                // Build the car buttons
                for (String[] carRow : carData) {
                    String car = carRow[0] + ", " + carRow[1] + ", " + carRow[2] + ", " + carRow[3] + ", " + carRow[4];
                    JButton carButton = new JButton(car);
                    carButton.setPreferredSize(new Dimension(750, 50));
                    scroll.add(carButton, constraints);
                    constraints.gridy ++;
                }
            }
            else {
                JLabel dealerUnsold = new JLabel("No dealerships have this car available.");
                scroll.add(dealerUnsold);
            }

            // Make the buttons scrollable
            JScrollPane scrollPane = new JScrollPane(scroll,
                    ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
            scrollPane.setPreferredSize(new Dimension(1000,600));

            body.add(scrollPane, constraints);
            constraints.gridy ++;

            JButton homePage = new JButton("Home Page");
            homePage.addActionListener(e -> welcomePane());

            body.add(homePage, constraints);

            // Refresh the window
            body.revalidate();
            body.repaint();
        }

    }

    /**
     * Gets a JTable with each car that fits the sort-by data
     * @return a JTable with each car that fits the sort-by data
     */
    private JTable getCarsByDealerID(){
        // gets output from the database
        String[][] carData = AccessDatabase.getUnsoldCars(connection, dboSeries,dboModel,dboColor, dboWheelDiameter,
                dboWheelName,dboWheelStyle,dboWheelRF, dboUpholstery,dboDealerID);
        JTable cars = new JTable(carData.length, (carData.length > 0) ? carData[0].length: 0);
        // string and JTable builder
        for(int i = 0; i < carData.length; i++){
            for(int j = 0; j < carData[i].length; j++){
                cars.setValueAt(carData[i][j], i, j);
            }
        }
        // Creates the header for the columns
        String[] header = new String[]{"Series", "Model", "Color", "Design", "Upholstery", "Price",
                "Wheel Diameter", "Wheel Name", "Wheel Style", "Wheel Runflat"};
        for(int i = 0; i < cars.getColumnCount(); i++) {
            cars.getColumnModel().getColumn(i).setMinWidth(200);
            cars.getTableHeader().getColumnModel().getColumn(i).setHeaderValue(header[i]);
        }
        cars.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        return cars;
    }

    /**
     * Shows the dealer's history with regard to sales
     */
    private void getDealerHistory(){
        // Initialize the page
        GridBagConstraints constraints = setupNewPage(DEALER_HISTORY_HEADER);

        // Option checkboxes setup
        JPanel searchChecks = new JPanel();
        JCheckBox[] checkBoxes = new JCheckBox[]{
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
        searchChecks.setLayout(new GridLayout(checkBoxes.length,1));
        for(JCheckBox c:checkBoxes){
            searchChecks.add(c);
        }
        body.add(searchChecks,constraints);
        constraints.gridx++;

        // Results page setup
        JPanel resultsPanel = new JPanel();
        resultsPanel.setOpaque(false);
        body.add(resultsPanel, constraints);
        constraints.gridx = 0;
        constraints.gridy++;

        // Buttons
        JButton show = new JButton("Show data");
        show.addActionListener(e -> {
            boolean[] isSelected = new boolean[checkBoxes.length];
            for(int i = 0; i < checkBoxes.length; i++){
                isSelected[i] = checkBoxes[i].isSelected();
            }

            String[][] carData = AccessDatabase.getCarHistoryByDealerID(connection,dboDealerID, isSelected);
            JTable table = new JTable(carData.length, (carData.length > 0) ? carData[0].length : 0);
            table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            for (int i = 0; i < carData.length; i++){
                for (int j = 0; j < carData[i].length; j++){
                    table.setValueAt(carData[i][j], i, j);
                }
            }
            for(int i = 0; i < table.getColumnCount(); i++){
                table.getColumnModel().getColumn(i).setMinWidth(200);
                table.getTableHeader().getColumnModel().getColumn(i).setHeaderValue(checkBoxes[i].getText());
            }

            JScrollPane scrollPane = new JScrollPane(table,
                    ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

            resultsPanel.removeAll();
            resultsPanel.add(scrollPane);
            body.revalidate();
            body.repaint();
        });
        body.add(show,constraints);
        constraints.gridy++;

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> welcomePane());
        body.add(backButton, constraints);

        // Refresh the window
        body.revalidate();
        body.repaint();
    }

    ///////////////////////////////////////////////////
    ////////////// CUSTOMER APPLICATIONS //////////////
    ///////////////////////////////////////////////////

    /**
     * The first customer pane where the specifics are specified
     */
    private void customerOrderPane() {
        inventoryPane(CUSTOMER_ORDER_HEADER, "Customer", e -> chooseCar());
    }

    /**
     * UI to actually choose the car based on their preferences
     */
    private void chooseCar() {
        // Initialize the page
        GridBagConstraints constraints = setupNewPage(CUSTOMER_CHOOSE_HEADER);
        JPanel scroll = new JPanel(new GridLayout(0, 1));

        // Gets the car information from the database and puts it into the buttons
        String[][] carData = AccessDatabase.getUnsoldCars(connection, dboSeries,  dboModel,dboColor, dboWheelDiameter,
                dboWheelName,dboWheelStyle,dboWheelRF, dboUpholstery,dboDealerID);

        // Build the car buttons
        for (String[] carRow : carData){
            String car = carRow[0]+", "+carRow[1]+", "+carRow[2]+", "+carRow[3]+", "+carRow[4]+", "+
                    carRow[5]+", "+carRow[6]+", "+carRow[7]+", "+carRow[8]+", "+carRow[9];
            JButton carButton = new JButton(car);
            carButton.setPreferredSize(new Dimension(750, 50));
            carButton.addActionListener(e -> customerReceipt(car));
            scroll.add(carButton);
        }

        // Make the buttons scrollable
        JScrollPane scrollPane = new JScrollPane(scroll,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(1000,600));
        scrollPane.setBorder(BORDER);

        body.add(scrollPane, constraints);

        // Refresh the window
        body.revalidate();
        body.repaint();
    }

    /**
     * A reciept of the transaction for the customer
     */
    private void customerReceipt(String selectedCar){
        // Initialize the page
        GridBagConstraints constraints = setupNewPage(CUSTOMER_RECEIPT_HEADER);

        // Actual reciept label
        JLabel reciept = new JLabel(getHeader(selectedCar));
        reciept.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));
        reciept.setOpaque(true);
        body.add(reciept, constraints);
        constraints.gridy++;

        // Fields for customer information
        JPanel fields = new JPanel(new GridLayout(4,2));
        fields.setBorder(BORDER);

        JTextField firstname = new JTextField(30);
        firstname.setText("First Name");
        fields.add(firstname);

        JTextField lastname = new JTextField(30);
        lastname.setText("Last Name");
        fields.add(lastname);

        JTextField gender = new JTextField(30);
        gender.setText("Gender");
        fields.add(gender);

        JTextField annualIncome = new JTextField(30);
        annualIncome.setText("Annual Income");
        fields.add(annualIncome);

        JTextField street = new JTextField(30);
        street.setText("Street");
        fields.add(street);

        JTextField county = new JTextField(30);
        county.setText("County");
        fields.add(county);

        JTextField state = new JTextField(30);
        state.setText("State");
        fields.add(state);

        JTextField zip = new JTextField(30);
        zip.setText("ZIP");
        fields.add(zip);

        body.add(fields,constraints);
        constraints.gridy++;

        // Buy button
        JButton buyButton = new JButton("Buy Car");
        buyButton.addActionListener(e -> {
            AccessDatabase.sellCar(connection, firstname.getText(), lastname.getText(), gender.getText(),
                    annualIncome.getText(), street.getText(), county.getText(), state.getText(), zip.getText(),
                    AccessDatabase.getVIN(connection, selectedCar));
            AccessDatabase.getVIN(connection, selectedCar);
            welcomePane();
        });
        body.add(buyButton, constraints);

        // Refresh the window
        body.revalidate();
        body.repaint();
    }

    ////////////////////////////////////////////////////////
    ////////////// ADMINISTRATOR APPLICATIONS //////////////
    ////////////////////////////////////////////////////////

    private void setupAdmin() {
        body.removeAll();

        adminPage.refresh();
        body.add(adminPage);

        body.revalidate();
        body.repaint();
    }

    /**
     * Main method
     * @param args command line args
     */
    public static void main(String[] args){
        Connection c = AccessDatabase.connect("./database/database","me","password");
        if(c != null) {
            new GUI(c);
        }
    }
}