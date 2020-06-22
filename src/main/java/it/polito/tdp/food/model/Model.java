package it.polito.tdp.food.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.food.db.FoodDao;

public class Model {

	private Graph<Food,DefaultWeightedEdge> graph;
	private FoodDao dao;
	private List<Food> food;
	
	public Model() {
		dao=new FoodDao();
	}
	
	public void creaGrafo(Integer porzioni) {
		this.graph=new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		this.food=dao.getFood(porzioni);
		
		//aggiungi vertici
		Graphs.addAllVertices(this.graph, this.food);
		
		//aggiungi archi
		for(Food f1:this.food) {
			for(Food f2:this.food) {
				if(!f1.equals(f2) && f1.getFood_code()<f2.getFood_code()) {
					Double peso=dao.getCalorie(f1, f2);
					if(peso!=null) {
						Graphs.addEdge(this.graph,f1,f2,peso);
					}
				}
			}
		}
		
	}
	
	public int nVertici() {
		return this.graph.vertexSet().size();
	}

	public int nArchi() {
		return this.graph.edgeSet().size();
	}
	
	public List<Food> getFood(Integer p){
		return food;
	}
	
	public List<Calorie> getFoodConnessi(Food f){
		
		List<Calorie> food=new ArrayList<>();
		List<Food> vicini=Graphs.neighborListOf(graph, f);
		
		for(Food v: vicini) {
			Double calorie=this.graph.getEdgeWeight(this.graph.getEdge(f, v));
			food.add(new Calorie(v,calorie));
		}
		Collections.sort(food);
		return food;
	}
	
	public String simula(Food cibo, int k) {
		Simulator sim = new Simulator(this.graph, this) ;
		sim.setK(k);
		sim.init(cibo);
		sim.run();
		String messaggio = String.format("Preparati %d cibi in %f minuti\n", 
				sim.getNumFoodTot(), sim.getTimePreparazione());
		return messaggio ;
	}
	
}
