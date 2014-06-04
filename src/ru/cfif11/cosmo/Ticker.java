package ru.cfif11.cosmo;

/**
 * This class is a time counter. Based on it is read the mouse event and buttons, as well as calculated Physics.
 */
public class Ticker {


    private int rate;
    private long s2;

    /**
     * Returns the current time in milliseconds
     * @return the current time
     */
    private static long getTime() {
        return System.currentTimeMillis();
    }

    /**
     * Creates a time counter based on the time interval in milliseconds.
     * @param tickRateMS the time interval in milliseconds
     */
    public Ticker(int tickRateMS) {
        rate = tickRateMS;
        s2 = Ticker.getTime();
    }

    /**
     * Sets the time interval in milliseconds
     * @param rate the time interval in milliseconds
     */
    public void setRate(int rate) {
        this.rate = rate;
    }

    /**
     * cCalculates the time elapsed since the last access
     * @return the time elapsed since the last treatment, if it is longer than the rate or 0
     */
    public int getTicks() {
        long i = Ticker.getTime();
        if (i - s2 > rate) {
            int ticks = (int) ((i - s2) / (long) rate);
            s2 += (long) rate * ticks;
            return ticks;
        }
        return 0;
    }
}
