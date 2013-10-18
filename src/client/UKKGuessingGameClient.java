package client;

import static common.Topics.GUESS;
import static common.Topics.HINT;
import static common.Topics.JOIN;

import common.Observer;

import java.util.*;

public class UKKGuessingGameClient extends GuessingGameClient {
	UKKGuessingGameClient c;
	Integer secretValue=3;
	int lastGuess=1;
	public static void main(String[] args) {
		try{
			new UKKGuessingGameClient();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public UKKGuessingGameClient(){
		String ip="10.20.60.170";
		int port=12345;
		String teamName="UKK";
		// subscribe observers
		c=this;
		// new game: figure out role
		c.subscribe(JOIN, new Observer(){
			@Override
			public void onUpdate(Object response) {
				boolean isHinting=(Boolean)response;
				if(isHinting){
					// set new secret value for this round
					secretValue=1+(int)(Math.random() * 9);
				}else{
					// send first guess
					lastGuess=5;
					try{
					c.publish(GUESS, lastGuess);
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}
		});
		// receive guess: send updated hint
		c.subscribe(GUESS, new Observer(){
			@Override
			public void onUpdate(Object response) {
				try{
					c.publish(HINT, secretValue.compareTo((Integer)response));
				}catch(Exception e){
					
				}
			}
		});
		// receive hint: send updated guess
		c.subscribe(HINT, new Observer(){
			@Override
			public void onUpdate(Object response) {
				int hint=(Integer)response;
				lastGuess+=hint;
				try{
					c.publish(GUESS, lastGuess);
				}catch(Exception e){
					
				}
			}
		});
		try{
			c.openConnection(ip,port,teamName);
		}catch(Exception e){
			
		}
	}
}
