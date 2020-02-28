package sample;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.UUID;

public class Controller  implements Initializable
{
    @FXML
    private ListView<Worker> employeeListView;
    @FXML
    private TextField firstNameTextField;
    @FXML
    private CheckBox isActiveCheckBox;
    @FXML
    JFXButton createdbbutton;
    @FXML
    JFXButton deletetablebutton;
    @FXML
    JFXButton loaddatabutton;
    @FXML
    JFXListView materialListView;
    @FXML
    ListView normalListView;
    @FXML
    Button createawsbutton;
    @FXML
    Button loadawsbutton;
    @FXML
    Button deleteawsbutton;



    final String hostname = "student-db.c5cfwou0gygn.us-east-1.rds.amazonaws.com";
    final String dbName = "student_db";
    final String port = "3306";
    final String userName = "mlbouben";
    final String password = "Mlb1399!";
    final String AWS_URL = "jdbc:mysql://" + hostname + ":" + port + "/" + dbName + "?user=" + userName + "&password=" + password;
    final String DB_URL = "jdbc:derby:EmployeeDB;create=true";


    private void createDatabase(String url)
    {
        try{

            Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            try
            {
                stmt.execute("CREATE TABLE Employee (" +
                        "FirstName CHAR(25), " +
                        "LastName CHAR(25), " +
                        "Id VARCHAR(36), " +
                        "IsActive BOOLEAN )");

                System.out.println("TABLE CREATED");
            }
            catch (Exception ex)
            {
                System.out.println("TABLE ALREADY EXISTS, NOT CREATED");
            }

            UUID id = UUID.randomUUID();
            String idString = id.toString();
            String firstName = url.equals(DB_URL) ? "Bruce" : "Bat";
            String lastName = url.equals(DB_URL) ? "Wayne" : "Man";

            String sql = "INSERT INTO Employee VALUES" +
                    "('" + firstName + "', '" + lastName + "', '" + idString+"', TRUE)";

            stmt.executeUpdate(sql);

            id = UUID.randomUUID();
            idString = id.toString();
            String firstName2 = url.equals(DB_URL) ? "Clark" : "Super";
            String lastName2 = url.equals(DB_URL) ? "Kent" : "Man";

            sql = "INSERT INTO Employee VALUES" +
                    "('" + firstName2 + "', '" + lastName2 + "', '" + idString+"', TRUE)";
            stmt.executeUpdate(sql);
            System.out.println("TABLE FILLED");

            stmt.close();
            conn.close();

        }
        catch (Exception ex)
        {
            String msg = ex.getMessage();
            System.out.println(msg);
        }
    }

    private void deleteTable(String url)
    {
        try{
            Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            stmt.execute("DROP TABLE Employee");
            stmt.close();
            conn.close();
            System.out.println("TABLE DROPPED");
        }
        catch (Exception ex)
        {
            String msg = ex.getMessage();
            System.out.println("TABLE NOT DROPPED");
            System.out.println(msg);
        }
    }

    private void loadData(String url)
    {
        try{
            Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            String sqlStatement = "SELECT FirstName, LastName, Id, IsActive FROM Employee";
            ResultSet result = stmt.executeQuery(sqlStatement);
            ObservableList<Employee> dbEmployeeList = FXCollections.observableArrayList();
            while (result.next())
            {
                Employee employee = new Employee();
                employee.id = UUID.fromString(result.getString("Id"));
                employee.firstName = result.getString("FirstName");
                employee.lastName = result.getString("LastName");
                employee.isActive = result.getBoolean("IsActive");
                dbEmployeeList.add(employee);
            }
            if(url.equals(DB_URL))
                materialListView.setItems(dbEmployeeList);
            else
                normalListView.setItems(dbEmployeeList);

            System.out.println("DATA LOADED");
            stmt.close();
            conn.close();
        }
        catch (Exception ex)
        {
            String msg = ex.getMessage();
            System.out.println("DATA NOT LOADED");
            System.out.println(msg);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        createawsbutton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event)
            {
                createDatabase(AWS_URL);
            }
        });
        createdbbutton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent)
            {
                createDatabase(DB_URL);
            }
        });
        deleteawsbutton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event)
            {
                deleteTable(AWS_URL);
            }
        });
        deletetablebutton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent)
            {
                deleteTable(DB_URL);
            }
        });

        loadawsbutton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event)
            {
                loadData(AWS_URL);
            }
        });
        loaddatabutton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent)
            {
                loadData(DB_URL);
            }
        });

        employeeListView.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Worker> ov, Worker old_val, Worker new_val) -> {
            Worker selectedItem = employeeListView.getSelectionModel().getSelectedItem();
            firstNameTextField.setText(((Employee)selectedItem).firstName);
            isActiveCheckBox.setSelected(((Employee) selectedItem).isActive);

        });


        ObservableList<Worker> items = employeeListView.getItems();
        Employee employee1 = new Employee();
        employee1.firstName = "Alyssa";
        employee1.lastName = "Anderson";
        Employee employee2 = new Employee();
        employee2.firstName = "Robert";
        employee2.lastName = "Smith";

        items.add(employee1);
        items.add(employee2);

        for(int i = 0; i < 10; i++)
        {
            Employee employee = new Employee();
            employee.firstName = "EMPLOYEE" + i;
            employee.lastName = "Incognito";
            employee.isActive = true;
            employee.id = UUID.randomUUID();
            items.add(employee);
        }

        Staff staff1 = new Staff();
        staff1.firstName = "Some Staff";
        staff1.lastName = "Lee";
        items.add(staff1);

        Faculty faculty1 = new Faculty();
        faculty1.firstName = "Some Faculty";
        faculty1.lastName = "Diaz";
        items.add(faculty1);



    }
}
