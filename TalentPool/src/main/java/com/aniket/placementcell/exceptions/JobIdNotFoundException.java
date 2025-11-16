package com.aniket.placementcell.exceptions;

public class JobIdNotFoundException extends  RuntimeException{
    public JobIdNotFoundException(String message)
    {
        super(message);
    }
}
