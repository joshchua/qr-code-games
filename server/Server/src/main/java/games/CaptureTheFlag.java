package games;

import models.ScanResult;

public class CaptureTheFlag extends Game{
    public CaptureTheFlag() {
        super();
        this.mGameName = "Capture the Flag";
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public ScanResult handleScan(String userName, String scanned) {
        return null;
    }
}
