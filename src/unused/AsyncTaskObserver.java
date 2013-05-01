/**
 * 
 */
package unused;

import com.khmelenko.lab.currency.R;

/**
 * Observer interface
 * 
 * @author Dmytro Khmelenko
 * 
 */
public interface AsyncTaskObserver {

	/**
	 * Called when loading is completed
	 * 
	 * @param aLoadedData
	 *            Loaded data
	 */
	public void loadingCompleted(String aLoadedData);

	/**
	 * Called when loading failed
	 * 
	 * @param aError
	 *            Loading error
	 */
	public void loadingFailed(int aError);
}
