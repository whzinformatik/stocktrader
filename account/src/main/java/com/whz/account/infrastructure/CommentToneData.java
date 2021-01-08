package com.whz.account.infrastructure;

import com.whz.account.model.account.Sentiment;

/**
 * DTO object which represents a CommentTone in the CommentTone microservice.
 * The given assessment will be adjusted to the Account's Sentiment.
 * 
 * @author Lation
 */
public final class CommentToneData {

	public final String owner;
	public Sentiment sentiment;

	public CommentToneData(String owner, Sentiment sentiment) {
		this.owner = owner;
		this.sentiment = sentiment;
	}

	@Override
	public String toString() {
		return "CommentTone{" + "owner='" + owner + '\'' + ", sentiment='" + sentiment + '\'' + '}';
	}
}