package net.arville.enumeration;

import net.arville.constant.ResponseConfig;
import net.arville.payload.ErrorSchema;
import net.arville.payload.OutputSchema;
import net.arville.payload.ResponseBodyHandler;

public enum ErrorCode {
    SUCCESS("APP-00-000", "Berhasil", "Success"),
    NO_RESULT_FOUND("APP-99-013", "Data tidak dapat ditemukan", "Data not found"),
    INVALID_DATE_FILTER("APP-99-015", "Format tanggal pencarian tidak sesuai (yyyy-mm-dd)", "Invalid date format (yyyy-mm-dd)"),
    INVALID_SORT_PROPERTY("APP-99-016", "Property pengurutan tidak sesuai", "Invalid sort property"),
    INVALID_PAGINATION_PAGE_NUM("APP-99-017", "Nomer halaman pagination tidak sesuai", "Invalid pagination page number"),
    INVALID_BODY_REQUEST("APP-99-991", "Isi body tidak sesuai", "Invalid body request"),
    INVALID_FIELD_VALUE("APP-99-992", "Isi field tidak sesuai", "Invalid field value"),
    UNKNOWN_ERROR("APP-99-999", "Sistem tidak tersedia", "System Not Available");

    private final String errorCode;
    private final String indonesianMessage;
    private final String englishMessage;

    public String getErrorCode() {
        return errorCode;
    }

    public String getIndonesianMessage() {
        return indonesianMessage;
    }

    public String getEnglishMessage() {
        return englishMessage;
    }

    ErrorCode(String errorCode, String indonesianMessage, String englishMessage) {
        this.errorCode = errorCode;
        this.indonesianMessage = indonesianMessage;
        this.englishMessage = englishMessage;
    }

    public ResponseBodyHandler Response(Object data) {
        ErrorSchema errorSchema = new ErrorSchema(this);
        OutputSchema outputSchema = new OutputSchema(errorSchema.getErrorMessage().get(ResponseConfig.RESPONSE_MSG_LOCALE_DEFAULT), data);
        return new ResponseBodyHandler(outputSchema, errorSchema);
    }

    public ResponseBodyHandler RawDataResponse(Object rawData) {
        ErrorSchema errorSchema = new ErrorSchema(this);
        return new ResponseBodyHandler(rawData, errorSchema);
    }
}
