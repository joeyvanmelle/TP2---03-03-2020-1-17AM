package devoir2;

import java.util.HashMap;
import java.util.TreeSet;
import java.util.ArrayList;
import java.util.Random;

public class Simulation {
	BinaryHeap<Sim> population; 	//Contains the living Sims.
	PQ eventQ;						//Contains ordonned list of event that will occur.
	HashMap<Sim, Sim> relations;	//A Sim imput return the Sim which whom it had its last relation.
	float r;						//Reproduction rate.
	float f;						//Fidelity ratio.
	double accident_rate;
	double death_rate;
	double age_factor;
					

	//Constructor
	public Simulation(double death_rate, double accident_rate, double age_factor, double r, double f) {
		this.r = (float) r;
		this.f = (float) f;
		this.accident_rate = accident_rate;
		this.death_rate = death_rate;
		this.age_factor = age_factor;
	}

	/* Simulates the growth of a population
	 * n being the initial Sims of the population
	 * Tmax being the duration of the simulation.
	 */
	public void simulate(int n, int Tmax) {

		//Initialises the structures.
		population = new BinaryHeap<Sim>(2 * n);
		relations = new HashMap<Sim, Sim>();
		eventQ = new PQ(new EventComparator());

		//Creation of the founders.
		for (int i = 0; i < n; i++) {
			Sim founder = new Sim();
			if (Math.random() < 0.5) founder.setSex("f");
			else founder.setSex("m");
			founder.setBirthTime(0.0f);
			Event birth = new Event(founder, "birth", 0.0f);
			founder.setDeathTime((float) new AgeModel(accident_rate,death_rate, age_factor).randomAge(new Random()));
			eventQ.insert(birth);
		}

		//Triggers the events in order.
		while(!eventQ.isEmpty()) {
			Event e = eventQ.deleteMin();
			System.out.println(e.type + "   occured at     " + e.time);
			
			if (e.time > Tmax) break;
			if (e.subject.getDeathTime() > e.time) {
				e.call(this);
			}
		}
	}

	/*
	 * Same as simulate(int n, int Tmax) but prints the size of the living population, the number
	 * of coalesced paternal bloodlines and the number of coalesced maternal bloodlines each century.
	 */
	public void empiricalStudy(int n, int Tmax) {

		//Initialises the structures.
		population = new BinaryHeap<Sim>(2 * n);
		relations = new HashMap<Sim, Sim>();
		eventQ = new PQ(new EventComparator());

		//Initialises the time.
		float time = 0.0f;

		//Creation of the founders.
		for (int i = 0; i < n; i++) {
			Sim founder = new Sim();
			if (Math.random() < 0.5) founder.setSex("f");
			else founder.setSex("m");
			founder.setBirthTime(0.0f);
			Event birth = new Event(founder, "birth", 0.0f);
			founder.setDeathTime((float) new AgeModel(accident_rate,death_rate, age_factor).randomAge(new Random()));
			eventQ.insert(birth);
		}

		//Triggers the events in order.
		while(!eventQ.isEmpty()) {
			Event e = eventQ.deleteMin();
			if (e.time > Tmax) break;
			if (e.subject.getDeathTime() > e.time) {
				e.call(this);
			}

			//Checks if 1 century passed since the last empirical study.
			if (e.time - time >= 100) {
				time = e.time;

				//Sets the structure for the empirical study.
				BinaryHeap<Sim> convHeap = population.copy(); //a copy of the population BinaryHeap.

				//Find every Coalescence point.
				ArrayList[] coalArray = findCoalescencePoints(convHeap);

				//Prints the population size.
				System.out.println("Time:" + time + "; Population size: " + population.size());

				//Prints the number of coalesced paternal bloodlines.
				System.out.println("Time:" + time + "; Number of coalesced paternal bloodlines: " + coalArray[0].size());

				//Prints the number of coalesced maternal bloodlines.
				System.out.println("Time:" + time + "; Number of coalesced maternal bloodlines: " + coalArray[1].size());
			}
		}
	}

	/*
	 *Returns every Coalescence Points of a population in an array of ArrayList
	 *The first ArrayList contains the coalescence points for fathers.
	 *The second ArrayList contains the coalescence points for mothers.
	 */ 
	public ArrayList[] findCoalescencePoints(BinaryHeap<Sim> convHeap) {

				//Sets the structures for the method.
				ArrayList[] result = new ArrayList[2];
				TreeSet<Sim> fathers = new TreeSet<Sim>();
				ArrayList<Coalescence> faArray = new ArrayList<Coalescence>();
				TreeSet<Sim> mothers = new TreeSet<Sim>();
				ArrayList<Coalescence> moArray = new ArrayList<Coalescence>();
				
				//Repeat until there is only one Sim in convHeap.
				while (convHeap.size() >1) {
					Sim removed = convHeap.poll();

					//Checks if the Sim is a female.
					if (removed.getSex().equals("f")) {

						//Checks if the mother is already in the TreeSet and if she is, create a coalescence point.
						Sim mother = removed.getMother();
						if(!mothers.isEmpty()&&mothers.contains(mother))
								moArray.add(new Coalescence(mothers.size(), removed.getBirthTime()));
						
						//Otherwise, adds the mother to the TreeSet.
						else  mothers.add(mother);
						
					}
					else {

						//Checks if the father is already in the TreeSet and if he is, create a coalescence point.
						Sim father = removed.getFather();
						
						if (!fathers.isEmpty()&&fathers.contains(father)) {
							faArray.add(new Coalescence(fathers.size(), removed.getBirthTime()));
						}

						//Otherwise, adds the father to the TreeSet.
						else fathers.add(father);
					}
				}
				result[0] = faArray;
				result[1] = moArray;
				return result;
	}
}
