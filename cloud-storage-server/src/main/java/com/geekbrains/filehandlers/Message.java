package com.geekbrains.filehandlers;

import java.io.Serializable;

public interface Message extends Serializable {
    public TypeMessage getType();
}
