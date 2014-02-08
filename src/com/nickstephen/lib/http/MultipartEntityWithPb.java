package com.nickstephen.lib.http;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;

public class MultipartEntityWithPb extends MultipartEntity {

	private OutputStreamProgress outputStream;
    private IWriteListener writeListener;
    private final int flags;
    
    public MultipartEntityWithPb(IWriteListener wListener, int f)
    {
        super();
        writeListener = wListener;
        flags = f;
    }
    
    public MultipartEntityWithPb(HttpMultipartMode mode, IWriteListener wListener, int f)
    {
        super(mode);
        writeListener = wListener;
        flags = f;
    }
    
    public MultipartEntityWithPb(HttpMultipartMode mode, String boundary, Charset charset, IWriteListener wListener, int f)
    {
        super(mode, boundary, charset);
        writeListener = wListener;
        flags = f;
    }
    
    @Override
    public void writeTo(OutputStream oStream) throws IOException {
        outputStream = new OutputStreamProgress(oStream, writeListener, flags);
        super.writeTo(outputStream);
    }
}
