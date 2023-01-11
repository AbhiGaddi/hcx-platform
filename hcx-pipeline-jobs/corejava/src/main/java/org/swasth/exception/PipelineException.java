package org.swasth.exception;

public class PipelineException extends Exception {

    public PipelineException(String code , String message,String trace){
        super(message);
    }

}
