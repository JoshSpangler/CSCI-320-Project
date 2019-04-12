import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.*;

public class AdminPage extends JPanel {

    private static final String BACK_BUTTON_TEXT = "Back";
    private static final String QUERY_BUTTON_TEXT = "Submit Query";
    private static final String EXECUTE_BUTTON_TEXT = "Execute";

    private final Connection con;
    private JPanel adminPage;
    private JPanel resultPage;
    private JTextArea errorField;
    private JScrollPane errorScroll;

    public AdminPage(Connection con, ActionListener backListener) {
        this.con = con;

        this.setOpaque(false);

        initPages(backListener);
        this.add(adminPage);
        this.revalidate();
        this.repaint();
    }

    private void initPages(ActionListener backListener) {
        // Initialize the admin page. Has everything in it besides results
        adminPage = new JPanel(new GridLayout(0, 1, 0, 5));

        JLabel instructions = new JLabel("<html><font color="+GUI.color+">"+ "Enter the SQL statement below:</font></html>");
        instructions.setOpaque(true);
        instructions.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 30));
        adminPage.add(instructions);

        errorField = new JTextArea(10, 80);
        errorField.setEditable(false);
        errorField.setOpaque(true);
        errorField.setLineWrap(true);
        errorField.setWrapStyleWord(true);
        errorField.setTabSize(4);
        errorScroll = new JScrollPane(errorField,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        //adminPage.add(errorScroll);

        JTextArea sqlField = new JTextArea(10, 80);
        sqlField.setLineWrap(true);
        sqlField.setWrapStyleWord(true);
        sqlField.setTabSize(4);
        JScrollPane scrollSql = new JScrollPane(sqlField,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        adminPage.add(scrollSql);

        JPanel buttons = new JPanel(new GridLayout(3, 1, 0, 5));

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
        backButton.addActionListener(backListener);
        buttons.add(backButton);
        adminPage.add(buttons);

        // Result page setup
        resultPage = new JPanel(new GridLayout(2, 1));
        resultPage.add(new JLabel());

        JButton toAdmin = new JButton(BACK_BUTTON_TEXT);
        toAdmin.addActionListener(e -> refresh());
        resultPage.add(toAdmin);

    }

    public void refresh() {
        this.removeAll();

        errorField.setText("");
        adminPage.remove(errorScroll);

        this.add(adminPage, 0);
        this.revalidate();
        this.repaint();
    }

    private void addError(String error) {
        errorField.setText(error);
        adminPage.remove(errorScroll);
        adminPage.add(errorScroll,1);
        this.revalidate();
        this.repaint();
    }

    private void makeQuery(String query) {
        Statement statement = null;
        try {
            statement = con.createStatement();
            ResultSet results = statement.executeQuery(query);

            ResultSetMetaData metaData = results.getMetaData();
            int numCols = metaData.getColumnCount();

            JPanel resultPanel = new JPanel(new GridLayout(0, numCols));
            for (int i = 0; i < numCols; i++) {
                resultPanel.add(new JLabel(metaData.getColumnName(i+1)));
            }
            while (results.next()) {
                for (int i = 0; i < numCols; i++) {
                    resultPanel.add(new JLabel(results.getString(i+1)));
                }
            }

            JScrollPane resultScroll = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            //resultScroll.setMaximumSize(new Dimension(10000, 400));
            resultScroll.add(resultPanel);

            resultPage.remove(0);
            resultPage.add(resultScroll, 0);

            this.removeAll();
            this.add(resultPage);
            this.revalidate();
            this.repaint();
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
}
