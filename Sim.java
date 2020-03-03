package devoir2;

public class Sim implements Comparable{
	
	private Sim father = null;
	private Sim mother = null;
	private float birthTime;
	private float deathTime;
	private String sex;
	static float MIN_MATING_AGE_F = 16;
	static float MAX_MATING_AGE_F = 50;

	//Checks if the Sim is alive
	public boolean isAlive(float time){
		if (time < deathTime) return true;
		return false;
	}

	//Return the age of the Sim
	public float getAge(float time) {
		return time - birthTime;
	}

	/*
	 *Compares the age of two Sims and returns a negative number if the first Sim is younger, 0 if they are equal 
	 *or a positive number if the first Sim is older
	*/

	@Override
	public int compareTo(Object o) {
		return (int) (((Sim) o).getBirthTime() - getBirthTime());
	}
	
	//Getters and Setters.
	public Sim getFather() {
		return father;
	}

	public void setFather(Sim father) {
		this.father = father;
	}

	public Sim getMother() {
		return mother;
	}

	public void setMother(Sim mother) {
		this.mother = mother;
	}

	public float getBirthTime() {
		return birthTime;
	}

	public void setBirthTime(float birthTime) {
		this.birthTime = birthTime;
	}

	public float getDeathTime() {
		return deathTime;
	}

	public void setDeathTime(float deathTime) {
		this.deathTime = deathTime;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}
	
}
