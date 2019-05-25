package name.kuraya.masaki.bbs.ui.model.output;

import java.util.Collections;
import java.util.List;

public class Error {
    
    private String code;
    private String message;
    private List<ErrorDetail> errors;

    public Error() {}

    public Error(String code, String message) {
        this(code, message, Collections.emptyList());
    }

    public Error(String code, String message, List<ErrorDetail> errors) {
        this.code =  code;
        this.message = message;
        this.errors = errors;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ErrorDetail> getErrors() {
        return errors;
    }

    public void setErrors(List<ErrorDetail> errors) {
        this.errors = errors;
    }

}