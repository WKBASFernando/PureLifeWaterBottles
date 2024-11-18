package com.assignment.purelifewaterbottles.controller;

import com.assignment.purelifewaterbottles.dto.DriverAndVehicleDto;
import com.assignment.purelifewaterbottles.dto.DriverDto;
import com.assignment.purelifewaterbottles.dto.VehicleDto;
import com.assignment.purelifewaterbottles.dto.tm.DriverAndVehicleTm;
import com.assignment.purelifewaterbottles.model.DriverModel;
import com.assignment.purelifewaterbottles.model.VehicleModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class DriverPageController implements Initializable {

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnUpdate;

    @FXML
    private ComboBox<String> cmbVehicleType;

    @FXML
    private TableColumn<?, ?> colDriverFee;

    @FXML
    private TableColumn<?, ?> colDriverId;

    @FXML
    private TableColumn<?, ?> colName;

    @FXML
    private TableColumn<?, ?> colPhoneNumber;

    @FXML
    private TableColumn<?, ?> colVehicleId;

    @FXML
    private TableColumn<?, ?> colVehicleNumber;

    @FXML
    private TableColumn<?, ?> colVehicleType;

    @FXML
    private AnchorPane content;

    @FXML
    private Label lblDriverId;

    @FXML
    private Label lblVehicleId;

    @FXML
    private TableView<DriverAndVehicleTm> tblDriver;

    @FXML
    private TextField txtDriverFee;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtPhoneNumber;

    @FXML
    private TextField txtVehicleNumber;

    @FXML
    void EmployeeOnAction(ActionEvent event) {
        navigateTo("/view/EmployeePage.fxml");
    }

    @FXML
    void deleteButtonAction(ActionEvent event) throws Exception {
        String vehicleId = lblVehicleId.getText();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> optionalButtonType = alert.showAndWait();

        if (optionalButtonType.isPresent() && optionalButtonType.get() == ButtonType.YES) {

            boolean isDeleted = vehicleModel.deleteVehicle(vehicleId);

            if (isDeleted) {
                refreshPage();
                new Alert(Alert.AlertType.INFORMATION, "Employee deleted...!").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Fail to delete Employee...!").show();
            }
        }
    }

    @FXML
    void homeOnAction(ActionEvent event) {
        navigateTo("/view/HomePage.fxml");
    }

    @FXML
    void resetOnAction(ActionEvent event) throws Exception {
        refreshPage();
    }

    @FXML
    void saveButtonAction(ActionEvent event) throws Exception {
        String driverId = lblDriverId.getText();
        String vehicleId = lblVehicleId.getText();
        String name = txtName.getText().trim();
        String phoneNumberText = txtPhoneNumber.getText().trim();
        String vehicleType = cmbVehicleType.getValue();
        String vehicleNumber = txtVehicleNumber.getText().trim();
        String vehicleFeeText = txtDriverFee.getText().trim();

        // Validate name
        if (name.isEmpty() || !name.matches("[a-zA-Z ]+")) {
            new Alert(Alert.AlertType.ERROR, "Invalid name. Please enter alphabetic characters only.").show();
            return;
        }

        // Validate phone number
        if (phoneNumberText.isEmpty() || !phoneNumberText.matches("\\d+")) {
            new Alert(Alert.AlertType.ERROR, "Invalid phone number. Please enter a valid numeric phone number.").show();
            return;
        }

        // Validate vehicle fee
        if (vehicleFeeText.isEmpty() || !vehicleFeeText.matches("\\d+(\\.\\d+)?")) {
            new Alert(Alert.AlertType.ERROR, "Invalid vehicle fee. Please enter a valid positive number.").show();
            return;
        }
        double vehicleFee = Double.parseDouble(vehicleFeeText);

        // Create DTOs
        DriverDto driverDto = new DriverDto(driverId, vehicleId, name, phoneNumberText, vehicleFee);
        VehicleDto vehicleDto = new VehicleDto(vehicleId, vehicleType, vehicleNumber);

        // Save and handle response
        boolean isSavedV = vehicleModel.saveVehicle(vehicleDto);
        boolean isSavedD = driverModel.saveDriver(driverDto);

        if (isSavedD && isSavedV) {
            refreshPage();
            new Alert(Alert.AlertType.INFORMATION, "Customer saved...!").show();
        } else {
            new Alert(Alert.AlertType.ERROR, "Failed to save customer...!").show();
        }
    }

    @FXML
    void tblDriverOnAction(MouseEvent event) {
        DriverAndVehicleTm driverAndVehicleTm = tblDriver.getSelectionModel().getSelectedItem();
        if (driverAndVehicleTm != null) {
            lblDriverId.setText(driverAndVehicleTm.getDriverId());
            lblVehicleId.setText(driverAndVehicleTm.getVehicleId());
            txtName.setText(driverAndVehicleTm.getName());
            txtPhoneNumber.setText(String.valueOf(driverAndVehicleTm.getPhoneNo()));
            cmbVehicleType.setValue(driverAndVehicleTm.getType());
            txtVehicleNumber.setText(driverAndVehicleTm.getVehicleNumber());
            txtDriverFee.setText(String.valueOf(driverAndVehicleTm.getDriver_fee()));

            btnSave.setDisable(true);

            btnDelete.setDisable(false);
            btnUpdate.setDisable(false);
        }

    }

    @FXML
    void updateButtonAction(ActionEvent event) throws Exception {
        String driverId = lblDriverId.getText();
        String vehicleId = lblVehicleId.getText();
        String name = txtName.getText().trim();
        String phoneNumberText = txtPhoneNumber.getText().trim();
        String vehicleType = cmbVehicleType.getValue();
        String vehicleNumber = txtVehicleNumber.getText().trim();
        String vehicleFeeText = txtDriverFee.getText().trim();

        // Validate inputs (similar to the save method)
        if (name.isEmpty() || !name.matches("[a-zA-Z ]+")) {
            new Alert(Alert.AlertType.ERROR, "Invalid name. Please enter alphabetic characters only.").show();
            return;
        }
        if (phoneNumberText.isEmpty() || !phoneNumberText.matches("\\d+")) {
            new Alert(Alert.AlertType.ERROR, "Invalid phone number. Please enter a valid numeric phone number.").show();
            return;
        }

        if (vehicleFeeText.isEmpty() || !vehicleFeeText.matches("\\d+(\\.\\d+)?")) {
            new Alert(Alert.AlertType.ERROR, "Invalid vehicle fee. Please enter a valid positive number.").show();
            return;
        }
        double vehicleFee = Double.parseDouble(vehicleFeeText);

        // Create DTOs for update
        DriverDto driverDto = new DriverDto(driverId, vehicleId, name, phoneNumberText, vehicleFee);
        VehicleDto vehicleDto = new VehicleDto(vehicleId, vehicleType, vehicleNumber);

        boolean isUpdatedD = driverModel.updateDriver(driverDto);
        boolean isUpdatedV = vehicleModel.updateVehicle(vehicleDto);

        if (isUpdatedD && isUpdatedV) {
            refreshPage();
            new Alert(Alert.AlertType.INFORMATION, "Driver updated...!").show();
        } else {
            new Alert(Alert.AlertType.ERROR, "Failed to update Driver").show();
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colDriverId.setCellValueFactory(new PropertyValueFactory<>("driverId"));
        colVehicleId.setCellValueFactory(new PropertyValueFactory<>("vehicleId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colPhoneNumber.setCellValueFactory(new PropertyValueFactory<>("phoneNo"));
        colVehicleType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colVehicleNumber.setCellValueFactory(new PropertyValueFactory<>("vehicleNumber"));
        colDriverFee.setCellValueFactory(new PropertyValueFactory<>("driver_fee"));

        ObservableList<String> vehicleTypes = FXCollections.observableArrayList("Lorry", "Mini Lorry", "Van", "Mini Van", "Three Wheel");
        cmbVehicleType.setItems(vehicleTypes);

        try {
            refreshPage();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load Data!").show();
        }
    }

    private void refreshPage() throws Exception {
        loadNextDriverId();
        loadNextVehicleId();
        loadTableData();

        txtName.setText("");
        txtPhoneNumber.setText("");
        cmbVehicleType.setValue(null);
        txtVehicleNumber.setText("");
        txtDriverFee.setText("");

        btnDelete.setDisable(true);
        btnUpdate.setDisable(true);
        btnSave.setDisable(false);
    }

    DriverModel driverModel = new DriverModel();
    VehicleModel vehicleModel = new VehicleModel();

    private void loadNextDriverId() throws Exception {
        String nextEmployeeId = driverModel.getNextDriverId();
        lblDriverId.setText(nextEmployeeId);
    }

    private void loadNextVehicleId() throws Exception {
        String nextEmployeeId = vehicleModel.getNextVehicleId();
        lblVehicleId.setText(nextEmployeeId);
    }

    private void loadTableData() throws Exception {
        ArrayList<DriverAndVehicleDto> driverAndVehicleDtos = vehicleModel.getAllDriversAndVehicles();

        ObservableList<DriverAndVehicleTm> driverAndVehicleTms = FXCollections.observableArrayList();

        for (DriverAndVehicleDto driverAndVehicleDto : driverAndVehicleDtos) {
            DriverAndVehicleTm driverAndVehicleTm = new DriverAndVehicleTm(driverAndVehicleDto.getDriverId(), driverAndVehicleDto.getVehicleId(), driverAndVehicleDto.getName(), driverAndVehicleDto.getPhoneNo(), driverAndVehicleDto.getType(), driverAndVehicleDto.getVehicleNumber(), driverAndVehicleDto.getDriver_fee());
            driverAndVehicleTms.add(driverAndVehicleTm);
        }

        tblDriver.setItems(driverAndVehicleTms);
    }

    public void navigateTo(String fxmlPath) {
        try {
            content.getChildren().clear();
            AnchorPane load = FXMLLoader.load(getClass().getResource(fxmlPath));

            load.prefWidthProperty().bind(content.widthProperty());
            load.prefHeightProperty().bind(content.heightProperty());

            content.getChildren().add(load);
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to load page!").show();
        }
    }
}