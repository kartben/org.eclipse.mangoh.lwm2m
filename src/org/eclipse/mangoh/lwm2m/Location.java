package org.eclipse.mangoh.lwm2m;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.kura.position.PositionService;
import org.eclipse.leshan.client.resource.BaseInstanceEnabler;
import org.eclipse.leshan.core.response.ReadResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Location extends BaseInstanceEnabler {
	private static final Logger s_logger = LoggerFactory.getLogger(Location.class);

	private PositionService ps;

	public Location(PositionService ps) {
		this.ps = ps;
	}

	@Override
	public ReadResponse read(int resourceId) {
		s_logger.info("Read on Location Resource " + resourceId);
		switch (resourceId) {
		case 0:
			return ReadResponse.success(resourceId, String.valueOf(ps.getNmeaPosition().getLatitude()));
		case 1:
			return ReadResponse.success(resourceId, String.valueOf(ps.getNmeaPosition().getLongitude()));
		case 2:
			return ReadResponse.success(resourceId, String.valueOf(ps.getNmeaPosition().getAltitude()));
		case 5:
			DateFormat dateFormat = new SimpleDateFormat("ddMMyy");
			Date date = new Date();
			try {
				date = dateFormat.parse(ps.getNmeaDate());
			} catch (ParseException e) {
			}

			return ReadResponse.success(resourceId, date);
		default:
			return super.read(resourceId);
		}
	}
}
