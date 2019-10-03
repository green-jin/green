/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.ncip.facades.populators;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.commercefacades.order.data.ConsignmentData;
import de.hybris.platform.commercefacades.order.data.ConsignmentEntryData;
import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.converters.Converters;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.storelocator.model.PointOfServiceModel;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;


/**
 * Populator implementation for {@link de.hybris.platform.ordersplitting.model.ConsignmentEntryModel} as source and
 * {@link de.hybris.platform.commercefacades.order.data.ConsignmentData} as target type.
 */
public class Ncipb2bConsignmentPopulator implements Populator<ConsignmentModel, ConsignmentData>
{
	private static final Logger LOG = Logger.getLogger(Ncipb2bConsignmentPopulator.class);

	private Converter<ConsignmentEntryModel, ConsignmentEntryData> consignmentEntryConverter;
	private Converter<PointOfServiceModel, PointOfServiceData> pointOfServiceConverter;
	private Converter<AddressModel, AddressData> addressConverter;

	protected Converter<ConsignmentEntryModel, ConsignmentEntryData> getConsignmentEntryConverter()
	{
		return consignmentEntryConverter;
	}

	@Required
	public void setConsignmentEntryConverter(final Converter<ConsignmentEntryModel, ConsignmentEntryData> consignmentEntryConverter)
	{
		this.consignmentEntryConverter = consignmentEntryConverter;
	}

	protected Converter<PointOfServiceModel, PointOfServiceData> getPointOfServiceConverter()
	{
		return pointOfServiceConverter;
	}

	@Required
	public void setPointOfServiceConverter(final Converter<PointOfServiceModel, PointOfServiceData> pointOfServiceConverter)
	{
		this.pointOfServiceConverter = pointOfServiceConverter;
	}

	protected Converter<AddressModel, AddressData> getAddressConverter()
	{
		return addressConverter;
	}

	@Required
	public void setAddressConverter(final Converter<AddressModel, AddressData> addressConverter)
	{
		this.addressConverter = addressConverter;
	}

	@Override
	public void populate(final ConsignmentModel source, final ConsignmentData target)
	{
		LOG.debug("============= Ncipb2bConsignmentPopulator Start=========================");
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		target.setCode(source.getCode());
		target.setTrackingID(source.getTrackingID());  
		target.setStatus(source.getStatus());
		target.setStatusDisplay(source.getStatusDisplay());
		target.setShippingDate(source.getShippingDate()); 
		target.setLfdat(source.getLfdat());
		target.setDely_type(source.getDely_type());
		target.setRemark(source.getRemark());
		target.setShipping_number(source.getShipping_number());
		target.setNcipdelivery(source.getNcipdelivery());
		 
		target.setEntries(Converters.convertAll(source.getConsignmentEntries(), getConsignmentEntryConverter()));
		if (ConsignmentStatus.SHIPPED.equals(source.getStatus()) || ConsignmentStatus.READY_FOR_PICKUP.equals(source.getStatus()))
		{
			target.setStatusDate(source.getShippingDate());
		}
		if (source.getDeliveryPointOfService() != null)
		{
			target.setDeliveryPointOfService(getPointOfServiceConverter().convert(source.getDeliveryPointOfService()));
		}
		if (source.getShippingAddress() != null)
		{
			target.setShippingAddress(getAddressConverter().convert(source.getShippingAddress()));
		}
		LOG.debug("============= Ncipb2bConsignmentPopulator End=========================");
	}
}
