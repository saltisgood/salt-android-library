/**
 * The Stephen Android Library
 *
 * Author: Nicholas Stephen (a.k.a. saltisgood) - salt_is_good@hotmail.com
 *
 * For general use in all of my Android projects and available for use
 * by anyone else given recognition and leaving this header on all 
 * source files. Feel free to use, distribute and modify these files
 * and let me know if you find them useful. :)
 */
package com.nickstephen.lib;

import android.util.Log;

/**
 * Class - Twig
 * Used in place of the Android Log class so that the app doesn't need to be rewritten when it
 * comes time to remove log output for release.
 * 
 * @author Nicholas Stephen (a.k.a. saltisgood)
 *
 */
public class Twig {
	protected static final int LOG_LEVEL = Log.WARN;
	
	/**
	 * Send an info log message. Will only send a message if the app is in debug mode or the log level
	 * is at or below info level.
	 * @param tag The tag to associate with the message
	 * @param msg The message to print out
	 */
	@SuppressWarnings("unused")
	public static void info(String tag, String msg) {
		// Note that the warning suppression is because eclipse thinks the VersionControl never changes...
		// Which is slightly true
		if (!VersionControl.IS_RELEASE || LOG_LEVEL <= Log.INFO) {
			Log.i(tag, msg);
		}
	}
	
	/**
	 * Send a warning log message. Will only send a message if the app is in debug mode or the log level
	 * is set at or below warning level.
	 * @param tag The tag to associate with the message
	 * @param msg The message to print out
	 */
	@SuppressWarnings("unused")
	public static void warning(String tag, String msg) {
		if (!VersionControl.IS_RELEASE || LOG_LEVEL <= Log.WARN) {
			Log.w(tag, msg);
		}
	}
	
	/**
	 * Send a verbose log message. Will only send a message if the app is in debug mode or the log level
	 * is set at or below verbose level.
	 * @param tag The tag to associate with the message
	 * @param msg The message to print out
	 */
	@SuppressWarnings("unused")
	public static void verbose(String tag, String msg) {
		if (!VersionControl.IS_RELEASE || LOG_LEVEL <= Log.VERBOSE) {
			Log.v(tag, msg);
		}
	}
	
	/**
	 * Send a debug log message. Will only send a message if the app is in debug mode or the log level
	 * is set at or below debug mode.
	 * @param tag The tag to associate with the message
	 * @param msg The message to print out
	 */
	@SuppressWarnings("unused")
	public static void debug(String tag, String msg) {
		if (!VersionControl.IS_RELEASE || LOG_LEVEL <= Log.DEBUG) {
			Log.d(tag, msg);
		}
	}
	
	/**
	 * Send an error log message. Will only send a message if the app is in debug mode or the log level
	 * is set at or below error mode.
	 * @param tag The tag to associate with the message
	 * @param msg The message to print out
	 */
	@SuppressWarnings("unused")
	public static void error(String tag, String msg) {
		if (!VersionControl.IS_RELEASE || LOG_LEVEL <= Log.ERROR) {
			Log.e(tag, msg);
		}
	}

    public static void printStackTrace(Exception e) {
        if (!VersionControl.IS_RELEASE || LOG_LEVEL <= Log.ERROR) {
            e.printStackTrace();
        }
    }
	
	private Twig() {}
}
