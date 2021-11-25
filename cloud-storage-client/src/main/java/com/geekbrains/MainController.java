package com.geekbrains;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

@Slf4j
public class MainController implements Initializable {
    private static final String CLIENT_DIRECTORY = "cloud-storage-november-client\\client";
    private Path clientDir;
    public ListView<String> clientView;
    public ListView<String> serverView;
    public TextField input;
    private DataInputStream is;
    private DataOutputStream os;
    private NetChanel netChanel;

    private String selectedFileName = "";

    public String getSelectedFileName() {
        return selectedFileName;
    }

    public void setSelectedFileName(String selectedFileName) {
        this.selectedFileName = selectedFileName;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            // Создать клиентскую директорию, если отсутствует
            clientDir = Paths.get("cloud-storage-client", "client");
            if (!Files.exists(clientDir)) {
                Files.createDirectory(clientDir);
            }

            // Сформировать список файлов в клиентской директории
            clientView.getItems().clear();
            clientView.getItems().addAll(getFiles(clientDir));

            // Добавить обработчик выделения файла
            clientView.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1) {
                    String item = clientView.getSelectionModel().getSelectedItem();
                    input.setText(item);
                    setSelectedFileName(item);
                }
            });

            netChanel = new NetChanel();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<String> getFiles(Path path) throws IOException {
        return Files.list(path).map(p -> p.getFileName().toString())
                .collect(Collectors.toList());
    }


    public void sendMessage(ActionEvent actionEvent) throws IOException {
        sendFile(getSelectedFileName());
    }

    private void sendFile(String fileName) throws IOException {

    }

    public void sendFileAction(ActionEvent actionEvent) throws IOException {
        sendFile(getSelectedFileName());
    }

    public Path getClientDir() {
        return clientDir;
    }

    public NetChanel getNetwork() {
        return netChanel;
    }
}