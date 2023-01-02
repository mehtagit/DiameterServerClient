package com.nmss.pojo;

import dk.i1.diameter.ECR_RESULT;

public class ECA {

    private ECR_RESULT status;

    public ECR_RESULT getStatus() {
        return status;
    }

    public void setStatus(ECR_RESULT status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ECA{" +
                "status=" + status +
                '}';
    }

}
