import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.*;

/**
 * Class to hold all the administrator's actions. Can execute arbitrary SQL statements.
 *
 * @author Tim Johnson
 */
public class AdminPage extends JPanel {

    //
    // CONSTANTS
    //

    private static final String BACK_BUTTON_TEXT = "Back";
    private static final String QUERY_BUTTON_TEXT = "Submit Query";
    private static final String EXECUTE_BUTTON_TEXT = "Execute";

    private static final String RESULT_PAGE_HEADER = GUI.getHeader("Results of your SQL Query:");
    private static final String INPUT_PAGE_HEADER = GUI.getHeader("Enter the SQL statement below:");

    //
    // ATTRIBUTES
    //

    private final Connection con;
    private JPanel inputPage;
    private JPanel resultPage;
    private JTextArea errorField;
    private JScrollPane errorScroll;

    /**
     * Creates a new administrator page component. Can execute arbitrary SQL statements.
     *
     * @param con connection to the SQL server
     * @param backListener callback that should be called when returning from this page
     */
    public AdminPage(Connection con, ActionListener backListener) {
        this.con = con;
        this.setBorder(GUI.BORDER);

        initPages(backListener);
        setCard(resultPage);
    }

    /**
     * Initializes the pages, those being the input page and the results page
     *
     * @param backListener callback that should be called when returning from this page
     */
    private void initPages(ActionListener backListener) {
        // Initialize the admin page. Has everything in it besides results
        inputPage = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = getDefaultConstraints();

        // Instructions, errorField setup, and sql statement entry area
        JLabel instructions = new JLabel(INPUT_PAGE_HEADER);
        instructions.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 30));
        inputPage.add(instructions, constraints);
        constraints.gridy++;

        errorField = new JTextArea(10, 80);
        errorField.setEditable(false);
        errorField.setOpaque(true);
        errorField.setLineWrap(true);
        errorField.setWrapStyleWord(true);
        errorField.setTabSize(4);
        errorScroll = new JScrollPane(errorField,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        constraints.gridy++;

        JTextArea sqlField = new JTextArea(10, 80);
        sqlField.setLineWrap(true);
        sqlField.setWrapStyleWord(true);
        sqlField.setTabSize(4);
        JScrollPane scrollSql = new JScrollPane(sqlField,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        inputPage.add(scrollSql, constraints);
        constraints.gridy++;

        // Buttons for actions
        JPanel buttons = new JPanel(new GridLayout(1, 3, 10, 0));

        JButton queryButton = new JButton(QUERY_BUTTON_TEXT);
        queryButton.addActionListener(e -> {
            makeQuery(sqlField.getText());
            sqlField.setText("");
        });
        buttons.add(queryButton);

        JButton executeButton = new JButton(EXECUTE_BUTTON_TEXT);
        executeButton.addActionListener(e -> {
            execute(sqlField.getText());
            sqlField.setText("");
        });
        buttons.add(executeButton);

        JButton backButton = new JButton(BACK_BUTTON_TEXT);
        backButton.addActionListener(e -> refresh());
        backButton.addActionListener(backListener);
        buttons.add(backButton);

        inputPage.add(buttons, constraints);

        // Result page setup
        resultPage = new JPanel(new GridBagLayout());
        constraints.gridy = 0;
        constraints.gridx = 0;

        JLabel resultHeader = new JLabel(RESULT_PAGE_HEADER);
        resultHeader.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 30));
        resultPage.add(resultHeader, constraints);
        constraints.gridy += 2;

        JButton toAdmin = new JButton(BACK_BUTTON_TEXT);
        toAdmin.addActionListener(e -> refresh());
        resultPage.add(toAdmin, constraints);
        constraints.gridy++;

        resultPage.add(new JLabel(), constraints); // placeholder

    }

    /**
     * Sets the current panel to the given card
     *
     * @param card panel to switch to
     */
    private void setCard(JPanel card) {
        this.removeAll();

        this.add(card);

        this.revalidate();
        this.repaint();
    }

    /**
     * Adds the specified error to the sql input page.
     *
     * @param error string of error message
     */
    private void addError(String error) {
        errorField.setText(error);
        inputPage.remove(errorScroll);

        GridBagConstraints constraints = getDefaultConstraints();
        constraints.gridy = 1;
        inputPage.add(errorScroll, constraints);

        this.revalidate();
        this.repaint();
    }

    /**
     * Function that refreshes the admin page, clearing errors and resetting to the start page
     */
    public void refresh() {
        errorField.setText("");
        inputPage.remove(errorScroll);

        setCard(inputPage);
    }

    /**
     * Executes an arbitrary SQL query and displays the results on the results page.
     *
     * @param query arbitrary SQL query to execute
     */
    private void makeQuery(String query) {
        Statement statement = null;
        try {
            statement = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet results = statement.executeQuery(query);

            // Extract the results into a table
            ResultSetMetaData metaData = results.getMetaData();
            int numCols = metaData.getColumnCount();
            if (!results.last()) {
                return;
            }
            int numRows = results.getRow();
            results.beforeFirst();
            JTable resultsTable = new JTable(numRows, numCols);
            for (int i = 0; i < numCols; i++) {
                resultsTable.getColumnModel().getColumn(i).setMinWidth(200);
                resultsTable.getTableHeader().getColumnModel().getColumn(i).setHeaderValue(metaData.getColumnName(i+1));
            }
            int row = 0;
            while (results.next()) {
                for (int i = 0; i < numCols; i++) {
                    resultsTable.setValueAt(results.getString(i+1), row, i);
                }
                row++;
            }
            resultsTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            resultsTable.setPreferredScrollableViewportSize(new Dimension(1000, 600));

            // Make the results able to fit on the page by adding it to a scroll pane
            JScrollPane resultScroll = new JScrollPane(resultsTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

            // Set current card to the result page after adding results
            GridBagConstraints constraints = getDefaultConstraints();
            constraints.gridy = 1;
            resultPage.remove(2);
            resultPage.add(resultScroll, constraints);
            setCard(resultPage);
        }
        catch (SQLException e) {
            addError(e.getMessage());
        }
        finally {
            if (statement != null) {
                try {
                    statement.close();
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Executes arbitrary sql code found in the input. Adds error to page if there was an error.
     *
     * @param executable SQL statement to execute.
     */
    private void execute(String executable) {
        Statement statement = null;
        try {
            statement = con.createStatement();
            statement.execute(executable);
        }
        catch (SQLException e) {
            addError(e.getMessage());
        }
        finally {
            if (statement != null) {
                try {
                    statement.close();
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static GridBagConstraints getDefaultConstraints() {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = new Insets(5, 5, 5, 5);
        return constraints;
    }
}
