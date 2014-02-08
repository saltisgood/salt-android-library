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
package com.nickstephen.lib.gui;

/**
 * Class - ListFragment
 * @author Nicholas Stephen (a.k.a. saltisgood)
 *
 */
public abstract class ListFragment extends org.holoeverywhere.app.ListFragment {
	private boolean isFocused = false;
    private boolean isExiting = false;

	public ListFragment() {}

	protected final void setFocused(boolean val) {
		isFocused = val;
	}

	public final boolean isFocused() {
		return isFocused;
	}

    public final boolean isExiting() {
        return isExiting;
    }

	public void popFragment() {
        isExiting = true;
		this.getFragmentManager().popBackStack();

		onFragmentPopped();
	}

	protected void onFragmentPopped() {
		isFocused = false;
	}
}
