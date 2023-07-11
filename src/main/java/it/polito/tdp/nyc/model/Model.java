package it.polito.tdp.nyc.model;

import java.util.ArrayList;
import java.util.Collections;
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
				if(n1.getNTACode().compareTo(n2.getNTACode())<0) //!n1.equals(n2)
				{
					//calcolo il peso
					Set<String> unione = new HashSet<>(n1.getSSIDs());
					unione.addAll(n2.getSSIDs());
					Graphs.addEdge(grafo, n1, n2, unione.size());
				}
			}
		}
		//System.out.println("Vertici: " + grafo.vertexSet().size());
		//System.out.println("Archi: " + grafo.edgeSet().size());  il grafo non è orientato, quindi devo considerare la metà di tutte le copppie 
	}
	
	
	
	/*Non ho un oggetto ben definito che posso identificare come arco, anzi l'arco sarà composto da un insieme di informazioni,
	 *  quindi mi creo la classe "Arco" contenente tutto quello che mi serve
	 */
	public List<Arco> analisiArchi() {
		double media=0.0;
		
		for(DefaultWeightedEdge ei: grafo.edgeSet())
		{
			media +=  grafo.getEdgeWeight(ei);
		}
		
		media = media/grafo.edgeSet().size();
		List<Arco> result = new ArrayList<Arco>();
		
		for(DefaultWeightedEdge ei: grafo.edgeSet())
		{
			if(grafo.getEdgeWeight(ei)>media)
			{
				result.add(new Arco(grafo.getEdgeSource(ei).getNTACode(), grafo.getEdgeTarget(ei).getNTACode(), (int)grafo.getEdgeWeight(ei)));
			}
		}
		Collections.sort(result);
		return result;
	}
	
}
