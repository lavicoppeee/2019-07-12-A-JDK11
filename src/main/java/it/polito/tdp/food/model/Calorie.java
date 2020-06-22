package it.polito.tdp.food.model;

public class Calorie implements Comparable<Calorie> {

	
	private Food food;
	private Double calorie;
	
	
	public Calorie(Food food, Double calorie) {
		super();
		this.food = food;
		this.calorie = calorie;
	}


	public Food getFood() {
		return food;
	}


	public void setFood(Food food) {
		this.food = food;
	}


	public Double getCalorie() {
		return calorie;
	}


	public void setCalorie(Double calorie) {
		this.calorie = calorie;
	}


	@Override
	public int compareTo(Calorie o) {
		// TODO Auto-generated method stub
		return -(this.calorie.compareTo(o.calorie));
	}

}
