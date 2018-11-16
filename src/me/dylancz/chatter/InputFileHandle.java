package me.dylancz.chatter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;

public class InputFileHandle extends FileHandle {

    private RandomAccessFile file;

    protected InputFileHandle(final File file) {
        super(file);
    }

    @Override
    public void open() throws FileNotFoundException {
        this.file = new RandomAccessFile(super.file, "r");
    }

    @Override
    public boolean isOpen() {
        return this.file != null && this.file.getChannel().isOpen();
    }

}
