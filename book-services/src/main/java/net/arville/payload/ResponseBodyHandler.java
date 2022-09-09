package net.arville.payload;

public class ResponseBodyHandler {

    private ErrorSchema errorSchema;
    private Object outputSchema;


    public Object getOutputSchema() {
        return outputSchema;
    }

    public void setOutputSchema(Object outputSchema) {
        this.outputSchema = outputSchema;
    }

    public ErrorSchema getErrorSchema() {
        return errorSchema;
    }

    public void setErrorSchema(ErrorSchema errorSchema) {
        this.errorSchema = errorSchema;
    }

    public ResponseBodyHandler(Object outputSchema, ErrorSchema errorSchema) {
        this.outputSchema = outputSchema;
        this.errorSchema = errorSchema;
    }
}
