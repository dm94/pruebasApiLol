import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.rithms.riot.api.RiotApi;
import net.rithms.riot.api.RiotApiException;
import net.rithms.riot.constant.PickType;
import net.rithms.riot.constant.Region;
import net.rithms.riot.constant.Season;
import net.rithms.riot.constant.SpectatorType;
import net.rithms.riot.constant.TournamentMap;
import net.rithms.riot.dto.Champion.Champion;
import net.rithms.riot.dto.League.League;
import net.rithms.riot.dto.MatchList.MatchList;
import net.rithms.riot.dto.MatchList.MatchReference;
import net.rithms.riot.dto.Static.SummonerSpell;
import net.rithms.riot.dto.Stats.AggregatedStats;
import net.rithms.riot.dto.Stats.ChampionStats;
import net.rithms.riot.dto.Stats.RankedStats;
import net.rithms.riot.dto.Summoner.Summoner;
import net.rithms.riot.dto.Team.Roster;
import net.rithms.riot.dto.Team.Team;
import net.rithms.riot.dto.Team.TeamMemberInfo;


public class Principal {

	public static void main(String[] args) throws RiotApiException {
		String key=apiKey.getApiKey();
		
        RiotApi api = new RiotApi(key);
        api.setRegion(Region.EUW);
        api.setSeason(Season.SIX);
        
        //Para crear un torneo
       /*int idProvider=api.createProvider(Region.EUW, "http://gzone.es/");
        int idTorneo=api.createTournament("Pruebas torneo Gzone", idProvider);
        
        String keyPartida=api.createTournamentCode(idTorneo, 3, TournamentMap.SUMMONERS_RIFT , PickType.ALL_RANDOM, SpectatorType.ALL);*/
        
        Summoner summoner = api.getSummonerByName(Region.EUW, "dm94");
        long id = summoner.getId();
        
        RankedStats rankedStats = api.getRankedStats(summoner.getId());
        System.out.println("ID del usuario: "+id);
        List<ChampionStats> campeones=rankedStats.getChampions();
        
        List<League> liga=api.getLeagueBySummoner(id);
        
        System.out.println("---------------------- Ligas ----------------------");
        
        for(int i=0;i<liga.size();i++){
        	System.out.println("Nombre de la liga: "+liga.get(i).getName());
        	System.out.println("Rango: "+liga.get(i).getTier());
        	System.out.println("Modo: "+liga.get(i).getQueue());
        }
        
        
        /*System.out.println("---------------------- Campeones ----------------------");
        
        int totalJugadas=0;
        int totalGanadas=0;
        int totalPerdidas=0;
        
        for(int i=0;i<campeones.size();i++){
        	AggregatedStats agrestats=campeones.get(i).getStats();
        	
        	if(campeones.get(i).getId()==0){
        		totalJugadas=agrestats.getTotalSessionsPlayed();
        		totalGanadas=agrestats.getTotalSessionsWon();
        		totalPerdidas=agrestats.getTotalSessionsLost();
        	}else{
        		System.out.println("ID de Campeon: "+campeones.get(i).getId());
            	System.out.println("Total de partidas jugadas "+agrestats.getTotalSessionsPlayed());
            	System.out.println("Total de partidas ganadas "+agrestats.getTotalSessionsLost());
            	System.out.println("Total de partidas perdidas "+agrestats.getTotalSessionsWon());
        	}
        }
        
        System.out.println("---------------------- Stats ----------------------");
        System.out.println("Total de partidas jugadas "+totalJugadas);
    	System.out.println("Total de partidas ganadas "+totalGanadas);
    	System.out.println("Total de partidas perdidas "+totalPerdidas);*/
        
        System.out.println("---------------------- Partidas ----------------------");
        MatchList detallesDePartidas=api.getMatchList(id);
        System.out.println("Total de partidas jugadas: "+detallesDePartidas.getTotalGames());
        List<MatchReference> listaPartidos=detallesDePartidas.getMatches();
        
        int SEASON2016=0;
        int partidasTop=0;
        int partdasJungle=0;
        int partidasMid=0;
        int partidasADC=0;
        int partidasSupp=0;
        int partidasBot=0;
        
        
        for(int i=0;i<listaPartidos.size();i++){
        	String linea="";
        	String rol="";
        	String platformID=listaPartidos.get(i).getPlatformId();
        	linea=listaPartidos.get(i).getLane();
        	rol=listaPartidos.get(i).getRole();
        	/*System.out.println("\nPartida: "+(i+1));
        	System.out.println("Linea: "+listaPartidos.get(i).getLane());
        	System.out.println("Rol: "+listaPartidos.get(i).getRole());
        	System.out.println("Season: "+listaPartidos.get(i).getSeason());*/
        	
        	if(linea!=null && rol!=null){
        		if(linea.contains("TOP")){
            		partidasTop++;
            	}else if(linea.contains("JUNGLE")){
            		partdasJungle++;
            	}else if(linea.contains("MID")){
            		partidasMid++;
            	}else if(linea.contains("BOTTOM")){
            		partidasBot++;
            		if(rol.contains("CARRY")){
            			partidasADC++;
            		}else{
            			partidasSupp++;
            		}
            	}
        	}
        
        }
        
        System.out.println("Partidas jugadas en Top: "+partidasTop);
        System.out.println("Partidas jugadas en Jungle: "+partdasJungle);
        System.out.println("Partidas jugadas en Mid: "+partidasMid);
        System.out.println("Partidas jugadas en ADC: "+partidasADC);
        System.out.println("Partidas jugadas en Supp: "+partidasSupp);
        System.out.println("Partidas jugadas en Bot: "+partidasBot);
        
        System.out.println("---------------------- Equipos ----------------------");
        
        List<Team> equipos=api.getTeamsBySummonerId(id);
        
        for(int i=0;i<equipos.size();i++){
        	try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
			}
        	
        	System.out.println("\nNombre: "+equipos.get(i).getName());
        	System.out.println("Tag: "+equipos.get(i).getTag());
        	System.out.println("Estado: "+equipos.get(i).getStatus());
        	Roster roster=equipos.get(i).getRoster();
        	
        	System.out.println("\t ----Miembros del equipo----");
        	List<TeamMemberInfo> miembrosDelEquipo=roster.getMemberList();
        	
        	for(int j=0;j<miembrosDelEquipo.size();j++){
        		
        		try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
				}
        		
        		Summoner miembro=api.getSummonerById(miembrosDelEquipo.get(j).getPlayerId());
        		System.out.println("\t Nombre: "+miembro.getName());
        		
        	}
        	
        }
    }

}
