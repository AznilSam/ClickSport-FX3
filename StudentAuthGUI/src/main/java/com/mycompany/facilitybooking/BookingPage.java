package com.mycompany.facilitybooking;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import com.mycompany.studentauthgui.Student;
import com.mycompany.studentauthgui.StudentAuthGUI;

import java.util.*;

public class BookingPage {
    private FacilityManager facilityManager = new FacilityManager();
    private List<Facility> facilities;
    private Set<Facility> selectedFacilities = new HashSet<>();
    private Student student;

    public BookingPage(Student student) {
        this.student = student;
    }

    public void start(Stage stage) {
        stage.setTitle("Facility Booking - Welcome " + student.getName());

        facilityManager.initializeDefaultFacilities();
        facilities = facilityManager.loadFacilities();

        BorderPane root = new BorderPane();

        Label titleLabel = new Label("ClickSport");
        titleLabel.setFont(new Font("Arial", 24));
        titleLabel.setTextFill(Color.BLACK);
        titleLabel.setAlignment(Pos.CENTER);
        titleLabel.setMaxWidth(Double.MAX_VALUE);
        titleLabel.setPadding(new Insets(10));
        titleLabel.setStyle("-fx-background-color: lightgray");
        root.setTop(titleLabel);

        VBox facilityListBox = new VBox(10);
        facilityListBox.setPadding(new Insets(10));

        for (Facility facility : facilities) {
            VBox card = createFacilityCard(facility);
            facilityListBox.getChildren().add(card);
        }

        ScrollPane scrollPane = new ScrollPane(facilityListBox);
        scrollPane.setFitToWidth(true);
        root.setCenter(scrollPane);

        HBox bottomBox = new HBox(10);
        bottomBox.setPadding(new Insets(10));
        bottomBox.setAlignment(Pos.CENTER_RIGHT);

        Button logoutButton = new Button("Logout");
        logoutButton.setFont(new Font("Arial", 16));
        logoutButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
        logoutButton.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Logout Confirmation");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to logout?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                stage.close();
                new StudentAuthGUI().start(new Stage());
            }
        });

        Button bookButton = new Button("Proceed Booking");
        bookButton.setFont(new Font("Arial", 16));
        bookButton.setStyle("-fx-background-color: green; -fx-text-fill: white;");
        bookButton.setOnAction(e -> {
            if (!selectedFacilities.isEmpty()) {
                PaymentPage paymentPage = new PaymentPage(selectedFacilities, this, student);
                paymentPage.start(new Stage());
                stage.close();
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText(null);
                alert.setContentText("Please select at least one facility first.");
                alert.showAndWait();
            }
        });

        bottomBox.getChildren().addAll(logoutButton, bookButton);
        root.setBottom(bottomBox);

        Scene scene = new Scene(root, 700, 600);
        stage.setScene(scene);
        stage.show();
    }

    private VBox createFacilityCard(Facility facility) {
        VBox card = new VBox(5);
        card.setPadding(new Insets(10));
        card.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, new CornerRadii(10), Insets.EMPTY)));
        card.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, new CornerRadii(10), BorderWidths.DEFAULT)));

        ImageView imageView = new ImageView();

        //load image from classpath resource 
        try {
            String imagePath = facility.getImagePath(); //com/mycompany/facilitybooking/images/ - from resources file
            var is = getClass().getClassLoader().getResourceAsStream(imagePath);
            if (is != null) {
                Image image = new Image(is);
                imageView.setImage(image);
            } else {
                System.out.println("Image not found: " + imagePath);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        imageView.setFitWidth(150);
        imageView.setFitHeight(100);

        VBox infoBox = new VBox(3);
        infoBox.getChildren().addAll(
            new Label("Facility No: " + facility.getFacilityNo()),
            new Label("Facility Name: " + facility.getName()),
            new Label("Price/Hour: RM" + facility.getPricePerHour()),
            new Label("Availability: " + (facility.isAvailable() ? "Available" : "Not Available")),
            new Label("Open Time: " + facility.getOpenTime()),
            new Label("Close Time: " + facility.getCloseTime())
        );

        card.getChildren().addAll(imageView, infoBox);
        card.setOnMouseClicked(e -> {
            if (selectedFacilities.contains(facility)) {
                selectedFacilities.remove(facility);
                card.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, new CornerRadii(10), BorderWidths.DEFAULT)));
            } else {
                selectedFacilities.add(facility);
                card.setBorder(new Border(new BorderStroke(Color.BLUE, BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(3))));
            }
        });

        return card;
    }

    public Set<Facility> getSelectedFacilities() {
        return selectedFacilities;
    }
}
