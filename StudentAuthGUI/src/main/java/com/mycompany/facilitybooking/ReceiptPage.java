package com.mycompany.facilitybooking;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import com.mycompany.studentauthgui.Student;

import java.time.format.DateTimeFormatter;
import java.util.Map;

public class ReceiptPage extends Stage {

    public ReceiptPage(Student student, Map<Facility, Payment> bookingMap) {
        setTitle("Receipt");

        VBox mainVBox = new VBox(15);
        mainVBox.setPadding(new Insets(15));
        mainVBox.setStyle("-fx-background-color: white;");

        //student info section - pull from student.txt - from student registration
        VBox studentInfoBox = new VBox(5);
        studentInfoBox.setPadding(new Insets(10));
        studentInfoBox.setStyle("-fx-border-color: black; -fx-border-width: 1;");

        Label studentTitle = new Label("Student Info");
        studentTitle.setFont(Font.font("Arial", 18));
        studentInfoBox.getChildren().addAll(
                studentTitle,
                new Label("Matric No: " + student.getMatric()),
                new Label("Name: " + student.getName()),
                new Label("Faculty: " + student.getFaculty())
        );

        mainVBox.getChildren().add(studentInfoBox);
        
        //separator
        mainVBox.getChildren().add(new Separator());

        //prepare DateTimeFormatters for LocalDate and LocalTime
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        //facilities section receipts
        for (Map.Entry<Facility, Payment> entry : bookingMap.entrySet()) {
            Facility facility = entry.getKey();
            Payment payment = entry.getValue();

            VBox facilityBox = new VBox(5);
            facilityBox.setPadding(new Insets(10));
            facilityBox.setStyle("-fx-background-color: #D3D3D3; -fx-border-color: black; -fx-border-width: 1;");
            facilityBox.setMaxWidth(Double.MAX_VALUE);

            Label facilityTitle = new Label(facility.getName());
            facilityTitle.setFont(Font.font("Arial", 16));

            facilityBox.getChildren().addAll(
                    facilityTitle,
                    new Label("Facility No: " + facility.getFacilityNo()),
                    new Label("Date: " + payment.getDate().format(dateFormatter)), //format LocalDate and LocalTime properly
                    new Label("Time: " + payment.getTime().format(timeFormatter)),
                    new Label("Hours: " + payment.getHoursBooked()),
                    new Label(String.format("Total: RM %.2f", payment.getTotalPrice()))
            );

            //add QR code image (dummy)
            try {
                Image qrImage = new Image("com/mycompany/facilitybooking/images/qrcode.png", 100, 100, true, true);
                ImageView qrView = new ImageView(qrImage);
                facilityBox.getChildren().add(qrView);
            } catch (Exception e) {
                facilityBox.getChildren().add(new Label("QR Code image not found."));
            }

            mainVBox.getChildren().add(facilityBox);
        }

        ScrollPane scrollPane = new ScrollPane(mainVBox);
        scrollPane.setFitToWidth(true);

        //buttons at bottom
        Button printButton = new Button("Print Receipt");
        printButton.setStyle("-fx-background-color: #009900; -fx-text-fill: white;");
        printButton.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Receipt will be downloaded as PDF.");
            alert.showAndWait();
        });

        Button continueButton = new Button("Continue Booking");
        continueButton.setOnAction(e -> {
            this.close();
            Stage bookingStage = new Stage();
            BookingPage bookingPage = new BookingPage(student);
            bookingPage.start(bookingStage);
        });

        HBox buttonsBox = new HBox(20, printButton, continueButton);
        buttonsBox.setAlignment(Pos.CENTER);
        buttonsBox.setPadding(new Insets(10));

        VBox root = new VBox(scrollPane, buttonsBox);
        Scene scene = new Scene(root, 520, 700);
        setScene(scene);
        setResizable(true);
    }
}
