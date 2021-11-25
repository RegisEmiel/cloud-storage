package com.geekbrains.messages;

public class SendFileMessage implements Message{
    private String fileName;

    public String getFileName() {
        return fileName;
    }

    public SendFileMessage(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public TypeMessage getType() {
        return TypeMessage.SEND_FILE_TO_CLIENT;
    }
}
