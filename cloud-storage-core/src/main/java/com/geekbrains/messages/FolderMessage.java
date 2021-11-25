package com.geekbrains.messages;

public class FolderMessage implements Message{
    private String folderName;

    public String getFolderName() {
        return folderName;
    }

    public FolderMessage(String folderName) {
        this.folderName = folderName;
    }

    @Override
    public TypeMessage getType() {
        return TypeMessage.FOLDER;
    }
}
