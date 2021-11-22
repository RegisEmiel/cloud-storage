package com.geekbrains.filehandlers;

public class SendFileMessage implements Message{
    private String fileName;

    public SendFileMessage(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public TypeMessage getType() {
        return TypeMessage.SEND_FILE;
    }
}
