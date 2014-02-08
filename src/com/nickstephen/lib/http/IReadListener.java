package com.nickstephen.lib.http;

public interface IReadListener {
	void setReadLength(long bytes);

	void registerRead(long amountOfBytesWritten, int flags);
}
