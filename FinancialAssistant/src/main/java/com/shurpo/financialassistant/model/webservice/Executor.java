package com.shurpo.financialassistant.model.webservice;

import com.shurpo.financialassistant.exceptions.ProcessorException;
import com.shurpo.financialassistant.model.processor.Processor;

public interface Executor {
    public boolean execute(String urlString, Processor processor) throws ProcessorException;
}
