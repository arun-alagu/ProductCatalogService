package com.example.productcatalogservice.services;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.productcatalogservice.exceptions.RatingNotFoundException;
import com.example.productcatalogservice.models.Product;
import com.example.productcatalogservice.models.Rating;
import com.example.productcatalogservice.repositories.RatingRepository;

@Service
public class SelfRatingService implements IRatingService{
	
	private final RatingRepository ratingRepository;
	
	public SelfRatingService(RatingRepository ratingRepository) {
		this.ratingRepository = ratingRepository;
	}

	@Override
	@Transactional(readOnly = true)
	public Rating getRatingById(Long ratingId) throws RatingNotFoundException {
		if(ratingId == null) throw new IllegalArgumentException("Rating id cannot be empty");
		return ratingRepository.findById(ratingId).orElseThrow(()-> 
		new RatingNotFoundException("Rating not found"));
	}

	@Override
	public Rating getRatingByProduct(Product Product) throws RatingNotFoundException {
		if(Product == null) throw new IllegalArgumentException("Product cannot be empty");
		return Optional.ofNullable(ratingRepository.findByProduct(Product)).orElseThrow(()-> 
		new RatingNotFoundException("Rating not found"));
	}

	@Override
	public Rating addRating(Rating rating) throws RatingNotFoundException {
		if(rating == null) throw new IllegalArgumentException("Rating cannot be empty");
		
		return Optional.ofNullable(ratingRepository.save(rating)).orElseThrow(()->
		new RatingNotFoundException("Rating is not saved"));
	}

	@Override
	public Rating updateRating(Rating rating, Long ratingId) throws RatingNotFoundException {
		if(rating == null) throw new IllegalArgumentException("Rating cannot be empty");
		if(ratingId == null) throw new IllegalArgumentException("Rating id cannot be empty");
		Rating oldRating = getRatingById(ratingId);
		
		Optional.ofNullable(rating.getRate()).ifPresent(oldRating::setRate);
		Optional.ofNullable(rating.getCount()).ifPresent(oldRating::setCount);
		
		return Optional.ofNullable(ratingRepository.save(oldRating)).orElseThrow(()->
		new RatingNotFoundException("Rating is not saved"));
	}

	@Override
	public Rating deleteRating(Long ratingId) throws RatingNotFoundException {
		Rating deletedRating  = getRatingById(ratingId);
		if(deletedRating == null) throw new RatingNotFoundException("Rating not Found");
		ratingRepository.deleteById(ratingId);
		return deletedRating;
	}
	
}
