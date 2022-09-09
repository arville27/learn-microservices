package net.arville.payload;

import com.fasterxml.jackson.annotation.JsonAlias;
import net.arville.enumeration.ErrorCode;

import java.util.HashMap;

public class ErrorSchema {
    @JsonAlias("error_code")
    private String errorCode;
    @JsonAlias("error_message")
    private HashMap<String, String> errorMessage;

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public HashMap<String, String> getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(HashMap<String, String> errorMessage) {
        this.errorMessage = errorMessage;
    }

    public ErrorSchema(ErrorCode errorCodeEnum) {
        this.errorCode = errorCodeEnum.getErrorCode();
        this.errorMessage = new HashMap<>();
        this.errorMessage.put("indonesian", errorCodeEnum.getIndonesianMessage());
        this.errorMessage.put("english", errorCodeEnum.getEnglishMessage());
    }

    public ErrorSchema() {
    }
}
