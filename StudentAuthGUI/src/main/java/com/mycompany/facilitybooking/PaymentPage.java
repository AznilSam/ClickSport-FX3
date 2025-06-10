package com.mycompany.facilitybooking;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import com.mycompany.studentauthgui.Student;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class PaymentPage extends BorderPane {

    private final Set<Facility> selectedFacilities;
    private final BookingPage bookingPage;
    private final Student student;

    private final Map<Facility, FacilityBookingPane> facilityBookingPanes = new HashMap<>();
    private final Label grandTotalLabel = new Label("Grand Total: RM 0.00");

    public PaymentPage(Set<Facility> selectedFacilities, BookingPage bookingPage, Student student) {
        this.selectedFacilities = Objects.requireNonNull(selectedFacilities);
        this.bookingPage = bookingPage;
        this.student = student;

        setPadding(new Insets(10));

        VBox facilitiesBox = new VBox(10);
        facilitiesBox.setPadding(new Insets(10));
        for (Facility f : selectedFacilities) {
            FacilityBookingPane pane = new FacilityBookingPane(f);
            facilityBookingPanes.put(f, pane);
            facilitiesBox.getChildren().add(pane);
        }

        ScrollPane scrollPane = new ScrollPane(facilitiesBox);
        scrollPane.setFitToWidth(true);
        setCenter(scrollPane);

        grandTotalLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");
        updateGrandTotal();

        Button backButton = new Button("Back");
        Button confirmButton = new Button("Confirm Booking");
        confirmButton.setStyle("-fx-background-color: #008000; -fx-text-fill: white;");

        HBox buttons = new HBox(10, backButton, confirmButton);
        buttons.setPadding(new Insets(10));

        VBox bottomBox = new VBox(5, grandTotalLabel, buttons);
        bottomBox.setPadding(new Insets(10));
        setBottom(bottomBox);

        backButton.setOnAction(e -> {
            bookingPage.getSelectedFacilities().clear();
            Stage stage = (Stage) getScene().getWindow();
            stage.close();

            Stage bookingStage = new Stage();
            bookingPage.start(bookingStage);
        });

        confirmButton.setOnAction(e -> {
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirm Booking");
            confirmAlert.setHeaderText(null);
            confirmAlert.setContentText("Are you sure you want to confirm your booking and proceed to payment?");

            Optional<ButtonType> result = confirmAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                Map<Facility, Payment> bookingMap = new HashMap<>();
                for (Map.Entry<Facility, FacilityBookingPane> entry : facilityBookingPanes.entrySet()) {
                    Facility f = entry.getKey();
                    FacilityBookingPane pane = entry.getValue();

                    Payment payment = new Payment(f, pane.getSelectedDate(), pane.getSelectedTime(), pane.getHours());
                    bookingMap.put(f, payment);
                }
                
                //close current payment page
                Stage stage = (Stage) getScene().getWindow();
                stage.close();

                //open receipt page (ReceiptPage extends Stage)
                ReceiptPage receiptPage = new ReceiptPage(student, bookingMap);
                receiptPage.show();
            }
        });
    }

    private void updateGrandTotal() {
        double grandTotal = 0;
        for (FacilityBookingPane pane : facilityBookingPanes.values()) {
            Payment payment = new Payment(pane.getFacility(), pane.getSelectedDate(), pane.getSelectedTime(), pane.getHours());
            grandTotal += payment.getTotalPrice();
        }
        grandTotalLabel.setText(String.format("Grand Total: RM %.2f", grandTotal));
    }

    private void resetAllInputs() {
        for (FacilityBookingPane pane : facilityBookingPanes.values()) {
            pane.reset();
        }
        updateGrandTotal();
    }

    private class FacilityBookingPane extends VBox {
        private final Facility facility;
        private final Label totalLabel = new Label("RM 0.00");

        private final DatePicker datePicker = new DatePicker(LocalDate.now());
        private final ComboBox<String> timePicker = new ComboBox<>();
        private final Spinner<Integer> hoursSpinner = new Spinner<>(1, 12, 1); //minimum, maximum, default

        public FacilityBookingPane(Facility facility) {
            this.facility = facility;
            setPadding(new Insets(10));
            setSpacing(5);
            setStyle("-fx-border-color: gray; -fx-border-radius: 5; -fx-border-width: 1;");

            Label title = new Label(facility.getName());
            title.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(8);

            grid.add(new Label("Facility No:"), 0, 0); //column, row index
            grid.add(new Label(String.valueOf(facility.getFacilityNo())), 1, 0);

            grid.add(new Label("Price per Hour (RM):"), 0, 1);
            grid.add(new Label(String.format("%.2f", facility.getPricePerHour())), 1, 1);

            grid.add(new Label("Booking Date:"), 0, 2);
            grid.add(datePicker, 1, 2);

            grid.add(new Label("Booking Time:"), 0, 3);
            grid.add(timePicker, 1, 3);

            grid.add(new Label("Hours to book:"), 0, 4);
            grid.add(hoursSpinner, 1, 4);

            grid.add(new Label("Total Price:"), 0, 5);
            grid.add(totalLabel, 1, 5);

            getChildren().addAll(title, grid);

            populateTimePicker();
            timePicker.getSelectionModel().selectFirst();

            datePicker.valueProperty().addListener((obs, oldVal, newVal) -> updatePriceDisplay());
            timePicker.valueProperty().addListener((obs, oldVal, newVal) -> updatePriceDisplay());
            hoursSpinner.valueProperty().addListener((obs, oldVal, newVal) -> updatePriceDisplay());

            updatePriceDisplay();
        }

        private void populateTimePicker() {
            timePicker.getItems().clear();
            DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");
            LocalTime openTime = LocalTime.parse(facility.getOpenTime()); //get time from facility.txt
            LocalTime closeTime = LocalTime.parse(facility.getCloseTime());

            for (LocalTime time = openTime; !time.isAfter(closeTime.minusMinutes(30)); time = time.plusMinutes(30)) {
                timePicker.getItems().add(time.format(timeFormat));
            }
        }

        private void updatePriceDisplay() {
            Payment payment = new Payment(facility, getSelectedDate(), getSelectedTime(), getHours());
            totalLabel.setText(String.format("RM %.2f", payment.getTotalPrice()));
            updateGrandTotal();
        }

        public Facility getFacility() {
            return facility;
        }

        public LocalDate getSelectedDate() {
            return datePicker.getValue();
        }

        public LocalTime getSelectedTime() {
            String timeString = timePicker.getValue();
            if (timeString != null) {
                return LocalTime.parse(timeString, DateTimeFormatter.ofPattern("HH:mm"));
            }
            return LocalTime.of(0, 0);
        }

        public int getHours() {
            return hoursSpinner.getValue();
        }

        public void reset() {
            datePicker.setValue(LocalDate.now());
            timePicker.getSelectionModel().selectFirst();
            hoursSpinner.getValueFactory().setValue(1);
            updatePriceDisplay();
        }
    }

    public void start(Stage stage) {
        Scene scene = new Scene(this, 600, 600);
        stage.setScene(scene);
        stage.setTitle("Payment Page");
        stage.show();
    }
}
