package com.geekbrains.messages;

public class CreateDirectoryMessage  implements Message{
    private String dirName;

    public CreateDirectoryMessage(String dirName) {
        this.dirName = dirName;
    }

    public String getDirName() {
        return dirName;
    }

    @Override
    public TypeMessage getType() {
        return TypeMessage.CREATE_DIRECTORY;
    }
}
