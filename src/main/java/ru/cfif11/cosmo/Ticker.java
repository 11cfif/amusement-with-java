package ru.cfif11.cosmo;

/**
 * This class is a time counter. Based on it is read the mouse event and buttons, as well as calculated Physics.
 */
public class Ticker {

	//============ private fields ============

	private long physTimestamp;
	private long timestamp;
	private long start = 0;
	private long finish = 0;
	private long lastTime;
	private boolean notOver;

	//============ private static implementation ============

	private static long getTime() {
		return System.currentTimeMillis();
	}

	//============ public methods ============

	/**
	 * Create a time counter. During the time {@code timestamp} physical system live time {@code physTimestamp}
	 *
	 * <p> This time counter does not stop. It switched by calling {@link #start()}.
	 *
	 * @param timestamp the step for read the mouse and buttons event, as well as for reset buffer
	 * @param physTimestamp the step for physical engine.
	 */
	public Ticker(long timestamp, long physTimestamp) {
		this(timestamp, physTimestamp, 0);
	}

	/**
	 * Create a time counter. During the time {@code timestamp} physical system live time {@code physTimestamp}
	 *
	 * <p> This time counter will stop after a time {@code delta} after switching on.
	 * It switched by calling {@link #start()}
	 *
	 * @param timestamp the step for read the mouse and buttons event, as well as for reset buffer
	 * @param physTimestamp the step for physical engine.
	 * @param delta the period of time after which the counter is switched off after it is switched.
	 */
	public Ticker(long timestamp, long physTimestamp, long delta) {
		this.timestamp = timestamp;
		this.physTimestamp = physTimestamp;
		this.finish = delta;
	}

	/**
	 * Starts this time counter to the time {@code delta}
	 *
	 * @param delta time during which the time counter will work
	 * @return {@code true} if the counter works
	 */
	public boolean start(long delta) {
		this.start = getTime();
		finish = start + delta;
		return isNotOver();
	}

	/**
	 * Starts this time counter. If the operating time counter has not been established,
	 * then it will work until it is set during time for work({@link #setDeltaTime(long)} or {@link #setFinish(long)})
	 * or {@link #stop()} the timer.
	 *
	 * @return {@code true} if the counter works
	 */
	public boolean start() {
		this.start = getTime();
		if(finish != 0)
			this.finish = start + finish;
		setNotOver();
		return isNotOver();
	}

	/**
	 * Stops the timer.
	 */
	public void stop() {
		finish = 1;
		setNotOver();
	}

	/**
	 * The timer counter waiting time which is equal to the timestamp.
	 */
	public void waitTimestamp() {
		if(!isNotOver())
			return;
		long delta = getTime() - lastTime;
		if(delta < timestamp)
			try {
				Thread.sleep(timestamp - delta);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		setNotOver();
	}

	/**
	 * Returns the timestamp.
	 *
	 * @return the timestamp
	 */
	public long getTimestamp() {
		return timestamp;
	}

	/**
	 * Returns the step for physical engine.
	 *
	 * @return the step for physical engine.
	 */
	public float getPhysTimestamp() {
		return physTimestamp * 0.001f;
	}

	/**
	 * Sets the timestamp
	 *
	 * @param timestamp the period of time that the timer counts
	 */
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * Set the time during which the timer will run.
	 *
	 * @param delta the time during which the timer will run
	 * @return {@code true} if the counter works
	 */
	public boolean setDeltaTime(long delta) {
		if(isNotOver()) {
			if (start != 0)
				this.finish = lastTime + delta;
			else
				this.finish = delta;
		}
		return isNotOver();
	}

	/**
	 * Sets the stop counter.
	 *
	 * @param finish stop time
	 * @return {@code true} if the counter works
	 */
	public boolean setFinish(long finish) {
		this.finish = finish;
		return isNotOver();
	}

	/**
	 * Returns {@code true} if the counter starts.
	 *
	 * @return {@code true} if the counter starts
	 */
	public boolean isStarted() {
		return start != 0;
	}

	/**
	 * Returns {@code false} if the counter is stopped.
	 *
	 * @return {@code false} if the counter is stopped
	 */
	public boolean isNotOver() {
		setNotOver();
		return notOver;
	}

	//============ private implementation ============

	private void setNotOver() {
		this.lastTime = getTime();
		notOver = finish == 0 || finish > lastTime;
	}
}
