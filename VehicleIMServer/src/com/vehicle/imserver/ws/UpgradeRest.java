package com.vehicle.imserver.ws;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.vehicle.imserver.dao.bean.VersionInfo;
import com.vehicle.imserver.service.interfaces.UpgradeService;
import com.vehicle.service.bean.LatestVersionResponse;

@Path("upgrade")
public class UpgradeRest {

	private UpgradeService upgradeService;

	public void setUpgradeService(UpgradeService service) {
		this.upgradeService = service;
	}

	public UpgradeService getUpgradeService() {
		return this.upgradeService;
	}

	@GET
	@Path("latestversion")
	@Produces("application/json")
	public Response ackAll(@Context HttpServletRequest request) {

		LatestVersionResponse resp = new LatestVersionResponse();
		VersionInfo version = null;

		try {
			version = this.upgradeService.getLatestVersion();
		} catch (Exception e) {
			e.printStackTrace();
		}

		resp.setVersion(version);

		return Response.status(Status.OK).entity(resp).build();
	}
}
