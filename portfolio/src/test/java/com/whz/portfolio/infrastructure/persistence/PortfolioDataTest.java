/*
 * Copyright © 2020-2021, Fachgruppe Informatik WHZ <help.flaxel@gmail.com>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.whz.portfolio.infrastructure.persistence;

import static org.junit.jupiter.api.Assertions.*;

import com.whz.portfolio.infrastructure.PortfolioData;
import com.whz.portfolio.model.portfolio.PortfolioState;

import org.junit.jupiter.api.Test;

public class PortfolioDataTest {

	private final String id = "new_id";

	private final String owner = "TestOwner";

	@Test
	public void testEmpty() {
		PortfolioData feedbackData = PortfolioData.empty();
		assertTrue(feedbackData.id.isEmpty());
		assertTrue(feedbackData.owner.isEmpty());
	}

	@Test
	public void testFromStateWithNull() {
		PortfolioState feedbackState = PortfolioState.identifiedBy(null);
		PortfolioData feedbackData = PortfolioData.from(feedbackState);
		assertNull(feedbackData.id);
		assertNull(feedbackData.owner);
	}

	@Test
	public void testFromStateNonNull() {
		PortfolioState feedbackState = PortfolioState.identifiedBy(id).withOwner(owner);
		PortfolioData feedbackData = PortfolioData.from(feedbackState);
		assertEquals(id, feedbackData.id);
		assertEquals(owner, feedbackData.owner);
	}

}
