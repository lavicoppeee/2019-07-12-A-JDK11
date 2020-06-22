package it.polito.tdp.food.model;

import java.time.Duration;
import java.util.*;
import java.util.PriorityQueue;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.food.model.Event.EventType;
import it.polito.tdp.food.model.Food.StatoPreparazione;

public class Simulator {

	//MONDO
	private Graph<Food,DefaultWeightedEdge> graph;
	private Model model;
	
	private List<Food> foods;
	private List<Stazione> stazioni;
	
	//INPUT
	private int k=5;
	//OUTPUT
	private int numFoodTot;
	private Double timePreparazione;
	//CODE
	private PriorityQueue<Event> queue ;
	
	public Simulator(Graph<Food, DefaultWeightedEdge> graph, Model model) {
		super();
		this.graph = graph;
		this.model = model;
	}
	
	public void init(Food partenza) {
		this.queue = new PriorityQueue<>() ;
		this.foods=new ArrayList<>(this.graph.vertexSet()); //nel cibo ci sono tutti i vertici del grafo
		
		for(Food c: foods)
			 c.setPreparazione(StatoPreparazione.DA_PREPARARE);
		
		this.stazioni = new ArrayList<>() ;
		for(int i=0; i<this.k; i++) {
			this.stazioni.add(new Stazione(true, null)) ;
		}
		
		this.timePreparazione = 0.0 ;
		this.numFoodTot = 0;
		
		List<Calorie> vicini=model.getFoodConnessi(partenza);
		
		for(int i=0; i<this.k && i<vicini.size(); i++) {
			this.stazioni.get(i).setLibera(false);
			this.stazioni.get(i).setFood(vicini.get(i).getFood());
			vicini.get(i).getFood().setPreparazione(StatoPreparazione.IN_CORSO);
			
			Event e = new Event(vicini.get(i).getCalorie(),
					EventType.FINE_PREPARAZIONE,
					this.stazioni.get(i),
					vicini.get(i).getFood() 
					) ;
			queue.add(e) ;
		}
		
	}
	
	

	public void run() {
		while(!queue.isEmpty()) {
			Event e = queue.poll() ;
			processEvent(e) ;
		}
	}
	
	private void processEvent(Event e) {
    switch(e.getType()) {
		
		case INIZIO_PREPARAZIONE:
			List<Calorie> vicini = model.getFoodConnessi(e.getFood());
			Calorie prossimo = null ;
			for(Calorie vicino: vicini) {
				if(vicino.getFood().getPreparazione()==StatoPreparazione.DA_PREPARARE) {
					prossimo = vicino ;
					break ; // non proseguire nel ciclo
				}
			}
			
			if(prossimo != null) {
				prossimo.getFood().setPreparazione(StatoPreparazione.IN_CORSO);
				e.getStazione().setLibera(false);
				e.getStazione().setFood(prossimo.getFood());
				
				Event e2 = new Event(e.getTime()+prossimo.getCalorie(),
						EventType.FINE_PREPARAZIONE,
						e.getStazione(),
						prossimo.getFood()
						);
				this.queue.add(e2) ;
			}
			
			break;
			
			
		case FINE_PREPARAZIONE:
			this.numFoodTot++ ;
			this.timePreparazione = e.getTime() ;
			
			e.getStazione().setLibera(true);
			e.getFood().setPreparazione(StatoPreparazione.PREPARATO);
			
			Event e2 = new Event(e.getTime(),
					EventType.INIZIO_PREPARAZIONE, 
					e.getStazione(), 
					e.getFood()) ;
			
			this.queue.add(e2) ;

			break;
		}
	}
	
	

	public int getK() {
		return k;
	}

	public void setK(int k) {
		this.k = k;
	}

	public int getNumFoodTot() {
		return numFoodTot;
	}

	public Double getTimePreparazione() {
		return timePreparazione;
	}
	
	
}

