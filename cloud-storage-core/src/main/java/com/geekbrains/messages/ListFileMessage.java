package com.geekbrains.messages;

import com.geekbrains.utilities.FileDescriptions;

import java.util.List;

public class ListFileMessage implements Message{
    private List<FileDescriptions> files;
    private boolean isHead;

    public ListFileMessage() {
    }

    public ListFileMessage(List<FileDescriptions> files) {
        this.files = files;
    }
    public ListFileMessage(List<FileDescriptions> files, boolean isHead) {
        this.files = files;
        this.isHead = isHead;
    }
    @Override
    public TypeMessage getType() {
        return TypeMessage.LIST_FILES;
    }
}
