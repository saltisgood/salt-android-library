package com.nickstephen.lib.http;

public interface IWriteListener {
	void setLength(long totalBytes);
	
	void registerWrite(long amountOfBytesWritten, int flags);
}
