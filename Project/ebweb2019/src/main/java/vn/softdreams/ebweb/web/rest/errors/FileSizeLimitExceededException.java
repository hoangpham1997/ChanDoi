package vn.softdreams.ebweb.web.rest.errors;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

/**
 * @author dungvm
 */
public class FileSizeLimitExceededException extends AbstractThrowableProblem {
    private static final long serialVersionUID = 1L;

    public FileSizeLimitExceededException() {
        super(ErrorConstants.FILE_SIZE_LIMIT_EXCEEDED_EXCEPTION, "The file is too large", Status.REQUEST_ENTITY_TOO_LARGE);
    }
}
