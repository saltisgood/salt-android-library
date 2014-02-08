package com.nickstephen.lib.http;

import java.io.ByteArrayOutputStream;

public class ByteArrayOutputStreamProgress extends ByteArrayOutputStream {
	private long bytesWritten = 0;
	private final IWriteListener writeListener;
	private final int flags;

	public ByteArrayOutputStreamProgress(IWriteListener wListener, int f) {
		super();
		writeListener = wListener;
		flags = f;
	}

	public ByteArrayOutputStreamProgress(int size, ByteArrayOutputStream oStream, IWriteListener wListener, int f) {
		super(size);
		
		writeListener = wListener;
		flags = f;
	}

	@Override
	public void write(byte[] buff, int offset, int len) {
		super.write(buff, offset, len);
		
		bytesWritten += len;
		writeListener.registerWrite(bytesWritten, flags);
	}
}
