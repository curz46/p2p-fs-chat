package me.dylancz.chatter;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class OutputFileHandle extends FileHandle {

    private FileOutputStream stream;
    private DataOutputStream out;

    protected OutputFileHandle(final File file) {
        super(file);
    }

    @Override
    public DataOutputStream open() throws FileNotFoundException {
        if (this.isOpen()) return this.out;
        this.stream = new FileOutputStream(super.file);
        return this.out = new DataOutputStream(this.stream);
    }

    @Override
    public void close() throws IOException {
        this.out.close();
        this.out = null;
        this.stream = null;
    }

    @Override
    public boolean isOpen() {
        return this.stream != null && this.out != null && this.stream.getChannel().isOpen();
    }

}
