package me.dylancz.chatter;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class OutputFileHandle extends FileHandle {

    private FileOutputStream stream;
    private DataOutputStream out;

    protected OutputFileHandle(final File file) {
        super(file);
    }

    @Override
    public void open() throws FileNotFoundException {
        this.stream = new FileOutputStream(super.file);
        this.out = new DataOutputStream(this.stream);
    }

    @Override
    public boolean isOpen() {
        return this.stream != null && this.out != null && this.stream.getChannel().isOpen();
    }

}
