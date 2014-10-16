
public class Agent implements Comparable
{
	private int score;
	
	public Agent(int number)
	{
		this.score = number;
	}
	
	public int getScore()
	{
		return this.score;
	}

	public void setScore(int number)
	{
		this.score = number;
	}
	
	@Override
	public int compareTo(Object o) 
	{
		Agent a = (Agent) o;
		
		if (this.getScore() < a.getScore())
			return -1;
		else if (this.getScore() > a.getScore())
			return 1;
		else return 0;
	}
}
