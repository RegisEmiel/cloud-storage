package com.geekbrains.filehandlers;

public enum TypeMessage {
    LIST_FILES, // Список файлов
    FOLDER, // Папка
    SENDING_FILE, // Пересылаемый файла
    FILE_PART, // Следующя часть большого файла
    NEXT_PART_UPLOAD, //
    CREATE_DIRECTORY, // Создать каталог
    DELETE_FILE, // Удаление файла
    SEND_FILE; // Отправить файл
}
