package name.kuraya.masaki.bbs.ui.service;

import name.kuraya.masaki.bbs.ui.model.output.Error;

public class ServiceException extends RuntimeException {

    private static final long serialVersionUID = -8211988899182315843L;

    private Error error;

    ServiceException(Error error) {
        super(error.getMessage());
        this.error = error;
    }

    public Error getError() {
        return error;
    }

}