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

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

/**
 * Class - VersionControl
 * @author Nicholas Stephen (a.k.a. saltisgood)
 *
 */
public final class VersionControl {
	public static final boolean IS_RELEASE = false;
	
	public static boolean isReleaseVersion() {
		return IS_RELEASE;
	}
	
	public static int getAppVersion(Context paramContext)
	{
		try {
			PackageInfo packageInfo = paramContext.getPackageManager().getPackageInfo(paramContext.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			// should never happen
			throw new RuntimeException("Could not get package name: " + e.getMessage());
		}
	}
	
	private VersionControl() {}
}
