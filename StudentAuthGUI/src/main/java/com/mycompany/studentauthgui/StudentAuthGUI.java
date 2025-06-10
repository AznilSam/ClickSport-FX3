package com.mycompany.studentauthgui;

import com.mycompany.facilitybooking.BookingPage;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class StudentAuthGUI extends Application {
    private TextField regMatricField, regNameField, regEmailField, regPhoneField;
    private PasswordField regPasswordField;
    private ComboBox<String> regFacultyComboBox;

    private TextField loginMatricField;
    private PasswordField loginPasswordField;

    private StudentManager manager = new StudentManager();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Student Login & Registration");

        TabPane tabPane = new TabPane();

        //student register tab
        VBox registerVBox = new VBox(10);
        registerVBox.setPadding(new Insets(10));

        regMatricField = new TextField();
        regMatricField.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("\\d{0,7}")) { //matric no - digits only - max 7 digits
                return change;
            }
            return null;
        }));

        regPasswordField = new PasswordField();
        regNameField = new TextField();
        regEmailField = new TextField();

        regPhoneField = new TextField();
        regPhoneField.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("\\d{0,11}")) { //phone no - digits only - max 11 digits
                return change;
            }
            return null;
        }));

        //kuliyyah ComboBox dropdown with all kulliyyah
        regFacultyComboBox = new ComboBox<>();
        regFacultyComboBox.getItems().addAll(
                "Ahmad Ibrahim Kulliyyah of Laws (AIKOL)",
                "Kulliyyah of Islamic Revealed Knowledge and Human Sciences (KIRHS)",
                "Kulliyyah of Information and Communication Technology (KICT)",
                "Kulliyyah of Engineering (KOE)",
                "Kulliyyah of Architecture and Environmental Design (KAED)",
                "Kulliyyah of Economics and Management Sciences (KENMS)",
                "Kulliyyah of Medicine (KOM)",
                "Kulliyyah of Allied Health Sciences (KAHS)",
                "Kulliyyah of Education (KOED)",
                "Kulliyyah of Languages and Management (KLM)",
                "Kulliyyah of Nursing (KON)",
                "Kulliyyah of Science (KOS)",
                "Kulliyyah of Sustainable Tourism and Contemporary Languages (KSTCL)",
                "Kulliyyah of Dentistry (KOD)",
                "Kulliyyah of Pharmacy (KOP)"
        );
        regFacultyComboBox.setPrefWidth(350);
        regFacultyComboBox.setPromptText("Select Kulliyyah");

        Button registerBtn = new Button("Register");

        registerVBox.getChildren().addAll(
                new Label("Matric No:"), regMatricField,
                new Label("Password:"), regPasswordField,
                new Label("Name:"), regNameField,
                new Label("Email:"), regEmailField,
                new Label("Phone:"), regPhoneField,
                new Label("Kulliyyah Name:"), regFacultyComboBox,
                registerBtn
        );

        Tab registerTab = new Tab("Register", registerVBox);

        //student login tab
        VBox loginVBox = new VBox(10);
        loginVBox.setPadding(new Insets(10));

        loginMatricField = new TextField();
        loginMatricField.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("\\d{0,7}")) { //matric no (login) - digits only - max 7 digits
                return change;
            }
            return null;
        }));

        loginPasswordField = new PasswordField();
        Button loginBtn = new Button("Login");

        loginVBox.getChildren().addAll(
                new Label("Matric No:"), loginMatricField,
                new Label("Password:"), loginPasswordField,
                loginBtn
        );


        Tab loginTab = new Tab("Login", loginVBox);

        tabPane.getTabs().addAll(registerTab, loginTab);

        //register button actions
        registerBtn.setOnAction(e -> {
            String matric = regMatricField.getText().trim();
            String password = regPasswordField.getText().trim();
            String name = regNameField.getText().trim();
            String email = regEmailField.getText().trim();
            String phone = regPhoneField.getText().trim();
            String faculty = regFacultyComboBox.getValue();

            if (matric.isEmpty() || password.isEmpty() || name.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Matric, Password, and Name are required.");
                return;
            }

            if (!matric.matches("\\d{1,7}")) {
                showAlert(Alert.AlertType.ERROR, "Matric No must be numeric.");
                return;
            }

            if (!phone.matches("\\d{1,11}")) {
                showAlert(Alert.AlertType.ERROR, "Phone No must be numeric.");
                return;
            }

            if (faculty == null || faculty.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Please select a kulliyyah.");
                return;
            }

            Student student = new Student(matric, password, name, email, phone, faculty);
            if (manager.registerStudent(student)) {
                showAlert(Alert.AlertType.INFORMATION, "Registration successful!");
                clearRegisterFields();
            } else {
                showAlert(Alert.AlertType.ERROR, "Registration failed.");
            }
        });

        //login button actions
        loginBtn.setOnAction(e -> {
            String matric = loginMatricField.getText().trim();
            String password = loginPasswordField.getText().trim();

            Student student = manager.loginStudent(matric, password);
            if (student != null) {
                showAlert(Alert.AlertType.INFORMATION, "Login successful! Welcome, " + student.getName());
                new BookingPage(student).start(new Stage());
                primaryStage.close();
            } else {
                showAlert(Alert.AlertType.ERROR, "Invalid credentials.");
            }
        });

        Scene scene = new Scene(tabPane, 400, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void clearRegisterFields() {
        regMatricField.clear();
        regPasswordField.clear();
        regNameField.clear();
        regEmailField.clear();
        regPhoneField.clear();
        regFacultyComboBox.getSelectionModel().clearSelection();
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setTitle("Message");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
