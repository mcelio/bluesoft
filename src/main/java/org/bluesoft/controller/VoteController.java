package org.bluesoft.controller;

import java.util.HashMap;
import java.util.Map;

import org.bluesoft.model.Restaurant;
import org.bluesoft.service.VoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 
 * Controller class that maps all actions  on the application based on URL
 * 
 * @author Marcos
 *
 */
@Controller
public class VoteController {

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
	 * Restaurant list
	 */
	private static Map<Long, Restaurant> restaurants;

	/**
	 * Restaurant already voted
	 */
	private Map<Long, Restaurant> voted;

	/**
	 * Flag indicating the first user vote
	 */
	private boolean isFirstVote = true;

	private Map<Long, Restaurant> myPoll;

	/**
	 * Restaurant chosen
	 */
	private Restaurant choice;

	/**
	 * Get restaurants singleton
	 * 
	 * @return List
	 * @throws Exception
	 */
	private Map<Long, Restaurant> getRestaurants() throws Exception {
		if (restaurants == null) {
			restaurants = new HashMap<Long, Restaurant>();
			for (Restaurant r : voteService.getAllRestaurants()) {
				restaurants.put(r.getId(), r);
			}
		}
		return restaurants;
	}
	
	/**
	 * Action perform the first vote
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/")
	public String context(Map<String, Object> model) {
		return vote(model);
	}

	/**
	 * Action perform the first vote
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/vote")
	public String vote(Map<String, Object> model) {
		try {
			Map<Long, Restaurant> map = getRestaurants();
			Restaurant restaurant2 = null;
			if (myPoll == null) {
				myPoll = voteService.createMyPoll(getRestaurants());
			}
			if (voted == null) {
				voted = new HashMap<Long, Restaurant>();
			}

			for (Map.Entry<Long, Restaurant> entry : map.entrySet()) {
				Restaurant restaurant = entry.getValue();
				Long id = restaurant.getId();
				if (isFirstVote) {
					log.debug("Performing the first vote");
					if (id.equals(1L)) {
						model.put("restaurant1", restaurant);
						voted.put(id, restaurant);
					}

					if (id.equals(2L)) {
						model.put("restaurant2", restaurant);
						voted.put(id, restaurant);
						isFirstVote = false;
						return "vote";
					}
				} else {
					if (voted.get(id) == null) {
						restaurant2 = restaurant;
						break;
					}
				}

			}
			if (restaurant2 == null) {
				return "signin";
			}
			model.put("restaurant1", choice);
			model.put("restaurant2", restaurant2);
			voted.put(restaurant2.getId(), restaurant2);
		} catch (Exception e) {
			log.error("Error when accessing /vote :", e);
		}
		return "vote";
	}

	/**
	 * Action shows the poll ranking.
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/ranking")
	public String ranking(Map<String, Object> model) {
		try {
			model.put("general_poll", voteService.getGeneralPoll());
			model.put("my_poll", voteService.getMyPoll(myPoll));
		} catch (Exception e) {
			log.error("Error when accessing /ranking :", e);
		}
		return "ranking";
	}

	/**
	 * Action performs the voting.
	 * 
	 * @param model
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/vote/{id}")
	public String vote(Map<String, Object> model, @PathVariable("id") Long id) throws Exception {
		try {
			log.debug("Voting " + id);
			voteService.updateRestaurant(id);
			choice = getRestaurants().get(id);
			// update my poll
			Restaurant restaurant = myPoll.get(id);
			Integer voteNumber = restaurant.getVoteNumber();
			restaurant.setVoteNumber(voteNumber + 1);
			myPoll.put(id, restaurant);
		} catch (Exception e) {
			log.error("Error when accessing /ranking :", e);
		}
		return vote(model);
	}

	/**
	 * Action perform the poll restart
	 * @return
	 */
	@RequestMapping("/restart")
	public String restart(Map<String, Object> model) {
		try {
			myPoll = voteService.createMyPoll(getRestaurants());
			isFirstVote = true;
			voted = new HashMap<Long, Restaurant>();
		} catch (Exception e) {
			log.error("Error when accessing /restart :", e);
		}
		return vote(model);
	}
}
