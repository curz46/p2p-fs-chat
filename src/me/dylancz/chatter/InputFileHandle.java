package me.dylancz.chatter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class InputFileHandle extends FileHandle<RandomAccessFile> {

    private RandomAccessFile randomAccessFile;

    protected InputFileHandle(final File file) {
        super(file);
    }

    @Override
    public RandomAccessFile open() throws FileNotFoundException {
        if (this.isOpen()) return this.randomAccessFile;
        return this.randomAccessFile = new RandomAccessFile(super.file, "r");
    }

    @Override
    public void close() throws IOException {
        this.randomAccessFile.close();
        this.randomAccessFile = null;
    }

    @Override
    public boolean isOpen() {
        return this.file != null && this.randomAccessFile.getChannel().isOpen();
    }

}
