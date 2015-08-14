/*
 * Copyright (c) SiteWhere, LLC. All rights reserved. http://www.sitewhere.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.sitewhere.web.rest.controllers;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sitewhere.SiteWhere;
import com.sitewhere.Tracer;
import com.sitewhere.core.user.SitewhereRoles;
import com.sitewhere.rest.model.asset.AssetModule;
import com.sitewhere.rest.model.asset.request.AssetCategoryCreateRequest;
import com.sitewhere.rest.model.search.SearchCriteria;
import com.sitewhere.rest.model.search.SearchResults;
import com.sitewhere.spi.SiteWhereException;
import com.sitewhere.spi.asset.AssetType;
import com.sitewhere.spi.asset.IAsset;
import com.sitewhere.spi.asset.IAssetCategory;
import com.sitewhere.spi.asset.IAssetModule;
import com.sitewhere.spi.command.ICommandResponse;
import com.sitewhere.spi.device.DeviceAssignmentStatus;
import com.sitewhere.spi.device.IDeviceAssignment;
import com.sitewhere.spi.search.ISearchResults;
import com.sitewhere.spi.server.debug.TracerCategory;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

/**
 * Controller for site operations.
 * 
 * @author Derek Adams
 */
@Controller
@RequestMapping(value = "/assets")
@Api(value = "assets", description = "Operations related to SiteWhere assets.")
public class AssetsController extends SiteWhereController {

	/** Static logger instance */
	private static Logger LOGGER = Logger.getLogger(AssetsController.class);

	/**
	 * Search for assets in an {@link IAssetModule} that meet the given criteria.
	 * 
	 * @param assetModuleId
	 * @param criteria
	 * @return
	 * @throws SiteWhereException
	 */
	@RequestMapping(value = "/modules/{assetModuleId}", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "Search hardware assets")
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Secured({ SitewhereRoles.ROLE_AUTHENTICATED_USER })
	public SearchResults<? extends IAsset> searchAssets(
			@ApiParam(value = "Unique asset module id", required = true) @PathVariable String assetModuleId,
			@ApiParam(value = "Criteria for search", required = false) @RequestParam(defaultValue = "") String criteria)
			throws SiteWhereException {
		Tracer.start(TracerCategory.RestApiCall, "searchAssets", LOGGER);
		List<? extends IAsset> found =
				SiteWhere.getServer().getAssetModuleManager().search(assetModuleId, criteria);
		SearchResults<? extends IAsset> results = new SearchResults(found);
		Tracer.stop(LOGGER);
		return results;
	}

	/**
	 * Get an asset from an {@link IAssetModule} by unique id.
	 * 
	 * @param assetModuleId
	 * @param assetId
	 * @return
	 * @throws SiteWhereException
	 */
	@RequestMapping(value = "/modules/{assetModuleId}/{assetId}", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "Find hardware asset by unique id")
	@Secured({ SitewhereRoles.ROLE_AUTHENTICATED_USER })
	public IAsset getAssetById(
			@ApiParam(value = "Unique asset module id", required = true) @PathVariable String assetModuleId,
			@ApiParam(value = "Unique asset id", required = true) @PathVariable String assetId)
			throws SiteWhereException {
		Tracer.start(TracerCategory.RestApiCall, "getAssetById", LOGGER);
		try {
			return SiteWhere.getServer().getAssetModuleManager().getAssetById(assetModuleId, assetId);
		} finally {
			Tracer.stop(LOGGER);
		}
	}

	/**
	 * Get all assignments for a given asset.
	 * 
	 * @param assetModuleId
	 * @param assetId
	 * @param siteToken
	 * @param page
	 * @param pageSize
	 * @return
	 * @throws SiteWhereException
	 */
	@RequestMapping(value = "/modules/{assetModuleId}/{assetId}/assignments", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "List all assignments for a given asset")
	@Secured({ SitewhereRoles.ROLE_AUTHENTICATED_USER })
	public ISearchResults<IDeviceAssignment> getAssignmentsForAsset(
			@ApiParam(value = "Unique asset module id", required = true) @PathVariable String assetModuleId,
			@ApiParam(value = "Unique asset id", required = true) @PathVariable String assetId,
			@ApiParam(value = "Unique token that identifies site", required = true) @RequestParam String siteToken,
			@ApiParam(value = "Assignment status", required = false) @RequestParam(required = false) String status,
			@ApiParam(value = "Page number (First page is 1)", required = false) @RequestParam(defaultValue = "1") int page,
			@ApiParam(value = "Page size", required = false) @RequestParam(defaultValue = "100") int pageSize)
			throws SiteWhereException {
		Tracer.start(TracerCategory.RestApiCall, "getAssetById", LOGGER);
		try {
			DeviceAssignmentStatus decodedStatus =
					(status != null) ? DeviceAssignmentStatus.valueOf(status) : null;
			SearchCriteria criteria = new SearchCriteria(page, pageSize);
			return SiteWhere.getServer().getDeviceManagement().getDeviceAssignmentsForAsset(siteToken,
					assetModuleId, assetId, decodedStatus, criteria);
		} catch (IllegalArgumentException e) {
			throw new SiteWhereException("Invalid device assignment status: " + status);
		} finally {
			Tracer.stop(LOGGER);
		}
	}

	/**
	 * List all asset modules.
	 * 
	 * @return
	 * @throws SiteWhereException
	 */
	@RequestMapping(value = "/modules", method = RequestMethod.GET)
	@ResponseBody
	@Secured({ SitewhereRoles.ROLE_AUTHENTICATED_USER })
	public List<AssetModule> listAssetModules() throws SiteWhereException {
		Tracer.start(TracerCategory.RestApiCall, "listAssetModules", LOGGER);
		try {
			List<AssetModule> converted = new ArrayList<AssetModule>();
			List<IAssetModule<?>> modules = SiteWhere.getServer().getAssetModuleManager().listModules();
			for (IAssetModule<?> module : modules) {
				converted.add(AssetModule.copy(module));
			}
			return converted;
		} finally {
			Tracer.stop(LOGGER);
		}
	}

	/**
	 * List all asset modules that contain device assets.
	 * 
	 * @return
	 * @throws SiteWhereException
	 */
	@RequestMapping(value = "/modules/devices", method = RequestMethod.GET)
	@ResponseBody
	@Secured({ SitewhereRoles.ROLE_AUTHENTICATED_USER })
	// TODO: This method needs to be refactored as search criteria for /modules method.
	// For example /modules?type=Device
	public List<AssetModule> listDeviceAssetModules() throws SiteWhereException {
		Tracer.start(TracerCategory.RestApiCall, "listDeviceAssetModules", LOGGER);
		try {
			List<AssetModule> matches = new ArrayList<AssetModule>();
			List<IAssetModule<?>> modules = SiteWhere.getServer().getAssetModuleManager().listModules();
			for (IAssetModule<?> module : modules) {
				if (module.getAssetType() == AssetType.Device) {
					matches.add(AssetModule.copy(module));
				}
			}
			return matches;
		} finally {
			Tracer.stop(LOGGER);
		}
	}

	/**
	 * Refresh all asset modules.
	 * 
	 * @return
	 * @throws SiteWhereException
	 */
	@RequestMapping(value = "/modules/refresh", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "Refresh the list of asset modules")
	@Secured({ SitewhereRoles.ROLE_AUTHENTICATED_USER })
	public List<ICommandResponse> refreshModules() throws SiteWhereException {
		Tracer.start(TracerCategory.RestApiCall, "refreshModules", LOGGER);
		try {
			return SiteWhere.getServer().getAssetModuleManager().refreshModules();
		} finally {
			Tracer.stop(LOGGER);
		}
	}

	/**
	 * Create a new asset category.
	 * 
	 * @param request
	 * @return
	 * @throws SiteWhereException
	 */
	@RequestMapping(value = "/categories", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "Create a new asset category")
	@Secured({ SitewhereRoles.ROLE_AUTHENTICATED_USER })
	public IAssetCategory createAssetCategory(@RequestBody AssetCategoryCreateRequest request)
			throws SiteWhereException {
		Tracer.start(TracerCategory.RestApiCall, "createAssetCategory", LOGGER);
		try {
			return SiteWhere.getServer().getAssetManagement().createAssetCategory(request);
		} finally {
			Tracer.stop(LOGGER);
		}
	}

	/**
	 * Get an asset category by unique id.
	 * 
	 * @param categoryId
	 * @return
	 * @throws SiteWhereException
	 */
	@RequestMapping(value = "/categories/{categoryId}", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "Find category by unique id")
	@Secured({ SitewhereRoles.ROLE_AUTHENTICATED_USER })
	public IAssetCategory getAssetCategoryById(
			@ApiParam(value = "Unique category id", required = true) @PathVariable String categoryId)
			throws SiteWhereException {
		Tracer.start(TracerCategory.RestApiCall, "getAssetCategoryById", LOGGER);
		try {
			return SiteWhere.getServer().getAssetManagement().getAssetCategory(categoryId);
		} finally {
			Tracer.stop(LOGGER);
		}
	}

	/**
	 * Delete an existing asset category.
	 * 
	 * @param categoryId
	 * @return
	 * @throws SiteWhereException
	 */
	@RequestMapping(value = "/categories/{categoryId}", method = RequestMethod.DELETE)
	@ResponseBody
	@ApiOperation(value = "Delete an existing asset category")
	@Secured({ SitewhereRoles.ROLE_AUTHENTICATED_USER })
	public IAssetCategory deleteAssetCategory(
			@ApiParam(value = "Unique category id", required = true) @PathVariable String categoryId)
			throws SiteWhereException {
		Tracer.start(TracerCategory.RestApiCall, "deleteAssetCategory", LOGGER);
		try {
			return SiteWhere.getServer().getAssetManagement().deleteAssetCategory(categoryId);
		} finally {
			Tracer.stop(LOGGER);
		}
	}

	/**
	 * List asset categories that match the given search criteria.
	 * 
	 * @param page
	 * @param pageSize
	 * @return
	 * @throws SiteWhereException
	 */
	@RequestMapping(value = "/categories", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "List asset categories that match the criteria")
	@Secured({ SitewhereRoles.ROLE_AUTHENTICATED_USER })
	public ISearchResults<IAssetCategory> listAssetCategories(
			@ApiParam(value = "Page Number (First page is 1)", required = false) @RequestParam(defaultValue = "1") int page,
			@ApiParam(value = "Page size", required = false) @RequestParam(defaultValue = "100") int pageSize)
			throws SiteWhereException {
		Tracer.start(TracerCategory.RestApiCall, "listAssetCategories", LOGGER);
		try {
			SearchCriteria criteria = new SearchCriteria(page, pageSize);
			return SiteWhere.getServer().getAssetManagement().listAssetCategories(criteria);
		} finally {
			Tracer.stop(LOGGER);
		}
	}
}