/*
 * Copyright (c) SiteWhere, LLC. All rights reserved. http://www.sitewhere.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.sitewhere.rules.microservice;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sitewhere.microservice.multitenant.MultitenantMicroservice;
import com.sitewhere.rules.spi.microservice.IRuleProcessingMicroservice;
import com.sitewhere.rules.spi.microservice.IRuleProcessingTenantEngine;
import com.sitewhere.spi.SiteWhereException;
import com.sitewhere.spi.tenant.ITenant;

/**
 * Microservice that provides rule processing functionality.
 * 
 * @author Derek
 */
public class RuleProcessingMicroservice extends MultitenantMicroservice<IRuleProcessingTenantEngine>
	implements IRuleProcessingMicroservice {

    /** Static logger instance */
    private static Logger LOGGER = LogManager.getLogger();

    /** Microservice name */
    private static final String NAME = "Rule Processing";

    /** Identifies module resources such as configuration file */
    private static final String MODULE_IDENTIFIER = "rule-processing";

    /*
     * @see com.sitewhere.spi.microservice.IMicroservice#getName()
     */
    @Override
    public String getName() {
	return NAME;
    }

    /*
     * @see com.sitewhere.spi.microservice.IMicroservice#getIdentifier()
     */
    @Override
    public String getIdentifier() {
	return MODULE_IDENTIFIER;
    }

    /*
     * @see com.sitewhere.spi.microservice.multitenant.IMultitenantMicroservice#
     * createTenantEngine(com.sitewhere.spi.tenant.ITenant)
     */
    @Override
    public IRuleProcessingTenantEngine createTenantEngine(ITenant tenant) throws SiteWhereException {
	return new RuleProcessingTenantEngine(this, tenant);
    }

    /*
     * @see com.sitewhere.spi.server.lifecycle.ILifecycleComponent#getLogger()
     */
    @Override
    public Logger getLogger() {
	return LOGGER;
    }
}