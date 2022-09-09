package net.arville.payload;


public class OutputSchema {
    private String message;
    private Object data;
    
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public OutputSchema(String message, Object data) {
        this.message = message;
        this.data = data;
    }


}
