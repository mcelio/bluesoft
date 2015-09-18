package org;

import org.bluesoft.VoteNoRestauranteApplication;
import org.bluesoft.model.Restaurant;
import org.bluesoft.service.VoteService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = VoteNoRestauranteApplication.class)
@WebAppConfiguration
public class VoteNoRestauranteApplicationTests {

	/**
	 * Logger object
	 */
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	/**
	 * vote Service
	 */
	@Autowired
	private VoteService voteService;

	/**
	 * Test getting all restaurants
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetAllRetsaurants() throws Exception {
		log.debug("Get all restaurants test");
		Restaurant r = voteService.getAllRestaurants().get(0);
		Assert.assertEquals(new Integer(0), r.getVoteNumber());
		log.debug("Restaurant number of votes " + r.getVoteNumber());
		voteService.updateRestaurant(r.getId());
		r = voteService.getAllRestaurants().get(0);
		Assert.assertEquals(new Integer(1), r.getVoteNumber());
		Assert.assertEquals(5, voteService.getAllRestaurants().size());
	}

	/**
	 * Test updating (voting) an restaurant 
	 * 
	 * @throws Exception
	 */
	@Test
	public void testUpdateRetsaurant() throws Exception {
		log.debug("Voting test");
		
		Assert.assertEquals(5, voteService.getAllRestaurants().size());
		;

	}

}
