/*
 * Copyright (c) SiteWhere, LLC. All rights reserved. http://www.sitewhere.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.sitewhere.asset.persistence;

import java.util.UUID;

import com.sitewhere.persistence.Persistence;
import com.sitewhere.rest.model.asset.Asset;
import com.sitewhere.rest.model.asset.AssetType;
import com.sitewhere.rest.model.common.MetadataProvider;
import com.sitewhere.rest.model.search.asset.AssetSearchCriteria;
import com.sitewhere.rest.model.search.device.DeviceAssignmentSearchCriteria;
import com.sitewhere.spi.SiteWhereException;
import com.sitewhere.spi.SiteWhereSystemException;
import com.sitewhere.spi.asset.IAsset;
import com.sitewhere.spi.asset.IAssetManagement;
import com.sitewhere.spi.asset.IAssetType;
import com.sitewhere.spi.asset.request.IAssetCreateRequest;
import com.sitewhere.spi.asset.request.IAssetTypeCreateRequest;
import com.sitewhere.spi.device.IDeviceAssignment;
import com.sitewhere.spi.device.IDeviceManagement;
import com.sitewhere.spi.error.ErrorCode;
import com.sitewhere.spi.error.ErrorLevel;
import com.sitewhere.spi.search.ISearchResults;

/**
 * Persistence logic for asset management components.
 * 
 * @author Derek
 */
public class AssetManagementPersistence extends Persistence {

    /**
     * Handle base logic for creating an asset type.
     * 
     * @param assetType
     * @param request
     * @return
     * @throws SiteWhereException
     */
    public static AssetType assetTypeCreateLogic(IAssetTypeCreateRequest request) throws SiteWhereException {
	AssetType type = new AssetType();
	type.setId(UUID.randomUUID());
	type.setDescription(request.getDescription());

	// Use token if provided, otherwise generate one.
	if (request.getToken() != null) {
	    type.setToken(request.getToken());
	} else {
	    type.setToken(UUID.randomUUID().toString());
	}

	require("Name", request.getName());
	type.setName(request.getName());

	require("Image URL", request.getImageUrl());
	type.setImageUrl(request.getImageUrl());

	MetadataProvider.copy(request.getMetadata(), type);
	AssetManagementPersistence.initializeEntityMetadata(type);

	return type;
    }

    /**
     * Handle common asset type create logic.
     * 
     * @param target
     * @param request
     * @throws SiteWhereException
     */
    public static void assetTypeUpdateLogic(AssetType target, IAssetTypeCreateRequest request)
	    throws SiteWhereException {
	if (request.getToken() != null) {
	    target.setToken(request.getToken());
	}
	if (request.getName() != null) {
	    target.setName(request.getName());
	}
	if (request.getDescription() != null) {
	    target.setDescription(request.getDescription());
	}
	if (request.getImageUrl() != null) {
	    target.setImageUrl(request.getImageUrl());
	}
	if (request.getMetadata() != null) {
	    target.getMetadata().clear();
	    MetadataProvider.copy(request.getMetadata(), target);
	}
	AssetManagementPersistence.setUpdatedEntityMetadata(target);
    }

    /**
     * Common logic for deleting an asset type.
     * 
     * @param assetType
     * @param assetManagement
     * @throws SiteWhereException
     */
    public static void assetTypeDeleteLogic(IAssetType assetType, IAssetManagement assetManagement)
	    throws SiteWhereException {
	AssetSearchCriteria criteria = new AssetSearchCriteria(1, 1);
	criteria.setAssetTypeId(assetType.getId());
	ISearchResults<IAsset> assets = assetManagement.listAssets(criteria);
	if (assets.getNumResults() > 0) {
	    throw new SiteWhereSystemException(ErrorCode.AssetTypeNoDeleteHasAssets, ErrorLevel.ERROR);
	}
    }

    /**
     * Handle base logic for creating an asset.
     * 
     * @param assetType
     * @param request
     * @return
     * @throws SiteWhereException
     */
    public static Asset assetCreateLogic(IAssetType assetType, IAssetCreateRequest request) throws SiteWhereException {
	Asset asset = new Asset();
	asset.setId(UUID.randomUUID());
	asset.setAssetTypeId(assetType.getId());

	// Use token if provided, otherwise generate one.
	if (request.getToken() != null) {
	    asset.setToken(request.getToken());
	} else {
	    asset.setToken(UUID.randomUUID().toString());
	}

	require("Name", request.getName());
	asset.setName(request.getName());

	require("Image URL", request.getImageUrl());
	asset.setImageUrl(request.getImageUrl());

	MetadataProvider.copy(request.getMetadata(), asset);
	AssetManagementPersistence.initializeEntityMetadata(asset);

	return asset;
    }

    /**
     * Handle base logic for updating an asset.
     * 
     * @param assetType
     * @param target
     * @param request
     * @throws SiteWhereException
     */
    public static void assetUpdateLogic(IAssetType assetType, Asset target, IAssetCreateRequest request)
	    throws SiteWhereException {
	if (request.getAssetTypeToken() != null) {
	    target.setAssetTypeId(assetType.getId());
	}
	if (request.getToken() != null) {
	    target.setToken(request.getToken());
	}
	if (request.getName() != null) {
	    target.setName(request.getName());
	}
	if (request.getImageUrl() != null) {
	    target.setImageUrl(request.getImageUrl());
	}
	if (request.getMetadata() != null) {
	    target.getMetadata().clear();
	    MetadataProvider.copy(request.getMetadata(), target);
	}
	AssetManagementPersistence.setUpdatedEntityMetadata(target);
    }

    /**
     * Common logic for deleting an asset.
     * 
     * @param asset
     * @param assetManagement
     * @param deviceManagement
     * @throws SiteWhereException
     */
    public static void assetDeleteLogic(IAsset asset, IAssetManagement assetManagement,
	    IDeviceManagement deviceManagement) throws SiteWhereException {
	DeviceAssignmentSearchCriteria criteria = new DeviceAssignmentSearchCriteria(1, 1);
	criteria.setAssetId(asset.getId());
	ISearchResults<IDeviceAssignment> assignments = deviceManagement.listDeviceAssignments(criteria);
	if (assignments.getNumResults() > 0) {
	    throw new SiteWhereSystemException(ErrorCode.AssetNoDeleteHasAssignments, ErrorLevel.ERROR);
	}
    }
}