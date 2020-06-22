package it.polito.tdp.food.model;

public class Event implements Comparable<Event> {
	
	public enum EventType {
		INIZIO_PREPARAZIONE, // viene assegnato un cibo ad una stazione
		FINE_PREPARAZIONE, // la stazione ha completato la prep. di un cibo
	}

	private Double time ; // tempo in minuti
	private EventType type ;
	private Stazione stazione ;
	private Food food ;
	
	public Double getTime() {
		return time;
	}
	public Stazione getStazione() {
		return stazione;
	}
	public Food getFood() {
		return food;
	}
	/**
	 * @param time
	 * @param stazione
	 * @param food
	 */
	public Event(Double time, EventType type, Stazione stazione, Food food) {
		super();
		this.time = time;
		this.type = type;
		this.stazione = stazione;
		this.food = food;
	}
	@Override
	public int compareTo(Event other) {
		return this.time.compareTo(other.time);
	}
	public EventType getType() {
		return type;
	}
}
