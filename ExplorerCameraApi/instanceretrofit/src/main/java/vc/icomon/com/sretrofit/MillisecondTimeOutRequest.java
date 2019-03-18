package vc.icomon.com.sretrofit;

public class MillisecondTimeOutRequest {

    private final long connectionTimeoutInMillis, readTimeoutInMillis, writeTimeoutInMillis;

    public MillisecondTimeOutRequest(long connectionTimeoutInMillis, long readTimeoutInMillis, long writeTimeoutInMillis) {
        this.connectionTimeoutInMillis = connectionTimeoutInMillis;
        this.readTimeoutInMillis = readTimeoutInMillis;
        this.writeTimeoutInMillis = writeTimeoutInMillis;
    }

    public long getConnectionTimeoutInMillis() {
        return connectionTimeoutInMillis;
    }

    public long getReadTimeoutInMillis() {
        return readTimeoutInMillis;
    }

    public long getWriteTimeoutInMillis() {
        return writeTimeoutInMillis;
    }
}
