package it.polito.tdp.nyc.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.nyc.db.NYCDao;

public class Model {
	
	private NYCDao dao;
	private List<String> boroughs;
	private List<NTA> NTAs;
	private Graph<NTA, DefaultWeightedEdge> grafo;

	
	
	public Model() {
		super();
		this.dao =  new NYCDao();
	}
	
	
	
	public List<String> getBoroughs() {
		if(boroughs == null)
			boroughs = dao.getAllBorough();
		return boroughs;
	}
	
	
	public void creaGrafo(String borough) {
		NTAs = dao.getNTAbyBorough(borough);
		//System.out.println(NTAs);
		
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(grafo, NTAs);
		
		for(NTA n1 : NTAs)
		{
			for(NTA n2 : NTAs)
			{
				if(!n1.equals(n2))
				{
					//calcolo il peso
					Set<String> unione = new HashSet<>(n1.getSSIDs());
					unione.addAll(n2.getSSIDs());
					Graphs.addEdge(grafo, n1, n2, unione.size());
				}
			}
		}
	}
	
	
	
}
