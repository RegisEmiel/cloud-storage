package com.geekbrains.messages;

public enum TypeMessage {
    LIST_FILES, // Список файлов
    FOLDER, // Папка
    ACCEPT_COMPLETE_FILE, // Принять файл целиком
    ACCEPT_FILE_PART, // Принять часть большого файла
    NEXT_PART_TO_SERVER, //
    SEND_FILE_TO_CLIENT, // Отправить файл клиенту
    NEXT_PART_TO_CLIENT,
    DELETE_FILE, // Удаление файла

    CREATE_DIRECTORY; // Создать каталог
}
