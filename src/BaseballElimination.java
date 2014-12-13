import java.util.ArrayList;
import java.util.List;

/**
 * Algorithms Assignment 
 * Baseball elimination problem. 
 * 
 * Given the standings in a sports division at some point during the season, 
 * determine which teams have been mathematically eliminated from winning their division.
 * 
 * @author David Morton and Donnchadh Murphy 
 */
public class BaseballElimination {

	// number of teams in the division
	private int m_iNumberOfTeams;
	private int m_iNumberOfMatches;

	private String[] m_straTeamNames;
	private int[] m_aiWins; // wins
	private int[] m_aiLosses; // losses
	private int[] m_aiRemaining; // remaining games
	private int[][] m_aaiAgainst; // game schedule

	// queue of teams
	private Queue<String> m_strqTeams;
	
	// queue of teams
	private Queue<String> t_strqTeams;

	// symbol table used to identify teams in the arrays
	private ST<String, Integer> m_stTeams;

	public BaseballElimination(String filename) {
		In in = new In(filename);

		// number of teams in the division
		m_iNumberOfTeams = in.readInt();

		// array of team names
		m_straTeamNames = new String[m_iNumberOfTeams];

		m_aiWins = new int[m_iNumberOfTeams]; // wins
		m_aiLosses = new int[m_iNumberOfTeams]; // losses
		m_aiRemaining = new int[m_iNumberOfTeams]; // remaining games
		m_aaiAgainst = new int[m_iNumberOfTeams][m_iNumberOfTeams]; // game
																	// schedule

		// queue to store each team in the division
		m_strqTeams = new Queue<String>();

		// symbol table to identify the teams in the arrays
		m_stTeams = new ST<String, Integer>();
		
		for(int i = 0; i < m_iNumberOfTeams; i++)
		{
			//StdOut.printf("%d ", i);
			// get the team name
			String teamName = in.readString();
			//StdOut.printf("%s\t", teamName); 
			// add team to the queue
			m_strqTeams.enqueue(teamName);

			// use team name as key and store the array index
			// as a value in the symbol table
			m_stTeams.put(teamName, i);

			// set the number of wins for the team
			m_aiWins[i] = in.readInt();
			//StdOut.printf("%d ", m_aiWins[i]);
			// set the number of losses for the team
			m_aiLosses[i] = in.readInt();
			//StdOut.printf("%d ", m_aiLosses[i]);
			// set the number of games left to play
			m_aiRemaining[i] = in.readInt();

			//StdOut.printf("%d    ", m_aiRemaining[i]);
			
			// set the number of games left to play against 
			// each of the other teams. j is the second teams
			// index
			for (int j = 0; j < m_iNumberOfTeams; j++) {
				// input the number of games
				m_aaiAgainst[i][j] = in.readInt();
				//StdOut.printf("%d ", m_aaiAgainst[i][j]);
			}			
			//StdOut.print("\n");
		}

	}

	// number of teams in the division
	public int numberOfTeams() {
		// return the number of teams in the division
		return m_iNumberOfTeams;
	}

	// queue of teams in the division
	public Iterable<String> teams() {
		// return the queue of teams
		return m_strqTeams;
	}

	// number of wins for team
	public int wins(String team) {
		// use the team name as a key value in a symbol table
		// and use the returned value as the array index in the
		// wins array to find the number of wins for that team
		int iIndex = m_stTeams.get(team);

		// return the number of wins for the team
		return m_aiWins[iIndex];
	}

	// number of losses for team
	public int losses(String team) {
		// use the team name as a key value in a symbol table
		// and use the returned value as the array index in the
		// losses array to find the number of losses for that team
		int iIndex = m_stTeams.get(team);

		// return the number of wins for the team
		return m_aiLosses[iIndex];
	}

	// number of games left to play by a team
	public int remaining(String team) {
		// use the team name as a key value in a symbol table
		// and use the returned value as the array index in the
		// remaining games array to find the number of
		// remaining games for that team
		int iIndex = m_stTeams.get(team);

		// return the number of wins for the team
		return m_aiRemaining[iIndex];
	}
	
	/*
	 * Find the number of games left between two teams.
	 * 
	 * @param team1 in the division 
	 * @param team2 in the division
	 * 
	 * @return int the number of games left between the two teams
	 */
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

	/*
	 * Has the team got a chance of winning the league?
	 * 
	 * @param team is the team being checked for elimination
	 * @return boolean if the team has been eliminated
	 */
	public boolean isEliminated(String team)
	{
		// check if the team is in the division table
		if (!m_stTeams.contains(team)) {
			// throw error if team is not in table
			throw new java.lang.IllegalArgumentException();
		}

		// is the team eliminated based on the number
		// of games it has left to play compared to the
		// other teams wins

		int iTeam = m_stTeams.get(team); // get the index of the team we are checking
		
		// trivial elimination
		for(int iOtherTeams = 0; iOtherTeams < m_iNumberOfTeams; iOtherTeams++)
		{
			// if the teams wins + remaining games is less than any other
			// teams total wins, the team is eliminated based on:
			// not being able to catch up with the other teams if they win
			// all of their games
			if (m_aiWins[iTeam] + m_aiRemaining[iTeam] < m_aiWins[iOtherTeams]) 
			{
				// team is eliminated
				return true;
			}

		} // end for: team is not trivially eliminated
		
		// create a flow network

		// get number of matches
		m_iNumberOfMatches = m_iNumberOfTeams * (m_iNumberOfTeams - 1) / 2;

		int sourceVertexIndex = m_iNumberOfMatches + m_iNumberOfTeams; // sourceIndex
		int targetVertexIndex = sourceVertexIndex + 1; // targetIndex

		// number of vertices = m_iNumberOfMatches + m_iNumberOfTeams +
		// sourceVertex + targetVertex
		FlowNetwork m_FlowNetwork = new FlowNetwork(m_iNumberOfMatches
				+ m_iNumberOfTeams + 2);


		int iCurrentVertex = 0; // the first match
		
		//  add edges between the vertices in the flow network
		for(int i = 0; i < m_iNumberOfTeams; i++) 
		{
			// connect the matches to appropriate team vertex
			// source -> match -> team -> target
			for (int j = i + 1; j < m_iNumberOfTeams; j++) 
			{
				// add edges between the sourceVertex and the matches.
				// set the flow capacity of the edge that is stored in the 
				// two dimensional array Against[][] that contains the number 
				// of matches each team has against each other

				m_FlowNetwork.addEdge(new FlowEdge(sourceVertexIndex, iCurrentVertex, m_aaiAgainst[i][j]));// source -> match
								
				// add edge between the match vertex and the two teams playing in that match
				// team one
				m_FlowNetwork.addEdge(new FlowEdge(iCurrentVertex, m_iNumberOfMatches + i, Double.POSITIVE_INFINITY)); // match -> team1
				// team two
				m_FlowNetwork.addEdge(new FlowEdge(iCurrentVertex, m_iNumberOfMatches + j, Double.POSITIVE_INFINITY)); // match -> team2
								
				// increment the match counter to deal with team1 & team2
				// in the next match
				iCurrentVertex++; // next match			
			}

			// add edge between the teams and the target vertex
			// the capacity of these edges is the difference between the
			// team we are checking, their wins + remaining games - all the
			// other
			// teams total wins
			m_FlowNetwork.addEdge(new FlowEdge(m_iNumberOfMatches + i, targetVertexIndex, m_aiWins[iTeam] + m_aiRemaining[iTeam] - m_aiWins[i])); // team -> target
		}
		
		// print the flow network
		//StdOut.println(m_FlowNetwork);
		
		// run the FordFulkerson algorithm on the flow network to determine
		// the augmenting paths
		FordFulkerson fordFulkerson = new FordFulkerson(m_FlowNetwork, sourceVertexIndex, targetVertexIndex);
		
		// check each edge from the source to each match vertex. If each edges
		// flow value is not at full capacity, the team is eliminated
		// because they cannot win even if they manage to win all of there
		// remaining games and the team in the lead loses all of their 
		// remaining games
		for (FlowEdge edge : m_FlowNetwork.adj(sourceVertexIndex)) 
		{
			//StdOut.println(edge.toString());
			// check each edge adjacent from the source vertex
			if (edge.flow() != edge.capacity())
			{
				// if each edge is not full the team is eliminated
				return true;
			}	    	  
	    }			
		
		// all edges adjacent from the source vertex are full
		// that means that this team has a chance to win. 
		return false;
	}

	public Iterable<String> certificateOfElimination(String team) {
		int x = m_stTeams.get(team); // x holds current team
		int m_iNumberOfMatches = this.m_iNumberOfTeams* (this.m_iNumberOfTeams - 1) / 2;
		int vID = 0; // current vertex

		
		for (int i = 0; i < this.m_iNumberOfTeams; i++) {
			
			/*
			 * If the teams wins + games remaining - wins is greater than 0
			 */
			if (m_aiWins[x] + m_aiRemaining[x] - m_aiWins[i] < 0)
				m_strqTeams.enqueue(team); // add team to queue
			
			
			}

		//System.out.println(m_strqTeams);

		if (m_strqTeams.size() > 0)
			return m_strqTeams;

		// creating flow network
		FlowNetwork m_FlowNetwork = new FlowNetwork(m_iNumberOfMatches
				+ m_iNumberOfTeams + 2);
		int sVertexIndex = m_iNumberOfMatches + m_iNumberOfTeams; // sourceIndex
		int tVertexIndex = sVertexIndex + 1; // targetIndex
		
		// adding edges to our vertices
		for (int i = 0; i < m_iNumberOfTeams; i++) {
			for (int j = i + 1; j < m_iNumberOfTeams; j++) {

				if (i == j) {

					// adding edges to the source vertex that match
					m_FlowNetwork.addEdge(new FlowEdge(sVertexIndex, vID, m_aaiAgainst[i][j]));

					// add edge between the match vertex and the two teams
					// playing in that match
					// team one
					m_FlowNetwork.addEdge(new FlowEdge(vID, m_iNumberOfMatches + i, Double.POSITIVE_INFINITY));
					// team two
					m_FlowNetwork.addEdge(new FlowEdge(vID, m_iNumberOfMatches + j, Double.POSITIVE_INFINITY));

					vID++; // go to next vertex
				}
			}
			
			m_FlowNetwork .addEdge(new FlowEdge(m_iNumberOfMatches + i, tVertexIndex, m_aiWins[x] + m_aiRemaining[x] - m_aiWins[i]));
		}
		
		FordFulkerson fordFulk = new FordFulkerson(m_FlowNetwork, sVertexIndex, tVertexIndex);
		StdOut.println(m_FlowNetwork);
		
		return null;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BaseballElimination division = new BaseballElimination(args[0]);

		for (String team : division.teams()) {
			if (division.isEliminated(team)) {
				StdOut.print(team + " is eliminated by the subset R = { ");
				
				for (String t : division.certificateOfElimination(team))
				{
					StdOut.print(t);
				}
				
				StdOut.println("}");
			} else {
				StdOut.println(team + " is not eliminated");
			}
		}
	}
}
