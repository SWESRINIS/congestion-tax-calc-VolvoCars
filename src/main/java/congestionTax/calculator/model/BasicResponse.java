package congestionTax.calculator.model;

public interface BasicResponse <T> {
        int getStatus();

        void setStatus(int status);

        T getData();

        void setData(T data);

        String getOperationId();

        void setOperationId(String operationId);
    }

