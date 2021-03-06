/*
 * Copyright (c) SiteWhere, LLC. All rights reserved. http://www.sitewhere.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.sitewhere.microservice.operations;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sitewhere.server.lifecycle.LifecycleProgressContext;
import com.sitewhere.server.lifecycle.LifecycleProgressMonitor;
import com.sitewhere.spi.microservice.configuration.IConfigurableMicroservice;
import com.sitewhere.spi.server.lifecycle.ILifecycleProgressMonitor;
import com.sitewhere.spi.server.lifecycle.LifecycleStatus;

/**
 * Operation that stops a microservice configuration and allows the completion
 * result to be tracked asynchronously.
 * 
 * @author Derek
 *
 * @param <T>
 */
public class StopConfigurationOperation<T extends IConfigurableMicroservice>
	extends CompletableConfigurationOperation<T> {

    /** Static logger instance */
    private static Log LOGGER = LogFactory.getLog(StopConfigurationOperation.class);

    /** Tenant engine being started */
    private T microservice;

    public StopConfigurationOperation(T microservice, CompletableFuture<T> completableFuture) {
	super(completableFuture);
	this.microservice = microservice;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.concurrent.Callable#call()
     */
    @Override
    public T call() throws Exception {
	try {
	    // Stop microservice.
	    LOGGER.info("Stopping '" + getMicroservice().getName() + "' configuration.");
	    ILifecycleProgressMonitor monitor = new LifecycleProgressMonitor(
		    new LifecycleProgressContext(1, "Stop microservice configuration."), getMicroservice());
	    long start = System.currentTimeMillis();
	    getMicroservice().configurationStop(getMicroservice().getGlobalApplicationContext(),
		    getMicroservice().getLocalApplicationContext(), monitor);
	    if (getMicroservice().getLifecycleStatus() == LifecycleStatus.LifecycleError) {
		throw getMicroservice().getLifecycleError();
	    }
	    LOGGER.info("Microservice configuration '" + getMicroservice().getName() + "' stopped in "
		    + (System.currentTimeMillis() - start) + "ms.");
	    getCompletableFuture().complete(getMicroservice());
	    return getMicroservice();
	} catch (Throwable t) {
	    LOGGER.error("Unable to stop microservice configuration '" + getMicroservice().getName() + "'.", t);
	    getCompletableFuture().completeExceptionally(t);
	    throw t;
	}
    }

    public T getMicroservice() {
	return microservice;
    }

    public void setMicroservice(T microservice) {
	this.microservice = microservice;
    }

    public static <T extends IConfigurableMicroservice> CompletableFuture<T> createCompletableFuture(T microservice,
	    ExecutorService executor) {
	CompletableFuture<T> completableFuture = new CompletableFuture<T>();
	executor.submit(new StopConfigurationOperation<T>(microservice, completableFuture));
	return completableFuture;
    }
}
