/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package drugdispensingtracker;
import javafx.application.Application;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import java.util.Date;
import java.text.SimpleDateFormat; 
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


/**
 *
 * @author ITS
 */
public class DrugDispensingTracker extends Application {
    
  // DRUG INVENTORY AND PRESCRIPTION INVENTORY
     /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author ITS
 */
    
  // DRUG INVENTORY AND PRESCRIPTION INVENTORY
     ObservableList<Drug> drugInventory = FXCollections.observableArrayList();
    ObservableList<Prescription> prescriptions = FXCollections.observableArrayList();
    

    // UI controls that need to be accessed in handlers
    private TableView<Drug> drugTable;
    private TableView<Prescription> prescriptionTable;
    private ComboBox<Drug> drugCombo;
    private Spinner<Integer> qtySpinner;
    private TextField patientField;
    private DatePicker datePicker;
    private Label messageLabel;
    

    public static void main(String[] args) {
        launch(args);
    }

   @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Drug Dispensing & Prescription Tracker)");
//drug inventory
        drugInventory.addAll(
                new Drug("Paracetamol", 50),
                new Drug("Amoxicillin", 30),
                new Drug("Ibuprofen", 40),
                new Drug("Lonart", 10)
                
        );

        // Top: drug table
        drugTable = createDrugTable();
        drugTable.setItems(drugInventory);

        // Right: form to add prescription
        GridPane form = createFormPane();

 // Bottom: prescriptions table
        prescriptionTable = createPrescriptionTable();
        prescriptionTable.setItems(prescriptions);

        
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        HBox topBox = new HBox(10, drugTable, form);
        topBox.setPadding(new Insets(0, 0, 10, 0));

        root.setTop(topBox);
        root.setCenter(prescriptionTable);

        Scene scene = new Scene(root, 900, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private TableView<Drug> createDrugTable() {
        TableView<Drug> table = new TableView<>();
        table.setPrefWidth(450);

        TableColumn<Drug, String> nameCol = new TableColumn<>("Drug");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setPrefWidth(300);

        TableColumn<Drug, Integer> stockCol = new TableColumn<>("Stock");
        stockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        stockCol.setPrefWidth(120);

        table.getColumns().addAll(nameCol, stockCol);
        table.setPlaceholder(new Label("No drugs in inventory."));
        return table;
    }

    private GridPane createFormPane() {
        GridPane grid = new GridPane();
        grid.setHgap(8);
        grid.setVgap(8);
        grid.setPadding(new Insets(0, 0, 0, 10));
        grid.setPrefWidth(420);
        

        Label formTitle = new Label("Add Prescription");
        formTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        patientField = new TextField();
        patientField.setPromptText("Patient Name");

        drugCombo = new ComboBox<>(drugInventory);
        drugCombo.setPromptText("Select drug");
        drugCombo.setPrefWidth(250);

        qtySpinner = new Spinner<>(1, 1000, 1);
        qtySpinner.setEditable(true);
        qtySpinner.setPrefWidth(100);
        
        datePicker = new DatePicker(); // Initialize DatePicker
        datePicker.setValue(LocalDate.now());
       
        Button addBtn = new Button("Add Prescription");
        addBtn.setOnAction(e -> handleAddPrescription());
        messageLabel = new Label();

        // Layout placements - REORDERED: Add button comes after Date Issued
        grid.add(formTitle, 0, 0, 2, 1);
        grid.add(new Label("Patient:"), 0, 1);
        grid.add(patientField, 1, 1);
        grid.add(new Label("Drug:"), 0, 2);
        grid.add(drugCombo, 1, 2);
        grid.add(new Label("Quantity:"), 0, 3);
        grid.add(qtySpinner, 1, 3);
        //date issued 
        grid.add(new Label("Date Issued:"), 0, 4);
        grid.add(datePicker, 1, 4);
        //add button
        grid.add(addBtn, 1, 5);
        grid.add(messageLabel, 0, 6, 2, 1);
        
        

        return grid;
    }

    private TableView<Prescription> createPrescriptionTable() {
        TableView<Prescription> table = new TableView<>();
        table.setPrefHeight(250);
    table.setStyle("-fx-border-color: #bdc3c7; -fx-border-width: 2;");

        TableColumn<Prescription, String> patientCol = new TableColumn<>("Patient");
        patientCol.setCellValueFactory(new PropertyValueFactory<>("PatientName"));
        patientCol.setPrefWidth(300);

        TableColumn<Prescription, String> drugCol = new TableColumn<>("Drug");
        drugCol.setCellValueFactory(new PropertyValueFactory<>("DrugName"));
        drugCol.setPrefWidth(300);

        TableColumn<Prescription, Integer> qtyCol = new TableColumn<>("Qty");
        qtyCol.setCellValueFactory(new PropertyValueFactory<>("Quantity"));
        qtyCol.setPrefWidth(100);
        
        TableColumn<Prescription, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date")); 
        dateCol.setPrefWidth(200);
        
       table.getColumns().addAll(patientCol, drugCol, qtyCol,dateCol);
       table.setPlaceholder(new Label("No prescriptions yet."));
        
        return table;
    }

    private void handleAddPrescription() {
        String patient = patientField.getText().trim();
        Drug selectedDrug = drugCombo.getValue();
        int qty = qtySpinner.getValue();
        LocalDate selectedDate = datePicker.getValue();
        
         
        if (patient.isEmpty()) {
            messageLabel.setText(" Please enter patient name.");
            return;
        }
        if (selectedDrug == null) {
            messageLabel.setText(" Please select a drug.");
            return;
        }
        if (qty <= 0) {
            messageLabel.setText("Quantity must be at least 1.");
            return;
        }

        if (selectedDrug.getStock() < qty) {
            messageLabel.setText("Not enough stock available!");
            return;
        }

        String formattedDate = selectedDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

        // Deduct stock and add prescription
        selectedDrug.setStock(selectedDrug.getStock() - qty);
         
        prescriptions.add(new Prescription(patient, selectedDrug.getName(), qty, formattedDate));

        // clear inputs and show success
        patientField.clear();
        drugCombo.getSelectionModel().clearSelection();
        qtySpinner.getValueFactory().setValue(1);
        datePicker.setValue(LocalDate.now()); 
        messageLabel.setText("Prescription added for " + patient);
        


        // refresh tables (stock is observable so table will update automatically)
        drugTable.refresh();
        prescriptionTable.refresh();
    }

    // === Model classes ===
    public static class Drug {
        private final StringProperty name = new SimpleStringProperty();
        private final IntegerProperty stock = new SimpleIntegerProperty();

        public Drug(String name, int stock) {
            this.name.set(name);
            this.stock.set(stock);
        }

        public String getName() { return name.get(); }
        public void setName(String value) { name.set(value); }
        public StringProperty nameProperty() { return name; }

        public int getStock() { return stock.get(); }
        public void setStock(int value) { stock.set(value); }
        public IntegerProperty stockProperty() { return stock; }

        @Override
        public String toString() { return getName(); }
    }

    public static class Prescription {
        private final StringProperty patientName = new SimpleStringProperty();
        private final StringProperty drugName = new SimpleStringProperty();
        private final IntegerProperty quantity = new SimpleIntegerProperty();
        private final StringProperty date = new SimpleStringProperty(); 

        public Prescription(String patientName, String drugName, int quantity,String date) {
            this.patientName.set(patientName);
            this.drugName.set(drugName);
            this.quantity.set(quantity);
            this.date.set(date);
            
        }

        public String getPatientName() { return patientName.get(); }
        public StringProperty patientNameProperty() { return patientName; }

        public String getDrugName() { return drugName.get(); }
        public StringProperty drugNameProperty() { return drugName; }

        public int getQuantity() { return quantity.get(); }
        public IntegerProperty quantityProperty() { return quantity; }
        
       public String getDate() { return date.get(); } // Added getter for date
        public StringProperty dateProperty() { return date; } 

}
        
        
    }
