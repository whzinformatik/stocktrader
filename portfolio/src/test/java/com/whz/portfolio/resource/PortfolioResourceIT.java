/*
 * Copyright © 2020-2021, Fachgruppe Informatik WHZ <help.flaxel@gmail.com>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.whz.portfolio.resource;

import static org.hamcrest.Matchers.*;
import com.whz.portfolio.infrastructure.PortfolioData;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

public class PortfolioResourceIT extends ResourceTestCase {

	@Test
	public void testReady() {
		givenJsonClient().when().get("/ready").then().statusCode(200);
	}

	@Test
	public void testPost() {
		createPortfolio("TestOwner").then().statusCode(201).body("id", notNullValue()).body("owner",
				equalTo("TestOwner"));
	}

	@Test
	public void testGet() {
		PortfolioData data = createPortfolio("TestOwner").getBody().as(PortfolioData.class);

		givenJsonClient().when().get("/portfolio/" + data.id).then().statusCode(200).body("id", equalTo(data.id)).body("owner",
				equalTo(data.owner));
	}

	private Response createPortfolio(String owner) {
		return givenJsonClient().body(owner).when().post("/portfolio");
	}
}
