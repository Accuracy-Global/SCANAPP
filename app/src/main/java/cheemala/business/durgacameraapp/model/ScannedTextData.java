package cheemala.business.durgacameraapp.model;

public class ScannedTextData {

    private String id;
    private String scannedTxt;
    private String timeStamp;

    public ScannedTextData(String id,String scannedTxt,String timeStamp){
        this.id = id;
        this.scannedTxt = scannedTxt;
        this.timeStamp = timeStamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getScannedTxt() {
        return scannedTxt;
    }

    public void setScannedTxt(String scannedTxt) {
        this.scannedTxt = scannedTxt;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

}
