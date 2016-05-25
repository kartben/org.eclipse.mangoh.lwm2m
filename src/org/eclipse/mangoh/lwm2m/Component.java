package org.eclipse.mangoh.lwm2m;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.kura.configuration.ConfigurableComponent;
import org.eclipse.kura.position.PositionService;
import org.eclipse.leshan.client.californium.LeshanClient;
import org.osgi.service.component.ComponentContext;

public class Component implements ConfigurableComponent {

	private PositionService m_positionService;
	private Map<String, Object> m_properties;

	LeshanClient m_client = null;

	protected void activate(ComponentContext componentContext, Map<String, Object> properties) {
		m_properties = new HashMap<String, Object>();

		doUpdate(properties);

	}

	protected void deactivate(ComponentContext componentContext) {
		m_client.stop(true);
	}

	public void updated(Map<String, Object> properties) {
		doUpdate(properties);
	}

	public void setPositionService(PositionService positionService) {
		m_positionService = positionService;
	}

	public void unsetPositionService(PositionService positionService) {
		m_positionService = null;
	}

	private void doUpdate(Map<String, Object> properties) {
		if (m_client != null) {
			m_client.stop(true);
		}

		// update
		String endpoint = ProcessUtil.command("cm", "info", "fsn");
		
		String host = properties.get("server.address").toString();
		int port = Integer.valueOf(properties.get("server.port").toString());
		
		String serverURI = "coap://" + host + ":" + port;
		int localPort = 0;
		String localAddress = null;
		String secureLocalAddress = null;
		int secureLocalPort = 0;
		boolean needBootstrap = false;
		byte[] pskIdentity = null;
		byte[] pskKey = null;

		m_client = MangohClient.createClient(endpoint, localAddress, localPort, secureLocalAddress, secureLocalPort,
				needBootstrap, serverURI, pskIdentity, pskKey, m_positionService);

		m_client.start();

	}

}
