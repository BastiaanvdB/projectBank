package io.swagger.model.ResponseDTO;

public class ExceptionResponseDTO {

    private String exception_Reason;

    public ExceptionResponseDTO() {
    }

    public ExceptionResponseDTO(String exception_Reason) {
        this.exception_Reason = exception_Reason;
    }

    public String getException_Reason() {
        return exception_Reason;
    }

    public void setException_Reason(String exception_Reason) {
        this.exception_Reason = exception_Reason;
    }
}
