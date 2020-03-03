package devoir2;

import java.util.HashMap;
import java.util.Random;
import java.util.Collections;
import java.util.Arrays;
import java.util.List;

public class Event {
	Sim subject; //The Sim subject to the event.
	String type; //reproduction / birth / death.
	float time; //The time of the event.
	
	//Constructor.
	public Event(Sim subject, String type, float time) {
		this.subject = subject;
		this.type = type;
		this.time = time;
	}
	
	//Calls the right function according to the type of the event.
	public void call(Simulation simulation) {
		switch(type) {
		case "reproduction":
			reproduction(simulation.eventQ, simulation.relations, simulation.population, simulation.f, simulation.r);
			break;
		case "birth":
			birth(simulation.eventQ, simulation.population, simulation.r, simulation.death_rate, simulation.accident_rate, simulation.age_factor);
			break;
		case "death":
			death(simulation.population);
			break;
		default :
			return;		
		}
	};
	
	//Function to be called once a birth event is triggered.
	private void birth(PQ eventQ, BinaryHeap<Sim> population, float r, double death_rate, double accident_rate, double age_factor) {

		//Sets the death time of the new born.
		if(subject.getDeathTime()==0.0)
				subject.setDeathTime((float) new AgeModel(accident_rate, death_rate, age_factor).randomAge(new Random()));
		if (subject.getSex().equals("f")) {
			
			//Sets the next reproduction of the new born.
			float waitingTime = (float) AgeModel.randomWaitingTime(new Random(), r);
			Event reproduction = new Event(subject, "reproduction", time + waitingTime);
			eventQ.insert(reproduction);
		}

		//Adds the new born to the population.
		population.add(subject);
	}
	
	//Function to be called once a death event is triggered.
	private void death(BinaryHeap<Sim> population) {
		
		//Remove the sim from the population.
		population.removeNode(subject);
	}
	
	//Function to be called once a reproduction event is triggered.
	private void reproduction(PQ eventQ, HashMap<Sim, Sim> relations, BinaryHeap<Sim> population, float f, float r) {
		
		//Checks if the Sim is dead, and return if it does.
		if (!subject.isAlive(time)) return;
		
		float motherAge = subject.getAge(time);

		//Checks if the age is good for reproduction.
		if (motherAge >= 16f && motherAge <= 50f) {
			Sim mother = subject;
			Sim father = null;
			Sim previousMan = relations.get(mother);
			boolean isFaithful = false;
			
			//Chooses the father.
			if (relations.get(mother) != null) {
				if (relations.get(previousMan) == mother && Math.random() <= f && previousMan.isAlive(time)){
					isFaithful = true;
				}
			}
			if (isFaithful) {
				father = previousMan;
			} else {
				//Checks if another father is available and choses one randomly if it does.
				if (fatherExists(population, previousMan)) {
					father = findFather(population, previousMan, relations, f);
				} else father = null;
			}
			
			if (father != null) {
				//Creates the baby and assign its parameters.
				Sim baby = new Sim();
				baby.setMother(mother);
				baby.setFather(father);
				baby.setBirthTime(time);
				Event e = new Event(baby, "birth", time);
				eventQ.insert(e);

				//Sets up the new relation.
				relations.put(mother, father);
				relations.put(father, mother);
			}
		}
		
		//Sets the next reproduction
		float waitingTime = (float) AgeModel.randomWaitingTime(new Random(), r);
		Event reproduction = new Event(subject, "reproduction", time + waitingTime);
		eventQ.insert(reproduction);
	}

	private boolean fatherExists(BinaryHeap<Sim> population, Sim previousMan) {
		Sim father;
		float fatherAge;
		boolean existence=false;
		for (int i=0;i<population.size();i++) {
			father=population.getNodeAtIndex(i);
			fatherAge = father.getAge(time);
			if (father.getSex() == "m" && fatherAge <= 74 &&fatherAge >= 16 && father != previousMan) {
				existence=true;
				break;
			}
		}
		return existence;
	}

	//Returns an available man which is not the last man with whom the woman had a relationship.
	private Sim findFather(BinaryHeap<Sim> population, Sim previousMan, HashMap<Sim, Sim> relations, float f) {
		
		//Creates a shuffled array of int which do not repeat.
		Integer[] intArr = new Integer[population.capacity()];
		for (int i =0; i < intArr.length; i++) {
			intArr[i] = i;
		}
		List<Integer> list = Arrays.asList(intArr);
		Collections.shuffle(list);
		Object[] shuffled = list.toArray();


		Sim father = null;
		while (father == null) {
			for (int i = 0; i < population.capacity(); i++) {

				//Gets a random Sim from the shuffled array.
				Sim sim = population.getNodeAtIndex((int) shuffled[i]);
				if (sim == null) continue;
				float simAge = sim.getAge(time);

				//Checks if the age and the sex are okay as well as if the sim is the previous man.
				if (sim.getSex() == "f" || simAge >= 74 || simAge < 16 || sim == previousMan) continue;

				//Checks if the sim was in another relationship and willing to change if so.
				if (relations.get(sim) != null && Math.random() >= 1 - f) continue;

				//When an available sim is found, assigns it to father
				father = sim;
				break;
			}
		}
		return father;
	}
	
}
