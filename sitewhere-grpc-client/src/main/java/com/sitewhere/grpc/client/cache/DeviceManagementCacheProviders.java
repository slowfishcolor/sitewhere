/*
 * Copyright (c) SiteWhere, LLC. All rights reserved. http://www.sitewhere.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.sitewhere.grpc.client.cache;

import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sitewhere.spi.area.IArea;
import com.sitewhere.spi.device.IDevice;
import com.sitewhere.spi.device.IDeviceAssignment;
import com.sitewhere.spi.device.IDeviceType;
import com.sitewhere.spi.microservice.IMicroservice;

/**
 * Cache providers for device management entities.
 * 
 * @author Derek
 */
public class DeviceManagementCacheProviders {

    /** Cache id for area cache */
    public static final String ID_AREA_CACHE = "area_by_token";

    /** Cache id for area by id cache */
    public static final String ID_AREA_ID_CACHE = "area_by_id";

    /** Cache id for device type cache */
    public static final String ID_DEVICE_TYPE_CACHE = "device_type_by_token";

    /** Cache id for device type by id cache */
    public static final String ID_DEVICE_TYPE_ID_CACHE = "device_type_by_id";

    /** Cache id for device cache */
    public static final String ID_DEVICE_CACHE = "device_by_token";

    /** Cache id for device by id cache */
    public static final String ID_DEVICE_ID_CACHE = "device_by_id";

    /** Cache id for device assignment cache */
    public static final String ID_ASSIGNMENT_CACHE = "assignment_by_token";

    /** Cache id for device assignment by id cache */
    public static final String ID_ASSIGNMENT_ID_CACHE = "assignment_by_id";

    /**
     * Cache for areas.
     * 
     * @author Derek
     */
    public static class AreaCache extends CacheProvider<String, IArea> {

	/** Static logger instance */
	private static Log LOGGER = LogFactory.getLog(AreaCache.class);

	public AreaCache(IMicroservice microservice, boolean createOnStartup) {
	    super(microservice, ID_AREA_CACHE, createOnStartup);
	}

	/*
	 * @see com.sitewhere.spi.server.lifecycle.ILifecycleComponent#getLogger()
	 */
	@Override
	public Log getLogger() {
	    return LOGGER;
	}
    }

    /**
     * Cache for areas by id.
     * 
     * @author Derek
     */
    public static class AreaByIdCache extends CacheProvider<UUID, IArea> {

	/** Static logger instance */
	private static Log LOGGER = LogFactory.getLog(AreaByIdCache.class);

	public AreaByIdCache(IMicroservice microservice, boolean createOnStartup) {
	    super(microservice, ID_AREA_ID_CACHE, createOnStartup);
	}

	/*
	 * @see com.sitewhere.spi.server.lifecycle.ILifecycleComponent#getLogger()
	 */
	@Override
	public Log getLogger() {
	    return LOGGER;
	}
    }

    /**
     * Cache for device types.
     * 
     * @author Derek
     */
    public static class DeviceTypeCache extends CacheProvider<String, IDeviceType> {

	/** Static logger instance */
	private static Log LOGGER = LogFactory.getLog(DeviceTypeCache.class);

	public DeviceTypeCache(IMicroservice microservice, boolean createOnStartup) {
	    super(microservice, ID_DEVICE_TYPE_CACHE, createOnStartup);
	}

	/*
	 * @see com.sitewhere.spi.server.lifecycle.ILifecycleComponent#getLogger()
	 */
	@Override
	public Log getLogger() {
	    return LOGGER;
	}
    }

    /**
     * Cache for device types by id.
     * 
     * @author Derek
     */
    public static class DeviceTypeByIdCache extends CacheProvider<UUID, IDeviceType> {

	/** Static logger instance */
	private static Log LOGGER = LogFactory.getLog(DeviceTypeByIdCache.class);

	public DeviceTypeByIdCache(IMicroservice microservice, boolean createOnStartup) {
	    super(microservice, ID_DEVICE_TYPE_ID_CACHE, createOnStartup);
	}

	/*
	 * @see com.sitewhere.spi.server.lifecycle.ILifecycleComponent#getLogger()
	 */
	@Override
	public Log getLogger() {
	    return LOGGER;
	}
    }

    /**
     * Cache for devices.
     * 
     * @author Derek
     */
    public static class DeviceCache extends CacheProvider<String, IDevice> {

	/** Static logger instance */
	private static Log LOGGER = LogFactory.getLog(DeviceCache.class);

	public DeviceCache(IMicroservice microservice, boolean createOnStartup) {
	    super(microservice, ID_DEVICE_CACHE, createOnStartup);
	}

	/*
	 * @see com.sitewhere.spi.server.lifecycle.ILifecycleComponent#getLogger()
	 */
	@Override
	public Log getLogger() {
	    return LOGGER;
	}
    }

    /**
     * Cache for devices by id.
     * 
     * @author Derek
     */
    public static class DeviceByIdCache extends CacheProvider<UUID, IDevice> {

	/** Static logger instance */
	private static Log LOGGER = LogFactory.getLog(DeviceByIdCache.class);

	public DeviceByIdCache(IMicroservice microservice, boolean createOnStartup) {
	    super(microservice, ID_DEVICE_ID_CACHE, createOnStartup);
	}

	/*
	 * @see com.sitewhere.spi.server.lifecycle.ILifecycleComponent#getLogger()
	 */
	@Override
	public Log getLogger() {
	    return LOGGER;
	}
    }

    /**
     * Cache for device assignments.
     * 
     * @author Derek
     */
    public static class DeviceAssignmentCache extends CacheProvider<String, IDeviceAssignment> {

	/** Static logger instance */
	private static Log LOGGER = LogFactory.getLog(DeviceAssignmentCache.class);

	public DeviceAssignmentCache(IMicroservice microservice, boolean createOnStartup) {
	    super(microservice, ID_ASSIGNMENT_CACHE, createOnStartup);
	}

	/*
	 * @see com.sitewhere.spi.server.lifecycle.ILifecycleComponent#getLogger()
	 */
	@Override
	public Log getLogger() {
	    return LOGGER;
	}
    }

    /**
     * Cache for device assignments by id.
     * 
     * @author Derek
     */
    public static class DeviceAssignmentByIdCache extends CacheProvider<UUID, IDeviceAssignment> {

	/** Static logger instance */
	private static Log LOGGER = LogFactory.getLog(DeviceAssignmentByIdCache.class);

	public DeviceAssignmentByIdCache(IMicroservice microservice, boolean createOnStartup) {
	    super(microservice, ID_ASSIGNMENT_ID_CACHE, createOnStartup);
	}

	/*
	 * @see com.sitewhere.spi.server.lifecycle.ILifecycleComponent#getLogger()
	 */
	@Override
	public Log getLogger() {
	    return LOGGER;
	}
    }
}
