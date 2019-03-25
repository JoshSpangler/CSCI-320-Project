import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

public class gui{
    private JFrame jf;
    //for sorting based on any conjunction of these
    private String dboSeries, dboModel,dboColor, dboWheelDiameter, dboWheelName, dboWheelStyle, dboWheelRF, dboUpholstry, dboDealerID, selectedCar;
    private Connection c;

    /**
     * Constructor for initializing the JFrame and setting the connection
     * @param c the connection
     */
    public gui(Connection c){
        jf=new JFrame();
        this.c=c;
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        menuPane();
    }

    /**
     * Creates the initial menu pane for distinguishing customers vs. dealers
     * Sets all sorting options to *
     */

    public void menuPane(){
        jf.setVisible(true);
        jf.getContentPane().removeAll();
        jf.setLayout(new GridLayout(4, 1));
        JButton dealer=new JButton("Dealer");
        JButton customer=new JButton("Customer");
        //sets sorting options to *
        dboSeries="*";
        dboModel="*";
        dboColor="*";
        dboWheelDiameter="*";
        dboWheelName="*";
        dboWheelStyle="*";
        dboWheelRF="*";
        dboUpholstry="*";
        dboDealerID="*";
        jf.add(new JLabel("Welcome to BWM.\nAre you a dealer or customer? "));
        jf.add(dealer);
        jf.add(customer);
        //if dealer is chosen
        dealer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getDealerName();
            }
        });
        //if customer is chosen
        customer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                customerPaneInit();
            }
        });
        jf.pack();
    }

    /**
     * The initial dealer pane displaying possible actions that the dealer can take
     */
    public void dealerMenuPane(){
        jf.getContentPane().removeAll();
        jf.setLayout(new GridLayout(4,1));
        //if the dealer wants to order a new car
        JButton order=new JButton("Order");
        order.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dealerOrderingPane();
            }
        });
        //if the dealer wants to look at their inventory
        JButton inventory=new JButton("Inventory");
        inventory.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inventoryPane();
            }
        });
        //if the dealer wants to look at the sale history
        JButton history=new JButton("History");
        history.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getDealerHistory(new JPanel());
            }
        });
        jf.add(new JLabel("Do you want to order a new car, look at your inventory, or check your sale history?"));
        jf.add(order);
        jf.add(inventory);
        jf.add(history);
        jf.pack();
        jf.repaint();
    }

    /**
     * Creates the dealer gui for ordering
     */
    public void dealerOrderingPane(){
        jf.getContentPane().removeAll();
        jf.setLayout(new GridLayout(1,3));
        //left pane containing some options
        JPanel dealerLeft=new JPanel();
        //right pane containing the cars
        JPanel dealerRight=new JPanel();
        //the panel to put buttons on in the JScrollPane
        JPanel dealerScroll=new JPanel();
        dboDealerID="*";
        JComboBox<String> Series=getSeries("Dealer");
        //The models option
        JComboBox<String> Model=getModel("Dealer");
        //The color option
        JComboBox<String> ColorChoice=getColor("Dealer");
        //The wheel option
        JComboBox<String> WheelChoice=getWheels("Dealer");
        //The upholstry option
        JComboBox<String> Upholstry=getUpholstry("Dealer");
        //Tells what to sort by
        JComboBox<String> SortBy=new JComboBox<String>(new String[]{"A-Z","Cost"});
        //Creates the buttons for the scrollPane
        JButton[] cars=new JButton[]{new JButton("Car")};//getOrderableCars();
        dealerScroll.setLayout(new GridLayout(cars.length,1));
        //Adds the buttons to the pane
        for(JButton b:cars){
            dealerScroll.add(b);
        }
        //Creates the scrollPane
        JScrollPane dealerCars=new JScrollPane(dealerScroll);
        dealerCars.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        dealerCars.setPreferredSize(new Dimension(100,100));
        //adding the panels
        jf.add(dealerLeft);
        jf.add(dealerRight);
        //set layouts
        dealerLeft.setLayout(new GridLayout(6,1));
        dealerRight.setLayout(new GridLayout(2,1));
        //Add all the choices
        dealerRight.add(dealerCars);
        dealerLeft.add(Series);
        dealerLeft.add(Model);
        dealerLeft.add(ColorChoice);
        dealerLeft.add(WheelChoice);
        dealerLeft.add(Upholstry);
        dealerLeft.add(SortBy);
        jf.pack();
        jf.repaint();
    }

    public JComboBox<String> getSeries(String accessor){
        //gets the output from the database
        String[][] carData=accessDatabase.getUnsoldCars(c,dboSeries,dboModel,dboColor, dboWheelDiameter,dboWheelName,dboWheelStyle
                ,dboWheelRF,dboUpholstry,dboDealerID);
        //creates and adds the strings to the combo box
        String[] series=new String[carData.length+1];
        series[0]="Series";
        for(int i=0; i<carData.length; i++){
            series[i+1]=carData[i][0];
        }
        JComboBox<String> seriesCBox=new JComboBox<String>(series);
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
                String series2=e.toString();
                series2=series2.substring(series2.indexOf("selectedItemReminder=")+21,series2.length()-1);
                if(series2.equals("Series")){
                    series2="*";
                }
                System.out.println(series2);
                dboSeries=series2;
                //refreshes the pane
                if(accessor.toLowerCase().equals("dealer")) {
                    dealerOrderingPane();
                }
                else{
                    customerPaneInit();
                }
            }
        });
        return seriesCBox;
    }
    
    /**
     * Gets the model based on the sort by data
     * @param accessor the person accessing the info
     * @return a JComboBox of models
     */
    public JComboBox<String> getModel(String accessor){
        //gets output from the database
        String[][] carData=accessDatabase.getUnsoldCars(c,dboSeries, dboModel,dboColor, dboWheelDiameter,dboWheelName,dboWheelStyle
                ,dboWheelRF,dboUpholstry,dboDealerID);
        String[] models=new String[carData.length+1];
        models[0]="Model";
        for(int i=0; i<carData.length; i++){
            models[i+1]=carData[i][1];
        }
        JComboBox<String> modelCBox=new JComboBox<String>(models);
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
                String model=e.toString();
                model=model.substring(model.indexOf("selectedItemReminder=")+21,model.length()-1);
                if(model.equals("Model")){
                    model="*";
                }
                System.out.println(model);
                dboModel=model;
                //refreshes the pane
                if(accessor.toLowerCase().equals("dealer")) {
                    dealerOrderingPane();
                }
                else{
                    customerPaneInit();
                }
            }
        });
        return modelCBox;
    }

    /**
     * Uses the model, series, wheels, and upholstry to sort out the color combo box
     * @return a JComboBox based on model, series, wheels, and upholstry
     */

    public JComboBox<String> getColor(String accessor){
        //gets output from the database
        String[][] carData=accessDatabase.getUnsoldCars(c, dboSeries,dboModel,dboColor, dboWheelDiameter,
                dboWheelName,dboWheelStyle,dboWheelRF, dboUpholstry,dboDealerID);
        String[] colors=new String[carData.length+1];
        colors[0]="Colors";
        for(int i=0; i<carData.length; i++){
            colors[i+1]=carData[i][2];
        }
        JComboBox<String> colorsCBox=new JComboBox<String>(colors);
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
                String color=e.toString();
                color=color.substring(color.indexOf("selectedItemReminder=")+21,color.length()-1);
                if(color.equals("Colors")){
                    color="*";
                }
                System.out.println(color);
                dboColor=color;
                //refreshes the pane
                if(accessor.toLowerCase().equals("dealer")) {
                    dealerOrderingPane();
                }
                else{
                    customerPaneInit();
                }
            }
        });
        return colorsCBox;
    }

    /**
     * Uses the model, series, color, and upholstry to sort out the wheels combo box
     * @return a JComboBox based on model, series, color, and upholstry
     */
    public JComboBox<String> getWheels(String accessor){
        //gets the data to fill in the wheel combo box from the database
        String[][] carData=accessDatabase.getUnsoldCars(c,dboSeries,dboModel,dboColor, dboWheelDiameter,
                dboWheelName,dboWheelStyle,dboWheelRF, dboUpholstry, dboDealerID);
        //Creates the combo box
        String[] wheels=new String[carData.length+1];
        wheels[0]="Wheels";
        for(int i=0; i<carData.length; i++){
            wheels[i+1]=carData[i][6]+", "+carData[i][7]+", "+carData[i][8]+", "+carData[i][9];
        }
        JComboBox<String> wheelsCBox=new JComboBox<String>(wheels);
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
                String wheels=e.toString();
                wheels=wheels.substring(wheels.indexOf("selectedItemReminder=")+21,wheels.length()-1);
                if(wheels.equals("Wheels")){
                    wheels="*";
                }
                //Changes the values for the database to be queried on
                String[] wheelAttribs=wheels.split(", ");
                dboWheelDiameter=wheelAttribs[0];
                dboWheelName=wheelAttribs[1];
                dboWheelStyle=wheelAttribs[2];
                dboWheelRF=wheelAttribs[3];
                //reloads the pane
                if(accessor.toLowerCase().equals("dealer")) {
                    dealerOrderingPane();
                }
                else{
                    customerPaneInit();
                }
            }
        });
        return wheelsCBox;
    }

    /**
     * Uses the model, series, color, and wheels to sort out the wheels combo box
     * @param accessor the person accessing the database
     * @return a JComboBox based on model, series, color, and wheels
     */
    public JComboBox<String> getUpholstry(String accessor){
        //gets output from the database
        String[][] carData=accessDatabase.getUnsoldCars(c, dboSeries, dboModel, dboColor, dboWheelDiameter, dboWheelName,
                dboWheelStyle, dboWheelRF, dboUpholstry, dboDealerID);
        String[] upholstries=new String[carData.length+1];
        upholstries[0]="Upholstry";
        for(int i=0; i<carData.length; i++){
            upholstries[i+1]=carData[i][4];
        }
        JComboBox<String> upholstriesCBox=new JComboBox<String>(upholstries);
        //what to set the selected item to based on the sort-by data
        if(dboUpholstry.equals("*")){
            upholstriesCBox.setSelectedItem("Upholstry");
        }
        else{
            upholstriesCBox.setSelectedItem(dboUpholstry);
        }
        upholstriesCBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String upholstry=e.toString();
                upholstry=upholstry.substring(upholstry.indexOf("selectedItemReminder=")+21,upholstry.length()-1);
                if(upholstry.equals("Upholstry")){
                    upholstry="*";
                }
                dboUpholstry=upholstry;
                //refreshes the pane
                if(accessor.toLowerCase().equals("dealer")) {
                    dealerOrderingPane();
                }
                else{
                    customerPaneInit();
                }
            }
        });
        return upholstriesCBox;
    }

    /**
     * Returns a list of JButtons for a scrollPane that matches the attributes that the user put in
     * @return a list of JButtons for cars
     */
    public JButton[] getDealerCars(){
        JButton[] cars=new JButton[]{new JButton("Car")};
        for(JButton b:cars){
            b.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    selectedCar=b.getText();
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
        jf.getContentPane().removeAll();
        jf.setLayout(new GridLayout(2,1));
        jf.add(new JLabel("You purchased: "+selectedCar));
        JButton cont=new JButton("Continue");
        cont.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuPane();
            }
        });
        jf.add(cont);
        jf.pack();
        jf.repaint();
    }

    /**
     * pane to get the dealerID and add it to the sort-by data
     */
    public void getDealerName(){
        jf.getContentPane().removeAll();
        JTextField dealerField=new JTextField();
        JButton cont=new JButton("Continue");
        cont.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dboDealerID=dealerField.getText();
                dealerMenuPane();
            }
        });
        jf.add(new JLabel("Enter your dealer ID"));
        jf.add(dealerField);
        jf.add(cont);
        jf.pack();
        jf.repaint();
    }

    /**
     * Shows the dealer's inventory
     */
    public void inventoryPane(){
        jf.getContentPane().removeAll();
        jf.setLayout(new GridLayout(2,1));
        //creates the scroll pane
        JButton cont=new JButton("Continue");
        JTable table=getCarsByDealerID();
        JScrollPane scrollPane=new JScrollPane(table);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(750,325));
        //when the user tries to continue
        cont.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dboDealerID="*";
                menuPane();
            }
        });
        cont.setPreferredSize(new Dimension(100,50));
        jf.add(scrollPane);
        jf.add(cont);
        jf.pack();
        jf.repaint();
    }

    /**
     * Gets a JTable with each car that fits the sort-by data
     * @return a JTable with each car that fits the sort-by data
     */
    public JTable getCarsByDealerID(){
        //gets output from the database
        String[][] carData=accessDatabase.getUnsoldCars(c, dboSeries,dboModel,dboColor, dboWheelDiameter,
                dboWheelName,dboWheelStyle,dboWheelRF,dboUpholstry,dboDealerID);
        JTable cars=new JTable(carData.length, (carData.length>0)?(carData[0].length):(0));
        //string and JTable builder
        for(int i=0; i<carData.length; i++){
            for(int j=0; j<carData[i].length; j++){
                cars.setValueAt(carData[i][j],i,j);
            }
        }
        //Creates the header for the columns
        String[] header=new String[]{"Series","Model","Color","Design","Upholstery", "Price", "Wheel Diameter","Wheel Name",
                "Wheel Style", "Wheel Runflat"};
        for(int i=0; i<cars.getColumnCount(); i++) {
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
        jf.getContentPane().removeAll();
        jf.setLayout(new GridLayout(2,2));
        JPanel checkBoxes=new JPanel();
        JButton show=new JButton("Show data");
        JButton cont=new JButton("Continue");
        JCheckBox[] cba=new JCheckBox[]{
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
                boolean[] isSelected=new boolean[cba.length];
                for(int i=0; i<cba.length; i++){
                    isSelected[i]=cba[i].isSelected();
                }
                String[][] carData=accessDatabase.getCarHistoryByDealerID(c,dboDealerID, isSelected);
                JTable table=new JTable(carData.length, (carData.length>0)?(carData[0].length):(0));
                JScrollPane scrollPane=new JScrollPane(table,ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                        ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
                for(int i=0; i<carData.length; i++){
                    for(int j=0; j<carData[i].length; j++){
                        table.setValueAt(carData[i][j],i,j);
                    }
                }
                for(int i=0; i<table.getColumnCount(); i++){
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
                menuPane();
            }
        });
        jf.add(checkBoxes);
        jf.add(scrollPanel);
        jf.add(show);
        jf.add(cont);
        jf.pack();
        jf.repaint();
    }

    /**
     * The first customer pane where the specifics are specified
     */
    public void customerPaneInit(){
        jf.getContentPane().removeAll();
        jf.setLayout(new GridLayout(6,2));
        JButton cont=new JButton("Continue");
        //when continue is pressed
        cont.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chooseCar();
            }
        });
        JLabel seriesPane=new JLabel("Series");
        JLabel modelPane=new JLabel("Model");
        JLabel colorPane=new JLabel("Color");
        JLabel upholstryPane=new JLabel("Upholstry");
        JLabel wheelPane=new JLabel("Wheels");
        //gets all JComboBoxes based on the sort-by data
        jf.add(seriesPane);
        jf.add(getSeries("Customer"));
        jf.add(modelPane);
        jf.add(getModel("Customer"));
        jf.add(colorPane);
        jf.add(getColor("Customer"));
        jf.add(upholstryPane);
        jf.add(getWheels("Customer"));
        jf.add(wheelPane);
        jf.add(getUpholstry("Customer"));
        jf.add(cont);
        jf.pack();
        jf.repaint();
    }

    /**
     * UI to actually choose the car based on their preferences
     */
    public void chooseCar(){
        jf.getContentPane().removeAll();
        jf.setLayout(new GridLayout(2,1));
        JPanel scroll=new JPanel();
        //gets the car information from the database and puts it into the scrollpane buttons
        JButton[] buttons=getUnsoldCars();
        scroll.setLayout(new GridLayout(buttons.length, 1));
        for(JButton b:buttons){
            scroll.add(b);
        }
        //creates the scrollpane
        JScrollPane scrollPane=new JScrollPane(scroll,ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(750,325));
        jf.add(new JLabel("Choose the car you want"));
        jf.add(scrollPane);
        jf.pack();
        jf.repaint();
    }

    /**
     * returns a list of JButtons with all of the possible cars matching user input
     * @return all the possible cars based on the user input
     */

    public JButton[] getUnsoldCars(){
        //Gets output from the database
        String[][] carData=accessDatabase.getUnsoldCars(c, dboSeries,  dboModel,dboColor, dboWheelDiameter,
                dboWheelName,dboWheelStyle,dboWheelRF,dboUpholstry,dboDealerID);
        JButton[] buttons=new JButton[carData.length];
        //builds the car buttons
        for(int i=0; i<carData.length; i++){
            String car=carData[i][0]+", "+carData[i][1]+", "+carData[i][2]+", "+carData[i][3]+", "+carData[i][4]+", "+
                    carData[i][5]+", "+carData[i][6]+", "+carData[i][7]+", "+carData[i][8];
            buttons[i]=new JButton(car);
        }
        //what to do when the car is chosen
        for(JButton b:buttons){
            b.setPreferredSize(new Dimension(750, 50));
            b.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    selectedCar=b.getText();
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
        jf.getContentPane().removeAll();
        JButton cont=new JButton("Continue");
        //what to do when continue is chosen
        cont.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuPane();
            }
        });
        jf.setLayout(new GridLayout(2,1));
        jf.add(new JLabel(selectedCar));
        jf.add(cont);
        jf.pack();
        jf.repaint();
    }

    /**
     * Main method
     * @param args command line args
     */
    public static void main(String[] args){
        Connection c=accessDatabase.connect("./database/database","me","password");
        if(c!=null) {
            gui g = new gui(c);
        }
    }
}
