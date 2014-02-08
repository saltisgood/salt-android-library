package com.nickstephen.lib.http;

import java.io.IOException;
import java.io.OutputStream;


public class OutputStreamProgress extends OutputStream {
	private final OutputStream outputStream;
	private long bytesWritten = 0;
	private final IWriteListener writeListener;
	private final int flags;
	
	public OutputStreamProgress(OutputStream outStream, IWriteListener wListener, int f) {
		outputStream = outStream;
		writeListener = wListener;
		flags = f;
	}

	@Override
	public void write(int arg0) throws IOException {
		outputStream.write(arg0);
		bytesWritten++;
		writeListener.registerWrite(bytesWritten, flags);
	}
	
	@Override
    public void write(byte[] b) throws IOException {
        outputStream.write(b);
        bytesWritten += b.length;
        writeListener.registerWrite(bytesWritten, flags);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        outputStream.write(b, off, len);
        bytesWritten += len;
        writeListener.registerWrite(bytesWritten, flags);
    }

    @Override
    public void flush() throws IOException {
        outputStream.flush();
    }

    @Override
    public void close() throws IOException {
        outputStream.close();
    }
}
