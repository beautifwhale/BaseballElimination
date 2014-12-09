// david
public class BaseballElimination {
	
	// number of teams in the division
	private int m_iNumberOfTeams;
			
	private String[] m_straTeamNames;
	private int[] m_aiWins; // wins
	private int[] m_aiLosses; // losses
	private int[] m_aiRemaining; // remaining games
	private int[][] m_aaiAgainst; // game schedule
		
	// queue of teams
	private Queue<String> m_strqTeams;
	
	// symbol table used to identify teams in the arrays
	private ST<String, Integer> m_stTeams;
			
	
	public BaseballElimination(String filename)
	{
		In in = new In(filename);
		
		// number of teams in the division
		m_iNumberOfTeams = in.readInt();
		
		// array of team names
		m_straTeamNames = new String[m_iNumberOfTeams];
						
		m_aiWins = new int[m_iNumberOfTeams]; // wins
		m_aiLosses = new int[m_iNumberOfTeams]; // losses
		m_aiRemaining = new int[m_iNumberOfTeams]; // remaining games
		m_aaiAgainst = new int[m_iNumberOfTeams][m_iNumberOfTeams]; // game schedule
		
		// queue to store each team in the division
		m_strqTeams = new Queue<String>();
		
		// symbol table to identify the teams in the arrays
		m_stTeams = new ST<String, Integer>();
		
		for(int i = 0; i < m_iNumberOfTeams; i++)
		{
			StdOut.printf("%d ", i);
			// get the team name
			String teamName = in.readString();
			StdOut.printf("%s\t", teamName); 
			// add team to the queue
			m_strqTeams.enqueue(teamName);
			
			// use team name as key and store the array index
			// as a value in the symbol table
			m_stTeams.put(teamName, i);
			
			// set the number of wins for the team
			m_aiWins[i] = in.readInt();
			StdOut.printf("%d ", m_aiWins[i]);
			// set the number of losses for the team
			m_aiLosses[i] = in.readInt();
			StdOut.printf("%d ", m_aiLosses[i]);
			// set the number of games left to play
			m_aiRemaining[i] = in.readInt();
			StdOut.printf("%d    ", m_aiRemaining[i]);
			// set the number of games left to play against 
			// each of the other teams. j is the second teams
			// index
			for(int j = 0; j < m_iNumberOfTeams; j++)
			{
				// input the number of games
				m_aaiAgainst[i][j] = in.readInt();
				StdOut.printf("%d ", m_aaiAgainst[i][j]);
			}			
			StdOut.print("\n");
		}
		// create a flow network
		// number of vertices = s + (Number of teams * (Number of teams -1)) + t
		FlowNetwork m_FlowNetwork = new FlowNetwork(1 + (m_iNumberOfTeams * (m_iNumberOfTeams -1)) + 1);
		StdOut.println(m_FlowNetwork);
		
	}
	
	// number of teams in the division
	public int numberOfTeams()
	{
		// return the number of teams in the division
		return m_iNumberOfTeams;
	}
	
	// queue of teams in the division
	public Iterable<String> teams()
	{
		// return the queue of teams
		return m_strqTeams;		
	}
	
	// number of wins for team
	public int wins(String team)
	{
		// use the team name as a key value in a symbol table
		// and use the returned value as the array index in the 
		// wins array to find the number of wins for that team
		int iIndex = m_stTeams.get(team);
		
		// return the number of wins for the team
		return m_aiWins[iIndex];
	}
	
	// number of losses for team
	public int losses(String team)
	{
		// use the team name as a key value in a symbol table
		// and use the returned value as the array index in the 
		// losses array to find the number of losses for that team
		int iIndex = m_stTeams.get(team);
		
		// return the number of wins for the team
		return m_aiLosses[iIndex];
	}
	
	// number of games left to play by a team
	public int remaining(String team)
	{
		// use the team name as a key value in a symbol table
		// and use the returned value as the array index in the 
		// remaining games array to find the number of 
		// remaining games for that team
		int iIndex = m_stTeams.get(team);
		
		// return the number of wins for the team
		return m_aiRemaining[iIndex];
	}
	
	public int against(String team1, String team2)
	{
		// use the team names as a key values in a symbol table
		// and use the returned value as the array index in the 
		// against two dimensional array to find the number of 
		// game team1 has against team2
		int iIndexTeam1 = m_stTeams.get(team1);
		int iIndexTeam2 = m_stTeams.get(team2);
		
		// return the number of games teams have against each other
		return m_aaiAgainst[iIndexTeam1][iIndexTeam2];
	}
	
	public boolean isEliminated(String team)
	{
		StdOut.printf("in isEliminated %s\n", team);
		return true;
	}
	
	public Iterable<String> certificateOfElimination(String team)
	{
		StdOut.printf("in certificateOfElimination %s\n", team);
		return null;
	}

	/**
	 * @param args
	 */
	
	public static void main(String[] args) 
	{		
		BaseballElimination division = new BaseballElimination(args[0]);
		
		for(String team : division.teams())
		{
			if(division.isEliminated(team))
			{
				StdOut.print(team + " is eliminated by the subset R = { ");
				for (String t : division.certificateOfElimination(team))
				{
					StdOut.print(t);
				}
				StdOut.println("}");
			}
			else
			{
				StdOut.println(team + " is not eliminated");
			}
		}		
	}
}
