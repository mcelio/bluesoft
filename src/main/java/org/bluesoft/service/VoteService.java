package org.bluesoft.service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.bluesoft.model.Restaurant;
import org.bluesoft.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@javax.transaction.Transactional
@Service
public class VoteService {
	
	
	@Autowired
	private RestaurantRepository restaurantRespository;
		
	/**
	 * Get all restaurants
	 * @return
	 * @throws Exception
	 */
	public List<Restaurant> getAllRestaurants() throws Exception{
		return restaurantRespository.findAll();
	}
	
	/**
	 * Update restaurant, incerment one vote
	 * 
	 * @param id
	 * @throws Exception
	 */
	public void updateRestaurant(Long id) throws Exception{		
		Restaurant restaurant = restaurantRespository.getOne(id);
		Integer voteNumber = restaurant.getVoteNumber();
		restaurant.setVoteNumber(voteNumber + 1);
		restaurantRespository.save(restaurant);
	}
	

	/**
	 * Load my poll map
	 * 
	 * @return Map
	 * @throws Exception
	 */
	public Map<Long, Restaurant> createMyPoll(Map<Long, Restaurant> map) throws Exception {
		Map<Long, Restaurant> myPoll = new HashMap<Long, Restaurant>();
		for (Restaurant value : map.values()) {
			Restaurant r = new Restaurant();
			r.setId(value.getId());
			r.setName(value.getName());
			r.setVoteNumber(0);
			myPoll.put(r.getId(), r);
		}
		return myPoll;
	}

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public Set<Restaurant> getMyPoll(Map<Long, Restaurant> myPoll) throws Exception {
		Set<Restaurant> list = new TreeSet<Restaurant>(new VoteComp());
		for (Map.Entry<Long, Restaurant> entry : myPoll.entrySet()) {
			list.add(entry.getValue());
		}
		return list;
	}

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public Set<Restaurant> getGeneralPoll() throws Exception {
		Set<Restaurant> list = new TreeSet<Restaurant>(new VoteComp());
		for (Restaurant r : getAllRestaurants()) {
			list.add(r);
		}
		return list;
	}

	/**
	 * Inner class to perform the comparison between Restaurants based on the number of votes
	 * @author Marcos
	 *
	 */
	class VoteComp implements Comparator<Restaurant> {

		@Override
		public int compare(Restaurant e1, Restaurant e2) {
			if (e1.getVoteNumber() > e2.getVoteNumber()) {
				return -1;
			} else {
				return 1;
			}
		}
	}
}
